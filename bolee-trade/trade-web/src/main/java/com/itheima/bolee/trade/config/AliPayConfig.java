package com.itheima.bolee.trade.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.easysdk.kernel.Config;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.face.PayChannelFace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * @ClassName AlipayConfig.java
 * @Description 支付宝配置类
 */
@Slf4j
@Configuration
public class AliPayConfig {

    @Autowired
    PayChannelFace payChannelFace;

    /***
     * @description easy-sdk创建配置
     * @return
     */
    public Config config(String companyNo ){
        //1、缓存配置
        PayChannelVO payChannelVO = payChannelFace.findPayChannelVlid(TradeConstant.TRADE_CHANNEL_ALI_PAY);
        //2、配置信息校验
        if (EmptyUtil.isNullOrEmpty(payChannelVO)){
            throw  new ProjectException(TradeEnum.CONFIG_EMPT);
        }
        //2、创建配置
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost =payChannelVO.getDomain();
        config.signType = "RSA2";
        config.appId = payChannelVO.getAppId();
        //2.1、配置应用私钥
        config.merchantPrivateKey = payChannelVO.getMerchantPrivateKey();
        //2.2、配置支付宝公钥
        config.alipayPublicKey = payChannelVO.getPublicKey();
        //2.3、可设置异步通知接收服务地址（可选）
        config.notifyUrl = payChannelVO.getNotifyUrl()+"/"+payChannelVO.getCompanyNo();
        //2.4、设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = payChannelVO.getEncryptKey();
        //2.5、配置信息放入configHashMap中
        return config;
    }

    /***
     * @description 通用-sdk创建配置
     * @return
     */
    public DefaultAlipayClient createAlipayClient(String companyNo ){
        //1、缓存配置
        PayChannelVO payChannelVO = payChannelFace.findPayChannelVlid(TradeConstant.TRADE_CHANNEL_ALI_PAY);
        //2、配置信息校验
        if (EmptyUtil.isNullOrEmpty(payChannelVO)){
            throw  new ProjectException(TradeEnum.CONFIG_EMPT);
        }
        return  new DefaultAlipayClient("https://"+payChannelVO.getDomain()+"/gateway.do",
                payChannelVO.getAppId(),
                payChannelVO.getMerchantPrivateKey(),
                "json",
                "GBK",
                payChannelVO.getPublicKey(),
                "RSA2");
    }

}
