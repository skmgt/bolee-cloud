package com.itheima.bolee.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName RestTemplateConfig.java
 * @Description 远程调用
 */
@Configuration
public class RestTemplateConfig {

    //请求客户端
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
