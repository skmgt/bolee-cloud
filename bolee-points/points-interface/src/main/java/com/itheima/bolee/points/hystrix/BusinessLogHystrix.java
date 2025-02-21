package com.itheima.bolee.points.hystrix;

import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.points.feign.BusinessLogFeign;

/**
 * @ClassName BusinessLogHystrix.java
 * @Description TODO
 */
public class BusinessLogHystrix implements BusinessLogFeign {

    @Override
    public Boolean createBusinessLog(LogBusinessVO logBusinessVO) {
        return null;
    }
}
