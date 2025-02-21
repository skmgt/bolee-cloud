package com.itheima.bolee.file.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DictFenginConfig.java
 * @Description feign的最优化配置
 */
@EnableFeignClients(basePackages = "com.itheima.bolee.file.feign")
@Configuration
public class FileFeignConfig {
}
