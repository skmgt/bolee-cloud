package com.itheima.bolee.sms.balancer;

import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ChannelLoadBalancing.java
 * @Description 短信发送负载均衡算法
 */
public interface SendLoadBalancer {

    /***
     * @description 根据模板、电话负载均衡发送
     * @param SmsTemplates 模板列表
     * @param mobile 电话号码
     * @return 通道类型
     */
    String chooseChannel(List<SmsTemplateVO> SmsTemplates, Set<String> mobile);

    /***
     * @description 查询拥有当前模板，活跃状态的通道
     * @param SmsTemplates 模板列表
     * @return 通道类型
     */
    Map<String, String> getChannelList(List<SmsTemplateVO> SmsTemplates);
}
