package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.service.ICustomerReportService;
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
 * @ClassName CustomerReportFeignController.java
 * @Description 用户统计
 */
@Slf4j
@Api(tags = "计划任务：用户统计")
@RestController
@RequestMapping("customer-report-feign")
public class CustomerReportFeignController {

    @Autowired
    ICustomerReportService customerReportService;

    /**
     * 日新增用户数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dnu-job/{reportTime}")
    @ApiOperation(value = "日新增用户数", notes = "日新增用户数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dnuJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dnuJob(reportTime);
    }


    /**
     * 日新增注册用户归属城市
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dnu-city-job/{reportTime}")
    @ApiOperation(value = "日新增注册用户归属城市", notes = "日新增注册用户归属城市")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dnuCityJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dnuCityJob(reportTime);
    }

    /**
     * 用户日活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dau-job/{reportTime}")
    @ApiOperation(value = "用户日活跃数", notes = "用户日活跃数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dauJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dauJob(reportTime);
    }

    /**
     * 用户每时活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dau-time-job/{reportTime}")
    @ApiOperation(value = "用户每时活跃数", notes = "用户每时活跃数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dauTimeJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dauTimeJob(reportTime);
    }

    /**
     * 日访问量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dpv-job/{reportTime}")
    @ApiOperation(value = "日访问量", notes = "日访问量")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dpvJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dpvJob(reportTime);
    }

    /**
     * 日用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("duv-job/{reportTime}")
    @ApiOperation(value = "日用户访问数", notes = "日用户访问数")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean duvJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.duvJob(reportTime);
    }

    /**
     * 用户日活跃数范围
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("dau-range-job/{reportTime}")
    @ApiOperation(value = "用户日活跃数范围", notes = "用户日活跃数范围")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean dauRangeJob(@PathVariable("reportTime") String reportTime) {
        return customerReportService.dauRangeJob(reportTime);
    }
}
