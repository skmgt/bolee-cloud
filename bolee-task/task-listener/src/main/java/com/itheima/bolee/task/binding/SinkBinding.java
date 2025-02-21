package com.itheima.bolee.task.binding;

import com.itheima.bolee.framework.rabbitmq.sink.*;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @ClassName Binding.java
 * @Description 绑定声明
 */
@EnableBinding({LogSink.class, SmsSink.class, FileSink.class, TradeSink.class, WarrantySink.class})
public class SinkBinding {
}
