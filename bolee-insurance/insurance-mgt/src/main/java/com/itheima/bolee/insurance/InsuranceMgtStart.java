package com.itheima.bolee.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName GateWayStart.java
 * @Description 认证网关启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class InsuranceMgtStart {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceMgtStart.class);
    }
}
