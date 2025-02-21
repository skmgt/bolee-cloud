package com.itheima.bolee.framework.taskexecutor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName TaskExecutorProperties.java
 * @Description spring-boot的线程池配置
 */
@Data
@ConfigurationProperties(prefix = "spring.task.extcutor")
public class TaskExecutorProperties {

    //核心线程数
    private int corePoolSize;

    //配置最大线程数
    private int maxPoolSize;

    //配置队列大小
    private int queueCapacity;
}

