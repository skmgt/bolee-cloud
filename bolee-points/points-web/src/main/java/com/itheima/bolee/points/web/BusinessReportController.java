package com.itheima.bolee.points.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.points.service.IBusinessReportService;
import com.itheima.bolee.points.vo.analysis.DauVO;
import com.itheima.bolee.points.vo.analysis.DoInsureFailDpvVO;
import com.itheima.bolee.points.vo.table.FunnelPlotChartsVO;
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
 * @ClassName BusinessReportFeignController.java
 * @Description 数据展示：业务统计
 */
@Slf4j
@Api(tags = "数据展示：业务统计")
@RestController
@RequestMapping("business-report")
public class BusinessReportController {

    @Autowired
    IBusinessReportService businessReportService;

    /**
     * 数据展示：7日|点击立即投保用户数|次数
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("do-insure-dpv-duv/{startTime}/{endTime}")
    @ApiOperation(value = "7日|点击立即投保用户数|次数", notes = "7日|点击立即投保用户数|次数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> doInsureDpvDuv(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = businessReportService.doInsureDpvDuv(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：7日|各险种点击率
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("category-dpv/{startTime}/{endTime}")
    @ApiOperation(value = "7日|各险种点击率", notes = "7日|各险种点击率")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> categoryDpv(@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = businessReportService.categoryDpv(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：30日|投保用户性别分布
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("do-insure-gender-duv/{startTime}/{endTime}")
    @ApiOperation(value = "30日|投保用户性别分布", notes = "30日|投保用户性别分布")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> doInsureGenderDuv(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = businessReportService.doInsureGenderDuv(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：30日|投保用户城市分布
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("do-insure-city-duv/{startTime}/{endTime}")
    @ApiOperation(value = "30日|投保用户城市分布", notes = "30日|投保用户城市分布")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> doInsureCityDuv(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = businessReportService.doInsureCityDuv(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：投保流程转换漏斗
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("do-insure-conversion-dpv/{startTime}/{endTime}")
    @ApiOperation(value = "投保流程转换漏斗", notes = "投保流程转换漏斗")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<FunnelPlotChartsVO> doInsureConversionDpv(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        FunnelPlotChartsVO funnelPlotChartsVO = businessReportService.doInsureConversionDpv(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,funnelPlotChartsVO);
    }


    /**
     * 数据展示：30日|投保提交失败次数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    @PostMapping("do-insure-fail-dpv/{startTime}/{endTime}")
    @ApiOperation(value = "30日|投保提交失败次数", notes = "30日|投保提交失败次数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<DoInsureFailDpvVO> doInsureFailDpv(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        DoInsureFailDpvVO doInsureFailDpvVO = businessReportService.doInsureFailDpv(startTime,endTime);
        return ResponseResultBuild.successBuild(doInsureFailDpvVO);
    }
}
