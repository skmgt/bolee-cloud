package com.itheima.bolee.framework.rabbitmq.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName LogSource.java
 * @Description 日志发送
 */
public interface LogSource {

    public static String LOG_OUTPUT = "log-output";

    @Output(LOG_OUTPUT)
    MessageChannel logOutput();
}
