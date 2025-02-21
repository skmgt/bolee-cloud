package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.hystrix.SaleReportFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName SaleReportFeign.java
 * @Description 销售统计
 */
@FeignClient(value = "points-web",fallback = SaleReportFeignHystrix.class)
public interface SaleReportFeign {


    /**
     * 日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("sale-report-feign/do-insure-details-day-job/{reportTime}")
    public Boolean doInsureDetailsDayJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("sale-report-feign/doInsure-category-job/{reportTime}")
    public Boolean doInsureCategoryJob(@PathVariable("reportTime") String reportTime);

}
