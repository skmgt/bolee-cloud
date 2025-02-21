package com.itheima.bolee.framework.taskexecutor.config;

import com.itheima.bolee.framework.taskexecutor.properties.TaskExecutorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName TaskExecutorConfig.java
 * @Description spring-boot的线程池配置文件
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(value = TaskExecutorProperties.class)
public class TaskExecutorConfig {

    @Autowired
    TaskExecutorProperties taskExecutorProperties;

    @Bean
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean("analysisExecutor")
    public Executor analysisExecutor() {
        log.info("start analysisExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("analysis-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 搜索记录线程池
     * @return
     */
    @Bean("searchRecordExecutor")
    public Executor searchRecordExecutor() {
        log.info("start searchRecordExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("searchRecordExecutor");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 核保补发线程池
     * @return
     */
    @Bean("approveExecutor")
    public Executor approveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("approve-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 周期扣款线程池
     * @return
     */
    @Bean("periodicPayExecutor")
    public Executor periodicPayExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("periodic-pay-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 周期扣款线程池
     * @return
     */
    @Bean("syncPaymentExecutor")
    public Executor syncPaymentExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("sync-payment-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
