package com.itheima.bolee.trade.binding;

import com.itheima.bolee.framework.rabbitmq.source.TradeSource;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @ClassName Binding.java
 * @Description 绑定支付发送者声明
 */
@EnableBinding({TradeSource.class})
public class SourceBinding {
}
