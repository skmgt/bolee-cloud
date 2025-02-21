package com.itheima.bolee.points.feign;

import com.itheima.bolee.points.hystrix.CustomerReportHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName CustomerReportFeign.java
 * @Description 用户统计
 */
@FeignClient(value = "points-web",fallback = CustomerReportHystrix.class)
public interface CustomerReportFeign {


    /**
     * 日新增用户数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dnu-job/{reportTime}")
    Boolean dnuJob(@PathVariable("reportTime") String reportTime);


    /**
     * 日新增注册用户归属城市
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dnu-city-job/{reportTime}")
    Boolean dnuCityJob(@PathVariable("reportTime") String reportTime);

    /**
     * 用户日活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dau-job/{reportTime}")
    Boolean dauJob(@PathVariable("reportTime") String reportTime);

    /**
     * 用户每时活跃数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dau-time-job/{reportTime}")
    Boolean dauTimeJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日访问量
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dpv-job/{reportTime}")
    Boolean dpvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 日用户访问数
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/duv-job/{reportTime}")
    Boolean duvJob(@PathVariable("reportTime") String reportTime);

    /**
     * 用户日活跃数范围
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("customer-report-feign/dau-range-job/{reportTime}")
    Boolean dauRangeJob(@PathVariable("reportTime") String reportTime);
}
