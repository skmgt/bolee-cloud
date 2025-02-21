package com.itheima.bolee.trade.feign;

import com.itheima.bolee.trade.hystrix.PagePayHystrix;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName PagePayFeign.java
 * @Description PC网页支付feign接口
 */
@FeignClient(value = "trade-web",fallback = PagePayHystrix.class )
public interface PagePayFeign {

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("trade-page-feign/page-trade")
    TradeVO pageTrade(@RequestBody TradeVO tradeVO);
}

