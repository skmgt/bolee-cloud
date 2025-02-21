package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.service.IBusinessReportService;
import com.itheima.bolee.points.service.ISaleReportService;
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
 * @ClassName SaleReportFeignController.java
 * @Description 销售统计
 */
@Slf4j
@Api(tags = "计划任务：销售统计")
@RestController
@RequestMapping("sale-report-feign")
public class SaleReportFeignController {

    @Autowired
    ISaleReportService saleReportService;

    /**
     * 日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-details-day-job/{reportTime}")
    @ApiOperation(value = "日投保额度明细", notes = "日投保额度明细")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureDetailsDayJob(@PathVariable("reportTime") String reportTime) {
        return saleReportService.doInsureDetailDayJob(reportTime);
    }

    /**
     * 日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("doInsure-category-job/{reportTime}")
    @ApiOperation(value = "日投保分类明细", notes = "日投保分类明细")
    @ApiImplicitParam(name = "reportTime", value = "统计时间", required = true, dataType = "String")
    public Boolean doInsureCategoryJob(@PathVariable("reportTime") String reportTime) {
        return saleReportService.doInsureCategoryJob(reportTime);
    }

}
