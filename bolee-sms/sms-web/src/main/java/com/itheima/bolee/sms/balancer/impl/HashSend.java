package com.itheima.bolee.sms.balancer.impl;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.balancer.BaseSendLoadBalancer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName HashSend.java
 * @Description mobile哈希（Hash）法
 */
@Component
public class HashSend extends BaseSendLoadBalancer {

    @Override
    public String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile) {
        //获得当前模板对应的渠道
        Map<String, String> channelMap = super.getChannelList(SmsTemplates);
        //取得channel地址List
        Set<String> keySet = channelMap.keySet();
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.addAll(keySet);

        // 使用mobile取余
        int hashCode = mobile.hashCode();
        int serverListSize = keyList.size();
        int serverPos = hashCode % serverListSize;
        return keyList.get(serverPos);
    }

}
