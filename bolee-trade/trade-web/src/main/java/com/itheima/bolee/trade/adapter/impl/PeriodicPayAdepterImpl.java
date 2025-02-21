package com.itheima.bolee.trade.adapter.impl;

import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.trade.adapter.PeriodicPayAdepter;
import com.itheima.bolee.trade.handler.PeriodicPayHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PeriodicPayAdepterImpl.java
 * @Description 周期性扣款接口
 */
@Component
public class PeriodicPayAdepterImpl implements PeriodicPayAdepter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    private static Map<String,String> periodicPayHandlers =new HashMap<>();

    static {
        periodicPayHandlers.put(TradeConstant.TRADE_CHANNEL_ALI_PAY,"aliPeriodicPayHandler");
        periodicPayHandlers.put(TradeConstant.TRADE_CHANNEL_WECHAT_PAY,"wechatPayPeriodicPayHandler");
    }

    @Override
    public TradeVO appPaySign(TradeVO tradeVO) {
        //1、从IOC容器中找到PeriodicPayHandler实现
        String periodicPayHandlerString = periodicPayHandlers.get(tradeVO.getTradeChannel());
        PeriodicPayHandler periodicPayHandler = registerBeanHandler
                .getBean(periodicPayHandlerString, PeriodicPayHandler.class);
        //2、contractDeduction支付交易处理
        return periodicPayHandler.appPaySign(tradeVO);
    }

    @Override
    public TradeVO h5SignContract(TradeVO tradeVO) {
        //1、从IOC容器中找到PeriodicPayHandler实现
        String periodicPayHandlerString = periodicPayHandlers.get(tradeVO.getTradeChannel());
        PeriodicPayHandler periodicPayHandler = registerBeanHandler
                .getBean(periodicPayHandlerString, PeriodicPayHandler.class);
        //2、contractDeduction支付交易处理
        return periodicPayHandler.h5SignContract(tradeVO);
    }

    @Override
    public TradeVO h5PeriodicPay(TradeVO tradeVO) {
        //1、从IOC容器中找到PeriodicPayHandler实现
        String periodicPayHandlerString = periodicPayHandlers.get(tradeVO.getTradeChannel());
        PeriodicPayHandler periodicPayHandler = registerBeanHandler
                .getBean(periodicPayHandlerString, PeriodicPayHandler.class);
        //2、contractDeduction支付交易处理
        return periodicPayHandler.h5PeriodicPay(tradeVO);
    }
}
