package com.itheima.bolee.sms.balancer.impl;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.balancer.BaseSendLoadBalancer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName WeightRoundRobinSend.java
 * @Description 加权轮询（Weight Round Robin）法
 * 与轮询法类似，只是在获取通道之前增加了一段权重计算的代码，根据权重的大小，
 * 将通道重复地增加到服务器地址列表中，权重越大，该服务器每轮所获得的请求数量越多。
 */
@Component
public class WeightRoundRobinSend extends BaseSendLoadBalancer {

    private static Integer pos;

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        //获得当前模板对应的渠道
        Map<String, String> channelMap = super.getChannelList(SmsTemplates);

        // 取得channel地址List
        Set<String> keySet = channelMap.keySet();
        Iterator<String> iterator = keySet.iterator();

        List<String> channelListHandler = new ArrayList<String>();
        while (iterator.hasNext()) {
            String server = iterator.next();
            Integer weight = Integer.valueOf(channelMap.get(server));
            for (int i = 0; i < weight; i++)
                channelListHandler.add(server);
        }

        String channelName = null;
        synchronized (pos) {
            if (pos > keySet.size())
                pos = 0;
            channelName = channelListHandler.get(pos);
            pos++;
        }
        return channelName;
    }
}
