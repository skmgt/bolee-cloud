package com.itheima.bolee.sms.balancer.impl;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.balancer.BaseSendLoadBalancer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RoundRobin.java
 * @Description 轮询（Round Robin）法
 */
@Component
public class RoundRobinSend extends BaseSendLoadBalancer {

    private static Integer pos = 0;

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        //获得当前模板对应的渠道
        Map<String, String> channelMap = super.getChannelList(SmsTemplates);

        // 取得通道地址List
        Set<String> keySet = channelMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        String channelName = null;
        synchronized (pos) {
            if (pos >= keySet.size())
                pos = 0;
            channelName = keyList.get(pos);
            pos ++;
        }
        return channelName;
    }

}
