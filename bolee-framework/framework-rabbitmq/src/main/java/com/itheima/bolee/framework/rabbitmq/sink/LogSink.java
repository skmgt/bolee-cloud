package com.itheima.bolee.framework.rabbitmq.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName LogSink.java
 * @Description 日志接收
 */
public interface LogSink {

    public static String LOG_INPUT="log-input";

    @Input(LOG_INPUT)
    SubscribableChannel logInput();

}
