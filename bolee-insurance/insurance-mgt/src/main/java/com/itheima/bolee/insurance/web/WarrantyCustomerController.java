package com.itheima.bolee.insurance.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.WarrantyApplicantVO;
import com.itheima.bolee.insurance.pojo.WarrantyInsured;
import com.itheima.bolee.insurance.service.IWarrantyCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description：保险客户响应接口
 */
@Slf4j
@Api(tags = "合同客户相关")
@RestController
@RequestMapping("warranty-customer")
public class WarrantyCustomerController {

    @Autowired
    IWarrantyCustomerService warrantyCustomerService;

    /**
     * @param warrantyNo 保单号
     * @return CustomerInfoVO
     * @Description 按保单号查询投保人信息
     */
    @PostMapping("/warranty-applicant/{warrantyNo}")
    @ApiOperation(value = "合同投保人信息", notes = "合同投保人信息")
    @ApiImplicitParam(paramType = "path", name = "warrantyNo", value = "合同编号", dataType = "String")
    public ResponseResult<WarrantyApplicantVO> findWarrantyApplicant(@PathVariable("warrantyNo") String warrantyNo) {
        WarrantyApplicantVO warrantyApplicant = warrantyCustomerService.findWarrantyApplicant(warrantyNo);
        return ResponseResultBuild.successBuild(warrantyApplicant);
    }

    /**
     * @param warrantyNo 身份证
     * @return CustomerInfoVO
     * @Description 按保单号查询被投保人信息
     */
    @PostMapping("/warranty-insured/{warrantyNo}")
    @ApiOperation(value = "合同被保人信息", notes = "合同被保人信息")
    @ApiImplicitParam(paramType = "path", name = "warrantyNo", value = "合同编号", dataType = "String")
    public ResponseResult<List<WarrantyInsured>> findWarrantyInsured(@PathVariable("warrantyNo") String warrantyNo) {
        List<WarrantyInsured> warrantyInsureds = warrantyCustomerService.findWarrantyInsured(warrantyNo);
        return ResponseResultBuild.successBuild(warrantyInsureds);
    }
}