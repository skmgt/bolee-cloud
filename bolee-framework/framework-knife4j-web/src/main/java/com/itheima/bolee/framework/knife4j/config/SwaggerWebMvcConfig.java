package com.itheima.bolee.framework.knife4j.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName UserWebMvcConfig.java
 * @Description webMvc高级配置
 */
@Configuration
@ComponentScan("springfox.documentation.swagger.web")
public class SwaggerWebMvcConfig implements WebMvcConfigurer {

    /**
     * 资源路径 映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //支持webjars
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //支持swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        //支持小刀
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
