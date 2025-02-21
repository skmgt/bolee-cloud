package com.itheima.bolee.framework.rabbitmq.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName FileSource.java
 * @Description 文件消息队列的发送者
 */
public interface FileSource {

    public static String FILE_OUTPUT = "file-output";

    @Output(FILE_OUTPUT)
    MessageChannel fileOutput();
}
