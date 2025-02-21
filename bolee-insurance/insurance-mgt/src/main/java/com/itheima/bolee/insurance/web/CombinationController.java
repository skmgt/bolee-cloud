package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CombinationVO;
import com.itheima.bolee.insurance.service.ICombinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：组合方案响应接口
 */
@Slf4j
@Api(tags = "组合方案")
@RestController
@RequestMapping("combination")
public class CombinationController {

    @Autowired
    ICombinationService combinationService;

    /***
     * @description 多条件查询组合方案分页
     * @param combinationVO 组合方案VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CombinationVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "组合方案分页",notes = "组合方案分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "combinationVO",value = "组合方案VO对象",required = true,dataType = "CombinationVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"combinationVO.combinationName","combinationVO.riskAnalysis","combinationVO.riskScenarioJson"})
    public ResponseResult<Page<CombinationVO>> findCombinationVOPage(
                                    @RequestBody CombinationVO combinationVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<CombinationVO> combinationVOPage = combinationService.findPage(combinationVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(combinationVOPage);
    }

    /**
     * @Description 保存组合方案
     * @param combinationVO 组合方案VO对象
     * @return CombinationVO
     */
    @PutMapping
    @ApiOperation(value = "保存Combination",notes = "添加Combination")
    @ApiImplicitParam(name = "combinationVO",value = "组合方案VO对象",required = true,dataType = "CombinationVO")
    @ApiOperationSupport(includeParameters = {"combinationVO.dataState","combinationVO.combinationName","combinationVO.riskAnalysis","combinationVO.riskScenarioJson"})
    public ResponseResult<CombinationVO> createCombination(@RequestBody CombinationVO combinationVO) {
        CombinationVO combinationVOResult = combinationService.save(combinationVO);
        return ResponseResultBuild.successBuild(combinationVOResult);
    }

    /**
     * @Description 修改组合方案
     * @param combinationVO 组合方案VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改组合方案",notes = "修改组合方案")
    @ApiImplicitParam(name = "combinationVO",value = "组合方案VO对象",required = true,dataType = "CombinationVO")
    @ApiOperationSupport(includeParameters = {"combinationVO.id","combinationVO.dataState","combinationVO.combinationName","combinationVO.riskAnalysis","combinationVO.riskScenarioJson"})
    public ResponseResult<Boolean> updateCombination(@RequestBody CombinationVO combinationVO) {
        Boolean flag = combinationService.update(combinationVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除组合方案
     * @param combinationVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除组合方案",notes = "删除组合方案")
    @ApiImplicitParam(name = "combinationVO",value = "组合方案VO对象",required = true,dataType = "CombinationVO")
    @ApiOperationSupport(includeParameters = {"combinationVO.checkedIds"})
    public ResponseResult<Boolean> deleteCombination(@RequestBody CombinationVO combinationVO) {
        Boolean flag = combinationService.delete(combinationVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询组合方案列表
     * @param combinationVO 组合方案VO对象
     * @return List<CombinationVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询组合方案列表",notes = "多条件查询组合方案列表")
    @ApiImplicitParam(name = "combinationVO",value = "组合方案VO对象",required = true,dataType = "CombinationVO")
    @ApiOperationSupport(includeParameters = {"combinationVO.combinationName","combinationVO.riskAnalysis","combinationVO.riskScenarioJson"})
    public ResponseResult<List<CombinationVO>> combinationList(@RequestBody CombinationVO combinationVO) {
        List<CombinationVO> combinationVOList = combinationService.findList(combinationVO);
        return ResponseResultBuild.successBuild(combinationVOList);
    }

}
