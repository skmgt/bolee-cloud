package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.InsurancePlanVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.service.IInsurancePlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：保险方案响应接口
 */
@Slf4j
@Api(tags = "保险方案")
@RestController
@RequestMapping("insurance-plan")
public class InsurancePlanController {

    @Autowired
    IInsurancePlanService insurancePlanService;

    /**
     * @Description 保存保险方案
     * @param insurancePlanVOs 保险产品方案VO对象列表
     * @return InsurancePlanVO
     */
    @PutMapping
    @ApiOperation(value = "保存InsurancePlan",notes = "添加InsurancePlan")
    @ApiImplicitParam(name = "insurancePlanVOs",value = "保险方案VO对象",required = true,dataType = "InsurancePlanVO")
    public ResponseResult<Boolean> createInsurancePlans(@RequestBody List<InsurancePlanVO> insurancePlanVOs) {
        Boolean flag=insurancePlanService.save(insurancePlanVOs);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 修改保险方案
     * @param insurancePlanVOs 保险产品方案VO对象列表
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险方案",notes = "修改保险方案")
    @ApiImplicitParam(name = "insurancePlanVOs",value = "保险方案VO对象",required = true,dataType = "InsurancePlanVO")
    public ResponseResult<Boolean> updateInsurancePlan(@RequestBody List<InsurancePlanVO> insurancePlanVOs) {
        Boolean flag = insurancePlanService.update(insurancePlanVOs);
        return ResponseResultBuild.successBuild(true);
    }

    /***
     * @description 多条件查询保险方案列表
     * @param insurancePlanVO 保险方案VO对象
     * @return List<InsurancePlanVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险方案列表",notes = "多条件查询保险方案列表")
    @ApiImplicitParam(name = "insurancePlanVO",value = "保险方案VO对象",required = true,dataType = "InsurancePlanVO")
    @ApiOperationSupport(includeParameters = {"insurancePlanVO.insuranceId","insurancePlanVO.palnName","insurancePlanVO.palnRemake","insurancePlanVO.price","insurancePlanVO.priceUnit","insurancePlanVO.rateTopGrade","insurancePlanVO.rateLastYear","insurancePlanVO.rateGuarantee","insurancePlanVO.sortNo"})
    public ResponseResult<List<InsurancePlanVO>> insurancePlanList(@RequestBody InsurancePlanVO insurancePlanVO) {
        List<InsurancePlanVO> insurancePlanVOList = insurancePlanService.findList(insurancePlanVO);
        return ResponseResultBuild.successBuild(insurancePlanVOList);
    }

}
