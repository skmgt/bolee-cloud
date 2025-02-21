package com.itheima.bolee.insurance.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.InsuranceConditionVO;
import com.itheima.bolee.insurance.service.IInsuranceConditionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险筛选项响应接口
 */
@Slf4j
@Api(tags = "保险筛选项")
@RestController
@RequestMapping("insurance-condition")
public class InsuranceConditionController {

    @Autowired
    IInsuranceConditionService insuranceConditionService;

    /**
     * @Description 保存保险筛选项
     * @param insuranceConditionListVOs 保险筛选项VO对象
     * @return InsuranceConditionVO
     */
    @PutMapping
    @ApiOperation(value = "保存InsuranceCondition", notes = "添加InsuranceCondition")
    @ApiImplicitParam(name = "insuranceConditionListVOs", value = "保险筛选项VO对象", required = true, dataType = "InsuranceConditionVO")
    public ResponseResult<Boolean> createInsuranceCondition(@RequestBody List<InsuranceConditionVO> insuranceConditionListVOs) {
        Boolean flag = insuranceConditionService.save(insuranceConditionListVOs);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 修改保险筛选项
     * @param insuranceConditionListVOs 保险筛选项VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险筛选项", notes = "修改保险筛选项")
    @ApiImplicitParam(name = "insuranceConditionListVOs", value = "保险筛选项VO对象", required = true, dataType = "InsuranceConditionVO")
    public ResponseResult<Boolean> updateInsuranceCondition(@RequestBody List<InsuranceConditionVO> insuranceConditionListVOs) {
        Boolean flag = insuranceConditionService.update(insuranceConditionListVOs);
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保险筛选项列表
     * @param insuranceConditionVO 保险筛选项VO对象
     * @return List<InsuranceConditionVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险筛选项列表", notes = "多条件查询保险筛选项列表")
    @ApiImplicitParam(name = "insuranceConditionVO", value = "保险筛选项VO对象", required = true, dataType = "InsuranceConditionVO")
    public ResponseResult<List<InsuranceConditionVO>> insuranceConditionList(@RequestBody InsuranceConditionVO insuranceConditionVO) {
        List<InsuranceConditionVO> insuranceConditionVOList = insuranceConditionService.findList(insuranceConditionVO);
        return ResponseResultBuild.successBuild(insuranceConditionVOList);
    }

}
