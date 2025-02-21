package com.itheima.bolee.sms.handler.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSendEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.sms.handler.aliyun.config.AliyunSmsConfig;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import com.itheima.bolee.sms.service.ISmsChannelService;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import com.itheima.bolee.sms.service.ISmsSignService;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import com.itheima.bolee.sms.handler.SmsSendHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName SmsSendAdapter.java
 * @Description 阿里云邮件发送处理器接口
 */
@Slf4j
@Service("aliyunSmsSendHandler")
public class AliyunSmsSendHandlerImpl implements SmsSendHandler {

    @Autowired
    AliyunSmsConfig aliyunSmsConfig;

    @Autowired
    ISmsSendRecordService smsSendRecordService;

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Autowired
    ISmsChannelService smsChannelService;

    @Autowired
    ISmsSignService smsSignService;

    @Override
    public Boolean SendSms(SmsTemplateVO smsTemplate,
                           SmsChannelVO smsChannel,
                           SmsSignVO smsSign,
                           Set<String> mobiles,
                           LinkedHashMap<String, String> templateParam) throws ProjectException {
        //超过发送上限
        if (mobiles.size()>1000){
            throw new ProjectException(SmsSendEnum.PEXCEED_THE_LIMIT);
        }
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        //接收短信的手机号码，JSON数组格式。
        request.setPhoneNumberJson(JSONObject.toJSONString(mobiles));
        //签名处理
        String signCode = smsSign.getSignCode();
        List<String> signNames = new ArrayList<>();
        for (String mobile : mobiles) {
            signNames.add(signCode);
        }
        request.setSignNameJson(JSONObject.toJSONString(signNames));
        //模板处理
        request.setTemplateCode(smsTemplate.getTemplateCode());
        //模板参数
        List<Map<String, String>> templateParams = new ArrayList<>();
        for (String mobile : mobiles) {
            templateParams.add(templateParam);
        }
        request.setTemplateParamJson(JSONObject.toJSONString(templateParams));
        //发送短信
        Client client =aliyunSmsConfig.queryClient();
        SendBatchSmsResponse response = null;
        try {
            response = client.sendBatchSms(request);
        } catch (Exception e) {
            log.error("阿里云发送短信：{}，失败",request.toString());
            throw new ProjectException(SmsSendEnum.SEND_FAIL);
        }
        //三方结果
        String code = response.getBody().getCode();
        String acceptStatus = null;
        String acceptMsg = null;
        String sendStatus = null;
        String sendMsg = null;
        if ("OK".equals(code)){
            //受理成功
            acceptStatus = SmsConstant.STATUS_ACCEPT_0;
            acceptMsg = "受理成功";
            sendStatus = SmsConstant.STATUS_SEND_2;
            sendMsg = "发送中";
        }else {
            //受理失败
            acceptStatus=SmsConstant.STATUS_ACCEPT_1;
            acceptMsg = response.getBody().getMessage();
            sendStatus = SmsConstant.STATUS_SEND_1;
            sendMsg = "发送失败";
        }
        //构建发送记录
        String message = response.getBody().getMessage();
        String bizId = response.getBody().getBizId();
        String content = smsTemplate.getContent();
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        List<SmsSendRecord> sendRecords = new ArrayList<>();
        for (String mobile : mobiles) {
            SmsSendRecord smsSendRecord = SmsSendRecord.builder()
                    .sendContent(content)
                    .channelLabel(smsChannel.getChannelLabel())
                    .channelName(smsChannel.getChannelName())
                    .acceptStatus(acceptStatus)
                    .acceptMsg(acceptMsg)
                    .sendStatus(sendStatus)
                    .sendMsg(sendMsg)
                    .mobile(mobile)
                    .signCode(smsSign.getSignCode())
                    .signName(smsSign.getSignName())
                    .templateNo(smsTemplate.getTemplateNo())
                    .templateCode(smsTemplate.getTemplateCode())
                    .templateId(smsTemplate.getId())
                    .templateType(smsTemplate.getSmsType())
                    .serialNo(bizId)
                    .templateParams(JSONObject.toJSONString(templateParam))
                    .build();
            sendRecords.add(smsSendRecord);
        }
        return smsSendRecordService.saveBatch(sendRecords);
    }

    @Override
    public Boolean querySendSms(SmsSendRecord smsSendRecord) throws ProjectException {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        request.setBizId(smsSendRecord.getSerialNo());
        request.setPhoneNumber(smsSendRecord.getMobile());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(simpleDateFormat.format(smsSendRecord.getCreateTime()));
        request.setPageSize(50L);
        request.setCurrentPage(1L);
        // 复制代码运行请自行打印 API 的返回值
        Client client =aliyunSmsConfig.queryClient();
        QuerySendDetailsResponse response = null;
        try {
            response = client.querySendDetails(request);
        } catch (Exception e) {
            log.error("阿里云查询短信发送状态：{}，失败",request.toString());
            throw new ProjectException(SmsSendEnum.QUERY_FAIL);
        }

        String code = response.getBody().getCode();
        //处理结果
        if ("OK".equals(code)){
            QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO result =
                    response.getBody().getSmsSendDetailDTOs().getSmsSendDetailDTO().stream()
                    .filter(n -> n.getPhoneNum().equals(smsSendRecord.getMobile())
                        && n.getTemplateCode().equals(smsSendRecord.getTemplateCode()))
                    .findFirst().get();
            if (result.getSendStatus()==3){
                smsSendRecord.setSendStatus(SmsConstant.STATUS_SEND_0);
                smsSendRecord.setSendMsg("发送成功");
                return smsSendRecordService.updateById(smsSendRecord);
            }else if (result.getSendStatus()==2){
                smsSendRecord.setSendStatus(SmsConstant.STATUS_SEND_1);
                smsSendRecord.setSendMsg("发送失败");
                return smsSendRecordService.updateById(smsSendRecord);
            }else {
                log.info("短信：{}，等待回执!",smsSendRecord.toString());
            }
        }
        return true;
    }

    @Override
    public Boolean retrySendSms(SmsSendRecord smsSendRecord) throws ProjectException {
        //已发送，发送中的短信不处理
        if (smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_0)||
            smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_2)) {
            throw new ProjectException(SmsSendEnum.SEND_SUCCEED);
        }
        SmsTemplateVO smsTemplate = BeanConv.toBean(smsTemplateService.getById(smsSendRecord.getTemplateId()),SmsTemplateVO.class);
        SmsChannelVO smsChannel = smsChannelService.findChannelByChannelLabel(smsSendRecord.getChannelLabel());
        SmsSignVO smsSign = smsSignService.findSmsSignBySignCodeAndChannelLabel(
                smsSendRecord.getSignCode(),
                smsSendRecord.getChannelLabel());
        Set<String> mobiles = new HashSet<>();
        mobiles.add(smsSendRecord.getMobile());
        LinkedHashMap<String, String> templateParam = JSON.parseObject(smsSendRecord.getTemplateParams(), LinkedHashMap.class);
        Boolean flag = SendSms(smsTemplate, smsChannel, smsSign, mobiles, templateParam);
        if (flag){
            flag = smsSendRecordService.removeById(smsSendRecord.getId());
        }
        return flag;
    }
}
