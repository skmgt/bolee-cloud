package com.itheima.bolee.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName GateWayStart.java
 * @Description 认证网关启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class SecurityWebStart {

    public static void main(String[] args) {
        SpringApplication.run(SecurityWebStart.class);
    }
}
