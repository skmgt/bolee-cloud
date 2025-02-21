package com.itheima.bolee.insurance.web;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.dto.InsuranceApproveVO;
import com.itheima.bolee.insurance.service.IInsuranceApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保批信息响应接口
 */
@Slf4j
@Api(tags = "保批信息")
@RestController
@RequestMapping("insurance-approve")
public class InsuranceApproveController {

    @Autowired
    IInsuranceApproveService insuranceApproveService;

    /***
     * @description 多条件查询保批信息分页
     * @param insuranceApproveVO 保批信息VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<InsuranceApproveVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保批信息分页",notes = "保批信息分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "insuranceApproveVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"insuranceApproveVO.changeFieldKey","insuranceApproveVO.changeFieldName","insuranceApproveVO.changeBeforeValue","insuranceApproveVO.changeAfterValue","insuranceApproveVO.insuranceId","insuranceApproveVO.insuranceName","insuranceApproveVO.warrantyNo","insuranceApproveVO.approveState","insuranceApproveVO.applicantId","insuranceApproveVO.applicantName","insuranceApproveVO.applicantTime","insuranceApproveVO.companyNo","insuranceApproveVO.companyName","insuranceApproveVO.insuredId","insuranceApproveVO.insuredName","insuranceApproveVO.sortNo","insuranceApproveVO.remake"})
    public ResponseResult<Page<InsuranceApproveVO>> findInsuranceApproveVOPage(
                                    @RequestBody InsuranceApproveVO insuranceApproveVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<InsuranceApproveVO> insuranceApproveVOPage = insuranceApproveService.findPage(insuranceApproveVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(insuranceApproveVOPage);
    }

    /**
     * @Description 保存保批信息
     * @return InsuranceApproveVO
     */
    @PostMapping
    @ApiOperation(value = "保存InsuranceApprove",notes = "添加InsuranceApprove")
    @ApiImplicitParam(name = "insuranceApproveVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveVO.dataState","insuranceApproveVO.changeFieldKey","insuranceApproveVO.changeFieldName","insuranceApproveVO.changeBeforeValue","insuranceApproveVO.changeAfterValue","insuranceApproveVO.insuranceId","insuranceApproveVO.insuranceName","insuranceApproveVO.warrantyNo","insuranceApproveVO.approveState","insuranceApproveVO.applicantId","insuranceApproveVO.applicantName","insuranceApproveVO.applicantTime","insuranceApproveVO.companyNo","insuranceApproveVO.companyName","insuranceApproveVO.insuredId","insuranceApproveVO.insuredName","insuranceApproveVO.sortNo","insuranceApproveVO.remake"})
    public ResponseResult<InsuranceApproveVO> createInsuranceApprove(@RequestBody InsuranceApproveVO insuranceApproveVO) {
        InsuranceApproveVO res = insuranceApproveService.save(insuranceApproveVO);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * @Description 修改保批信息
     * @param insuranceApproveVO 保批信息VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保批信息",notes = "修改保批信息")
    @ApiImplicitParam(name = "insuranceApproveVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveVO.id","insuranceApproveVO.dataState","insuranceApproveVO.changeFieldKey","insuranceApproveVO.changeFieldName","insuranceApproveVO.changeBeforeValue","insuranceApproveVO.changeAfterValue","insuranceApproveVO.insuranceId","insuranceApproveVO.insuranceName","insuranceApproveVO.warrantyNo","insuranceApproveVO.approveState","insuranceApproveVO.applicantId","insuranceApproveVO.applicantName","insuranceApproveVO.applicantTime","insuranceApproveVO.companyNo","insuranceApproveVO.companyName","insuranceApproveVO.insuredId","insuranceApproveVO.insuredName","insuranceApproveVO.sortNo","insuranceApproveVO.remake"})
    public ResponseResult<Boolean> updateInsuranceApprove(@RequestBody InsuranceApproveVO insuranceApproveVO) {
        Boolean flag = insuranceApproveService.update(insuranceApproveVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除保批信息
     * @param insuranceApproveVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除保批信息",notes = "删除保批信息")
    @ApiImplicitParam(name = "insuranceApproveVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveVO.checkedIds"})
    public ResponseResult<Boolean> deleteInsuranceApprove(@RequestBody InsuranceApproveVO insuranceApproveVO) {
        Boolean flag = insuranceApproveService.delete(insuranceApproveVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保批信息列表
     * @param insuranceApproveVO 保批信息VO对象
     * @return List<InsuranceApproveVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保批信息列表",notes = "多条件查询保批信息列表")
    @ApiImplicitParam(name = "insuranceApproveVO",value = "保批信息VO对象",required = true,dataType = "InsuranceApproveVO")
    @ApiOperationSupport(includeParameters = {"insuranceApproveVO.changeFieldKey","insuranceApproveVO.changeFieldName","insuranceApproveVO.changeBeforeValue","insuranceApproveVO.changeAfterValue","insuranceApproveVO.insuranceId","insuranceApproveVO.insuranceName","insuranceApproveVO.warrantyNo","insuranceApproveVO.approveState","insuranceApproveVO.applicantId","insuranceApproveVO.applicantName","insuranceApproveVO.applicantTime","insuranceApproveVO.companyNo","insuranceApproveVO.companyName","insuranceApproveVO.insuredId","insuranceApproveVO.insuredName","insuranceApproveVO.sortNo","insuranceApproveVO.remake"})
    public ResponseResult<List<InsuranceApproveVO>> insuranceApproveList(@RequestBody InsuranceApproveVO insuranceApproveVO) {
        List<InsuranceApproveVO> insuranceApproveVOList = insuranceApproveService.findList(insuranceApproveVO);
        return ResponseResultBuild.successBuild(insuranceApproveVOList);
    }


    /***
     * @description 根据合同编号获取报批信息
     * @return List<InsuranceApproveVO>
     */
    @PostMapping("approve-warrantyNo/{warrantyNo}")
    @ApiOperation(value = "根据合同编号获取报批信息",notes = "根据合同编号获取报批信息")
    public ResponseResult<List<InsuranceApproveVO>> approveWarrantyNo(@PathVariable String warrantyNo) {
        List<InsuranceApproveVO> insuranceApproveVOList = insuranceApproveService.findByWarrantyNo(warrantyNo);
        return ResponseResultBuild.successBuild(insuranceApproveVOList);
    }
}