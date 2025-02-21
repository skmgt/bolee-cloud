package com.itheima.bolee.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：项目启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class TaskJobStart {

    public static void main(String[] args) {
        SpringApplication.run(TaskJobStart.class, args);
    }
}
