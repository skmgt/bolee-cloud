package com.itheima.bolee.instance.feign;

import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.instance.hystrix.SaleReportHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * @ClassName SaleReportFeign.java
 * @Description 日投保明细服务接口
 */
@FeignClient(value = "insurance-mgt",fallback = SaleReportHystrix.class)
public interface SaleReportFeign {

    /**
     * 计划任务：日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("sale-report-feign/do-insure-detail-day/{reportTime}")
    SaleReportDTO doInsureDetailDay(@PathVariable("reportTime")String reportTime);

    /**
     * 计划任务：统计日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    @PostMapping("sale-report-feign/do-insure-category/{reportTime}")
    List<InsureCategoryDTO> doInsureCategory(@PathVariable("reportTime")String reportTime);
}
