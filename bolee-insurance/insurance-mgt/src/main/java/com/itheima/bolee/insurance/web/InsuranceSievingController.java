package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.insurance.enums.InsuranceSievingEnum;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.insurance.dto.InsuranceSievingVO;
import com.itheima.bolee.insurance.service.IInsuranceSievingService;
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
 * @Description：初筛结果响应接口
 */
@Slf4j
@Api(tags = "初筛结果")
@RestController
@RequestMapping("insurance-sieving")
public class InsuranceSievingController {

    @Autowired
    IInsuranceSievingService insuranceSievingService;

    /***
     * @description 多条件查询初筛结果分页
     * @param insuranceSievingVO 初筛结果VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<InsuranceSievingVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "初筛结果分页",notes = "初筛结果分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "insuranceSievingVO",value = "初筛结果VO对象",required = true,dataType = "InsuranceSievingVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"insuranceSievingVO.name","insuranceSievingVO.sicksName","insuranceSievingVO.sicksKey","insuranceSievingVO.result","insuranceSievingVO.sortNo"})
    public ResponseResult<Page<InsuranceSievingVO>> findInsuranceSievingVOPage(
                                    @RequestBody InsuranceSievingVO insuranceSievingVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<InsuranceSievingVO> insuranceSievingVOPage = insuranceSievingService.findPage(insuranceSievingVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(insuranceSievingVOPage);
    }

    /**
     * @Description 保存初筛结果
     * @param insuranceSievingVO 初筛结果VO对象
     * @return InsuranceSievingVO
     */
    @PutMapping
    @ApiOperation(value = "保存InsuranceSieving",notes = "添加InsuranceSieving")
    @ApiImplicitParam(name = "insuranceSievingVO",value = "初筛结果VO对象",required = true,dataType = "InsuranceSievingVO")
    @ApiOperationSupport(includeParameters = {"insuranceSievingVO.dataState","insuranceSievingVO.name","insuranceSievingVO.sicksName","insuranceSievingVO.sicksKey","insuranceSievingVO.result","insuranceSievingVO.sortNo"})
    public ResponseResult<InsuranceSievingVO> createInsuranceSieving(@RequestBody InsuranceSievingVO insuranceSievingVO) {
        InsuranceSievingVO insuranceSievingVOResult = insuranceSievingService.save(insuranceSievingVO);
        return ResponseResultBuild.successBuild(insuranceSievingVOResult);
    }

    /**
     * @Description 修改初筛结果
     * @param insuranceSievingVO 初筛结果VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改初筛结果",notes = "修改初筛结果")
    @ApiImplicitParam(name = "insuranceSievingVO",value = "初筛结果VO对象",required = true,dataType = "InsuranceSievingVO")
    @ApiOperationSupport(includeParameters = {"insuranceSievingVO.id","insuranceSievingVO.dataState","insuranceSievingVO.name","insuranceSievingVO.sicksName","insuranceSievingVO.sicksKey","insuranceSievingVO.result","insuranceSievingVO.sortNo"})
    public ResponseResult<Boolean> updateInsuranceSieving(@RequestBody InsuranceSievingVO insuranceSievingVO) {
        Boolean flag = insuranceSievingService.update(insuranceSievingVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除初筛结果
     * @param insuranceSievingVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除初筛结果",notes = "删除初筛结果")
    @ApiImplicitParam(name = "insuranceSievingVO",value = "初筛结果VO对象",required = true,dataType = "InsuranceSievingVO")
    @ApiOperationSupport(includeParameters = {"insuranceSievingVO.checkedIds"})
    public ResponseResult<Boolean> deleteInsuranceSieving(@RequestBody InsuranceSievingVO insuranceSievingVO) {
        Boolean flag = insuranceSievingService.delete(insuranceSievingVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询初筛结果列表
     * @param insuranceSievingVO 初筛结果VO对象
     * @return List<InsuranceSievingVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询初筛结果列表",notes = "多条件查询初筛结果列表")
    @ApiImplicitParam(name = "insuranceSievingVO",value = "初筛结果VO对象",required = true,dataType = "InsuranceSievingVO")
    @ApiOperationSupport(includeParameters = {"insuranceSievingVO.name","insuranceSievingVO.sicksName","insuranceSievingVO.sicksKey","insuranceSievingVO.result","insuranceSievingVO.sortNo"})
    public ResponseResult<List<InsuranceSievingVO>> insuranceSievingList(@RequestBody InsuranceSievingVO insuranceSievingVO) {
        List<InsuranceSievingVO> insuranceSievingVOList = insuranceSievingService.findList(insuranceSievingVO);
        return ResponseResultBuild.successBuild(insuranceSievingVOList);
    }

}