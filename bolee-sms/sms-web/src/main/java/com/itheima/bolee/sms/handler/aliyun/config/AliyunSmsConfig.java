package com.itheima.bolee.sms.handler.aliyun.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.service.ISmsChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName AlipayConfig.java
 * @Description 支付宝配置类
 */
@Slf4j
@Configuration
public class AliyunSmsConfig {

    @Autowired
    ISmsChannelService smsChannelService;


    /***
     * @description 获得配置
     * @return
     */
    public Client queryClient(){
        SmsChannelVO smsChannelVO = smsChannelService.findSmsChannelByChannelLabel(SmsConstant.ALIYUN_SMS);
        Config config = new Config()
            // 阿里云AccessKey ID
            .setAccessKeyId(smsChannelVO.getAccessKeyId())
            // 阿里云AccessKey Secret
            .setAccessKeySecret(smsChannelVO.getAccessKeySecret());
        // 访问的域名
        config.endpoint = smsChannelVO.getDomain();
        try {
            return new Client(config);
        } catch (Exception e) {
            log.error("阿里云SMS的配置信息出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            return null;
        }
    }
}
