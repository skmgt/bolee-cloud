package com.itheima.bolee.instance.feign;

import com.itheima.bolee.framework.commons.dto.report.CategoryReportVO;
import com.itheima.bolee.instance.hystrix.AnalysisBusinessHystrix;
import com.itheima.bolee.instance.hystrix.CategoryHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description：保险分类响应接口
 */
@FeignClient(value = "insurance-mgt",fallback = CategoryHystrix.class)
public interface CategoryFeign {

    /***
     * @description 多条件查询保险分类列表
     * @param categoryVO 保险分类VO对象
     * @return List<CategoryVO>
     */
    @PostMapping("category-feign/list")
    public List<CategoryReportVO> categoryList(@RequestBody CategoryReportVO categoryVO);

}
