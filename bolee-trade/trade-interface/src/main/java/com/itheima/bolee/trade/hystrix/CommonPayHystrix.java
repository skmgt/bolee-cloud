package com.itheima.bolee.trade.hystrix;

import com.itheima.bolee.trade.feign.CommonPayFeign;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName CommonPayHystrix.java
 * @Description TODO
 */
@Component
public class CommonPayHystrix implements CommonPayFeign {


    @Override
    public String queryQrCode(TradeVO tradeVO) {
        return null;
    }

    @Override
    public TradeVO queryTrade(TradeVO tradeVO) {
        return null;
    }

    @Override
    public TradeVO refundTrade(TradeVO tradeVO) {
        return null;
    }

    @Override
    public void queryRefundTrade(RefundRecordVO refundRecordVO) {

    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        return null;
    }

    @Override
    public Boolean syncPaymentJob() {
        return null;
    }
}
