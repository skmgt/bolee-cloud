package com.itheima.bolee.trade.config;

import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.client.wechat.Config;
import com.itheima.bolee.trade.face.PayChannelFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName WeChatPayConfig.java
 * @Description 微信配置类
 */
@Configuration
public class WechatPayConfig {

    @Autowired
    PayChannelFace payChannelFace;


    /***
     * @description 获得配置
     * @return
     */
    public Config config(String companyNo){
        //1、缓存配置
        PayChannelVO payChannelVO = payChannelFace.findPayChannelVlid(TradeConstant.TRADE_CHANNEL_WECHAT_PAY);
        //2、配置信息校验
        if (EmptyUtil.isNullOrEmpty(payChannelVO)){
            throw  new ProjectException(TradeEnum.CONFIG_EMPT);
        }
        //2、转换其他属性为map结构
        List<OtherConfigVO> otherConfigs = payChannelVO.getOtherConfigs();
        Map<String,String> otherConfigMap = new HashMap<>();
        otherConfigs.forEach(n->{
            otherConfigMap.put(n.getConfigKey(),n.getConfigValue());
        });
        return Config.builder()
            .appid(payChannelVO.getAppId())
            .domain(payChannelVO.getDomain())
            .mchId(otherConfigMap.get("mchId"))
            .mchSerialNo(otherConfigMap.get("mchSerialNo"))
            .apiV3Key(otherConfigMap.get("apiV3Key"))
            .privateKey(payChannelVO.getMerchantPrivateKey())
            .notifyUrl(payChannelVO.getNotifyUrl()+"/"+payChannelVO.getCompanyNo())
            .build();
    }
}
