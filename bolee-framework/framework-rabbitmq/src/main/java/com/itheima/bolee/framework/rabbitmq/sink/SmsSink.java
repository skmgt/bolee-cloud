package com.itheima.bolee.framework.rabbitmq.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName SmsSink.java
 * @Description 短信接受
 */
public interface SmsSink {

    public static String SMS_INPUT="sms-input";

    @Input(SMS_INPUT)
    SubscribableChannel smsInput();
}
