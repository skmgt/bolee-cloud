package com.itheima.bolee.framework.seata;

import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName SeataWebMvcConfig.java
 * @Description webMvc高级配置
 */
@Configuration
public class SeataWebMvcConfig implements WebMvcConfigurer {

    /***
     * @description 解决XID传递问题
     * @return
     */
    @Bean
    public SeataHandlerInterceptor seataHandlerInterceptor(){
        return new SeataHandlerInterceptor();
    }

    /**
     * @Description 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(seataHandlerInterceptor()).addPathPatterns("/**");
    }

}
