package com.itheima.bolee.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.SmsSource;
import com.itheima.bolee.sms.adapter.SmsSendAdapter;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import com.itheima.bolee.sms.service.ISmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @ClassName SmsSendFace.java
 * @Description dubbo短信发送接口
 */
@Slf4j
@Component
public class SmsSendServiceImpl implements ISmsSendService {

    @Autowired
    SmsSendAdapter smsSendAdapter;

    @Autowired
    SmsSource smsSource;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @Override
    public Boolean sendSmsForMq(SendMessageVO sendMessageVO) {
        String sendMessageVOstring = JSONObject.toJSONString(sendMessageVO);
        //发送队列信息
        MqMessage mqMessage = MqMessage.builder()
            .id((Long)identifierGenerator.nextId(sendMessageVO))
            .title("sms-message")
            .content(sendMessageVOstring)
            .messageType("sms-request")
            .produceTime(Timestamp.valueOf(LocalDateTime.now()))
            .sender(applicationName+":"+port)
            .build();
        Message<MqMessage> message = MessageBuilder.withPayload(mqMessage)
            .setHeader("type", "sms-key")
            .build();
        Boolean flag =  smsSource.smsOutput().send(message);
        log.info("发送：{}结果：{}",mqMessage.toString(),flag);
        return flag;
    }

    @Override
    @Transactional
    public Boolean sendSms(SendMessageVO sendMessageVO) {
        return smsSendAdapter.SendSms(sendMessageVO.getTemplateNo(),
            sendMessageVO.getSginNo(),
            sendMessageVO.getLoadBalancerType(),
            sendMessageVO.getMobiles(),
            sendMessageVO.getTemplateParam());
    }

    @Override
    @Transactional
    public Boolean querySendSms(SmsSendRecordVO smsSendRecordVO) {
        SmsSendRecord smsSendRecord = BeanConv.toBean(smsSendRecordVO, SmsSendRecord.class);
        return smsSendAdapter.querySendSms(smsSendRecord);
    }

    @Override
    @Transactional
    public Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO) {
        SmsSendRecord smsSendRecord = BeanConv.toBean(smsSendRecordVO, SmsSendRecord.class);
        return smsSendAdapter.retrySendSms(smsSendRecord);
    }
}
