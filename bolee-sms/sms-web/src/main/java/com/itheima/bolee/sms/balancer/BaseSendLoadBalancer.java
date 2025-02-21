package com.itheima.bolee.sms.balancer;

import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.sms.service.ISmsChannelService;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName abSendLoadBalancer.java
 * @Description TODO
 */
@Component
public class BaseSendLoadBalancer implements SendLoadBalancer {

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Autowired
    ISmsChannelService smsChannelService;

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        return null;
    }

    @Override
    public Map<String, String> getChannelList(List<SmsTemplateVO> SmsTemplates) {
        //处理模板
        Set<String> channelLabelList = SmsTemplates.stream()
                .map(SmsTemplateVO::getChannelLabel).collect(Collectors.toSet());
        //查询模板对应的渠道
        List<SmsChannelVO> smsChannels =smsChannelService.findChannelInChannelLabel(channelLabelList);
        if (!EmptyUtil.isNullOrEmpty(smsChannels)){
            return smsChannels.stream()
                .collect(Collectors.toMap(SmsChannelVO::getChannelLabel, SmsChannelVO::getLevel));
        }
        return null;
    }
}
