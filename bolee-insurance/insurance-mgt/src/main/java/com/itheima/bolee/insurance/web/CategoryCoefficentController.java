package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CategoryCoefficentVO;
import com.itheima.bolee.insurance.service.ICategoryCoefficentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：分类系数项响应接口
 */
@Slf4j
@Api(tags = "分类系数项")
@RestController
@RequestMapping("category-coefficent")
public class CategoryCoefficentController {

    @Autowired
    ICategoryCoefficentService categoryCoefficentService;

    /***
     * @description 多条件查询分类系数项列表
     * @param categoryCoefficentVO 分类系数项VO对象
     * @return List<CategoryCoefficentVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询分类系数项列表",notes = "多条件查询分类系数项列表")
    @ApiImplicitParam(name = "categoryCoefficentVO",value = "分类系数项VO对象",required = true,dataType = "CategoryCoefficentVO")
    @ApiOperationSupport(includeParameters = {"categoryCoefficentVO.categoryNo","categoryCoefficentVO.coefficentKey","categoryCoefficentVO.sortNo","categoryCoefficentVO.remake"})
    public ResponseResult<List<CategoryCoefficentVO>> categoryCoefficentList(@RequestBody CategoryCoefficentVO categoryCoefficentVO) {
        List<CategoryCoefficentVO> categoryCoefficentVOList = categoryCoefficentService.findList(categoryCoefficentVO);
        return ResponseResultBuild.successBuild(categoryCoefficentVOList);
    }

}
