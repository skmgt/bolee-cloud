package com.itheima.bolee.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itheima.bolee.framework.commons.dto.analysis.TimeDTO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.DateAvgUtil;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.TimeHandlerUtils;
import com.itheima.bolee.points.mapper.BusinessLogMapper;
import com.itheima.bolee.points.pojo.*;
import com.itheima.bolee.points.service.*;
import com.itheima.bolee.points.vo.analysis.DauTimeVO;
import com.itheima.bolee.points.vo.analysis.DauVO;
import com.itheima.bolee.points.vo.analysis.DnuVO;
import com.itheima.bolee.points.vo.table.LineChartsVO;
import com.itheima.bolee.points.vo.table.PiChartsVO;
import com.itheima.bolee.points.vo.table.StackingChartsVO;
import com.itheima.bolee.points.vo.table.StackingIndexVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName CustomerReportServiceImpl.java
 * @Description 用户统计接口实现
 */
@Service
public class CustomerReportServiceImpl implements ICustomerReportService {

    @Autowired
    BusinessLogMapper businessLogMapper;

    @Autowired
    IDnuService dnuService;

    @Autowired
    IDnuCityService dnuCityService;

    @Autowired
    IDauService dauService;

    @Autowired
    IDauTimeService dauTimeService;

    @Autowired
    IDpvService dpvService;

    @Autowired
    IDuvService duvService;

    @Autowired
    IDauRangeService dauRangeService;

    @Override
    public Boolean dnuJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<Dnu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dnu::getReportTime,targetTime.getTargetDate());
        Dnu dnu = dnuService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dnu)){
            dnuService.remove(queryWrapper);
        }
        //从influxdb中统计当天所有注册用户
        List<BusinessLog> dnuHandler = businessLogMapper.dnu(targetTime.getBegin(), targetTime.getEnd());
        if (!EmptyUtil.isNullOrEmpty(dnuHandler)){
            dnuService.save(Dnu.builder()
            .reportTime(targetTime.getTargetDate())
            .dnu(Long.valueOf(dnuHandler.size()))
            .build());
        }else {
            dnuService.save(Dnu.builder()
            .reportTime(targetTime.getTargetDate())
            .dnu(0L)
            .build());
        }
        return true;
    }

    @Override
    public Boolean dnuCityJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<DnuCity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DnuCity::getReportTime,targetTime.getTargetDate());
        List<DnuCity> dnuCity = dnuCityService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dnuCity)){
            dnuCityService.remove(queryWrapper);
        }
        //从influxdb中统计当天所有注册用户
        List<BusinessLog> dnuHandler = businessLogMapper.dnu(targetTime.getBegin(), targetTime.getEnd());
        if (!EmptyUtil.isNullOrEmpty(dnuHandler)){
            //进行分组处理
            Map<String, List<BusinessLog>> mapHandler = dnuHandler.stream().collect(Collectors.groupingBy(BusinessLog::getCity));
            List<DnuCity> list = Lists.newArrayList();
            for (String key : mapHandler.keySet()) {
                DnuCity dnuCityHandler = DnuCity.builder()
                    .city(key)
                    .dnu(Long.valueOf(mapHandler.get(key).size()))
                    .reportTime(targetTime.getTargetDate())
                    .build();
                list.add(dnuCityHandler);
            }
            dnuCityService.saveBatch(list);
        }
        return true;
    }

    @Override
    public Boolean dauJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<Dau> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dau::getReportTime,targetTime.getTargetDate());
        Dau dau = dauService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dau)){
            dauService.remove(queryWrapper);
        }
        //统计当天所有活跃用户userIds
        List<String> allDauForUserId = businessLogMapper.allDauForUserId(targetTime.getBegin(), targetTime.getEnd());
        //所有活跃用户数
        Integer allDau = allDauForUserId.size();
        //统计当天所有注册用户
        List<BusinessLog> dnu = businessLogMapper.dnu(targetTime.getBegin(), targetTime.getEnd());
        //所有活跃新用户数
        Integer newDau = dnu.size();
        //所有活跃老用户数
        Integer oldDau = allDau-newDau;
        dauService.save(Dau.builder()
            .allDau(Long.valueOf(allDau))
            .newDau(Long.valueOf(newDau))
            .oldDau(Long.valueOf(oldDau))
            .reportTime(targetTime.getTargetDate())
            .build());
        return true;
    }

    @Override
    public Boolean dauTimeJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        if (flag){
            reportTime = LocalDate.parse(reportTime).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        QueryWrapper<DauTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().likeRight(DauTime::getReportTime,reportTime);
        List<DauTime> dauTimeList = dauTimeService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dauTimeList)){
            dauTimeService.remove(queryWrapper);
        }
        //目标日期24个时间段
        List<TimeDTO> targetTimes = TimeHandlerUtils.getToday24Time(reportTime);
        if (flag){
            targetTimes = TimeHandlerUtils.getYesterday24Time(reportTime);
        }
        List<DauTime> dauTimes = Lists.newArrayList();
        //分时段查询用户活跃数
        for (TimeDTO timeDTO : targetTimes) {
            List<String> allDauForUserId = businessLogMapper.allDauForUserId(timeDTO.getBegin(), timeDTO.getEnd());
            dauTimes.add(DauTime.builder()
                .dau(Long.valueOf(allDauForUserId.size()))
                .reportTime(timeDTO.getTargetDateTime())
                .build());
        }
        dauTimeService.saveBatch(dauTimes);
        return true;
    }

    @Override
    public Boolean dpvJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<Dpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dpv::getReportTime,targetTime.getTargetDate());
        Dpv dpv = dpvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dpv)){
            dpvService.remove(queryWrapper);
        }
        //统计日访问量
        Integer dpvNums = businessLogMapper.dpv(targetTime.getBegin(), targetTime.getEnd());
        dpvService.save(Dpv.builder()
            .dpv(Long.valueOf(dpvNums))
            .reportTime(targetTime.getTargetDate())
            .build());
        return true;
    }

    @Override
    public Boolean duvJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<Duv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Duv::getReportTime,targetTime.getTargetDate());
        Duv duv = duvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(duv)){
            duvService.remove(queryWrapper);
        }
        //统计日访问量
        Integer duvNums = businessLogMapper.duv(targetTime.getBegin(), targetTime.getEnd());
        duvService.save(Duv.builder()
            .duv(Long.valueOf(duvNums))
            .reportTime(targetTime.getTargetDate())
            .build());
        return true;
    }

    @Override
    public Boolean dauRangeJob(String reportTime) {
        //判断报表时间是否指定，未指定则使用当前时间
        if (EmptyUtil.isNullOrEmpty(reportTime)){
            reportTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        //判断统计时间是否为00:00到06:00
        boolean flag = TimeHandlerUtils.isTimeInRange();
        //目标日期
        TimeDTO targetTime = TimeHandlerUtils.getTodayTime(reportTime);
        if (flag){
            targetTime = TimeHandlerUtils.getYesterdayTime(reportTime);
        }
        QueryWrapper<DauRange> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DauRange::getReportTime,targetTime.getTargetDate());
        List<DauRange> dauRanges = dauRangeService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(dauRanges)){
            dauRangeService.remove(queryWrapper);
        }
        //统计每日点击首页业务记录
        List<BusinessLog> dpvForIndex = businessLogMapper.dpvForIndex(targetTime.getBegin(), targetTime.getEnd());
        //对每日点击首页业务记录按用户进行分组处理
        Map<String, List<BusinessLog>> userRangMap = dpvForIndex.stream().collect(Collectors.groupingBy(BusinessLog::getUserId));
        //创建分布范围
        Map<String,Long> rangeMap = Maps.newLinkedHashMap();
        rangeMap.put("1-5",0L);
        rangeMap.put("5-10",0L);
        rangeMap.put("10-20",0L);
        rangeMap.put("20-40",0L);
        rangeMap.put("40-80",0L);
        rangeMap.put("80以上",0L);
        for (Map.Entry<String,Long> range : rangeMap.entrySet()) {
            List<String> rangeListHandler = Lists.newArrayList();
            String[] Keys = range.getKey().split("-");
            for (String key : Keys) {
                rangeListHandler.add(key.replace("以上",""));
            }
            if (rangeListHandler.size()==2){
                Long rangeStart = Long.valueOf(rangeListHandler.get(0));
                Long rangeEnd = Long.valueOf(rangeListHandler.get(1));
                for (Map.Entry<String, List<BusinessLog>>  entry : userRangMap.entrySet()) {
                    Long userNum = Long.valueOf(entry.getValue().size());
                    if (rangeStart<=userNum&&userNum<rangeEnd){
                        long targetLong = range.getValue()+1L;
                        rangeMap.put(range.getKey(),targetLong);
                    }
                }
            }else {
                Long rangeStart = Long.valueOf(rangeListHandler.get(0));
                for (Map.Entry<String, List<BusinessLog>>  entry : userRangMap.entrySet()) {
                    Long userNum = Long.valueOf(entry.getValue().size());
                    if (rangeStart<=userNum){
                        long targetLong = range.getValue()+1L;
                        rangeMap.put(range.getKey(),targetLong);
                    }
                }
            }
        }
        List<DauRange> dauRangesHandler = Lists.newArrayList();
        for (Map.Entry<String,Long> range : rangeMap.entrySet()) {
            dauRangesHandler.add(DauRange.builder()
                .dauRang(range.getKey())
                .userNum(range.getValue())
                .reportTime(targetTime.getTargetDate())
                .build());
        }
        dauRangeService.saveBatch(dauRangesHandler);
        return true;
    }

    @Override
    public DnuVO dnu(String startTime,String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new DnuVO();
        }
        //查询时间范围数据
        QueryWrapper<Dnu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(Dnu::getReportTime,startTime,endTime).orderByAsc(Dnu::getReportTime);
        List<Dnu> dnus = dnuService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(dnus)){
            return new DnuVO();
        }
        DnuVO dnuVO = new DnuVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData = dnus.stream().map(Dnu::getDnu).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        dnuVO.setTable(lineChartsVO);
        //合计
        Long total = dnus.stream().mapToLong(Dnu::getDnu).sum();
        dnuVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        dnuVO.setAverage(new BigDecimal(total).divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2,RoundingMode.HALF_UP));
        //环比上周=本日注册/7日前注册
        Dnu dnuToday = dnus.stream().filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        BigDecimal dnuTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(dnuToday)){
            dnuTodayBigDecimal = new BigDecimal(dnuToday.getDnu());
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Dnu dnuEarlier7 = dnus.stream().filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(dnuEarlier7)&&!dnuEarlier7.getDnu().equals(0L)){
            BigDecimal qoqLastWeek =dnuTodayBigDecimal.divide(new BigDecimal(dnuEarlier7.getDnu()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            dnuVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日注册/昨日前注册
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Dnu dnuYesterday = dnus.stream().filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(dnuYesterday)&&!dnuYesterday.getDnu().equals(0L)) {
            BigDecimal qoq = dnuTodayBigDecimal.divide(new BigDecimal(dnuYesterday.getDnu()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            dnuVO.setQoq(qoq);
        }
        //当日总注册人数
        Long perNums = dnuTodayBigDecimal.longValue();
        dnuVO.setPerNums(perNums);
        return dnuVO;
    }

    @Override
    public List<PiChartsVO> dnuCity(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return Lists.newArrayList();
        }
        //查询时间范围数据
        QueryWrapper<DnuCity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DnuCity::getReportTime,startTime,endTime);
        List<DnuCity> dnuCitys = dnuCityService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(dnuCitys)){
            return Lists.newArrayList();
        }
        //按照城市进行分组
        Map<String, List<DnuCity>> dnuCityMap = dnuCitys.stream().collect(Collectors.groupingBy(DnuCity::getCity));
        Map<String,Long> dnuCityMapHandler = Maps.newHashMap();
        BigDecimal total = BigDecimal.ZERO;
        //城市注册分布Map
        for (Map.Entry<String, List<DnuCity>> entry : dnuCityMap.entrySet()) {
            dnuCityMapHandler.put(entry.getKey(),entry.getValue().stream().mapToLong(DnuCity::getDnu).sum());
            total = total.add(new BigDecimal(entry.getValue().stream().mapToLong(DnuCity::getDnu).sum()));
        }
        //超过10个:构建饼状图
        if (Long.valueOf(dnuCityMapHandler.size())>10L){
            LinkedHashMap<String,Long> longLinkedHashMap = dnuCityMapHandler.entrySet().stream()
                .sorted((entry1,entry2)->{return entry2.getValue().compareTo(entry1.getValue());}).limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(oldVal,newVal)->oldVal,LinkedHashMap::new));
            List<PiChartsVO> piChartsVOs = Lists.newArrayList();
            for (Map.Entry<String, Long> entry : longLinkedHashMap.entrySet()) {
                piChartsVOs.add(PiChartsVO.builder()
                    .name(entry.getKey())
                    .value(new BigDecimal(entry.getValue()).divide(total,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)))
                    .build());
            }
            //构建其他城市
            BigDecimal otherTotal = BigDecimal.ZERO;
            BigDecimal topTotal = BigDecimal.ZERO;
            for (Map.Entry<String, Long> entry : longLinkedHashMap.entrySet()) {
                dnuCityMapHandler.put(entry.getKey(),entry.getValue());
                topTotal = topTotal.add(new BigDecimal(entry.getValue()));
            }
            otherTotal = total.subtract(topTotal);
            piChartsVOs.add(PiChartsVO.builder()
                .name("其他")
                .value(otherTotal.divide(total,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)))
                .build());
            return piChartsVOs;
        }
        //未超过10个:构建饼状图
        List<PiChartsVO> piChartsVOs = Lists.newArrayList();
        for (Map.Entry<String, Long> entry : dnuCityMapHandler.entrySet()) {
            piChartsVOs.add(PiChartsVO.builder()
                .name(entry.getKey())
                .value(new BigDecimal(entry.getValue()).divide(total,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)))
                .build());
        }
        return piChartsVOs;
    }

    @Override
    public DauVO dau(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new DauVO();
        }
        //查询时间范围数据
        QueryWrapper<Dau> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(Dau::getReportTime,startTime,endTime).orderByAsc(Dau::getReportTime);
        List<Dau> daus = dauService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(daus)){
            return new DauVO();
        }
        DauVO dauVO = new DauVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData = dateRange.stream()
            .map(date -> {
                return daus.stream()
                    .filter(dto -> date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(Dau::getAllDau).sum();
            }).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        dauVO.setTable(lineChartsVO);
        //合计
        Long total = daus.stream().mapToLong(Dau::getAllDau).sum();
        dauVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        dauVO.setAverage(new BigDecimal(total)
            .divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2,RoundingMode.HALF_UP));
        //环比上周=本日/7日前
        Dau dauToday = daus.stream()
            .filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .findFirst().orElse(null);
        BigDecimal dauTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(dauToday)){
            dauTodayBigDecimal = new BigDecimal(dauToday.getAllDau());
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Dau dauEarlier7 = daus.stream()
            .filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(dauEarlier7)&&!dauEarlier7.getAllDau().equals(0L)) {
            BigDecimal qoqLastWeek = dauTodayBigDecimal
                .divide(new BigDecimal(dauEarlier7.getAllDau()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            dauVO.setQoqLastWeek(qoqLastWeek);
        }
        //环比上期=本日/昨日
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Dau dauYesterday = daus.stream()
            .filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(dauYesterday)&&!dauYesterday.getAllDau().equals(0L)) {
            BigDecimal qoq = dauTodayBigDecimal
                .divide(new BigDecimal(dauYesterday.getAllDau()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            dauVO.setQoq(qoq);
        }
        //当日总活跃人数
        Long perNums = dauTodayBigDecimal.longValue();
        dauVO.setPerNums(perNums);
        return dauVO;
    }

    @Override
    public StackingChartsVO newOldDau(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<Dau> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(Dau::getReportTime,startTime,endTime);
        List<Dau> daus = dauService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(daus)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //新用户活跃
        List<Long> newDauValues = Lists.newArrayList();
        //老用户活跃
        List<Long> oldDauValues =  Lists.newArrayList();
        for (String date : dateRanges) {
            Dau dau = daus.stream()
                .filter(dto -> date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .findFirst()
                .orElse(null);
            if (!EmptyUtil.isNullOrEmpty(dau)){
                newDauValues.add(dau.getNewDau());
                oldDauValues.add(dau.getOldDau());
            }else {
                newDauValues.add(0L);
                oldDauValues.add(0L);
            }
        }
        List<StackingIndexVO> valueData = Lists.newArrayList();
        valueData.add(new StackingIndexVO(newDauValues, "新用户"));
        valueData.add(new StackingIndexVO(oldDauValues, "老用户"));
        return new StackingChartsVO(dateRanges, valueData);
    }

    @Override
    public LineChartsVO dauRange(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new LineChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DauRange> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DauRange::getReportTime,startTime,endTime);
        List<DauRange> dauRanges = dauRangeService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(dauRanges)){
            return new LineChartsVO();
        }
        // 创建x轴和y轴数据
        List<String> xLabels = new ArrayList<>();
        //填充x轴内容
        xLabels.add("1-5");
        xLabels.add("5-10");
        xLabels.add("10-20");
        xLabels.add("20-40");
        xLabels.add("40-80");
        xLabels.add("80以上");
        List<Long> yValues = new ArrayList<>();
        //按照活跃数区间分组
        Map<String, List<DauRange>> dauRangeMap = dauRanges.stream().collect(Collectors.groupingBy(DauRange::getDauRang));
        //构建y轴内容
        for (Map.Entry<String, List<DauRange>> entry : dauRangeMap.entrySet()) {
            xLabels.stream().forEach(x->{
                if (x.equals(entry.getKey())){
                    yValues.add(entry.getValue().stream().mapToLong(DauRange::getUserNum).sum());
                }
            });
        }
        return new LineChartsVO(xLabels, yValues);
    }

    @Override
    public DauTimeVO dauTime(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new DauTimeVO();
        }
        //查询时间范围数据
        QueryWrapper<DauTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DauTime::getReportTime,startTime,endTime);
        List<DauTime> dauTimes = dauTimeService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(dauTimes)){
            return new DauTimeVO();
        }
        DauTimeVO dauTimeVO = new DauTimeVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.HOURS);
        //Y轴
        List<Long> valueData = dateRange.stream()
            .map(date -> {
                return dauTimes.stream()
                    .filter(dto -> date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                    .mapToLong(DauTime::getDau).sum();
            }).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        dauTimeVO.setTable(lineChartsVO);
        //合计
        Long total = dauTimes.stream().mapToLong(DauTime::getDau).sum();
        dauTimeVO.setTotal(total);
        //均值
        LocalDateTime LocalDateTimeStartTime = LocalDateTime.parse(startTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime LocalDateTimeEndTime = LocalDateTime.parse(endTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dauTimeVO.setAverage(new BigDecimal(total)
            .divide(new BigDecimal(LocalDateTimeStartTime.until(LocalDateTimeEndTime, ChronoUnit.DAYS)),2,RoundingMode.HALF_UP));
        //环比上周=本日注册/7日前注册
        String endTimeHandler = endTime.substring(0,10);
        List<DauTime> dauTimeTodays = dauTimes.stream()
            .filter(dto -> endTimeHandler.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .collect(Collectors.toList());
        BigDecimal dauTimeTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(dauTimeTodays)){
            dauTimeTodayBigDecimal = new BigDecimal(dauTimeTodays.stream().mapToLong(DauTime::getDau).sum());
        }
        String earlier7Time = LocalDate
            .parse(endTimeHandler, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<DauTime> dauTimeEarliers7 = dauTimes.stream()
            .filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .collect(Collectors.toList());
        BigDecimal dauTimeEarliers7BigDecimal = new BigDecimal(dauTimeEarliers7.stream().mapToLong(DauTime::getDau).sum());
        if (dauTimeEarliers7BigDecimal.compareTo(BigDecimal.ZERO)!=0) {
            BigDecimal qoqLastWeek = dauTimeTodayBigDecimal
                .divide(dauTimeEarliers7BigDecimal, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            dauTimeVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日注册/昨日前注册
        String yesterdayTime = LocalDate
            .parse(endTimeHandler, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<DauTime> dauTimeYesterdays = dauTimes.stream()
            .filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .collect(Collectors.toList());
        BigDecimal dauTimeYesterdaysBigDecimal = new BigDecimal(dauTimeYesterdays.stream().mapToLong(DauTime::getDau).sum());
        if (dauTimeYesterdaysBigDecimal.compareTo(BigDecimal.ZERO)!=0) {
            BigDecimal qoq = dauTimeEarliers7BigDecimal
                .divide(dauTimeYesterdaysBigDecimal, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            dauTimeVO.setQoq(qoq);
        }
        //当日总活跃人数
        Long perNums = dauTimeTodayBigDecimal.longValue();
        dauTimeVO.setPerNums(perNums);
        return dauTimeVO;
    }
}
