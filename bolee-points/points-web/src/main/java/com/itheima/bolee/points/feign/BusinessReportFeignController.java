package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.service.IBusinessReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BusinessReportFeignController.java
 * @Description 业务统计
 */
@Slf4j
@Api(tags = "计划任务：业务统计")
@RestController
@RequestMapping("business-report-feign")
public class BusinessReportFeignController {

    @Autowired
    IBusinessReportService businessReportService;

    /**
     * 日投保访页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-dpv-job/{reportTime}")
    @ApiOperation(value = "日投保访问页面量", notes = "日投保访问页面量")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureDpvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureDpvJob(reportTime);
    }

    /**
     * 日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-duv-job/{reportTime}")
    @ApiOperation(value = "日投保用户访问数", notes = "日投保用户访问数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureDuvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureDuvJob(reportTime);
    }

    /**
     * 日保险分类访问页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("category-dpv-job/{reportTime}")
    @ApiOperation(value = "日保险分类访问页面量", notes = "日保险分类访问页面量")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean categoryDpvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.categoryDpvJob(reportTime);
    }

    /**
     * 性别日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-gender-duv-job/{reportTime}")
    @ApiOperation(value = "性别日投保用户访问数", notes = "性别日投保用户访问数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureGenderDuvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureGenderDuvJob(reportTime);
    }

    /**
     * 城市日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-city-duv-job/{reportTime}")
    @ApiOperation(value = "城市日投保用户访问数", notes = "城市日投保用户访问数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureCityDuvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureCityDuvJob(reportTime);
    }

    /**
     * 投保转换率
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-conversion-dpv-job/{reportTime}")
    @ApiOperation(value = "投保转换率", notes = "投保转换率")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureConversionDpvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureConversionDpvJob(reportTime);
    }

    /**
     * 日投保访问失败页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-fail-dpv-job/{reportTime}")
    @ApiOperation(value = "日投保访问失败页面量", notes = "日投保访问失败页面量")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureFailDpvJob(@PathVariable("reportTime") String reportTime) {
        return businessReportService.doInsureFailDpvJob(reportTime);
    }

}
