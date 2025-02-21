package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.insurance.enums.RiskEnum;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.insurance.dto.RiskVO;
import com.itheima.bolee.insurance.service.IRiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：风险表响应接口
 */
@Slf4j
@Api(tags = "风险表")
@RestController
@RequestMapping("risk")
public class RiskController {

    @Autowired
    IRiskService riskService;

    /***
     * @description 多条件查询风险表分页
     * @param riskVO 风险表VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<RiskVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "风险表分页",notes = "风险表分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "riskVO",value = "风险表VO对象",required = true,dataType = "RiskVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"riskVO.riskTypeKey","riskVO.riskTypeName","riskVO.riskKey","riskVO.riskName","riskVO.sortNo"})
    public ResponseResult<Page<RiskVO>> findRiskVOPage(
                                    @RequestBody RiskVO riskVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<RiskVO> riskVOPage = riskService.findPage(riskVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(riskVOPage);
    }

    /**
     * @Description 保存风险表
     * @param riskVO 风险表VO对象
     * @return RiskVO
     */
    @PutMapping
    @ApiOperation(value = "保存Risk",notes = "添加Risk")
    @ApiImplicitParam(name = "riskVO",value = "风险表VO对象",required = true,dataType = "RiskVO")
    @ApiOperationSupport(includeParameters = {"riskVO.dataState","riskVO.riskTypeKey","riskVO.riskTypeName","riskVO.riskKey","riskVO.riskName","riskVO.sortNo"})
    public ResponseResult<RiskVO> createRisk(@RequestBody RiskVO riskVO) {
        RiskVO riskVOResult = riskService.save(riskVO);
        return ResponseResultBuild.successBuild(riskVOResult);
    }

    /**
     * @Description 修改风险表
     * @param riskVO 风险表VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改风险表",notes = "修改风险表")
    @ApiImplicitParam(name = "riskVO",value = "风险表VO对象",required = true,dataType = "RiskVO")
    @ApiOperationSupport(includeParameters = {"riskVO.id","riskVO.dataState","riskVO.riskTypeKey","riskVO.riskTypeName","riskVO.riskKey","riskVO.riskName","riskVO.sortNo"})
    public ResponseResult<Boolean> updateRisk(@RequestBody RiskVO riskVO) {
        Boolean flag = riskService.update(riskVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除风险表
     * @param riskVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除风险表",notes = "删除风险表")
    @ApiImplicitParam(name = "riskVO",value = "风险表VO对象",required = true,dataType = "RiskVO")
    @ApiOperationSupport(includeParameters = {"riskVO.checkedIds"})
    public ResponseResult<Boolean> deleteRisk(@RequestBody RiskVO riskVO) {
        Boolean flag = riskService.delete(riskVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询风险表列表
     * @param riskVO 风险表VO对象
     * @return List<RiskVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询风险表列表",notes = "多条件查询风险表列表")
    @ApiImplicitParam(name = "riskVO",value = "风险表VO对象",required = true,dataType = "RiskVO")
    @ApiOperationSupport(includeParameters = {"riskVO.riskTypeKey","riskVO.riskTypeName","riskVO.riskKey","riskVO.riskName","riskVO.sortNo"})
    public ResponseResult<List<RiskVO>> riskList(@RequestBody RiskVO riskVO) {
        List<RiskVO> riskVOList = riskService.findList(riskVO);
        return ResponseResultBuild.successBuild(riskVOList);
    }

}
