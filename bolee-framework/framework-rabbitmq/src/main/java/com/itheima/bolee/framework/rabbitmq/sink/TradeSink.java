package com.itheima.bolee.framework.rabbitmq.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName SmsSink.java
 * @Description 交易结果接受
 */
public interface TradeSink {

    public static String TRADE_INPUT="trade-input";

    @Input(TRADE_INPUT)
    SubscribableChannel tradeInput();
}
