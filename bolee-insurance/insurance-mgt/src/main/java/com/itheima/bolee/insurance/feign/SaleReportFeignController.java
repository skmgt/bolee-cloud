package com.itheima.bolee.insurance.feign;

import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.insurance.service.IWarrantyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SaleReportFeign.java
 * @Description 日投保明细服务接口
 */
@Slf4j
@Api(tags = "保险分类-feign")
@RestController
@RequestMapping("sale-report-feign")
public class SaleReportFeignController {

    @Autowired
    IWarrantyService warrantyService;

    /**
     * 计划任务：日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-detail-day/{reportTime}")
    @ApiOperation(value = "日投保额度明细",notes = "日投保额度明细")
    SaleReportDTO doInsureDetailDay(@PathVariable("reportTime")String reportTime){
        return warrantyService.doInsureDetailDay(reportTime);
    }

    /**
     * 计划任务：统计日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("do-insure-category/{reportTime}")
    @ApiOperation(value = "统计日投保分类明细",notes = "统计日投保分类明细")
    List<InsureCategoryDTO> doInsureCategory(@PathVariable("reportTime")String reportTime){
        return warrantyService.doInsureCategory(reportTime);
    }

}
