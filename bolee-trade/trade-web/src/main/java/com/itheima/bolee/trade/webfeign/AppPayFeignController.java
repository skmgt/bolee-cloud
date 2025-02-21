package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.trade.face.AppPayFace;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AppPayController.java
 * @Description APP支付
 */
@RequestMapping("trade-app-feign")
@RestController
@Api(tags = "APP支付feign-controller")
public class AppPayFeignController {

    @Autowired
    AppPayFace appPayFace;


    /***
     * @description 外部商户APP唤起快捷SDK创建订单并支付
     * @param tradeVO 订单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("app-trade")
    @ApiOperation(value = "APP支付",notes = "APP支付")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.productOrderNo","tradeVO.tradeAmount",
            "tradeVO.memo","tradeVO.tradeChannel","tradeVO.enterpriseId"} )
    TradeVO appTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = appPayFace.appTrade(tradeVO);
        return tradeVOResult;
    }
}
