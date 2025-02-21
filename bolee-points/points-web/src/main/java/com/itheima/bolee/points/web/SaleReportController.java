package com.itheima.bolee.points.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.points.service.ISaleReportService;
import com.itheima.bolee.points.vo.analysis.*;
import com.itheima.bolee.points.vo.table.StackingChartsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@Api(tags = "数据展示：销售统计")
@RestController
@RequestMapping("sale-report")
public class SaleReportController {

    @Autowired
    ISaleReportService saleReportService;

    /**
     * 数据展示：销售顶部统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return SaleTopVO
     */
    @PostMapping("sale-top/{startTime}/{endTime}")
    @ApiOperation(value = "销售顶部统计", notes = "销售顶部统计")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<SaleTopVO> saleTop(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        SaleTopVO saleTopVO = saleReportService.saleTop(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,saleTopVO);
    }

    /**
     * 数据展示：累计保单客户数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return CcnVO
     */
    @PostMapping("ccn/{startTime}/{endTime}")
    @ApiOperation(value = "累计保单客户数", notes = "累计保单客户数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<CcnVO> ccn(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        CcnVO ccnVO = saleReportService.ccn(startTime,endTime);
        return ResponseResultBuild.successBuild(ccnVO);
    }

    /**
     * 数据展示：7日|保单数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return WarrantyNumVO
     */
    @PostMapping("warranty-num/{startTime}/{endTime}")
    @ApiOperation(value = "7日|保单数", notes = "7日|保单数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<WarrantyNumVO> warrantyNum(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        WarrantyNumVO warrantyNum = saleReportService.warrantyNum(startTime,endTime);
        return ResponseResultBuild.successBuild(warrantyNum);
    }

    /**
     * 数据展示：7日|各分类投保保单
     * @param startTime
     * @param endTime
     * @return StackingChartsVO
     */
    @PostMapping("do-insure-category/{startTime}/{endTime}")
    @ApiOperation(value = "7日|各分类投保保单", notes = "7日|各分类投保保单")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> doInsureCategory(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = saleReportService.doInsureCategory(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：7日|人均保费
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return WarrantyNumVO
     */
    @PostMapping("avg-person-amount/{startTime}/{endTime}")
    @ApiOperation(value = "7日|人均保费", notes = "7日|人均保费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<AvgPersonAmountVO> avgPersonAmount(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        AvgPersonAmountVO avgPersonAmount = saleReportService.avgPersonAmount(startTime,endTime);
        return ResponseResultBuild.successBuild(avgPersonAmount);
    }

}
