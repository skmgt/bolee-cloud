package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CategoryConditionVO;
import com.itheima.bolee.insurance.service.ICategoryConditionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：分类筛选项响应接口
 */
@Slf4j
@Api(tags = "分类筛选项")
@RestController
@RequestMapping("category-condition")
public class CategoryConditionController {

    @Autowired
    ICategoryConditionService categoryConditionService;

    /***
     * @description 多条件查询分类筛选项列表
     * @param categoryConditionVO 分类筛选项VO对象
     * @return List<CategoryConditionVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询分类筛选项列表",notes = "多条件查询分类筛选项列表")
    @ApiImplicitParam(name = "categoryConditionVO",value = "分类筛选项VO对象",required = true,dataType = "CategoryConditionVO")
    @ApiOperationSupport(includeParameters = {"categoryConditionVO.categoryNo","categoryConditionVO.conditionKey","categoryConditionVO.sortNo","categoryConditionVO.remake"})
    public ResponseResult<List<CategoryConditionVO>> categoryConditionList(@RequestBody CategoryConditionVO categoryConditionVO) {
        List<CategoryConditionVO> categoryConditionVOList = categoryConditionService.findList(categoryConditionVO);
        return ResponseResultBuild.successBuild(categoryConditionVOList);
    }

}
