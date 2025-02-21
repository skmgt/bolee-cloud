package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.CategorySafeguardVO;
import com.itheima.bolee.insurance.service.ICategorySafeguardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：分类保障项响应接口
 */
@Slf4j
@Api(tags = "分类保障项")
@RestController
@RequestMapping("category-safeguard")
public class CategorySafeguardController {

    @Autowired
    ICategorySafeguardService categorySafeguardService;

    /***
     * @description 多条件查询分类保障项列表
     * @param categorySafeguardVO 分类保障项VO对象
     * @return List<CategorySafeguardVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询分类保障项列表",notes = "多条件查询分类保障项列表")
    @ApiImplicitParam(name = "categorySafeguardVO",value = "分类保障项VO对象",required = true,dataType = "CategorySafeguardVO")
    @ApiOperationSupport(includeParameters = {"categorySafeguardVO.categoryNo","categorySafeguardVO.safeguardKey","categorySafeguardVO.sortNo","categorySafeguardVO.remake"})
    public ResponseResult<List<CategorySafeguardVO>> categorySafeguardList(@RequestBody CategorySafeguardVO categorySafeguardVO) {
        List<CategorySafeguardVO> categorySafeguardVOList = categorySafeguardService.findList(categorySafeguardVO);
        return ResponseResultBuild.successBuild(categorySafeguardVOList);
    }

}
