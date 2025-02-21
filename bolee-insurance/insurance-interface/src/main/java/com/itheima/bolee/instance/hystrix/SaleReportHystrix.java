package com.itheima.bolee.instance.hystrix;

import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.instance.feign.SaleReportFeign;

import java.util.List;

/**
 * @ClassName SaleReportFeignHystrix.java
 * @Description TODO
 */
public class SaleReportHystrix implements SaleReportFeign {

    @Override
    public SaleReportDTO doInsureDetailDay(String reportTime) {
        return null;
    }

    @Override
    public List<InsureCategoryDTO> doInsureCategory(String reportTime) {
        return null;
    }
}
