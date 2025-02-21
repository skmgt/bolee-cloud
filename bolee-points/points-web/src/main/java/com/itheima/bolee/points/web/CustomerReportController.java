package com.itheima.bolee.points.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.points.service.ICustomerReportService;
import com.itheima.bolee.points.vo.analysis.DauTimeVO;
import com.itheima.bolee.points.vo.analysis.DauVO;
import com.itheima.bolee.points.vo.analysis.DnuVO;
import com.itheima.bolee.points.vo.table.LineChartsVO;
import com.itheima.bolee.points.vo.table.PiChartsVO;
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

import java.util.List;

/**
 * @ClassName CustomerReportFeignController.java
 * @Description 用户统计
 */
@Slf4j
@Api(tags = "数据展示：用户统计")
@RestController
@RequestMapping("customer-report")
public class CustomerReportController {

    @Autowired
    ICustomerReportService customerReportService;

    /**
     * 数据展示：30日|每日新增用户数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DnuVO
     */
    @PostMapping("dnu/{startTime}/{endTime}")
    @ApiOperation(value = "30日|每日新增用户数", notes = "30日|每日新增用户数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<DnuVO> dnu(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        DnuVO dnuVO = customerReportService.dnu(startTime,endTime);
        return ResponseResultBuild.successBuild(dnuVO);
    }

    /**
     * 数据展示：新增注册用户城市分布
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DnuVO
     */
    @PostMapping("dnu-city/{startTime}/{endTime}")
    @ApiOperation(value = "新增注册用户城市分布", notes = "新增注册用户城市分布")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<List<PiChartsVO>> dnuCity(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        List<PiChartsVO> piChartsVOs = customerReportService.dnuCity(startTime,endTime);
        return ResponseResultBuild.successBuild(piChartsVOs);
    }

    /**
     * 数据展示：30日|活跃用户数趋势
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    @PostMapping("dau/{startTime}/{endTime}")
    @ApiOperation(value = "30日|活跃用户数趋势", notes = "30日|活跃用户数趋势")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<DauVO> dau(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        DauVO dauVO = customerReportService.dau(startTime,endTime);
        return ResponseResultBuild.successBuild(dauVO);
    }

    /**
     * 数据展示：30日|新老活跃用户结构
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    @PostMapping("new-old-dau/{startTime}/{endTime}")
    @ApiOperation(value = "30日|新老活跃用户结构", notes = "30日|新老活跃用户结构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<StackingChartsVO> newOldDau(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        StackingChartsVO stackingChartsVO = customerReportService.newOldDau(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,stackingChartsVO);
    }

    /**
     * 数据展示：30日|每天活跃次数分布
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    @PostMapping("dau-range/{startTime}/{endTime}")
    @ApiOperation(value = "30日|每天活跃次数分布", notes = "30日|每天活跃次数分布")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String")
    })
    public ResponseResult<LineChartsVO> dauRange(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
        LineChartsVO lineChartsVO = customerReportService.dauRange(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,lineChartsVO);
    }

    /**
     * 数据展示：7日|活跃时段分布
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return DauVO
     */
    @PostMapping("dau-time/{startTime}/{endTime}")
    @ApiOperation(value = "7日|活跃时段分布", notes = "7日|活跃时段分布")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "开始时间格式：yyyy-MM-dd HH:mm:ss", required = true, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间：yyyy-MM-dd HH:mm:ss", required = true, dataType = "String")
    })
    public ResponseResult<DauTimeVO> dauTime(@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime) {
        DauTimeVO dauTimeVO = customerReportService.dauTime(startTime,endTime);
        return ResponseResultBuild.build(BaseEnum.SUCCEED,dauTimeVO);
    }
}
