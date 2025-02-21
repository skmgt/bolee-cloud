package com.itheima.bolee.insurance.binding;

import com.itheima.bolee.framework.rabbitmq.source.FileSource;
import com.itheima.bolee.framework.rabbitmq.source.WarrantySource;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @ClassName Binding.java
 * @Description 绑定文件发送者声明
 */
@EnableBinding({WarrantySource.class})
public class SourceBinding {
}
