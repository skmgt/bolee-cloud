package com.itheima.bolee.trade.feign;

import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.trade.hystrix.WapPayHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName WapPayFeign.java
 * @Description 手机网页支付feign接口
 */
@FeignClient(value = "trade-web",fallback = WapPayHystrix.class )
public interface PeriodicPayFeign {

    /***
     * @description app签约并支付
     * @param tradeVO 交易单
     * @return 交易单
     */
    @PostMapping("trade-periodic-feign/app-pay-sign")
    TradeVO appPaySign(@RequestBody TradeVO tradeVO);

    /***
     * @description h5签约
     * @param tradeVO 交易单
     * @return  交易单
     */
    @PostMapping("trade-periodic-feign/h5-sign-contract")
    TradeVO h5SignContract(@RequestBody TradeVO tradeVO);

    /***
     * @description h5签约代扣
     * @param tradeVO 交易单
     * @return  交易单
     */
    @PostMapping("trade-periodic-feign/h5-periodic-pay")
    TradeVO h5PeriodicPay(@RequestBody TradeVO tradeVO);

    /***
     * @description 签约同步
     * @param warrantyNo 合同编号
     * @param tradingChannel 支付渠道
     * @param agreementNo 支付签约号
     * @return
     */
    @PostMapping("trade-periodic-feign/sign-contract-sync/{warrantyNo}/{tradingChannel}/{agreementNo}")
    Boolean signContractSync(@PathVariable("warrantyNo") String warrantyNo,
         @PathVariable("tradingChannel")String tradingChannel,
         @PathVariable("agreementNo")String agreementNo);

    /***
     * @description 关闭h5签约
     * @param warrantyNo 合同号
     * @param tradingChannel 渠道号
     * @return  交易单
     */
    @PostMapping("trade-periodic-feign/h5-close-sign-contract/{warrantyNo}/{tradingChannel}")
    Boolean h5CloseSignContract(@PathVariable("warrantyNo") String warrantyNo,
        @PathVariable("tradingChannel")String tradingChannel);
}
