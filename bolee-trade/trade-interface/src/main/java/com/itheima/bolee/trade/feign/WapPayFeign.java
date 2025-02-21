package com.itheima.bolee.trade.feign;

import com.itheima.bolee.trade.hystrix.WapPayHystrix;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName WapPayFeign.java
 * @Description 手机网页支付feign接口
 */
@FeignClient(value = "trade-web",fallback = WapPayHystrix.class )
public interface WapPayFeign {

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("trade-wap-feign/wap-trade")
    TradeVO wapTrade(@RequestBody TradeVO tradeVO);

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("trade-common-feign/close")
    TradeVO closeTrade(@RequestBody TradeVO tradeVO);
}
