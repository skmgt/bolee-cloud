package com.itheima.bolee.task.listen;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.sink.FileSink;
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
 * @ClassName FileLosten.java
 * @Description TODO
 */
@Component
@Slf4j
public class FileListen {
    
    @Autowired
    FileBusinessFeign fileBusinessFeign;

    @StreamListener(FileSink.FILE_INPUT)
    public void onMessage(@Payload MqMessage message,
                          @Header(AmqpHeaders.CHANNEL) Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        String jsonConten = message.getContent();
        log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        FileVO fileVO= JSONObject.parseObject(jsonConten,FileVO.class);
        Boolean flag = fileBusinessFeign.clearFileById(fileVO.getId());
        //执行成功签收
        if (flag){
            channel.basicAck(deliveryTag,false);
        }
    }
}
