package com.itheima.bolee.framework.rabbitmq.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName WarrantySource.java
 * @Description 合同延迟发送
 */
public interface WarrantySource {

    public static String WARRANTY_OUTPUT = "warranty-output";

    @Output(WARRANTY_OUTPUT)
    MessageChannel warrantyOutput();
}
