package com.itheima.bolee.points.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.analysis.TimeDTO;
import com.itheima.bolee.framework.commons.dto.report.CategoryReportVO;
import com.itheima.bolee.framework.commons.dto.report.InsuranceReportVO;
import com.itheima.bolee.framework.commons.utils.CityUtil;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.framework.commons.utils.TimeHandlerUtils;
import com.itheima.bolee.instance.feign.CategoryFeign;
import com.itheima.bolee.points.mapper.BusinessLogMapper;
import com.itheima.bolee.points.pojo.*;
import com.itheima.bolee.points.service.*;
import com.itheima.bolee.points.vo.analysis.DoInsureFailDpvVO;
import com.itheima.bolee.points.vo.analysis.DoInsureFailDpvVO;
import com.itheima.bolee.points.vo.table.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName BusinessReportServiceImpl.java
 * @Description 业务统计接口实现
 */
@Slf4j
@Service
public class BusinessReportServiceImpl implements IBusinessReportService {

    @Autowired
    BusinessLogMapper businessLogMapper;

    @Autowired
    IDoInsureDpvService doInsureDpvService;

    @Autowired
    IDoInsureDuvService doInsureDuvService;

    @Autowired
    IDoInsureGenderDuvService doInsureGenderDuvService;

    @Autowired
    IDoInsureCityDuvService doInsureCityDuvService;

    @Autowired
    IDoInsureConversionDpvService doInsureConversionDpvService;

    @Autowired
    IDoInsureFailDpvService doInsureFailDpvService;

    @Autowired
    ICategoryDpvService categoryDpvService;

    @Autowired
    CategoryFeign categoryFeign;

    @Override
    public Boolean doInsureDpvJob(String reportTime) {
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
        QueryWrapper<DoInsureDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureDpv::getReportTime,targetTime.getTargetDate());
        DoInsureDpv doInsureDpv = doInsureDpvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureDpv)){
            doInsureDpvService.remove(queryWrapper);
        }
        //统计日投保访问页面量
        Integer doInsureDpvNums = businessLogMapper.doInsureDpv(targetTime.getBegin(), targetTime.getEnd());
        doInsureDpvService.save(DoInsureDpv.builder()
            .reportTime(targetTime.getTargetDate())
            .doInsureDpv(Long.valueOf(doInsureDpvNums))
            .build());
        return true;
    }

    @Override
    public Boolean doInsureDuvJob(String reportTime) {
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
        QueryWrapper<DoInsureDuv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureDuv::getReportTime,targetTime.getTargetDate());
        DoInsureDuv doInsureDuv = doInsureDuvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureDuv)){
            doInsureDuvService.remove(queryWrapper);
        }
        //统计日投保用户访问数
        Integer doInsureDuvNums = businessLogMapper.doInsureDuv(targetTime.getBegin(), targetTime.getEnd());
        doInsureDuvService.save(DoInsureDuv.builder()
            .reportTime(targetTime.getTargetDate())
            .doInsureDuv(Long.valueOf(doInsureDuvNums))
            .build());
        return true;
    }

    @Override
    public Boolean categoryDpvJob(String reportTime) {
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
        QueryWrapper<CategoryDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CategoryDpv::getReportTime,targetTime.getTargetDate());
        List<CategoryDpv> categoryDpvs = categoryDpvService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(categoryDpvs)){
            categoryDpvService.remove(queryWrapper);
        }
        //统计日保险分类访问记录
        List<String> categoryDpvBusinessLog = businessLogMapper
            .categoryDpvForResponseBody(targetTime.getBegin(), targetTime.getEnd());
        //无数据直接返回空
        if (EmptyUtil.isNullOrEmpty(categoryDpvBusinessLog)){
            return true;
        }
        List<InsuranceReportVO> insuranceReportVOs = Lists.newArrayList();
        categoryDpvBusinessLog.forEach(n->{
            insuranceReportVOs.add(JSONObject.parseObject(n, InsuranceReportVO.class));
        });
        //获得所有二级分类
        List<CategoryReportVO> categoryReportVOs = categoryFeign
            .categoryList(CategoryReportVO.builder().nodeFloors(Lists.newArrayList(2L)).build());
        //无分类直接返回空
        if (EmptyUtil.isNullOrEmpty(categoryReportVOs)){
            return true;
        }
        //补全分类名称
        insuranceReportVOs.forEach(n->{
            categoryReportVOs.forEach(k->{
                if (NoProcessing.isParent(k.getCategoryNo(),String.valueOf(n.getCategoryNo()))){
                    n.setCategoryName(k.getCategoryName());
                }
            });
        });
        //按保险分类名称进行分组
        Map<String, List<InsuranceReportVO>> mapHandler = insuranceReportVOs.stream()
            .collect(Collectors.groupingBy(InsuranceReportVO::getCategoryName));
        List<CategoryDpv> categoryDpvList = Lists.newArrayList();
        for (Map.Entry<String, List<InsuranceReportVO>> entry : mapHandler.entrySet()) {
            categoryDpvList.add(CategoryDpv.builder().categoryDpv(Long.valueOf(entry.getValue().size()))
                .categoryName(entry.getKey())
                .reportTime(targetTime.getTargetDate())
                .build());
        }
        categoryDpvService.saveBatch(categoryDpvList);
        return true;
    }

    @Override
    public Boolean doInsureGenderDuvJob(String reportTime) {
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
        QueryWrapper<DoInsureGenderDuv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureGenderDuv::getReportTime,targetTime.getTargetDate());
        DoInsureGenderDuv doInsureGenderDuv = doInsureGenderDuvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureGenderDuv)){
            doInsureGenderDuvService.remove(queryWrapper);
        }
        //统计男性日投保用户访问数
        Integer manDpv = businessLogMapper.doInsureGenderDuv(targetTime.getBegin(), targetTime.getEnd(), SuperConstant.MAN);
        //统计女性日投保用户访问数
        Integer womanDuv = businessLogMapper.doInsureGenderDuv(targetTime.getBegin(), targetTime.getEnd(), SuperConstant.WOMAN);
        doInsureGenderDuvService.save(DoInsureGenderDuv.builder().manDpv(Long.valueOf(manDpv))
            .womanDpv(Long.valueOf(womanDuv))
            .reportTime(targetTime.getTargetDate())
            .build());
        return true;
    }

    @Override
    public Boolean doInsureCityDuvJob(String reportTime) {
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
        QueryWrapper<DoInsureCityDuv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureCityDuv::getReportTime,targetTime.getTargetDate());
        List<DoInsureCityDuv> list = doInsureCityDuvService.list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(list)){
            doInsureCityDuvService.remove(queryWrapper);
        }
        //统计城市日投保用户访问数
        List<BusinessLog> businessLogList= businessLogMapper.doInsureCityDuv(targetTime.getBegin(), targetTime.getEnd());
        //统计结果为空则不处理
        if (EmptyUtil.isNullOrEmpty(businessLogList)){
            return true;
        }
        //按照用户id分组,然后循环取第一条【去重】
        Map<String, List<BusinessLog>> mapUserIdHandler = businessLogList.stream().collect(
            Collectors.groupingBy(BusinessLog::getUserId));
        List<BusinessLog> listHandler = Lists.newArrayList();
        for (Map.Entry<String, List<BusinessLog>> entry : mapUserIdHandler.entrySet()) {
            listHandler.add(entry.getValue().get(0));
        }
        //按照城市名称分组
        Map<String, List<BusinessLog>> mapCityHandler = listHandler.stream().collect(Collectors.groupingBy(BusinessLog::getCity));
        List<DoInsureCityDuv> doInsureCityDuvs = Lists.newArrayList();
        //保存城市日投保用户访问数
        for (Map.Entry<String,List<BusinessLog>> entry : mapCityHandler.entrySet()) {
            doInsureCityDuvs.add(DoInsureCityDuv.builder()
                .cityName(entry.getKey())
                .doInsureCityDuv(Long.valueOf(entry.getValue().size()))
                .reportTime(targetTime.getTargetDate())
                .build());
        }
        doInsureCityDuvService.saveBatch(doInsureCityDuvs);
        return true;
    }

    @Override
    public Boolean doInsureConversionDpvJob(String reportTime) {
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
        QueryWrapper<DoInsureConversionDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureConversionDpv::getReportTime,targetTime.getTargetDate());
        DoInsureConversionDpv doInsureConversionDpv = doInsureConversionDpvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureConversionDpv)){
            doInsureConversionDpvService.remove(queryWrapper);
        }
        //投保浏览次数
        Integer doBrowseDpv = businessLogMapper.doBrowseDpv(targetTime.getBegin(), targetTime.getEnd());
        //投保试算次数
        Integer doTrialDpv = businessLogMapper.doTrialDpv(targetTime.getBegin(), targetTime.getEnd());
        //投保提交次数
        Integer doInsureDpv = businessLogMapper.doInsureDpv(targetTime.getBegin(), targetTime.getEnd());

        doInsureConversionDpvService.save(
            DoInsureConversionDpv.builder()
                .doBrowseDpv(Long.valueOf(doBrowseDpv))
                .doTrialDpv(Long.valueOf(doTrialDpv))
                .doInsureDpv(Long.valueOf(doInsureDpv))
                .reportTime(targetTime.getTargetDate())
                .build()
        );
        return true;
    }

    @Override
    public Boolean doInsureFailDpvJob(String reportTime) {
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
        QueryWrapper<DoInsureFailDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DoInsureFailDpv::getReportTime,targetTime.getTargetDate());
        DoInsureFailDpv doInsureFailDpv = doInsureFailDpvService.getOne(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(doInsureFailDpv)){
            doInsureFailDpvService.remove(queryWrapper);
        }
        //日投保提交失败页面量
        Integer doInsureFailDpvs = businessLogMapper.doInsureFailDpv(targetTime.getBegin(), targetTime.getEnd());
        doInsureFailDpvService.save(DoInsureFailDpv.builder()
            .doInsureFailDpv(Long.valueOf(doInsureFailDpvs))
            .reportTime(targetTime.getTargetDate())
            .build());
        return true;
    }

    @Override
    public StackingChartsVO doInsureDpvDuv(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureDpv> queryWrapperDpv = new QueryWrapper<>();
        queryWrapperDpv.lambda().between(DoInsureDpv::getReportTime,startTime,endTime);
        List<DoInsureDpv> dauRangesDpvs = doInsureDpvService.list(queryWrapperDpv);
        QueryWrapper<DoInsureDuv> queryWrapperDuv = new QueryWrapper<>();
        queryWrapperDuv.lambda().between(DoInsureDuv::getReportTime,startTime,endTime);
        List<DoInsureDuv> dauRangesDuvs = doInsureDuvService.list(queryWrapperDuv);
        if (EmptyUtil.isNullOrEmpty(dauRangesDpvs)&&EmptyUtil.isNullOrEmpty(dauRangesDuvs)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴：点击立即投保次数
        List<Long> valueDataDoInsureDpv = dateRanges.stream()
            .map(date -> {
                return dauRangesDpvs.stream()
                    .filter(dto -> date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(DoInsureDpv::getDoInsureDpv).sum();
            }).collect(Collectors.toList());
        StackingIndexVO stackingIndexVODoInsureDpv = new StackingIndexVO();
        stackingIndexVODoInsureDpv.setName("投保 点击立即投保的总次数");
        stackingIndexVODoInsureDpv.setData(valueDataDoInsureDpv);
        //Y轴：点击立即投保用户数
        List<Long> valueDataDoInsureDuv = dateRanges.stream()
            .map(date -> {
                return dauRangesDuvs.stream()
                    .filter(dto -> date.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .mapToLong(DoInsureDuv::getDoInsureDuv).sum();
            }).collect(Collectors.toList());
        StackingIndexVO stackingIndexVODoInsureDuv = new StackingIndexVO();
        stackingIndexVODoInsureDuv.setName("投保 点击立即投保的用户数");
        stackingIndexVODoInsureDuv.setData(valueDataDoInsureDuv);
        //构建元素对象
        List<StackingIndexVO> stackingIndexVOs = new ArrayList<StackingIndexVO>();
        stackingIndexVOs.add(stackingIndexVODoInsureDpv);
        stackingIndexVOs.add(stackingIndexVODoInsureDuv);
        //返回最终结果
        return StackingChartsVO.builder()
            .label(dateRanges)
            .value(stackingIndexVOs)
            .build();
    }

    @Override
    public StackingChartsVO categoryDpv(String startTime, String endTime) {

        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<CategoryDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(CategoryDpv::getReportTime,startTime,endTime);
        List<CategoryDpv> categoryDpvs = categoryDpvService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(categoryDpvs)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //按照分类进行分组
        Map<String, List<CategoryDpv>> categoryDpvMapByCategoryName = categoryDpvs.stream().collect(Collectors.groupingBy(CategoryDpv::getCategoryName));
        //构建饼状图
        List<StackingIndexVO> stackingIndexVOs = Lists.newArrayList();
        //分类名称
        Set<String> categoryNames = categoryDpvMapByCategoryName.keySet();
        categoryNames.forEach(n->{
            StackingIndexVO stackingIndexVO = StackingIndexVO.builder().name(n).build();
            List<CategoryDpv> categoryDpvsHandler = categoryDpvMapByCategoryName.get(n);
            List<Long> data = Lists.newArrayList();
            dateRanges.forEach(d->{
                CategoryDpv categoryDpv = categoryDpvsHandler.stream()
                    .filter(dto -> d.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                    .findFirst()
                    .orElse(null);
                if (!EmptyUtil.isNullOrEmpty(categoryDpv)){
                    data.add(categoryDpv.getCategoryDpv());
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
    public StackingChartsVO doInsureGenderDuv(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureGenderDuv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureGenderDuv::getReportTime,startTime,endTime).orderByAsc(DoInsureGenderDuv::getReportTime);
        List<DoInsureGenderDuv> doInsureGenderDuvs = doInsureGenderDuvService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureGenderDuvs)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //y轴
        List<StackingIndexVO> value = Lists.newArrayList();
        StackingIndexVO manStackingIndexVO = StackingIndexVO.builder().name("男").build();
        StackingIndexVO womanStackingIndexVO = StackingIndexVO.builder().name("女").build();
        List<Long> manData   = Lists.newArrayList();
        List<Long> womanData = Lists.newArrayList();
        dateRanges.forEach(d->{
            DoInsureGenderDuv doInsureGenderDuv = doInsureGenderDuvs.stream()
                .filter(dto -> d.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .findFirst()
                .orElse(null);
            if (!EmptyUtil.isNullOrEmpty(doInsureGenderDuv)){
                manData.add(doInsureGenderDuv.getManDpv());
                womanData.add(doInsureGenderDuv.getWomanDpv());
            }else {
                manData.add(0L);
                womanData.add(0L);
            }
        });
        manStackingIndexVO.setData(manData);
        womanStackingIndexVO.setData(womanData);
        value.add(manStackingIndexVO);
        value.add(womanStackingIndexVO);
        return new StackingChartsVO(dateRanges,value);
    }

    @Override
    public StackingChartsVO doInsureCityDuv(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new StackingChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureCityDuv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureCityDuv::getReportTime,startTime,endTime).orderByAsc(DoInsureCityDuv::getReportTime);
        List<DoInsureCityDuv> doInsureCityDuvs = doInsureCityDuvService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureCityDuvs)){
            return new StackingChartsVO();
        }
        //X轴
        List<String> dateRanges = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //y轴
        List<StackingIndexVO> value = Lists.newArrayList();
        //一线城市
        StackingIndexVO firstTierCity = StackingIndexVO.builder().name("一线城市").build();
        //二线城市
        StackingIndexVO secondTierCity = StackingIndexVO.builder().name("二线城市").build();
        //三、四线城市
        StackingIndexVO otherTierCity = StackingIndexVO.builder().name("三、四线城市").build();
        //元素数据
        List<Long> firstTierCityData  = Lists.newArrayList();
        List<Long> secondTierCityData = Lists.newArrayList();
        List<Long> otherTierCityData  = Lists.newArrayList();
        //构建数据
        dateRanges.forEach(d->{
            List<DoInsureCityDuv> doInsureCityDuvHandler = doInsureCityDuvs.stream()
                .filter(dto -> d.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .collect(Collectors.toList());
            //当前时间无注册城市
            if (EmptyUtil.isNullOrEmpty(doInsureCityDuvHandler)){
                firstTierCityData.add(0L);
                secondTierCityData.add(0L);
                otherTierCityData.add(0L);
            //当前时间有注册城市，判断城市类型,并求和
            }else {
                Long firstTierCityCount  = doInsureCityDuvHandler.stream()
                    .filter(dto->CityUtil.isFirstTierCity(dto.getCityName()))
                    .mapToLong(DoInsureCityDuv::getDoInsureCityDuv).sum();
                Long secondTierCityDataCount = doInsureCityDuvHandler.stream()
                    .filter(dto->CityUtil.isSecondTierCity(dto.getCityName()))
                    .mapToLong(DoInsureCityDuv::getDoInsureCityDuv).sum();
                Long otherTierCityDataCount  = doInsureCityDuvHandler.stream()
                    .filter(dto->CityUtil.isOtherCity(dto.getCityName()))
                    .mapToLong(DoInsureCityDuv::getDoInsureCityDuv).sum();
                firstTierCityData.add(firstTierCityCount);
                secondTierCityData.add(secondTierCityDataCount);
                otherTierCityData.add(otherTierCityDataCount);
            }
        });
        //设置元素数据
        firstTierCity.setData(firstTierCityData);
        secondTierCity.setData(secondTierCityData);
        otherTierCity.setData(otherTierCityData);
        //报表对象
        value.add(firstTierCity);
        value.add(secondTierCity);
        value.add(otherTierCity);
        return new StackingChartsVO(dateRanges,value);
    }

    @Override
    public FunnelPlotChartsVO doInsureConversionDpv(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new FunnelPlotChartsVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureConversionDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureConversionDpv::getReportTime,startTime,endTime).orderByAsc(DoInsureConversionDpv::getReportTime);
        List<DoInsureConversionDpv> doInsureConversionDpvs = doInsureConversionDpvService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureConversionDpvs)){
            return new FunnelPlotChartsVO();
        }
        //X轴
        FunnelPlotChartsVO funnelPlotChartsVO = new FunnelPlotChartsVO();
        funnelPlotChartsVO.setLabel(Arrays.asList("投保浏览次数", "投保试算次数", "投保提交次数"));
        //合计投保浏览次数
        long sumDoBrowseDpv = doInsureConversionDpvs.stream().mapToLong(DoInsureConversionDpv::getDoBrowseDpv).sum();
        FunnelPlotChartsIndexVO sumDoBrowseDpvIndexVO = FunnelPlotChartsIndexVO.builder()
            .name("投保浏览次数").value(sumDoBrowseDpv).build();
        //合计投保试算次数
        long sumDoTrialDpv = doInsureConversionDpvs.stream().mapToLong(DoInsureConversionDpv::getDoTrialDpv).sum();
        FunnelPlotChartsIndexVO sumDoTrialDpvIndexVO = FunnelPlotChartsIndexVO.builder()
            .name("投保试算次数").value(sumDoTrialDpv).build();
        //合计投保提交次数
        long sumDoInsureDpv = doInsureConversionDpvs.stream().mapToLong(DoInsureConversionDpv::getDoInsureDpv).sum();
        FunnelPlotChartsIndexVO sumDoInsureDpvIndexVO = FunnelPlotChartsIndexVO.builder()
            .name("投保提交次数").value(sumDoInsureDpv).build();
        List<FunnelPlotChartsIndexVO> value = new ArrayList<>();
        value.add(sumDoBrowseDpvIndexVO);
        value.add(sumDoTrialDpvIndexVO);
        value.add(sumDoInsureDpvIndexVO);
        funnelPlotChartsVO.setValue(value);
        return funnelPlotChartsVO;
    }

    @Override
    public DoInsureFailDpvVO doInsureFailDpv(String startTime, String endTime) {
        //起始或结束时间为空则返回空结果
        if (EmptyUtil.isNullOrEmpty(startTime)||EmptyUtil.isNullOrEmpty(endTime)){
            return new DoInsureFailDpvVO();
        }
        //查询时间范围数据
        QueryWrapper<DoInsureFailDpv> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().between(DoInsureFailDpv::getReportTime,startTime,endTime).orderByAsc(DoInsureFailDpv::getReportTime);
        List<DoInsureFailDpv> doInsureFailDpvs = doInsureFailDpvService.list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(doInsureFailDpvs)){
            return new DoInsureFailDpvVO();
        }
        DoInsureFailDpvVO doInsureFailDpvVO = new DoInsureFailDpvVO();
        //X轴
        List<String> dateRange = TimeHandlerUtils.getGraduallys(startTime, endTime, TimeUnit.DAYS);
        //Y轴
        List<Long> valueData =doInsureFailDpvs.stream().map(DoInsureFailDpv::getDoInsureFailDpv).collect(Collectors.toList());
        //折线表格数据
        LineChartsVO lineChartsVO = new LineChartsVO();
        lineChartsVO.setLabel(dateRange);
        lineChartsVO.setValue(valueData);
        doInsureFailDpvVO.setTable(lineChartsVO);
        //合计
        Long total = doInsureFailDpvs.stream().mapToLong(DoInsureFailDpv::getDoInsureFailDpv).sum();
        doInsureFailDpvVO.setTotal(total);
        //均值
        LocalDate localDateStartTime = LocalDate.parse(startTime);
        LocalDate localDateEndTime = LocalDate.parse(endTime);
        doInsureFailDpvVO.setAverage(new BigDecimal(total).divide(new BigDecimal(localDateStartTime.until(localDateEndTime, ChronoUnit.DAYS)),2, RoundingMode.HALF_UP));
        //环比上周=本日注册/7日前注册
        DoInsureFailDpv doInsureFailDpvToday = doInsureFailDpvs.stream().filter(dto -> endTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        BigDecimal doInsureFailDpvTodayBigDecimal = BigDecimal.ZERO;
        if (!EmptyUtil.isNullOrEmpty(doInsureFailDpvToday)){
            doInsureFailDpvTodayBigDecimal = new BigDecimal(doInsureFailDpvToday.getDoInsureFailDpv());
        }
        String earlier7Time = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureFailDpv doInsureFailDpvTodayEarlier7 = doInsureFailDpvs.stream().filter(dto -> earlier7Time.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureFailDpvTodayEarlier7)&&!doInsureFailDpvTodayEarlier7.getDoInsureFailDpv().equals(0L)) {
            BigDecimal qoqLastWeek = doInsureFailDpvTodayBigDecimal
                .divide(new BigDecimal(doInsureFailDpvTodayEarlier7.getDoInsureFailDpv()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            doInsureFailDpvVO.setQoqLastWeek(qoqLastWeek);
        }
        //同比昨日=本日注册/昨日前注册
        String yesterdayTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DoInsureFailDpv doInsureFailDpvYesterday = doInsureFailDpvs.stream().filter(dto -> yesterdayTime.equals(dto.getReportTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).findFirst().orElse(null);
        if (!EmptyUtil.isNullOrEmpty(doInsureFailDpvYesterday)&&!doInsureFailDpvYesterday.getDoInsureFailDpv().equals(0L)) {
            BigDecimal qoq = doInsureFailDpvTodayBigDecimal
                .divide(new BigDecimal(doInsureFailDpvYesterday.getDoInsureFailDpv()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
            doInsureFailDpvVO.setQoq(qoq);
        }
        //当日总人数
        Long perNums = doInsureFailDpvTodayBigDecimal.longValue();
        doInsureFailDpvVO.setPerNums(perNums);
        return doInsureFailDpvVO;
    }
}
