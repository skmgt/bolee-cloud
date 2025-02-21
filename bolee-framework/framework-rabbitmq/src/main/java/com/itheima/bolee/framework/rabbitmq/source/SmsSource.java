package com.itheima.bolee.framework.rabbitmq.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName SmsSource.java
 * @Description 短信发送
 */
public interface SmsSource {

    public static String SMS_OUTPUT = "sms-output";

    @Output(SMS_OUTPUT)
    MessageChannel smsOutput();
}
