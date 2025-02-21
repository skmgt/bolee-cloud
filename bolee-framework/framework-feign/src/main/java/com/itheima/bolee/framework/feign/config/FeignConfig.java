package com.itheima.bolee.framework.feign.config;

import com.itheima.bolee.framework.feign.interceptor.FeignAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FeignConfig.java
 * @Description feign的拦截器配置
 */
@Configuration
public class FeignConfig {

    /**
     * @Description feign自定义拦截器
     * @return
     */
    @Bean
    public FeignAuthInterceptor feignAuthRequestInterceptor(){

        return new FeignAuthInterceptor();
    }

}
