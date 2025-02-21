package com.itheima.bolee.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.itheima.bolee.Validator;
import com.itheima.bolee.Verifier;
import com.itheima.bolee.auth.BoleeValidator;
import com.itheima.bolee.auth.PublicKeyVerifier;

import java.io.IOException;

/**
 * @ClassName NotifiyHandler.java
 * @Description 通知验证及解析出来
 */
public class NotifiyHandler {

    //校验者
    private Validator validator;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public NotifiyHandler(String publicKey) {
        this.validator = new BoleeValidator(new PublicKeyVerifier(publicKey));
    }

    /**
     * 解析通知请求结果
     * @param notifyRequest 微信支付通知请求
     * @return 微信支付通知报文解密结果@StreamListener(FileSink.FILE_INPUT)
     *     public void onMessage(@Payload MqMessage message,
     *                           @Header(AmqpHeaders.CHANNEL) Channel channel,
     *                           @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
     *         String jsonConten = message.getcontent();
     *         log.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
     *         FileVO fileVO= JSONObject.parseObject(jsonConten,FileVO.class);
     *         Boolean responseWrap = fileBusinessFeign.clearFileById(fileVO.getId());
     *         channel.basicAck(deliveryTag,false);
     *
     *     }
     */
    public NotifyResponse parse(NotifyRequest notifyRequest) throws IOException {
        // 验签
        boolean validate = validator.validateNotify(notifyRequest);
        if (!validate){
            throw new RuntimeException("验证签名失败");
        }
        // 解析请求体
        return parseBody(notifyRequest.getBody());
    }

    /**
     * 解析请求体
     *
     * @param body 请求体
     * @return 解析结果
     * @throws IOException 解析body失败
     */
    private NotifyResponse parseBody(String body) throws IOException {
        ObjectReader objectReader = objectMapper.reader();
        NotifyResponse notifyResponse = objectReader.readValue(body, NotifyResponse.class);
        return notifyResponse;
    }
}