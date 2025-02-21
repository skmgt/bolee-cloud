package com.itheima.bolee.trade.hystrix;

import com.itheima.bolee.trade.feign.PagePayFeign;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName PagePayHystrix.java
 * @Description TODO
 */
@Component
public class PagePayHystrix implements PagePayFeign {

    @Override
    public TradeVO pageTrade(TradeVO tradeVO) {
        return null;
    }
}
