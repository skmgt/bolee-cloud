package com.itheima.bolee.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.framework.commons.dto.analysis.TimeDTO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.TimeHandlerUtils;
import com.itheima.bolee.instance.feign.SaleReportFeign;
import com.itheima.bolee.points.pojo.DoInsureCategory;
import com.itheima.bolee.points.pojo.DoInsureDetailDay;
import com.itheima.bolee.points.pojo.DoInsureCategory;
import com.itheima.bolee.points.pojo.DoInsureDetailDay;
import com.itheima.bolee.points.service.IDoInsureCategoryService;
import com.itheima.bolee.points.service.IDoInsureDetailDayService;
import com.itheima.bolee.points.service.ISaleReportService;
import com.itheima.bolee.points.vo.analysis.*;
import com.itheima.bolee.points.vo.analysis.CcnVO;
import com.itheima.bolee.points.vo.table.LineChartsVO;
import com.itheima.bolee.points.vo.table.StackingChartsVO;
import com.itheima.bolee.points.vo.table.StackingIndexVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName SaleReportServiceImple.java
 * @Description 日投保明细服务接口实现
 */
@Slf4j
@Service
public class SaleReportServiceImple implements ISaleReportService {

    @Autowired
    SaleReportFeign saleReportFeign;

    @Autowired
    IDoInsureDetailDayService doInsureDetailDayService;

    @Autowired
    IDoInsureCategoryService doInsureCategoryService;

    @Override
    public Boolean doInsureDetailDayJob(String reportTime) {
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
        QueryWrapper<DoInsureDetailDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureDetailDay::getReportTime,targetTime.getTargetDate());
        DoInsureDetailDay doInsureDetailDay = doInsureDetailDayService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDay)){
            doInsureDetailDayService.remove(queryWrapper);
        }
        //统计日投保明细
        SaleReportDTO saleReportDTO = saleReportFeign.doInsureDetailDay(reportTime);
        //保存结果
        doInsureDetailDayService.save(
            DoInsureDetailDay.builder()
                .totalAmountDay(saleReportDTO.getTotalAmountDay())
                .totalWarrantyDay(Long.valueOf(saleReportDTO.getTotalWarrantyDay()))
                .avgPieceAmountDay(saleReportDTO.getAvgPieceAmountDay())
                .avgPersonAmountDay(saleReportDTO.getAvgPersonAmountDay())
                .persons(saleReportDTO.getPersons())
                .reportTime(saleReportDTO.getReportTime())
                .build()
        );
        return true;
    }

    @Override
    public Boolean doInsureCategoryJob(String reportTime) {
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
        QueryWrapper<DoInsureCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureCategory::getReportTime,targetTime.getTargetDate());
        List<DoInsureCategory> doInsureCategories = doInsureCategoryService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureCategories)){
            doInsureCategoryService.remove(queryWrapper);
        }
        //统计日投保分类明细
        List<InsureCategoryDTO> insureCategoryDTOs = saleReportFeign.doInsureCategory(reportTime);
        if (EmptyUtil.isNullOrEmpty(insureCategoryDTOs)){
            return true;
        }
        List<DoInsureCategory> doInsureCategorys = Lists.newArrayList();
        for (InsureCategoryDTO insureCategoryDTO : insureCategoryDTOs) {
            doInsureCategorys.add(DoInsureCategory.builder()
                .categoryName(insureCategoryDTO.getCategoryName())
                .doInsureNums(insureCategoryDTO.getDoInsureNums())
                .reportTime(insureCategoryDTO.getReportTime())
                .build());
        }
        doInsureCategoryService.saveBatch(doInsureCategorys);
        return true;
    }

    @Override
    public SaleTopVO saleTop(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new SaleTopVO();
        }
        //范围时间内数据
        QueryWrapper<DoInsureDetailDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureDetailDay::getReportTime,startTime,endTime);
        List<DoInsureDetailDay> doInsureDetailDays = doInsureDetailDayService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureDetailDays)){
            return new SaleTopVO();
        }
        //投保总保费
        BigDecimal totalInsurance = doInsureDetailDays.stream().map(DoInsureDetailDay::getTotalAmountDay).reduce(BigDecimal.ZERO,BigDecimal::add);
        //投保总保单
        Long totalInsuranceNum = doInsureDetailDays.stream().mapToLong(DoInsureDetailDay::getTotalWarrantyDay).sum();
        //投保件均保费
        BigDecimal insuranceAvgMoney = BigDecimal.ZERO;
        DoInsureDetailDay doInsureDetailDay = doInsureDetailDays.stream()
            .filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDay)){
            insuranceAvgMoney = doInsureDetailDay.getAvgPieceAmountDay();
        }
        //投保人均保费
        BigDecimal avgPersonAmountDay = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDay)){
            insuranceAvgMoney = doInsureDetailDay.getAvgPersonAmountDay();
        }
        return SaleTopVO.builder()
            .totalInsurance(totalInsurance)
            .totalInsuranceNum(totalInsuranceNum)
            .insuranceAvgMoney(insuranceAvgMoney)
            .insuranceAvgPerson(avgPersonAmountDay)
            .build();
    }

    @Override
    public CcnVO ccn(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new CcnVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureDetailDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureDetailDay::getReportTime,startTime,endTime).orderByAsc(DoInsureDetailDay::getReportTime);
        List<DoInsureDetailDay> doInsureDetailDays = doInsureDetailDayService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureDetailDays)){
            return new CcnVO();
        }
        CcnVO ccnVO = new CcnVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData = dateRange.stream()
            .map(date->{
                return doInsureDetailDays.stream()
                    .filter(dto->date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(DoInsureDetailDay::getPersons).sum();
            }).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        ccnVO.setTable(lineChartsVO);
        //合计
        Long total = doInsureDetailDays.stream().mapToLong(DoInsureDetailDay::getPersons).sum();
        ccnVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        ccnVO.setAverage(new BigDecimal(total).divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2, RoundingMode.HALF_UP));
        //环比上周=本日保单用户数/7日保单用户数
        DoInsureDetailDay doInsureDetailDayToday = doInsureDetailDays.stream().filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        BigDecimal doInsureDetailDayTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayToday)){
            doInsureDetailDayTodayBigDecimal = new BigDecimal(doInsureDetailDayToday.getPersons());
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayEarlier7 = doInsureDetailDays.stream().filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayEarlier7)&&!doInsureDetailDayEarlier7.getPersons().equals(0L)){
            BigDecimal qoqLastWeek =doInsureDetailDayTodayBigDecimal.divide(new BigDecimal(doInsureDetailDayEarlier7.getPersons()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            ccnVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日保单用户数/昨日保单用户数
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayYesterday = doInsureDetailDays.stream().filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayYesterday)&&!doInsureDetailDayYesterday.getPersons().equals(0L)) {
            BigDecimal qoq = doInsureDetailDayTodayBigDecimal.divide(new BigDecimal(doInsureDetailDayYesterday.getPersons()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            ccnVO.setQoq(qoq);
        }
        //当日总人数
        Long perNums = doInsureDetailDayTodayBigDecimal.longValue();
        ccnVO.setPerNums(perNums);
        return ccnVO;
    }

    @Override
    public WarrantyNumVO warrantyNum(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new WarrantyNumVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureDetailDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureDetailDay::getReportTime,startTime,endTime).orderByAsc(DoInsureDetailDay::getReportTime);
        List<DoInsureDetailDay> doInsureDetailDays = doInsureDetailDayService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureDetailDays)){
            return new WarrantyNumVO();
        }
        WarrantyNumVO warrantyNumVO = new WarrantyNumVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData = dateRange.stream()
            .map(date->{
                return doInsureDetailDays.stream()
                    .filter(dto->date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(DoInsureDetailDay::getTotalWarrantyDay).sum();
            }).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        warrantyNumVO.setTable(lineChartsVO);
        //合计
        Long total = doInsureDetailDays.stream().mapToLong(DoInsureDetailDay::getTotalWarrantyDay).sum();
        warrantyNumVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        warrantyNumVO.setAverage(new BigDecimal(total).divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2, RoundingMode.HALF_UP));
        //环比上周=本日保单用户数/7日保单用户数
        DoInsureDetailDay doInsureDetailDayToday = doInsureDetailDays.stream().filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        BigDecimal doInsureDetailDayTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayToday)){
            doInsureDetailDayTodayBigDecimal = new BigDecimal(doInsureDetailDayToday.getTotalWarrantyDay());
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayEarlier7 = doInsureDetailDays.stream().filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayEarlier7)&&!doInsureDetailDayEarlier7.getTotalWarrantyDay().equals(0L)){
            BigDecimal qoqLastWeek =doInsureDetailDayTodayBigDecimal.divide(new BigDecimal(doInsureDetailDayEarlier7.getTotalWarrantyDay()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            warrantyNumVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日保单数/昨日保单数
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayYesterday = doInsureDetailDays.stream().filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayYesterday)&&!doInsureDetailDayYesterday.getTotalWarrantyDay().equals(0L)) {
            BigDecimal qoq = doInsureDetailDayTodayBigDecimal.divide(new BigDecimal(doInsureDetailDayYesterday.getTotalWarrantyDay()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            warrantyNumVO.setQoq(qoq);
        }
        //当日保单
        Long perNums = doInsureDetailDayTodayBigDecimal.longValue();
        warrantyNumVO.setWarrantyNums(perNums);
        return warrantyNumVO;
    }

    @Override
    public StackingChartsVO doInsureCategory(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureCategory::getReportTime,startTime,endTime);
        List<DoInsureCategory> doInsureCategorys = doInsureCategoryService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureCategorys)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //按照分类进行分组
        Map<String, List<DoInsureCategory>> doInsureCategoryMapByCategoryName = doInsureCategorys.stream()
                .collect(Collectors.groupingBy(DoInsureCategory::getCategoryName));
        //构建饼状图
        List<StackingIndexVO> stackingIndexVOs = Lists.newArrayList();
        //分类名称
        Set<String> categoryNames = doInsureCategoryMapByCategoryName.keySet();
        categoryNames.forEach(n->{
            StackingIndexVO stackingIndexVO = StackingIndexVO.builder().name(n).build();
            List<DoInsureCategory> doInsureCategorysHandler = doInsureCategoryMapByCategoryName.get(n);
            List<Long> data = Lists.newArrayList();
            dateRanges.forEach(d->{
                DoInsureCategory doInsureCategory = doInsureCategorysHandler.stream()
                    .filter(dto -> d.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .findFirst()
                    .orElse(null);
                if (!EmptyUtil.isNullOrEmpty(doInsureCategory)){
                    data.add(doInsureCategory.getDoInsureNums());
                }else {
                    data.add(0L);
                }

            });
            stackingIndexVO.setData(data);
            stackingIndexVOs.add(stackingIndexVO);
        });
        return new StackingChartsVO(dateRanges,stackingIndexVOs);
    }

    @Override
    public AvgPersonAmountVO avgPersonAmount(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new AvgPersonAmountVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureDetailDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureDetailDay::getReportTime,startTime,endTime).orderByAsc(DoInsureDetailDay::getReportTime);
        List<DoInsureDetailDay> doInsureDetailDays = doInsureDetailDayService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureDetailDays)){
            return new AvgPersonAmountVO();
        }
        AvgPersonAmountVO avgPersonAmountVO = new AvgPersonAmountVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData = dateRange.stream()
            .map(date->{
                return doInsureDetailDays.stream()
                    .filter(dto->date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(DoInsureDetailDay::getTotalWarrantyDay).sum();
            }).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        avgPersonAmountVO.setTable(lineChartsVO);
        //合计
        BigDecimal total = doInsureDetailDays.stream().map(DoInsureDetailDay::getAvgPersonAmountDay).reduce(BigDecimal.ZERO,BigDecimal::add);
        avgPersonAmountVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        avgPersonAmountVO.setAverage(total.divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2, RoundingMode.HALF_UP));
        //环比上周=本日保费/7日前保费
        DoInsureDetailDay doInsureDetailDayToday = doInsureDetailDays.stream().filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        BigDecimal doInsureDetailDayTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayToday)){
            doInsureDetailDayTodayBigDecimal = doInsureDetailDayToday.getAvgPersonAmountDay();
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayEarlier7 = doInsureDetailDays.stream().filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayEarlier7)
            &&doInsureDetailDayEarlier7.getAvgPersonAmountDay().compareTo(BigDecimal.ZERO)!=0){
            BigDecimal qoqLastWeek =doInsureDetailDayTodayBigDecimal.divide(doInsureDetailDayEarlier7.getAvgPersonAmountDay(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            avgPersonAmountVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日保费/昨日保费
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureDetailDay doInsureDetailDayYesterday = doInsureDetailDays.stream().filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureDetailDayYesterday)&&
            doInsureDetailDayYesterday.getAvgPersonAmountDay().compareTo(BigDecimal.ZERO)!=0) {
            BigDecimal qoq = doInsureDetailDayTodayBigDecimal.divide(doInsureDetailDayYesterday.getAvgPersonAmountDay(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            avgPersonAmountVO.setQoq(qoq);
        }
        //当日保费
        avgPersonAmountVO.setPersonAmount(doInsureDetailDayTodayBigDecimal);
        return avgPersonAmountVO;
    }

}
