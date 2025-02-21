package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.category.CategoryConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.constant.InsuranceConstant;
import com.itheima.bolee.insurance.dto.BannerVO;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.itheima.bolee.insurance.dto.CombinationVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName IndexController.java
 * @Description 销售平台首页
 */
@Slf4j
@Api(tags = "销售平台首页")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IInsuranceService insuranceService;

    @Autowired
    private IInsurancePlanService iInsurancePlanService;

    @Autowired
    private ICombinationService combinationService;

    @Autowired
    IBannerService bannerService;

    /**
     * @Description 分类树形
     * @param categoryVO 分类对象
     * @return
     */
    @PostMapping("tree")
    @ApiOperation(value = "分类树形",notes = "分类树形")
    @ApiImplicitParam(name = "categoryVO",value = "分类对象",required = false,dataType = "CategoryVO")
    @ApiOperationSupport(includeParameters = {"categoryVO.parentCategoryNo","categoryVO.categoryType","categoryVO.checkedCategoryNos"})
    public ResponseResult<TreeVO> categoryTreeVO(@RequestBody CategoryVO categoryVO) {
        TreeVO treeVO = categoryService.categoryTreeVO(categoryVO.getParentCategoryNo(), categoryVO.getCategoryType(),categoryVO.getCheckedCategoryNos());
        return ResponseResultBuild.successBuild(treeVO);
    }

    /**
     * 获取所有的推荐分类
     *
     * @return List<CategoryVO>
     * @description 获取首页分类-->tab_category-->category_type:1
     */
    @ApiOperation(value = "产品分类", notes = "产品分类")
    @PostMapping("/find-index-category-product")
    public ResponseResult<List<CategoryVO>> findIndexCategory() {
        List<Long> nodeFloors = Lists.newArrayList(2L);
        CategoryVO categoryVO = CategoryVO.builder().
                categoryType(CategoryConstant.PRODUCT_TYPE).
                dataState(CategoryConstant.DATA_STATE_0).
                showIndex(CategoryConstant.SHOW_INDEX_STATE_0).
                build();
        categoryVO.setNodeFloors(nodeFloors);
        return ResponseResultBuild.successBuild(categoryService.findList(categoryVO));
    }

    /**
     * @description 热点保险推荐-找到每个分类下售卖的最好的一个保险
     * 热点保险推荐-->tab_insurance-->show_index:0并且是上架且有效状态--->附件调用fegin-->file
     * @return: Page<InsureVO>
     */
    @PostMapping("find-hotspot-insurePage")
    @ApiOperation(value = "热点保险推荐", notes = "APP首页热点保险推荐分页")
    public ResponseResult<List<InsuranceVO>> findHotspotInsurePage() {
        InsuranceVO insuranceVO = InsuranceVO.builder().
                showIndex(InsureConstant.SHOW_INDEX_STATE_0).
                dataState(InsureConstant.DATA_STATE_0).
                build();
        Page<InsuranceVO> page = insuranceService.findPage(insuranceVO, 0, 10);
        return ResponseResultBuild.successBuild(page.getRecords());
    }

    /**
     * 获取所有的产品分类
     * 产品分类-->tab_category-->category_type:1
     */
    @ApiOperation(value = "推荐分类", notes = "推荐分类")
    @PostMapping("/find-index-category-recommend")
    public ResponseResult<List<CategoryVO>> findCategoryRecommend() {
        CategoryVO categoryVO = CategoryVO.builder().
            categoryType(CategoryConstant.RECOMMEND_TYPE).
            dataState(CategoryConstant.DATA_STATE_0).
            showIndex(CategoryConstant.SHOW_INDEX_STATE_0).
            build();
        return ResponseResultBuild.successBuild(categoryService.findList(categoryVO));
    }

    //推荐保险列表-->tab_insurance[tab_insurance_plan价格排序]-->optimization升序再按创建时间--->附件调用fegin-->file

    /**
     * @description 推荐保险列表-
     * 推荐保险列表-->tab_insurance[tab_insurance_plan价格排序]-->optimization升序再按创建时间--->附件调用fegin-->file
     * @return: Page<InsureVO>
     */
    @PostMapping("recommend-insure-list")
    @ApiOperation(value = "推荐保险列表", notes = "推荐保险列表")
    @ApiImplicitParam(name = "recommendCategoryNo", value = "保险类型", required = false, dataType = "String")
    public ResponseResult<List<InsuranceVO>> recommendInsureList(@RequestParam(value = "recommendCategoryNo",required = false) String recommendCategoryNo) {
        InsuranceVO insuranceVO = InsuranceVO.builder().
                showIndex(InsureConstant.SHOW_INDEX_STATE_0).
                dataState(InsureConstant.DATA_STATE_0).
                build();
        if (!EmptyUtil.isNullOrEmpty(recommendCategoryNo)){
            insuranceVO.setRecommendCategoryNo(Long.valueOf(recommendCategoryNo));
        }
        Page<InsuranceVO> page = insuranceService.findPage(insuranceVO, 0, 10);
        return ResponseResultBuild.successBuild(page.getRecords());
    }

    /**
     * 金牌好品
     * @param category 保险分类编号
     * @description 金牌好品、安心培
     * -->tab_insurance-->optimization 或者 relieved
     */
    @PostMapping("glod-insurance-list/{category}")
    @ApiOperation(value = "金牌好品", notes = "金牌好品")
    @ApiImplicitParam(name = "category", value = "保险分类编号", required = true, dataType = "Long", paramType = "path", example = "分类接口中获得的内容")
    public ResponseResult<List<InsuranceVO>> glodInsurancelist(@PathVariable("category") String category) {
        InsuranceVO insuranceVO = InsuranceVO.builder().
                dataState(SuperConstant.DATA_STATE_0).
                categoryNo(Long.valueOf(category)).
                goldSelection(InsuranceConstant.GOLDSELECTION_0).
                build();
        Page<InsuranceVO> insuranceVOPage = insuranceService.findPage(insuranceVO, 0, 10);
        return ResponseResultBuild.successBuild(insuranceVOPage.getRecords());
    }

    /**
     * @description 保险方案
     */
    @PostMapping("insurance-plan")
    @ApiOperation(value = "保险方案", notes = "保险方案")
    public ResponseResult<List<CombinationVO>> insurancePlan() {
        Page<CombinationVO> page = combinationService.findPage(CombinationVO.builder().dataState(SuperConstant.DATA_STATE_0).build(), 0, 6);
        return ResponseResultBuild.successBuild(page.getRecords());
    }

    /***
     * @description 多条件查询列表
     * @return List<BannerVO>
     */
    @GetMapping("list")
    @ApiOperation(value = "banner查询列表",notes = "banner查询列表")
    public ResponseResult<List<BannerVO>> bannerList() {
        BannerVO bannerVO = BannerVO.builder()
            .dataState(SuperConstant.DATA_STATE_0)
            .build();
        List<BannerVO> bannerVOList = bannerService.findList(bannerVO);
        return ResponseResultBuild.successBuild(bannerVOList);
    }
}
