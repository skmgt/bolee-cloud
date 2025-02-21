package com.itheima.bolee.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：rabbit项目启动类
 */
@SpringBootApplication(scanBasePackages = "com.itheima.bolee")
public class FileWebStart {

    public static void main(String[] args) {
        SpringApplication.run(FileWebStart.class, args);
    }
}
