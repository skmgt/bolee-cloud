package com.itheima.bolee.sms.handler.baidu.config;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.http.DefaultRetryPolicy;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.SmsClientConfiguration;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.sms.service.ISmsChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName BaiduSmsConfig.java
 * @Description 百度短信配置类
 */
@Slf4j
@Configuration
public class BaiduSmsConfig {

    @Autowired
    ISmsChannelService smsChannelService;


    /***
     * @description 获得配置
     * @return
     */
    public SmsClient queryClient(){
        SmsChannelVO smsChannelVO = smsChannelService.findSmsChannelByChannelLabel(SmsConstant.BAIDU_SMS);
        //百度云配置
        SmsClientConfiguration config = new SmsClientConfiguration()
                //站点
                .withEndpoint(smsChannelVO.getDomain())
                //百度云密钥
                .withCredentials(new DefaultBceCredentials(smsChannelVO.getAccessKeyId(), smsChannelVO.getAccessKeySecret()))
                // 设置HTTP最大连接数为10
                .withMaxConnections(10)
                // 设置TCP连接超时为5000毫秒
                .withConnectionTimeoutInMillis(5000)
                // 设置默重试最大的重试次数为3
                .withRetryPolicy(new DefaultRetryPolicy())
                //设置Socket传输数据超时的时间为2000毫秒
                .withSocketTimeoutInMillis(2000);
        return new SmsClient(config);
    }
}
