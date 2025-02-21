package com.itheima.bolee.sms.handler.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.model.SendMessageItem;
import com.baidubce.services.sms.model.SendMessageV3Request;
import com.baidubce.services.sms.model.SendMessageV3Response;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSendEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.sms.handler.baidu.config.BaiduSmsConfig;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import com.itheima.bolee.sms.service.ISmsChannelService;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import com.itheima.bolee.sms.service.ISmsSignService;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import com.itheima.bolee.sms.handler.SmsSendHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName BaiduSmsSendHandlerImpl.java
 * @Description 百度短信发送
 */
@Slf4j
@Service("baiduSmsSendHandler")
public class BaiduSmsSendHandlerImpl implements SmsSendHandler {

    @Autowired
    BaiduSmsConfig baiduSmsConfig;

    @Autowired
    ISmsSendRecordService smsSendRecordService;

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Autowired
    ISmsChannelService smsChannelService;

    @Autowired
    ISmsSignService smsSignService;

    @Override
    public Boolean SendSms(SmsTemplateVO smsTemplateVO,
                           SmsChannelVO smsChannelVO,
                           SmsSignVO smsSignVO,
                           Set<String> mobiles,
                           LinkedHashMap<String, String> templateParam) {
        //超过发送上限
        if (mobiles.size() > 200) {
            throw new ProjectException(SmsSendEnum.PEXCEED_THE_LIMIT);
        }
        //实例化请求对象
        SendMessageV3Request request = new SendMessageV3Request();
        //手机号码,支持单个或多个手机号，多个手机号之间以英文逗号分隔，
        // 一次请求最多支持200个手机号。国际/港澳台号码请按照E.164规范表示，
        // 例如台湾手机号以+886开头，”+“不能省略。
        request.setMobile(JSONObject.toJSONString(mobiles)
                .replace("\"","")
                .replace("[","")
                .replace("]",""));
        //模板id
        request.setTemplate(smsTemplateVO.getTemplateCode());
        //签名Id
        request.setSignatureId(smsSignVO.getSignCode());
        request.setContentVar(templateParam);
        SmsClient client = baiduSmsConfig.queryClient();
        SendMessageV3Response response = client.sendMessage(request);
        //处理返回结果
        String content = smsTemplateVO.getContent();
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        List<SmsSendRecord> sendRecords = new ArrayList<>();
        List<SendMessageItem> sendMessageItems = response.getData();
        for (SendMessageItem sendMessageItem : sendMessageItems) {
            String acceptStatus = null;
            String acceptMsg = null;
            String sendStatus = null;
            String sendMsg = null;
            if ("1000".equals(sendMessageItem.getCode())){
                //受理成功
                acceptStatus = SmsConstant.STATUS_ACCEPT_0;
                acceptMsg = "受理成功";
                sendStatus = SmsConstant.STATUS_SEND_2;
                sendMsg = "发送中";
            }else {
                //受理失败
                acceptStatus=SmsConstant.STATUS_ACCEPT_1;
                acceptMsg = response.getMessage();
            }
            SmsSendRecord smsSendRecord = SmsSendRecord.builder()
                .sendContent(content)
                .channelLabel(smsChannelVO.getChannelLabel())
                .channelName(smsChannelVO.getChannelName())
                .acceptStatus(acceptStatus)
                .acceptMsg(acceptMsg)
                .sendStatus(sendStatus)
                .sendMsg(sendMsg)
                .mobile(sendMessageItem.getMobile())
                .signCode(smsSignVO.getSignCode())
                .signName(smsSignVO.getSignName())
                .templateNo(smsTemplateVO.getTemplateNo())
                .templateCode(smsTemplateVO.getTemplateCode())
                .templateId(smsTemplateVO.getId())
                .templateType(smsTemplateVO.getSmsType())
                .templateParams(JSONObject.toJSONString(templateParam))
                .build();
            sendRecords.add(smsSendRecord);
        }
        return smsSendRecordService.saveBatch(sendRecords);
    }

    @Override
    public Boolean querySendSms(SmsSendRecord smsSendRecord)throws ProjectException {
        log.warn("百度简单短信无主动轮询接口");
        return false;
    }

    @Override
    public Boolean retrySendSms(SmsSendRecord smsSendRecord) {
        //已发送，发送中的短信不处理
        if (smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_2)||
                smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_0)) {
            throw new ProjectException(SmsSendEnum.SEND_SUCCEED);
        }
        SmsTemplateVO smsTemplateVO = BeanConv.toBean(smsTemplateService.getById(smsSendRecord.getTemplateId()), SmsTemplateVO.class);
        SmsChannelVO smsChannelVO = smsChannelService.findChannelByChannelLabel(smsSendRecord.getChannelLabel());
        SmsSignVO smsSignVO = smsSignService.findSmsSignBySignCodeAndChannelLabel(smsSendRecord.getSignCode(), smsSendRecord.getChannelLabel());
        Set<String> mobiles = new HashSet<>();
        mobiles.add(smsSendRecord.getMobile());
        LinkedHashMap<String, String> templateParam =
                JSON.parseObject(smsSendRecord.getTemplateParams(), LinkedHashMap.class);
        Boolean flag = SendSms(smsTemplateVO, smsChannelVO, smsSignVO, mobiles, templateParam);
        if (flag){
            flag = smsTemplateService.removeById(smsSendRecord.getTemplateId());
        }
        return flag;
    }
}
