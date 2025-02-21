package com.itheima.bolee.framework.rabbitmq.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName SmsSource.java
 * @Description 交易结果发送
 */
public interface TradeSource {

    public static String TRADE_OUTPUT = "trade-output";

    @Output(TRADE_OUTPUT)
    MessageChannel tradeOutput();
}
