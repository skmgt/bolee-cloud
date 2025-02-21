package com.itheima.bolee.points.hystrix;

import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.points.feign.BusinessLogFeign;

/**
 * @ClassName SaleReportFeignHystrix.java
 * @Description TODO
 */
public class SaleReportFeignHystrix implements BusinessLogFeign {


    @Override
    public Boolean createBusinessLog(LogBusinessVO logBusinessVO) {
        return null;
    }
}
