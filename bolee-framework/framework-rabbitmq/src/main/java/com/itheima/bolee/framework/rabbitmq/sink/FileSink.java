package com.itheima.bolee.framework.rabbitmq.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName LogSink.java
 * @Description 日志接收
 */
public interface FileSink {

    public static String FILE_INPUT="file-input";

    @Input(FILE_INPUT)
    SubscribableChannel fileInput();

}
