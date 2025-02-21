package com.itheima.bolee.trade.handler;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

/**
 * @ClassName AppPayHandler.java
 * @Description 原生APP支付处理
 */
public interface AppPayHandler extends CommonPayHandler {

    /***
     * @description 外部商户APP唤起快捷SDK创建订单并支付
     * @param tradeVO 订单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    TradeVO appTrade(TradeVO tradeVO);
}
