package com.itheima.bolee.dict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description：rabbit项目启动类
 */
@SpringBootApplication(scanBasePackages = {"com.itheima.bolee"})
public class DictWebStart {

    public static void main(String[] args) {
        SpringApplication.run(DictWebStart.class, args);
    }
}
