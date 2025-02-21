package com.itheima.bolee.framework.rabbitmq.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName WarrantySink.java
 * @Description 合同延迟接受
 */
public interface WarrantySink {

    public static String WARRANTY_INPUT="warranty-input";

    @Input(WARRANTY_INPUT)
    SubscribableChannel warrantyInput();
}
