package com.itheima.bolee.trade.adapter;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

/**
 * @ClassName WapPayAdapter.java
 * @Description 手机网页支付适配
 */
public interface WapPayAdapter {

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    TradeVO wapTrade(TradeVO tradeVO);
}
