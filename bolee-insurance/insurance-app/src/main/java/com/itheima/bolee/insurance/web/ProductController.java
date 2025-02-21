package com.itheima.bolee.insurance.web;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.category.CategoryConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.constant.InsuranceConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.pojo.WarrantyOrder;
import com.itheima.bolee.insurance.service.ICategoryConditionService;
import com.itheima.bolee.insurance.service.ICategoryService;
import com.itheima.bolee.insurance.service.IInsuranceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName ProductController.java
 * @Description 产品
 */
@Slf4j
@Api(tags = "产品")
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICategoryConditionService categoryConditionService;

    @Autowired
    IInsuranceService insuranceService;

    /**
     * @description 获取产品页面下的分类
     * @return TreeVO<CategoryVO>
     */
    @ApiOperation(value = "查询产品页下的产品分类",notes = "查询产品页下的产品分类")
    @PostMapping("/find-product-categorys")
    public ResponseResult<TreeVO> findCategoryByType() {
        TreeVO treeVO = categoryService.categoryTreeVO(SuperConstant.ROOT_PARENT_ID, CategoryConstant.PRODUCT_TYPE, null);
        return ResponseResultBuild.successBuild(treeVO);
    }

    /***
     * @description 查询产品分类下筛选项目
     * @param categoryNo
     * @return ResponseResult<List<CategoryConditionVO>>
     */
    @ApiOperation(value = "查询产品分类下筛选项目",notes = "查询产品分类下筛选项目")
    @PostMapping("/find-condition/{categoryNo}")
    public ResponseResult<List<CategoryConditionVO>> findConditionByCategoryNo(@PathVariable(name = "categoryNo",required = false)String categoryNo) {
        return ResponseResultBuild.successBuild(categoryConditionService.findConditionByType(categoryNo));
    }

    /***
     * @description 查询产品列表
     * @param insuranceVO 搜索对象
     * @return ResponseResult<List<InsuranceVO>>
     */
    @PostMapping("find-insurance-product")
    @ApiOperation(value = "查询产品列表",notes = "查询产品列表")
    public ResponseResult<List<InsuranceVO>> findInsuranceProduct(@RequestBody InsuranceVO insuranceVO){
        insuranceVO.setInsuranceState(InsuranceConstant.INSURANCE_STATE_0);
        return ResponseResultBuild.successBuild(insuranceService.findList(insuranceVO));
    }

    /***
     * @description 保险产品详情
     * @param insuranceId 保险id
     * @return
     */
    @PostMapping("find-insurance/{insuranceId}")
    @ApiOperation(value = "保险产品详情",notes = "保险产品详情")
    @ApiImplicitParam(name = "insuranceId",value = "保险id",required = true,dataType = "String")
    public ResponseResult<InsuranceVO> findInsuranceDetails(@PathVariable("insuranceId")String insuranceId){
        InsuranceVO insuranceVO = insuranceService.findById(insuranceId);
        return ResponseResultBuild.successBuild(insuranceVO);
    }

}
