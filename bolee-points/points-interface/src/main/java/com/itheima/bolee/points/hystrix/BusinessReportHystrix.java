package com.itheima.bolee.points.hystrix;

import com.itheima.bolee.points.feign.BusinessReportFeign;

/**
 * @ClassName BusinessLogHystrix.java
 * @Description TODO
 */
public class BusinessReportHystrix implements BusinessReportFeign {


    @Override
    public Boolean doInsureDpvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean doInsureDuvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean categoryDpvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean doInsureGenderDuvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean doInsureCityDuvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean doInsureConversionDpvJob(String reportTime) {
        return null;
    }

    @Override
    public Boolean doInsureFailDpvJob(String reportTime) {
        return null;
    }
}
