package com.itheima.bolee.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName BasicJobListenStart.java
 * @Description 基础任务监听启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class TaskListenerStart {

    public static void main(String[] args) {
        SpringApplication.run(TaskListenerStart.class);
    }
}
