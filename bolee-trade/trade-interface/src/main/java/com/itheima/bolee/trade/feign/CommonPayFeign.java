package com.itheima.bolee.trade.feign;

import com.itheima.bolee.trade.hystrix.CommonPayHystrix;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName CommonPayFeign.java
 * @Description 基础支付处理能力feign接口
 */
@FeignClient(value = "trade-web",fallback = CommonPayHystrix.class )
public interface CommonPayFeign {

    /***
     * @description 查看二维码信息
     * 收银员通过收银台或商户后台调用此接口，生成二维码后，展示给用户，商户可以多次展示二维码
     * @param tradeVO 交易单
     * @return  二维码路径
     */
    @PostMapping("trade-common-feign/query-qr-code")
    String queryQrCode(@RequestBody TradeVO tradeVO);

    /***
     * @description 统一收单线下交易查询
     * 该接口提供所有支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * @param tradeVO 交易单
     * @return
     */
    @PostMapping("trade-common-feign/query")
    TradeVO queryTrade(@RequestBody TradeVO tradeVO);

    /***
     * @description 统一收单交易退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradeVO 交易单
     * @return
     */
    @PostMapping("trade-common-feign/refund")
    TradeVO refundTrade(@RequestBody TradeVO tradeVO);

    /***
     * @description 统一收单交易退款查询接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param refundRecordVO 退款交易单
     * @return
     */
    @PostMapping("trade-common-feign/query-refund")
    void queryRefundTrade(@RequestBody RefundRecordVO refundRecordVO);

    /***
     * @description 统一关闭订单
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * @param tradeVO 退款交易单
     * @return
     */
    @PostMapping("trade-common-feign/close-refund")
    TradeVO closeTrade(@RequestBody TradeVO tradeVO);

    /***
     * @description 计划任务：同步交易单结果
     * @return
     */
    @PostMapping("trade-common-feign/sync-payment-job")
    Boolean syncPaymentJob();
}
