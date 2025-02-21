package com.itheima.bolee.sms.balancer.impl;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.balancer.BaseSendLoadBalancer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName WeightRandomSend.java
 * @Description 加权随机算法
 */
@Component
public class WeightRandomSend extends BaseSendLoadBalancer {

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        //获得当前模板对应的渠道
        Map<String, String> channelMap = super.getChannelList(SmsTemplates);

        // 取得channel地址List
        Set<String> keySet = channelMap.keySet();
        Iterator<String> iterator = keySet.iterator();

        List<String> serverList = new ArrayList<String>();
        while (iterator.hasNext()) {
            String channel = iterator.next();
            int weight = Integer.valueOf(channelMap.get(channel));
            for (int i = 0; i < weight; i++)
                serverList.add(channel);
        }

        Random random = new Random();
        int randomPos = random.nextInt(serverList.size());

        return serverList.get(randomPos);
    }
}
