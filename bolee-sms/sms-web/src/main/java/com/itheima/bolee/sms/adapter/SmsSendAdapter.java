package com.itheima.bolee.sms.adapter;

import com.itheima.bolee.sms.pojo.SmsSendRecord;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @ClassName SmsSendAdapter.java
 * @Description 邮件发送适配器
 */
public interface SmsSendAdapter {

    /***
     * @description 发送短信接口
     * @param templateNo 应用模板编号
     * @param sginNo 应用签名编号
     * @param loadBalancerType 通道负载均衡策略
     * @param mobiles 手机号
     * @param templateParam 模板动态参数
     */
    Boolean SendSms(
            String templateNo,
            String sginNo,
            String loadBalancerType,
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
