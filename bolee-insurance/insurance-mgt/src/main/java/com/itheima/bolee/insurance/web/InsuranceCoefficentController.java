package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.InsuranceCoefficentVO;
import com.itheima.bolee.insurance.service.IInsuranceCoefficentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险系数项响应接口
 */
@Slf4j
@Api(tags = "保险系数项")
@RestController
@RequestMapping("insurance-coefficent")
public class InsuranceCoefficentController {

    @Autowired
    IInsuranceCoefficentService insuranceCoefficentService;

    /**
     * @Description 保存保险系数项
     * @param insuranceCoefficentVOs 保险系数项VO对象
     * @return Boolean 是否修改成功
     */
    @PutMapping
    @ApiOperation(value = "保存InsuranceCoefficent",notes = "添加InsuranceCoefficent")
    @ApiImplicitParam(name = "insuranceCoefficentVOs",value = "保险系数项VO对象",required = true,dataType = "InsuranceCoefficentVO")
    public ResponseResult<Boolean> createInsuranceCoefficent(@RequestBody List<InsuranceCoefficentVO> insuranceCoefficentVOs) {
        Boolean flag = insuranceCoefficentService.save(insuranceCoefficentVOs);
        return ResponseResultBuild.successBuild(flag);
    }


    /**
     * @Description 修改保险系数项
     * @param insuranceCoefficentVOs 保险系数项VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险系数项",notes = "修改保险系数项")
    @ApiImplicitParam(name = "insuranceCoefficentVOs",value = "保险系数项VO对象",required = true,dataType = "InsuranceCoefficentVO")
    public ResponseResult<Boolean> updateInsuranceCoefficent(@RequestBody List<InsuranceCoefficentVO> insuranceCoefficentVOs) {
        Boolean flag = insuranceCoefficentService.update(insuranceCoefficentVOs);
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保险系数项列表
     * @param insuranceCoefficentVO 保险系数项VO对象
     * @return List<InsuranceCoefficentVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险系数项列表",notes = "多条件查询保险系数项列表")
    @ApiImplicitParam(name = "insuranceCoefficentVO",value = "保险系数项VO对象",required = true,dataType = "InsuranceCoefficentVO")
    @ApiOperationSupport(includeParameters = {"insuranceCoefficentVO.insuranceId","insuranceCoefficentVO.coefficentKey","insuranceCoefficentVO.coefficentKeyName","insuranceCoefficentVO.coefficentValue","insuranceCoefficentVO.score","insuranceCoefficentVO.isDefault","insuranceCoefficentVO.sortNo"})
    public ResponseResult<List<InsuranceCoefficentVO>> insuranceCoefficentList(@RequestBody InsuranceCoefficentVO insuranceCoefficentVO) {
        List<InsuranceCoefficentVO> insuranceCoefficentVOList = insuranceCoefficentService.findList(insuranceCoefficentVO);
        return ResponseResultBuild.successBuild(insuranceCoefficentVOList);
    }


}
