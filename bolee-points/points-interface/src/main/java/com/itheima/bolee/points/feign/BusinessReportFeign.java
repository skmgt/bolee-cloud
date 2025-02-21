package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.hystrix.BusinessLogHystrix;
import com.itheima.bolee.points.hystrix.BusinessReportHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName BusinessReportFeign.java
 * @Description 业务统计
 */
@FeignClient(value = "points-web",fallback = BusinessReportHystrix.class)
public interface BusinessReportFeign {

    /**
     * 日投保访页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-dpv-job/{reportTime}")
    Boolean doInsureDpvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-duv-job/{reportTime}")
    Boolean doInsureDuvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日保险分类访问页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/category-dpv-job/{reportTime}")
    Boolean categoryDpvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 性别日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-gender-duv-job/{reportTime}")
    Boolean doInsureGenderDuvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 城市日投保用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-city-duv-job/{reportTime}")
    Boolean doInsureCityDuvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 投保转换率
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-conversion-dpv-job/{reportTime}")
    Boolean doInsureConversionDpvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日投保访问失败页面量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("business-report-feign/do-insure-fail-dpv-job/{reportTime}")
    Boolean doInsureFailDpvJob(@PathVariable("reportTime") String reportTime);

}
