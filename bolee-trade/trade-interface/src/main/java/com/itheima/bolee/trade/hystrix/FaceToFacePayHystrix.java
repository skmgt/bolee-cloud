package com.itheima.bolee.trade.hystrix;

import com.itheima.bolee.trade.feign.FaceToFacePayFeign;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName FaceToFacePayHystrix.java
 * @Description FaceToFacePayFeign的Hystrix i
 */
@Component
public class FaceToFacePayHystrix implements FaceToFacePayFeign {


    @Override
    public TradeVO pay(TradeVO tradeVO) {
        return null;
    }

    @Override
    public TradeVO precreate(TradeVO tradeVO) {
        return null;
    }
}
