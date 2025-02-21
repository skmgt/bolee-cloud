package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.trade.face.WapPayFace;
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
 * @ClassName WapPayController.java
 * @Description 手机网页支付
 */
@RequestMapping("trade-wap-feign")
@RestController
@Api(tags = "手机网页支付feign-controller")
public class WapPayFeignController {

    @Autowired
    WapPayFace wapPayFace;

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return  TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("wap-trade")
    @ApiOperation(value = "手机网页支付",notes = "手机网页支付")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.tradeOrderNo","tradeVO.returnUrl",
            "tradeVO.tradeAmount","tradeVO.quitUrl","tradeVO.returnUrl","tradeVO.tradeChannel",
            "tradeVO.openId","tradeVO.memo"})
    TradeVO wapTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = wapPayFace.wapTrade(tradeVO);
        return tradeVOResult;
    }
}
