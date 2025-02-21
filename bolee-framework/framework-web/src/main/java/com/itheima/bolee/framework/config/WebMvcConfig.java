package com.itheima.bolee.framework.config;

import com.itheima.bolee.framework.intercept.CompanyCheckIntercept;
import com.itheima.bolee.framework.intercept.WebAuthIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig.java
 * @Description webMvc高级配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    WebAuthIntercept webAuthIntercept;

    @Autowired
    CompanyCheckIntercept companyCheckIntercept;

    /**
     * @Description 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //多租户拦截
        registry.addInterceptor(webAuthIntercept).addPathPatterns("/**");
        //数字签名拦截
        registry.addInterceptor(companyCheckIntercept).addPathPatterns("/coefficent/**");
    }
}
