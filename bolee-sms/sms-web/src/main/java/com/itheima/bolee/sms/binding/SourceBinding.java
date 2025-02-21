package com.itheima.bolee.sms.binding;

import com.itheima.bolee.framework.rabbitmq.source.SmsSource;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @ClassName Binding.java
 * @Description 绑定声明
 */
@EnableBinding({SmsSource.class})
public class SourceBinding {
}
