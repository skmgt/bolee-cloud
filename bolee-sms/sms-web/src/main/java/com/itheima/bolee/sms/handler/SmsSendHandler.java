package com.itheima.bolee.sms.handler;

import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.pojo.SmsSendRecord;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @ClassName SmsSendHandler.java
 * @Description 邮件发送处理器
 */
public interface SmsSendHandler {

    /***
     * @description 发送短信接口
     * @param smsTemplateVO 模板
     * @param smsChannelVO 渠道
     * @param smsSignVO 签名
     * @param mobiles 手机号
     * @param templateParam 模板动态参数
     */
    Boolean SendSms(
            SmsTemplateVO smsTemplateVO,
            SmsChannelVO smsChannelVO,
            SmsSignVO smsSignVO,
            Set<String> mobiles,
            LinkedHashMap<String, String> templateParam);

    /***
     * @description 查询短信接受情况
     * @param smsSendRecord 发送记录
     * @return
     */
    Boolean querySendSms(SmsSendRecord smsSendRecord);

    /***
     * @description 重试发送
     * @param smsSendRecord
     * @return
     */
    Boolean retrySendSms(SmsSendRecord smsSendRecord);
}
