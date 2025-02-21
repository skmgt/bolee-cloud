package com.itheima.bolee.trade.adapter.impl;

import com.itheima.bolee.trade.adapter.PagePayAdapter;
import com.itheima.bolee.trade.handler.PagePayHandler;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PagePayAdapter.java
 * @Description PC网页支付适配实现
 */
@Slf4j
@Component
public class PagePayAdapterImpl implements PagePayAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    RedissonClient redissonClient;

    private static Map<String,String> pagePayHandlers =new HashMap<>();

    static {
        pagePayHandlers.put(TradeConstant.TRADE_CHANNEL_ALI_PAY,"aliPagePayHandler");
        pagePayHandlers.put(TradeConstant.TRADE_CHANNEL_WECHAT_PAY,"wechatPagePayHandler");
    }

    @Override
    public TradeVO pageTrade(TradeVO tradeVO) {
        //2、从IOC容器中找到PagePayHandler实现
        String pagePayHandlerString = pagePayHandlers.get(tradeVO.getTradeChannel());
        PagePayHandler pagePayHandler = registerBeanHandler.getBean(pagePayHandlerString, PagePayHandler.class);
        //3、payTrade支付交易处理
        TradeVO tradeVOResult = pagePayHandler.pageTrade(tradeVO);
        return tradeVOResult;
    }
}
