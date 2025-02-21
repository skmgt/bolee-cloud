package com.itheima.bolee.instance.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
* @ClassName InsuranceFeignConfig.java
* @Description feign的最优化配置
*/
@EnableFeignClients(basePackages = "com.itheima.bolee.instance.feign")
@Configuration
public class InsuranceFeignConfig {
}
