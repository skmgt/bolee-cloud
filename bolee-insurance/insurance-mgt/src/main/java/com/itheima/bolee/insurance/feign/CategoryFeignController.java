package com.itheima.bolee.insurance.feign;

import com.itheima.bolee.framework.commons.dto.report.CategoryReportVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.itheima.bolee.insurance.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description：保险分类响应接口
 */
@Slf4j
@Api(tags = "保险分类-feign")
@RestController
@RequestMapping("category-feign")
public class CategoryFeignController {

    @Autowired
    ICategoryService categoryService;

    /***
     * @description 多条件查询保险分类列表
     * @param categoryReportVO 保险分类VO对象
     * @return List<CategoryVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险分类列表",notes = "多条件查询保险分类列表")
    @ApiImplicitParam(name = "categoryReportVO",value = "保险分类报表VO对象",required = true,dataType = "CategoryReportVO")
    public List<CategoryReportVO> categoryList(@RequestBody CategoryReportVO categoryReportVO) {
        CategoryVO categoryVO = BeanConv.toBean(categoryReportVO, CategoryVO.class);
        return BeanConv.toBeanList(categoryService.findList(categoryVO),CategoryReportVO.class);
    }
}
