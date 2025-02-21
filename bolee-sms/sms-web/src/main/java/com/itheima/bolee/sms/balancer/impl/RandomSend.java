package com.itheima.bolee.sms.balancer.impl;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.balancer.BaseSendLoadBalancer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName Random.java
 * @Description 随机（Random）法
 */
@Component
public class RandomSend extends BaseSendLoadBalancer {

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        //获得当前模板对应的渠道
        Map<String, String> channelMap = super.getChannelList(SmsTemplates);

        // 取得channel地址List
        Set<String> keySet = channelMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        Random random = new Random();
        int randomPos = random.nextInt(keyList.size());
        return keyList.get(randomPos);
    }

}
