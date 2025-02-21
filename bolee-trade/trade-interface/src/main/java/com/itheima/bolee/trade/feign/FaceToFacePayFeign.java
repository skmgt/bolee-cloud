package com.itheima.bolee.trade.feign;

import com.itheima.bolee.trade.hystrix.FaceToFacePayHystrix;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName FaceToFacePayFeign.java
 * @Description 面对面支付接口
 */
@FeignClient(value = "trade-web",fallback = FaceToFacePayHystrix.class )
public interface FaceToFacePayFeign {

    /***
     * @description 扫用户出示的付款码，完成付款
     * @param tradeVO 订单
     * @return  支付结果
     */
    @PostMapping("trade-face-to-face-feign/pay")
    TradeVO pay(@RequestBody TradeVO tradeVO);

    /***
     * @description 生成交易付款码，待用户扫码付款
     * @param tradeVO 订单
     * @return  二维码路径
     */
    @PostMapping("trade-face-to-face-feign/precreate")
    TradeVO precreate(@RequestBody TradeVO tradeVO);

}
