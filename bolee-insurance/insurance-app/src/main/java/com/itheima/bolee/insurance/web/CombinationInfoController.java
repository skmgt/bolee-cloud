package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CombinationInsuranceVO;
import com.itheima.bolee.insurance.dto.CombinationVO;
import com.itheima.bolee.insurance.dto.CombinationDetailVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.service.ICombinationInsuranceService;
import com.itheima.bolee.insurance.service.ICombinationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName IndexController.java
 * @Description 保险方案详情
 */
@Slf4j
@Api(tags = "保险方案详情")
@RestController
@RequestMapping("combination-info")
public class CombinationInfoController {

    @Autowired
    private ICombinationInsuranceService combinationService;

    /**
     * 首页保险方案查询
     * @return
     */
    @PostMapping("find-combination-index")
    @ApiOperation(value = "首页保险方案",notes = "首页保险方案")
    public ResponseResult<Page<CombinationInsuranceVO>> findCombinationIndex() {
        return ResponseResultBuild.successBuild(combinationService.findPage(CombinationInsuranceVO.builder().dataState(SuperConstant.DATA_STATE_0).build(),0,5));
    }

    /**
     *保险方案详情页-->tab_combination-->tab_combination_insurance-->tab_insurance-->tab_insurance_plan-->tab_plan_safeguard
     */
    @PostMapping("find-combination/{id}")
    @ApiOperation(value = "保险方案详情",notes = "保险方案详情")
    @ApiImplicitParam(name = "id",value = "保险方案id",required = true,dataType = "String")
    public ResponseResult<CombinationInsuranceVO> findCombination(@PathVariable("id") String id) {
        return ResponseResultBuild.successBuild(combinationService.findById(id));
    }
}
