package com.itheima.bolee.task.listen;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.sink.SmsSink;
import com.itheima.bolee.sms.feign.SmsSendFeign;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName SmsListen.java
 * @Description 短信监听
 */
@Slf4j
@Component
public class SmsListen {

    @Autowired
    SmsSendFeign smsSendFeign;

    @StreamListener(SmsSink.SMS_INPUT)
    public void onMessage(@Payload MqMessage message,
                          @Header(AmqpHeaders.CHANNEL) Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        String jsonConten = message.getContent();
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        SendMessageVO sendMessageVO = JSONObject.parseObject(jsonConten, SendMessageVO.class);
        Boolean flag = smsSendFeign.sendSms(sendMessageVO);
        if (flag){
            channel.basicAck(deliveryTag,false);
        }
    }
}
