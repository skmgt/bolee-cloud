package com.itheima.bolee.trade.hystrix;

import com.itheima.bolee.trade.feign.WapPayFeign;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName WapPayHystrix.java
 * @Description TODO
 */
@Component
public class WapPayHystrix implements WapPayFeign {


    @Override
    public TradeVO wapTrade(TradeVO tradeVO) {
        return null;
    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        return null;
    }
}
