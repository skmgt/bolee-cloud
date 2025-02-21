package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.insurance.enums.InsuranceApproveDetailEnum;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.insurance.dto.InsuranceApproveDetailVO;
import com.itheima.bolee.insurance.service.IInsuranceApproveDetailService;
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
 * @Description：保批信息响应接口
 */
@Slf4j
@Api(tags = "保批信息详情")
@RestController
@RequestMapping("insurance-approve-detail")
public class InsuranceApproveDetailController {

    @Autowired
    IInsuranceApproveDetailService insuranceApproveDetailService;

    /***
     * @description 多条件查询保批信息分页
     * @param insuranceApproveDetailVO 保批信息VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<InsuranceApproveDetailVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保批信息分页",notes = "保批信息分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "insuranceApproveDetailVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveDetailVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"insuranceApproveDetailVO.approveId","insuranceApproveDetailVO.type","insuranceApproveDetailVO.changeFieldKey","insuranceApproveDetailVO.title","insuranceApproveDetailVO.label","insuranceApproveDetailVO.modifyCont","insuranceApproveDetailVO.modifyReason","insuranceApproveDetailVO.approveTime","insuranceApproveDetailVO.sortNo"})
    public ResponseResult<Page<InsuranceApproveDetailVO>> findInsuranceApproveDetailVOPage(
                                    @RequestBody InsuranceApproveDetailVO insuranceApproveDetailVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<InsuranceApproveDetailVO> insuranceApproveDetailVOPage = insuranceApproveDetailService.findPage(insuranceApproveDetailVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(insuranceApproveDetailVOPage);
    }

    /**
     * @Description 保存保批信息
     * @param insuranceApproveDetailVO 保批信息VO对象
     * @return InsuranceApproveDetailVO
     */
    @PutMapping
    @ApiOperation(value = "保存InsuranceApproveDetail",notes = "添加InsuranceApproveDetail")
    @ApiImplicitParam(name = "insuranceApproveDetailVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveDetailVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveDetailVO.dataState","insuranceApproveDetailVO.approveId","insuranceApproveDetailVO.type","insuranceApproveDetailVO.changeFieldKey","insuranceApproveDetailVO.title","insuranceApproveDetailVO.label","insuranceApproveDetailVO.modifyCont","insuranceApproveDetailVO.modifyReason","insuranceApproveDetailVO.approveTime","insuranceApproveDetailVO.sortNo"})
    public ResponseResult<InsuranceApproveDetailVO> createInsuranceApproveDetail(@RequestBody InsuranceApproveDetailVO insuranceApproveDetailVO) {
        InsuranceApproveDetailVO insuranceApproveDetailVOResult = insuranceApproveDetailService.save(insuranceApproveDetailVO);
        return ResponseResultBuild.successBuild(insuranceApproveDetailVOResult);
    }

    /**
     * @Description 修改保批信息
     * @param insuranceApproveDetailVO 保批信息VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保批信息",notes = "修改保批信息")
    @ApiImplicitParam(name = "insuranceApproveDetailVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveDetailVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveDetailVO.id","insuranceApproveDetailVO.dataState","insuranceApproveDetailVO.approveId","insuranceApproveDetailVO.type","insuranceApproveDetailVO.changeFieldKey","insuranceApproveDetailVO.title","insuranceApproveDetailVO.label","insuranceApproveDetailVO.modifyCont","insuranceApproveDetailVO.modifyReason","insuranceApproveDetailVO.approveTime","insuranceApproveDetailVO.sortNo"})
    public ResponseResult<Boolean> updateInsuranceApproveDetail(@RequestBody InsuranceApproveDetailVO insuranceApproveDetailVO) {
        Boolean flag = insuranceApproveDetailService.update(insuranceApproveDetailVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除保批信息
     * @param insuranceApproveDetailVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除保批信息",notes = "删除保批信息")
    @ApiImplicitParam(name = "insuranceApproveDetailVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveDetailVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveDetailVO.checkedIds"})
    public ResponseResult<Boolean> deleteInsuranceApproveDetail(@RequestBody InsuranceApproveDetailVO insuranceApproveDetailVO) {
        Boolean flag = insuranceApproveDetailService.delete(insuranceApproveDetailVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保批信息列表
     * @param insuranceApproveDetailVO 保批信息VO对象
     * @return List<InsuranceApproveDetailVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保批信息列表",notes = "多条件查询保批信息列表")
    @ApiImplicitParam(name = "insuranceApproveDetailVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveDetailVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveDetailVO.approveId","insuranceApproveDetailVO.type","insuranceApproveDetailVO.changeFieldKey","insuranceApproveDetailVO.title","insuranceApproveDetailVO.label","insuranceApproveDetailVO.modifyCont","insuranceApproveDetailVO.modifyReason","insuranceApproveDetailVO.approveTime","insuranceApproveDetailVO.sortNo"})
    public ResponseResult<List<InsuranceApproveDetailVO>> insuranceApproveDetailList(@RequestBody InsuranceApproveDetailVO insuranceApproveDetailVO) {
        List<InsuranceApproveDetailVO> insuranceApproveDetailVOList = insuranceApproveDetailService.findList(insuranceApproveDetailVO);
        return ResponseResultBuild.successBuild(insuranceApproveDetailVOList);
    }
}