package com.itheima.bolee.sms.handler.tencent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSendEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.handler.tencent.config.TencentSmsConfig;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import com.itheima.bolee.sms.service.ISmsChannelService;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import com.itheima.bolee.sms.service.ISmsSignService;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import com.itheima.bolee.sms.handler.SmsSendHandler;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName TencentSmsSendHandlerImpl.java
 * @Description 腾讯云邮件发送处理器接口
 */
@Slf4j
@Service("tencentSmsSendHandler")
public class TencentSmsSendHandlerImpl implements SmsSendHandler {

    @Lazy
    @Autowired
    TencentSmsConfig tencentSmsConfig;

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
        // 实例化一个请求对象,每个接口都会对应一个request对象
        SendSmsRequest request = new SendSmsRequest();
        //接收短信的手机号码，Array of String数组格式。
        request.setPhoneNumberSet(mobiles.stream().toArray(String[]::new));
        //短信SdkAppId在短信控制台 添加应用后生成的实际 SdkAppId，
        List <OtherConfigVO> otherConfigVoList = JSONArray.parseArray(smsChannelVO.getOtherConfig(),OtherConfigVO.class);
        for (OtherConfigVO otherConfigVo : otherConfigVoList) {
            if ("smsSdkAppId".equals(otherConfigVo.getConfigKey())){
                request.setSmsSdkAppId(otherConfigVo.getConfigValue());
                break;
            }
        }
        //模板ID必须填写已审核通过的模板 ID
        request.setTemplateId(smsTemplateVO.getTemplateCode());
        //短信签名内容，使用 UTF-8 编码，必须填写已审核通过的签名
        request.setSignName(smsSignVO.getSignName());
        //模板参数，若无模板参数，则设置为空。
        if (!EmptyUtil.isNullOrEmpty(templateParam)){
            LinkedList<String> templateParamList = new LinkedList<>();
            for (Map.Entry<String,String> result:templateParam.entrySet()){
                templateParamList.add(result.getValue());
            }
            request.setTemplateParamSet(templateParamList.stream().toArray(String[]::new));
        }
        //返回的resp是一个SendSmsResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        SendSmsResponse response = null;
        try {
            response = smsClient.SendSms(request);
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云发送短信：{}，失败:{}",request.toString(), ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendEnum.SEND_FAIL);
        }
        SendStatus[] sendStatusSet = response.getSendStatusSet();
        String content = smsTemplateVO.getContent();
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        List<SmsSendRecord> sendRecords = new ArrayList<>();
        for (SendStatus sendStatusHandler : sendStatusSet) {
            String code = sendStatusHandler.getCode();
            String acceptStatus = null;
            String acceptMsg = null;
            String sendStatus = null;
            String sendMsg = null;
            if ("Ok".equals(code)){
                //受理成功
                acceptStatus = SmsConstant.STATUS_ACCEPT_0;
                acceptMsg = "受理成功";
                sendStatus = SmsConstant.STATUS_SEND_2;
                sendMsg = "发送中";
            }else {
                //受理失败
                acceptStatus=SmsConstant.STATUS_ACCEPT_1;
                acceptMsg = sendStatusHandler.getMessage();
                sendStatus = SmsConstant.STATUS_SEND_1;
                sendMsg = "发送失败";
            }
            SmsSendRecord smsSendRecord = SmsSendRecord.builder()
                .sendContent(content)
                .channelLabel(smsChannelVO.getChannelLabel())
                .channelName(smsChannelVO.getChannelName())
                .acceptStatus(acceptStatus)
                .acceptMsg(acceptMsg)
                .sendStatus(sendStatus)
                .sendMsg(sendMsg)
                .mobile(sendStatusHandler.getPhoneNumber())
                .signCode(smsSignVO.getSignCode())
                .signName(smsSignVO.getSignName())
                .templateNo(smsTemplateVO.getTemplateNo())
                .templateNo(smsTemplateVO.getTemplateNo())
                .templateCode(smsTemplateVO.getTemplateCode())
                .templateId(smsTemplateVO.getId())
                .templateType(smsTemplateVO.getSmsType())
                .serialNo(sendStatusHandler.getSerialNo())
                .templateParams(JSONObject.toJSONString(templateParam))
                .build();
            sendRecords.add(smsSendRecord);
        }
        return smsSendRecordService.saveBatch(sendRecords);
    }

    @Override
    public Boolean querySendSms(SmsSendRecord smsSendRecord) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        PullSmsSendStatusByPhoneNumberRequest request = new PullSmsSendStatusByPhoneNumberRequest();
        //拉取起始时间，UNIX 时间戳（时间：秒）。
        //注：最大可拉取当前时期前7天的数据。
        request.setBeginTime(Timestamp.valueOf(smsSendRecord.getCreateTime()).getTime()/1000);
        //偏移量。
        //注：目前固定设置为0。
        request.setOffset(0L);
        //拉取最大条数，最多 100。
        request.setLimit(100L);
        //短信SdkAppId在短信控制台 添加应用后生成的实际 SdkAppId，
        SmsChannelVO smsChannelVO = smsChannelService.findChannelByChannelLabel(smsSendRecord.getChannelLabel());
        List <OtherConfigVO> otherConfigVoList = JSONArray.parseArray(smsChannelVO.getOtherConfig(),OtherConfigVO.class);
        for (OtherConfigVO otherConfigVo : otherConfigVoList) {
            if ("smsSdkAppId".equals(otherConfigVo.getConfigKey())){
                request.setSmsSdkAppId(otherConfigVo.getConfigValue());
                break;
            }
        }
        //下发目的手机号码
        request.setPhoneNumber(smsSendRecord.getMobile());
        //拉取截止时间，UNIX 时间戳（时间：秒）。
        request.setEndTime(new Date().getTime()/1000);
        // 返回的resp是一个PullSmsSendStatusByPhoneNumberResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        PullSmsSendStatusByPhoneNumberResponse response = null;
        try {
            response = smsClient.PullSmsSendStatusByPhoneNumber(request);
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云查询短信发送状态：{}，失败",request.toString());
            throw new ProjectException(SmsSendEnum.QUERY_FAIL);
        }
        PullSmsSendStatus[] pullSmsSendStatusSet = response.getPullSmsSendStatusSet();
        for (PullSmsSendStatus pullSmsSendStatus : pullSmsSendStatusSet) {
            if (pullSmsSendStatus.getSerialNo().equals(smsSendRecord.getSerialNo())){
                if ("SUCCESS".equals(pullSmsSendStatus.getReportStatus())){
                    smsSendRecord.setSendStatus(SmsConstant.STATUS_SEND_0);
                    smsSendRecord.setSendMsg("发送成功");
                    return smsSendRecordService.updateById(smsSendRecord);
                }else if ("FAIL".equals(pullSmsSendStatus.getReportStatus())){
                    smsSendRecord.setSendStatus(SmsConstant.STATUS_SEND_1);
                    smsSendRecord.setSendMsg("发送失败");
                    return smsSendRecordService.updateById(smsSendRecord);
                }else {
                    log.info("短信：{}，等待回执!",smsSendRecord.toString());
                }
                break;
            }
        }
        return true;
    }

    @Override
    public Boolean retrySendSms(SmsSendRecord smsSendRecord) {
        //已发送，发送中的短信不处理
        if (smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_2)||
            smsSendRecord.getSendStatus().equals(SmsConstant.STATUS_SEND_0)) {
            throw new ProjectException(SmsSendEnum.SEND_SUCCEED);
        }
        SmsTemplateVO smsTemplateVO = BeanConv.toBean(smsTemplateService.getById(smsSendRecord.getTemplateId()),SmsTemplateVO.class);
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
