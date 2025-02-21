package com.itheima.bolee.task.listen;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.sink.LogSink;
import com.itheima.bolee.points.feign.BusinessLogFeign;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName LogListen.java
 * @Description 日志监听
 */
@Slf4j
@Component
@EnableBinding(Sink.class)
public class LogListen {

    @Autowired
    BusinessLogFeign businessLogFeign;

    @StreamListener(LogSink.LOG_INPUT)
    public void onMessage(@Payload MqMessage message,
                           @Header(AmqpHeaders.CHANNEL) Channel channel,
                           @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        String jsonContent = message.getContent();
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        LogBusinessVO logBusinessVO = JSONObject.parseObject(jsonContent, LogBusinessVO.class);
        Boolean flag = businessLogFeign.createBusinessLog(logBusinessVO);
        if (flag) {
            //日志记录成功直接消费消息
            channel.basicAck(deliveryTag, false);
        }
    }
}
