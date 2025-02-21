package com.itheima.bolee.points.feign;

import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.points.hystrix.BusinessLogHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName BusinessLogFeign.java
 * @Description 业务统计
 */
@FeignClient(value = "points-web",fallback = BusinessLogHystrix.class)
public interface BusinessLogFeign {


    /**
     * 持久化日志信息
     * @param logBusinessVO 日志对象
     * @return 是否执行成功
     */
    @PostMapping("business-log-feign/create-business-log")
    public Boolean createBusinessLog(@RequestBody LogBusinessVO logBusinessVO);

}
