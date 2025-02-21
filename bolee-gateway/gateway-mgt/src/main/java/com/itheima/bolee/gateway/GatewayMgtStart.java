package com.itheima.bolee.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：后端项目启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class GatewayMgtStart {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMgtStart.class, args);
    }
}
