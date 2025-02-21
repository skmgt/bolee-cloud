package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.RiskAnalysisVO;
import com.itheima.bolee.insurance.service.IRiskAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：风险类目响应接口
 */
@Slf4j
@Api(tags = "风险类目")
@RestController
@RequestMapping("risk-analysis")
public class RiskAnalysisController {

    @Autowired
    IRiskAnalysisService riskAnalysisService;

    /***
     * @description 多条件查询风险类目分页
     * @param riskAnalysisVO 风险类目VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<RiskAnalysisVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "风险类目分页",notes = "风险类目分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "riskAnalysisVO",value = "风险类目VO对象",required = true,dataType = "RiskAnalysisVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"riskAnalysisVO.insuranceId","riskAnalysisVO.assessmentKey","riskAnalysisVO.assessmentKeyName","riskAnalysisVO.assessmentVal","riskAnalysisVO.assessmentType","riskAnalysisVO.sortNo","riskAnalysisVO.remake"})
    public ResponseResult<Page<RiskAnalysisVO>> findRiskAnalysisVOPage(
                                    @RequestBody RiskAnalysisVO riskAnalysisVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<RiskAnalysisVO> riskAnalysisVOPage = riskAnalysisService.findPage(riskAnalysisVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(riskAnalysisVOPage);
    }

    /**
     * @Description 保存风险类目
     * @param riskAnalysisVO 风险类目VO对象
     * @return RiskAnalysisVO
     */
    @PutMapping
    @ApiOperation(value = "保存RiskAnalysis",notes = "添加RiskAnalysis")
    @ApiImplicitParam(name = "riskAnalysisVO",value = "风险类目VO对象",required = true,dataType = "RiskAnalysisVO")
    @ApiOperationSupport(includeParameters = {"riskAnalysisVO.dataState","riskAnalysisVO.insuranceId","riskAnalysisVO.assessmentKey","riskAnalysisVO.assessmentKeyName","riskAnalysisVO.assessmentVal","riskAnalysisVO.assessmentType","riskAnalysisVO.sortNo","riskAnalysisVO.remake"})
    public ResponseResult<RiskAnalysisVO> createRiskAnalysis(@RequestBody RiskAnalysisVO riskAnalysisVO) {
        RiskAnalysisVO riskAnalysisVOResult = riskAnalysisService.save(riskAnalysisVO);
        return ResponseResultBuild.successBuild(riskAnalysisVOResult);
    }

    /**
     * @Description 修改风险类目
     * @param riskAnalysisVO 风险类目VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改风险类目",notes = "修改风险类目")
    @ApiImplicitParam(name = "riskAnalysisVO",value = "风险类目VO对象",required = true,dataType = "RiskAnalysisVO")
    @ApiOperationSupport(includeParameters = {"riskAnalysisVO.id","riskAnalysisVO.dataState","riskAnalysisVO.insuranceId","riskAnalysisVO.assessmentKey","riskAnalysisVO.assessmentKeyName","riskAnalysisVO.assessmentVal","riskAnalysisVO.assessmentType","riskAnalysisVO.sortNo","riskAnalysisVO.remake"})
    public ResponseResult<Boolean> updateRiskAnalysis(@RequestBody RiskAnalysisVO riskAnalysisVO) {
        Boolean flag = riskAnalysisService.update(riskAnalysisVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除风险类目
     * @param riskAnalysisVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除风险类目",notes = "删除风险类目")
    @ApiImplicitParam(name = "riskAnalysisVO",value = "风险类目VO对象",required = true,dataType = "RiskAnalysisVO")
    @ApiOperationSupport(includeParameters = {"riskAnalysisVO.checkedIds"})
    public ResponseResult<Boolean> deleteRiskAnalysis(@RequestBody RiskAnalysisVO riskAnalysisVO) {
        Boolean flag = riskAnalysisService.delete(riskAnalysisVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询风险类目列表
     * @param riskAnalysisVO 风险类目VO对象
     * @return List<RiskAnalysisVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询风险类目列表",notes = "多条件查询风险类目列表")
    @ApiImplicitParam(name = "riskAnalysisVO",value = "风险类目VO对象",required = true,dataType = "RiskAnalysisVO")
    @ApiOperationSupport(includeParameters = {"riskAnalysisVO.insuranceId","riskAnalysisVO.assessmentKey","riskAnalysisVO.assessmentKeyName","riskAnalysisVO.assessmentVal","riskAnalysisVO.assessmentType","riskAnalysisVO.sortNo","riskAnalysisVO.remake"})
    public ResponseResult<List<RiskAnalysisVO>> riskAnalysisList(@RequestBody RiskAnalysisVO riskAnalysisVO) {
        List<RiskAnalysisVO> riskAnalysisVOList = riskAnalysisService.findList(riskAnalysisVO);
        return ResponseResultBuild.successBuild(riskAnalysisVOList);
    }

}