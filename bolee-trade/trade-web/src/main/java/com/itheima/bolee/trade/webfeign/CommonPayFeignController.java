package com.itheima.bolee.trade.webfeign;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.trade.face.CommonPayFace;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
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
 * @ClassName CommonPayController.java
 * @Description 基础支付控制器
 */
@RequestMapping("trade-common-feign")
@RestController
@Api(tags = "支付基础服务feign-controller")
public class CommonPayFeignController {

    @Autowired
    CommonPayFace commonPayFace;

    /***
     * @description 统一收单线下交易查询
     * 该接口提供所有支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * @param tradeVO 交易单
     * @return
     */
    @PostMapping("query")
    @ApiOperation(value = "支付结果查询",notes = "支付结果查询")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.tradeOrderNo","tradeVO.tradeChannel","tradeVO.enterpriseId"})
    TradeVO queryTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = commonPayFace.queryTrade(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description 申请退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradeVO 交易单
     * @return
     */
    @PostMapping("refund")
    @ApiOperation(value = "申请退款",notes = "申请退款")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.tradeOrderNo","tradeVO.enterpriseId",
            "tradeVO.operTionRefund","tradeVO.tradeChannel"})
    TradeVO refundTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = commonPayFace.refundTrade(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description 申请退款查询接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param refundRecordVO 退款交易单
     * @return
     */
    @PostMapping("query-refund")
    @ApiOperation(value = "退款结果查询",notes = "退款结果查询")
    @ApiImplicitParam(name = "refundRecordVO",value = "退款交易单",required = true,dataType = "RefundRecordVO")
    @ApiOperationSupport(includeParameters ={"refundRecordVO.refundNo","refundRecordVO.tradeChannel",
            "tradeVO.enterpriseId","refundRecordVO.tradeOrderNo"})
    RefundRecordVO queryRefundDownLineTrade(@RequestBody RefundRecordVO refundRecordVO){
        RefundRecordVO refundRecordVOResult = commonPayFace.queryRefundTrade(refundRecordVO);
        return refundRecordVOResult;
    }

    /***
     * @description 统一关闭订单
     * 1、商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     * 2、系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     * @param tradeVO 退款交易单
     * @return
     */
    @PostMapping("close")
    @ApiOperation(value = "统一关闭订单",notes = "统一关闭订单")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.tradeOrderNo","tradeVO.enterpriseId","tradeVO.tradeChannel"})
    TradeVO closeTrade(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = commonPayFace.closeTrade(tradeVO);
        return tradeVOResult;
    }

    /***
     * @description 为方便商户快速查账，支持商户通过本接口获取商户离线账单下载地址
     * @param tradeVO 退款交易单
     * @return
     */
    @PostMapping("down-load-bill")
    @ApiOperation(value = "下载交易单",notes = "下载交易单")
    @ApiImplicitParam(name = "tradeVO",value = "交易单",required = true,dataType = "TradeVO")
    @ApiOperationSupport(includeParameters ={"tradeVO.billType","tradeVO.enterpriseId","tradeVO.billDate","tradeVO.tradeChannel"})
    TradeVO downLoadBill(@RequestBody TradeVO tradeVO){
        TradeVO tradeVOResult = commonPayFace.downLoadBill(tradeVO);
        return tradeVOResult;
    }

    /**
     * @description 计划任务：同步交易单结果
     * @return
     */
    @PostMapping("sync-payment-job")
    @ApiOperation(value = "计划任务：同步交易单结果",notes = "计划任务：同步交易单结果")
    Boolean syncPaymentJob(){
        return commonPayFace.syncPaymentJob();
    }

}
