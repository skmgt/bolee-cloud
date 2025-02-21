package com.itheima.bolee.trade.adapter.impl;

import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.trade.adapter.AppPayAdapter;
import com.itheima.bolee.trade.handler.AppPayHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AppPayAdapter.java
 * @Description 原生APP支付适配支付适配实现
 */
@Slf4j
@Component
public class AppPayAdapterImpl implements AppPayAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    private static Map<String,String> appPayHandlers =new HashMap<>();

    static {
        appPayHandlers.put(TradeConstant.TRADE_CHANNEL_ALI_PAY,"aliAppPayHandler");
        appPayHandlers.put(TradeConstant.TRADE_CHANNEL_WECHAT_PAY,"wechatAppPayHandler");
    }

    @Override
    public TradeVO appTrade(TradeVO tradeVO) {
        //从IOC容器中找到AppPayHandler实现
        String appPayHandlerString = appPayHandlers.get(tradeVO.getTradeChannel());
        AppPayHandler appPayHandler = registerBeanHandler.getBean(appPayHandlerString, AppPayHandler.class);
        //appTrade支付交易处理
        return appPayHandler.appTrade(tradeVO);
    }
}
