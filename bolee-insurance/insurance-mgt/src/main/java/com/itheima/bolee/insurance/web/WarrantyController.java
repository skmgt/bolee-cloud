package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.WarrantyVO;
import com.itheima.bolee.insurance.service.IWarrantyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险合同响应接口
 */
@Slf4j
@Api(tags = "保险合同")
@RestController
@RequestMapping("warranty")
public class WarrantyController {

    @Autowired
    IWarrantyService warrantyService;

    /***
     * @description 多条件查询保险合同分页
     * @param warrantyVO 保险合同VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<WarrantyVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保险合同分页",notes = "保险合同分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "warrantyVO",value = "保险合同VO对象",required = true,dataType = "WarrantyVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"warrantyVO.insuranceId","warrantyVO.insuranceName","warrantyVO.insuranceJson","warrantyVO.warrantyNo","warrantyVO.applicantName","warrantyVO.applicantIdentityCard","warrantyVO.insuredName","warrantyVO.insuredIdentityCard","warrantyVO.companyNo","warrantyVO.companyName","warrantyVO.safeguardStartTime","warrantyVO.safeguardEndTime","warrantyVO.premium","warrantyVO.paymentMethod","warrantyVO.autoWarrantyExtension","warrantyVO.warrantyState","warrantyVO.underwritingState","warrantyVO.periods","warrantyVO.dutyPeriod","warrantyVO.sortNo","warrantyVO.hesitationTime","warrantyVO.grace","warrantyVO.graceUnit","warrantyVO.revival","warrantyVO.revivalUnit"})
    public ResponseResult<Page<WarrantyVO>> findWarrantyVOPage(
                                    @RequestBody WarrantyVO warrantyVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<WarrantyVO> warrantyVOPage = warrantyService.findPage(warrantyVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(warrantyVOPage);
    }

    /**
     * @Description 保存保险合同
     * @param warrantyVO 保险合同VO对象
     * @return WarrantyVO
     */
    @PutMapping
    @ApiOperation(value = "保存Warranty",notes = "添加Warranty")
    @ApiImplicitParam(name = "warrantyVO",value = "保险合同VO对象",required = true,dataType = "WarrantyVO")
    @ApiOperationSupport(includeParameters = {"warrantyVO.dataState","warrantyVO.insuranceId","warrantyVO.insuranceName","warrantyVO.insuranceJson","warrantyVO.warrantyNo","warrantyVO.applicantName","warrantyVO.applicantIdentityCard","warrantyVO.insuredName","warrantyVO.insuredIdentityCard","warrantyVO.companyNo","warrantyVO.companyName","warrantyVO.safeguardStartTime","warrantyVO.safeguardEndTime","warrantyVO.premium","warrantyVO.paymentMethod","warrantyVO.autoWarrantyExtension","warrantyVO.warrantyState","warrantyVO.underwritingState","warrantyVO.periods","warrantyVO.dutyPeriod","warrantyVO.sortNo","warrantyVO.hesitationTime","warrantyVO.grace","warrantyVO.graceUnit","warrantyVO.revival","warrantyVO.revivalUnit"})
    public ResponseResult<WarrantyVO> createWarranty(@RequestBody WarrantyVO warrantyVO) {
        WarrantyVO warrantyVOResult = warrantyService.save(warrantyVO);
        return ResponseResultBuild.successBuild(warrantyVOResult);
    }

    /**
     * @Description 修改保险合同
     * @param warrantyVO 保险合同VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险合同",notes = "修改保险合同")
    @ApiImplicitParam(name = "warrantyVO",value = "保险合同VO对象",required = true,dataType = "WarrantyVO")
    @ApiOperationSupport(includeParameters = {"warrantyVO.id","warrantyVO.dataState","warrantyVO.insuranceId","warrantyVO.insuranceName","warrantyVO.insuranceJson","warrantyVO.warrantyNo","warrantyVO.applicantName","warrantyVO.applicantIdentityCard","warrantyVO.insuredName","warrantyVO.insuredIdentityCard","warrantyVO.companyNo","warrantyVO.companyName","warrantyVO.safeguardStartTime","warrantyVO.safeguardEndTime","warrantyVO.premium","warrantyVO.paymentMethod","warrantyVO.autoWarrantyExtension","warrantyVO.warrantyState","warrantyVO.underwritingState","warrantyVO.periods","warrantyVO.dutyPeriod","warrantyVO.sortNo","warrantyVO.hesitationTime","warrantyVO.grace","warrantyVO.graceUnit","warrantyVO.revival","warrantyVO.revivalUnit"})
    public ResponseResult<Boolean> updateWarranty(@RequestBody WarrantyVO warrantyVO) {
        Boolean flag = warrantyService.update(warrantyVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除保险合同
     * @param warrantyVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除保险合同",notes = "删除保险合同")
    @ApiImplicitParam(name = "warrantyVO",value = "保险合同VO对象",required = true,dataType = "WarrantyVO")
    @ApiOperationSupport(includeParameters = {"warrantyVO.checkedIds"})
    public ResponseResult<Boolean> deleteWarranty(@RequestBody WarrantyVO warrantyVO) {
        Boolean flag = warrantyService.delete(warrantyVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保险合同列表
     * @param warrantyVO 保险合同VO对象
     * @return List<WarrantyVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险合同列表",notes = "多条件查询保险合同列表")
    @ApiImplicitParam(name = "warrantyVO",value = "保险合同VO对象",required = true,dataType = "WarrantyVO")
    @ApiOperationSupport(includeParameters = {"warrantyVO.insuranceId","warrantyVO.insuranceName","warrantyVO.insuranceJson","warrantyVO.warrantyNo","warrantyVO.applicantName","warrantyVO.applicantIdentityCard","warrantyVO.insuredName","warrantyVO.insuredIdentityCard","warrantyVO.companyNo","warrantyVO.companyName","warrantyVO.safeguardStartTime","warrantyVO.safeguardEndTime","warrantyVO.premium","warrantyVO.paymentMethod","warrantyVO.autoWarrantyExtension","warrantyVO.warrantyState","warrantyVO.underwritingState","warrantyVO.periods","warrantyVO.dutyPeriod","warrantyVO.sortNo","warrantyVO.hesitationTime","warrantyVO.grace","warrantyVO.graceUnit","warrantyVO.revival","warrantyVO.revivalUnit"})
    public ResponseResult<List<WarrantyVO>> warrantyList(@RequestBody WarrantyVO warrantyVO) {
        List<WarrantyVO> warrantyVOList = warrantyService.findList(warrantyVO);
        return ResponseResultBuild.successBuild(warrantyVOList);
    }

    /***
     * @description 核保补发
     * @return List<WarrantyVO>
     */
    @PostMapping("send-check-info")
    @ApiOperation(value = "核保补发",notes = "核保补发")
    public ResponseResult<Boolean> sendCheckInfo() {
        warrantyService.sendCheckInfo();
        return ResponseResultBuild.successBuild(true);
    }


    /***
     * @description 保批补发
     * @return List<WarrantyVO>
     */
    @PostMapping("send-approve-info")
    @ApiOperation(value = "保批补发",notes = "保批补发")
    public ResponseResult<Boolean> sendApproveInfo() {
        warrantyService.sendApproveInfo();
        return ResponseResultBuild.successBuild(true);
    }

    /**
     * @Description 查询保险合同
     * @param warrantyId 保险合同ID
     * @return WarrantyVO
     */
    @PostMapping("detail/{warrantyId}")
    @ApiOperation(value = "查询合同",notes = "查询合同")
    @ApiImplicitParam(paramType = "path",name = "warrantyId",value = "合同ID",dataType = "String")
    public ResponseResult<WarrantyVO> createWarranty(@PathVariable("warrantyId") String warrantyId) {
        WarrantyVO warrantyVOResult = warrantyService.findById(warrantyId);
        return ResponseResultBuild.successBuild(warrantyVOResult);
    }
}
