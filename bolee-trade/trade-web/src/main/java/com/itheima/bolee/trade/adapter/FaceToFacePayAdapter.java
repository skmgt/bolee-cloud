package com.itheima.bolee.trade.adapter;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

/**
 * @ClassName FaceToFacePayAdapter.java
 * @Description 面对面支付适配实现
 */
public interface FaceToFacePayAdapter {

    /***
     * @description 扫用户出示的付款码，完成付款
     * @param tradeVO 订单
     * @return  支付结果
     */
    TradeVO payTrade(TradeVO tradeVO);

    /***
     * @description 生成交易付款码，待用户扫码付款
     * @param tradeVO 订单
     * @return  二维码路径
     */
    TradeVO precreateTrade(TradeVO tradeVO);
}
