package com.itheima.bolee.dict.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DictFeignConfig.java
 * @Description feign的最优化配置
 */
@Configuration
@EnableFeignClients(basePackages = "com.itheima.bolee.dict")
public class DictFeignConfig {
}
