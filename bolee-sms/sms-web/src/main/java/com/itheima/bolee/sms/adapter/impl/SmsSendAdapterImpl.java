package com.itheima.bolee.sms.adapter.impl;

import com.alibaba.fastjson.JSONArray;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.sms.adapter.SmsSendAdapter;
import com.itheima.bolee.sms.balancer.SendLoadBalancer;
import com.itheima.bolee.sms.handler.SmsSendHandler;
import com.itheima.bolee.sms.pojo.SmsBlacklist;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import com.itheima.bolee.sms.service.ISmsBlacklistService;
import com.itheima.bolee.sms.service.ISmsChannelService;
import com.itheima.bolee.sms.service.ISmsSignService;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SmsSendAdapterImpl.java
 * @Description 短信适配器实现
 */
@Slf4j
@Component
public class SmsSendAdapterImpl implements SmsSendAdapter {

    @Autowired
    ISmsBlacklistService smsBlacklistService;

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Autowired
    ISmsChannelService smsChannelService;

    @Autowired
    ISmsSignService smsSignService;

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    private static Map<String,String> sendHandlers =new HashMap<>();

    private static Map<String,String> loadBalancers =new HashMap<>();

    static {
        sendHandlers.put(SmsConstant.ALIYUN_SMS,"aliyunSmsSendHandler");
        sendHandlers.put(SmsConstant.TENCENT_SMS,"tencentSmsSendHandler");
        sendHandlers.put(SmsConstant.BAIDU_SMS,"baiduSmsSendHandler");
        loadBalancers.put(SmsConstant.HASH,"hashSend");
        loadBalancers.put(SmsConstant.RANDOM,"randomSend");
        loadBalancers.put(SmsConstant.ROUND_ROBIN,"roundRobinSend");
        loadBalancers.put(SmsConstant.WEIGHT_RANDOM,"weightRandomSend");
        loadBalancers.put(SmsConstant.WEIGHT_ROUND_ROBIN,"weightRoundRobinSend");
    }

    @Override
    public Boolean SendSms(
        String templateNo,
        String sginNo,
        String loadBalancerType,
        Set<String> mobiles,
        LinkedHashMap<String, String> templateParam) {
        //过滤黑名单中电话号码
        List<SmsBlacklist> smsBlacklists = smsBlacklistService.list();
        if (!EmptyUtil.isNullOrEmpty(smsBlacklists)){
            Set<String> smsBlackMobiles= smsBlacklists.stream()
                .map(SmsBlacklist::getMobile)
                .collect(Collectors.toSet());
            mobiles.removeAll(smsBlackMobiles);
        }
        Boolean flag = false;
        //获得审核通过模板模板
        List<SmsTemplateVO> smsTemplateVOs = smsTemplateService.findSmsTemplateByTemplateNo(templateNo);
        if (EmptyUtil.isNullOrEmpty(smsTemplateVOs)){
            log.warn("模板templateNo：{}，不能使用",templateNo);
            return flag;
        }
        //负载均衡器选择对应通道
        String loadBalancersName = loadBalancers.get(loadBalancerType);
        SendLoadBalancer sendLoadBalancer = registerBeanHandler.getBean(loadBalancersName, SendLoadBalancer.class);
        String channelLabel = sendLoadBalancer.chooseChannel(smsTemplateVOs,mobiles);;
        if (EmptyUtil.isNullOrEmpty(channelLabel)){
            log.error("模板templateNo：{}，负载均衡器未找到对应通道",templateNo);
            return flag;
        }
        //查询渠道
        SmsChannelVO smsChannelVO= smsChannelService.findChannelByChannelLabel(channelLabel);
        //选择模板
        SmsTemplateVO smsTemplateVO = smsTemplateVOs.stream()
                .filter(n->n.getChannelLabel().equals(smsChannelVO.getChannelLabel()))
                .findFirst().get();
        //查询签名
        SmsSignVO smsSignVO = smsSignService.findSmsSignBySignNoAndChannelLabel(sginNo, channelLabel);
        if (EmptyUtil.isNullOrEmpty(smsSignVO)){
            log.warn("渠道smsChannel：{}，签名：{}，不能使用",channelLabel,sginNo);
            return flag;
        }
        //应用：应用模板变量转换服务模板参数
        List <OtherConfigVO> otherConfigVos = JSONArray.parseArray(smsTemplateVO.getOtherConfig(), OtherConfigVO.class);
        LinkedHashMap<String,String> otherConfigVoMap = new LinkedHashMap<>();
        for (OtherConfigVO otherConfigVo : otherConfigVos) {
            otherConfigVoMap.put(otherConfigVo.getConfigKey(),otherConfigVo.getConfigValue());
        }
        LinkedHashMap<String,String> templateParamHandler = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            String value = otherConfigVoMap.get(entry.getKey());
            if(!EmptyUtil.isNullOrEmpty(value)){
                templateParamHandler.put(value,entry.getValue());
            }
        }
        //发送短信
        String stringSendHandler = sendHandlers.get(channelLabel);
        SmsSendHandler smsSendHandler =registerBeanHandler.getBean(stringSendHandler,SmsSendHandler.class);
        return smsSendHandler.SendSms(smsTemplateVO,smsChannelVO,smsSignVO,mobiles,templateParamHandler);
    }

    @Override
    public Boolean querySendSms(SmsSendRecord smsSendRecord) {
        String channelLabel = smsSendRecord.getChannelLabel();
        String stringSendHandler = sendHandlers.get(channelLabel);
        SmsSendHandler smsSendHandler =registerBeanHandler.getBean(stringSendHandler,SmsSendHandler.class);
        return smsSendHandler.querySendSms(smsSendRecord);
    }

    @Override
    public Boolean retrySendSms(SmsSendRecord smsSendRecord) {
        String channelLabel = smsSendRecord.getChannelLabel();
        String stringSendHandler = sendHandlers.get(channelLabel);
        SmsSendHandler smsSendHandler =registerBeanHandler.getBean(stringSendHandler,SmsSendHandler.class);
        return smsSendHandler.retrySendSms(smsSendRecord);
    }

}
