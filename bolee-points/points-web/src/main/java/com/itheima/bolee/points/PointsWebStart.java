package com.itheima.bolee.points;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：log项目启动类
 */
@SpringBootApplication(scanBasePackages = {"com.itheima.bolee","com.itheima.bolee.points.mapper"})
public class PointsWebStart {

    public static void main(String[] args) {

        SpringApplication.run(PointsWebStart.class, args);
    }
}
