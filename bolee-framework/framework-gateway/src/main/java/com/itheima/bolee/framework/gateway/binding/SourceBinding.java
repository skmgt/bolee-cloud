package com.itheima.bolee.framework.gateway.binding;

import com.itheima.bolee.framework.rabbitmq.source.LogSource;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @ClassName Binding.java
 * @Description 绑定声明
 */
@EnableBinding({LogSource.class})
public class SourceBinding {
}
