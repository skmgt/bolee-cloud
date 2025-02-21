package com.itheima.bolee.task.listen;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.sink.TradeSink;
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
 * @ClassName SmsListen.java
 * @Description 交易业务监听
 */
@Slf4j
@Component
public class TradeListen {

    @Autowired
    WarrantyFeign warrantyFeign;

    @StreamListener(TradeSink.TRADE_INPUT)
    public void onMessage(@Payload MqMessage message,
                          @Header(AmqpHeaders.CHANNEL) Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        String jsonContent = message.getContent();
        log.info("========需要同步结果的合同订单：{}================", jsonContent);
        TradeVO tradeVO = JSONObject.parseObject(jsonContent, TradeVO.class);
        Boolean flag = warrantyFeign.syncPayment(String.valueOf(tradeVO.getProductOrderNo()),tradeVO.getTradeState());
        log.info("========执行结果：{}================", flag);
        //执行成功签收
        if (flag){
            channel.basicAck(deliveryTag,false);
        }
    }
}
