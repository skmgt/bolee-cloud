package com.itheima.bolee.trade.hystrix;

import com.itheima.bolee.trade.feign.AppPayFeign;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppPayHystrix.java
 * @Description TODO
 */
@Component
public class AppPayHystrix implements AppPayFeign {


    @Override
    public TradeVO appTrade(TradeVO tradeVO) {
        return null;
    }
}
