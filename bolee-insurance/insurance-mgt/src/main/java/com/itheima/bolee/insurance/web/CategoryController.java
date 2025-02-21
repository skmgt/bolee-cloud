package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.itheima.bolee.insurance.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险分类响应接口
 */
@Slf4j
@Api(tags = "保险分类")
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    /***
     * @description 多条件查询保险分类分页
     * @param categoryVO 保险分类VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CategoryVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保险分类分页",notes = "保险分类分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "categoryVO",value = "保险分类VO对象",required = true,dataType = "CategoryVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"categoryVO.parentCategoryNo","categoryVO.categoryNo","categoryVO.categoryName","categoryVO.icon","categoryVO.leafNode","categoryVO.showIndex","categoryVO.categoryType","categoryVO.sortNo","categoryVO.remake","categoryVO.insuranceType"})
    public ResponseResult<Page<CategoryVO>> findCategoryVOPage(
                                    @RequestBody CategoryVO categoryVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {

        return null;
    }

    /**
     * @Description 保存保险分类
     * @param categoryVO 保险分类VO对象
     * @return CategoryVO
     */
    @PutMapping
    @ApiOperation(value = "保存Category",notes = "添加Category")
    @ApiImplicitParam(name = "categoryVO",value = "保险分类VO对象",required = true,dataType = "CategoryVO")
    @ApiOperationSupport(includeParameters = {"categoryVO.dataState","categoryVO.parentCategoryNo",
            "categoryVO.categoryName","categoryVO.icon","categoryVO.leafNode","categoryVO.showIndex",
            "categoryVO.categoryType","categoryVO.sortNo","categoryVO.remake","categoryVO.insuranceType",
            "categoryVO.categoryClaimVOs.claimKey","categoryVO.categoryConditionVOs.conditionKey",
            "categoryVO.categoryCoefficentVOs.coefficentKey","categoryVO.categorySafeguardVOs.safeguardKey"})
    public ResponseResult<CategoryVO> createCategory(@RequestBody CategoryVO categoryVO) {
        CategoryVO categoryVOResult = categoryService.save(categoryVO);
        return ResponseResultBuild.successBuild(categoryVOResult);
    }

    /**
     * @Description 修改保险分类
     * @param categoryVO 保险分类VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险分类",notes = "修改保险分类")
    @ApiImplicitParam(name = "categoryVO",value = "保险分类VO对象",required = true,dataType = "CategoryVO")
    @ApiOperationSupport(includeParameters = {"categoryVO.id","categoryVO.dataState","categoryVO.parentCategoryNo",
            "categoryVO.categoryNo","categoryVO.categoryName","categoryVO.icon","categoryVO.leafNode",
            "categoryVO.showIndex","categoryVO.categoryType","categoryVO.sortNo","categoryVO.remake","categoryVO.insuranceType",
            "categoryVO.categoryClaimVOs.claimKey","categoryVO.categoryConditionVOs.conditionKey",
            "categoryVO.categoryCoefficentVOs.coefficentKey","categoryVO.categorySafeguardVOs.safeguardKey"})
    public ResponseResult<Boolean> updateCategory(@RequestBody CategoryVO categoryVO) {
        Boolean flag = categoryService.update(categoryVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除保险分类
     * @param categoryVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除保险分类",notes = "删除保险分类")
    @ApiImplicitParam(name = "categoryVO",value = "保险分类VO对象",required = true,dataType = "CategoryVO")
    @ApiOperationSupport(includeParameters = {"categoryVO.checkedIds"})
    public ResponseResult<Boolean> deleteCategory(@RequestBody CategoryVO categoryVO) {
        Boolean flag = categoryService.delete(categoryVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保险分类列表
     * @param categoryVO 保险分类VO对象
     * @return List<CategoryVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险分类列表",notes = "多条件查询保险分类列表")
    @ApiImplicitParam(name = "categoryVO",value = "保险分类VO对象",required = true,dataType = "CategoryVO")
    @ApiOperationSupport(includeParameters = {"categoryVO.parentCategoryNo","categoryVO.categoryNo","categoryVO.categoryName",
            "categoryVO.icon","categoryVO.leafNode","categoryVO.showIndex","categoryVO.categoryType",
            "categoryVO.sortNo","categoryVO.remake","categoryVO.insuranceType"})
    public ResponseResult<List<CategoryVO>> categoryList(@RequestBody CategoryVO categoryVO) {
        List<CategoryVO> categoryVOList = categoryService.findList(categoryVO);
        return ResponseResultBuild.successBuild(categoryVOList);
    }

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

        return null;
    }

}
