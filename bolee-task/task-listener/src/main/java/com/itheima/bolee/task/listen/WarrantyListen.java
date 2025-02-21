package com.itheima.bolee.task.listen;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.sink.FileSink;
import com.itheima.bolee.framework.rabbitmq.sink.WarrantySink;
import com.itheima.bolee.instance.feign.WarrantyFeign;
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
 * @ClassName WarrantyListen.java
 * @Description 合同延迟处理监听
 */
@Component
@Slf4j
public class WarrantyListen {


    @Autowired
    WarrantyFeign warrantyFeign;

    @StreamListener(WarrantySink.WARRANTY_INPUT)
    public void onMessage(@Payload MqMessage message,
                          @Header(AmqpHeaders.CHANNEL) Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        String jsonContent = message.getContent();
        log.info("========需要处理的合同订单：{}================", jsonContent);
        Boolean flag = warrantyFeign.cleanWarranty(jsonContent);
        log.info("========执行结果：{}================", flag);
        //执行成功签收
        if (flag){
            channel.basicAck(deliveryTag,false);
        }
    }
}
