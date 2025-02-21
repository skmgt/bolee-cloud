package com.itheima.bolee.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName GateWayStart.java
 * @Description 认证网关启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
@EnableDiscoveryClient
public class SecurityOauthStart {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauthStart.class);
    }
}
