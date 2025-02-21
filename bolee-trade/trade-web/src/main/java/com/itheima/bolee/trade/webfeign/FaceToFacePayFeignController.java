package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.trade.face.FaceToFacePayFace;
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
 * @ClassName FaceToFacePayController.java
 * @Description 面对面支付
 */
@RequestMapping("trade-face-to-face-feign")
@RestController
@Api(tags = "面对面支付feign-controller")
public class FaceToFacePayFeignController {

    @Autowired
    FaceToFacePayFace faceToFacePayFace;

    /***
     * @description 扫码-用户扫商家，完成付款
     * @param tradeVO 订单
     * @return  支付结果
     */
    @PostMapping("pay")
    @ApiOperation(value = "扫码-商家扫用户",notes = "扫码-商家扫用户")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.tradeChannel",
            "tradeVO.productOrderNo", "tradeVO.enterpriseId","tradeVO.memo",
            "tradeVO.authCode","tradeVO.tradeAmount"})
    TradeVO payTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = faceToFacePayFace.payTrade(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description 生成交易付款码，待用户扫码付款
     * @param tradeVO 订单
     * @return  二维码路径
     */
    @PostMapping("precreate")
    @ApiOperation(value = "扫码-用户扫商家",notes = "扫码-用户扫商家")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.memo", "tradeVO.tradeAmount",
            "tradeVO.tradeChannel","tradeVO.productOrderNo", "tradeVO.enterpriseId"})
    TradeVO precreateTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = faceToFacePayFace.precreateTrade(tradeVO);
        return tradeVOResult;
    }

}
