package com.itheima.bolee.trade.feign;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.trade.hystrix.AppPayHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName AppPayFeign.java
 * @Description App方式支付feign接口
 */
@FeignClient(value = "trade-web",fallback = AppPayHystrix.class )
public interface AppPayFeign {

    /***
     * @description 外部商户APP唤起快捷SDK创建订单并支付
     * @param tradeVO 订单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("trade-app-feign/app-trade")
    TradeVO appTrade(@RequestBody TradeVO tradeVO);
}
