package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.trade.face.PagePayFace;
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
 * @ClassName PagePayController.java
 * @Description PC网页支付
 */
@RequestMapping("trade-page-feign")
@RestController
@Api(tags = "PC网页feign-controller")
public class PagePayFeignController {

    @Autowired
    PagePayFace pagePayFace;

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param tradeVO 订单单
     * @return TradeVO对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    @PostMapping("trade-page/page-trade")
    @ApiOperation(value = "PC网页支付",notes = "PC网页支付")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.productOrderNo","tradeVO.returnUrl",
            "tradeVO.tradeAmount","tradeVO.tradeChannel","tradeVO.openId","tradeVO.memo"})
    TradeVO pageTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult =  pagePayFace.pageTrade(tradeVO);
        return tradeVOResult;
    }
}
