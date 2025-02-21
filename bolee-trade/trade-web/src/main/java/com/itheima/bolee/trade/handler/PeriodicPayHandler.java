package com.itheima.bolee.trade.handler;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

/**
 * @ClassName PeriodicPayHandler.java
 * @Description 周期性扣款接口
 */
public interface PeriodicPayHandler {

    /***
     * @description App支付并签约
     * @param tradeVO 交易单信息
     * @return
     */
    TradeVO appPaySign(TradeVO tradeVO);

    /***
     * @description H5先签约
     * @param tradeVO 交易单信息
     * @return
     */
    TradeVO h5SignContract(TradeVO tradeVO);

    /***
     * @description H5签约后代扣
     * @param tradeVO 交易单信息
     * @return
     */
    TradeVO h5PeriodicPay(TradeVO tradeVO);
}
