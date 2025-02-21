package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.security.hystrix.UserHtstrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName AuthChannelFeign.java
 * @Description TODO
 */
@FeignClient(value = "security-web", fallback = UserHtstrix.class)
public interface AuthChannelFeign {

    /**
     * @Description 按企业编号和配置类型查询对应的企业信息
     * @param companyNo 企业编号
     * @return
     */
    @PostMapping("auth-channel-feign/find-auth-channel/{companyNo}/{channelLabel}")
    public AuthChannelVO findAuthChannelByCompanyNoAndChannelLabel(@PathVariable("companyNo") String companyNo,
                           @PathVariable("channelLabel") String channelLabel);
}
