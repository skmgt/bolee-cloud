package com.itheima.bolee.trade.handler;

import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

/**
 * @ClassName IdempotentHandler.java
 * @Description 交易前置处理接口
 */

public interface BeforePayHandler {


    /***
     * @description CreateTrade交易幂等性
     * @param tradeVO 交易订单
     * @return
     */
    void idempotentCreateTrade(TradeVO tradeVO) throws ProjectException;

    /***
     * @description AppPaySign交易单参数校验
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeAppPaySign(TradeVO tradeVO);

    /***
     * @description H5Sign交易单参数校验
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeH5Sign(TradeVO tradeVO);

    /***
     * @description H5Sign周期扣款参数教育
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeH5PeriodicPay(TradeVO tradeVO);

    /***
     * @description CreateTrade交易单参数校验
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeCreateTrade(TradeVO tradeVO);

    /***
     * @description QueryTrade交易单参数校验
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeQueryTrade(TradeVO tradeVO);

    /***
     * @description RefundTrade退款交易幂等性
     * @param tradeVO 交易订单
     * @return
     */
    void idempotentRefundTrade(TradeVO tradeVO);

    /***
     * @description RefundTrade退款交易单参数校验
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeRefundTrade(TradeVO tradeVO);


    /***
     * @description QueryRefundTrade交易单参数校验
     * @param refundRecordVO 退款记录
     * @return
     */
    Boolean checkeQueryRefundTrade(RefundRecordVO refundRecordVO);

    /***
     * @description closeTradin交易单参数校验c
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeCloseTrade(TradeVO tradeVO);

    /***
     * @description DownLoadBill下载订单交易
     * @param tradeVO 交易订单
     * @return
     */
    Boolean checkeDownLoadBill(TradeVO tradeVO);

    /***
     * @description SignContract交易幂等性
     * @param tradeVO 交易订单
     * @return
     */
    void idempotentSignContract(TradeVO tradeVO);

}
