package com.itheima.bolee.trade.adapter;

import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;

/**
 * @ClassName CommonPayAdapter.java
 * @Description 基础支付适配
 */
public interface CommonPayAdapter {

    /***
     * @description 统一收单线下交易查询
     * 该接口提供所有支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * @param tradeVO 交易单
     * @return
     */
    TradeVO queryTrade(TradeVO tradeVO) throws ProjectException;

    /***
     * @description 统一收单交易退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradeVO 交易单
     * @return
     */
    TradeVO refundTrade(TradeVO tradeVO) throws ProjectException;

    /***
     * @description 统一收单交易退款查询接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param refundRecordVO 退款交易单
     * @return
     */
    RefundRecordVO queryRefundTrade(RefundRecordVO refundRecordVO) throws ProjectException;

    /***
     * @description 统一关闭订单
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * @param tradeVO 交易单
     * @return
     */
    TradeVO closeTrade(TradeVO tradeVO);

    /***
     * @description 查看二维码信息
     * 收银员通过收银台或商户后台调用此接口，生成二维码后，展示给用户，商户可以多次展示二维码
     * @param tradeVO 交易单
     * @return  二维码路径
     */
    String queryQrCode(TradeVO tradeVO);

    /***
     * @description 为方便商户快速查账，支持商户通过本接口获取商户离线账单下载地址
     * @param tradeVO 退款交易单
     * @return
     */
    TradeVO downLoadBill(TradeVO tradeVO);
}
