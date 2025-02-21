package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.rule.AccessRuleDTO;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.IRuleService;
import com.itheima.bolee.insurance.service.IWarrantyOrderService;
import com.itheima.bolee.insurance.service.IWarrantyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName InsureController.java
 * @Description 投保
 */
@Slf4j
@Api(tags = "保险投保")
@RestController
@RequestMapping("insure")
public class InsureController {

    @Autowired
    IWarrantyService warrantyService;

    @Autowired
    IWarrantyOrderService warrantyOrderService;

    @Autowired
    private IInsuranceService insuranceService;

    /***
     * @description 保险投保
     * @param doInsureVo 保险产品VO对象
     * @return  保险合同
     */
    @PostMapping("do-insure")
    @ApiOperation(value = "保险投保",notes = "保险投保")
    @ApiImplicitParam(name = "doInsureVo",value = "保险产品详情Vo对象",required = true,dataType = "DoInsureVo")
    public ResponseResult<WarrantyVO> doInsure(@RequestBody DoInsureVo doInsureVo) {
        WarrantyVO warrantyVO = warrantyService.doInsure(doInsureVo);
        return ResponseResultBuild.successBuild(warrantyVO);
    }

    /**
     * @Description 查询保险合同及未支付的合同订单
     * @param warrantyId 保险合同ID
     * @return WarrantyVO
     */
    @PostMapping("warranty/{warrantyId}")
    @ApiOperation(value = "查询合同",notes = "查询合同")
    @ApiImplicitParam(paramType = "path",name = "warrantyId",value = "合同ID",dataType = "String")
    public ResponseResult<WarrantyVO> findWarranty(@PathVariable("warrantyId") String warrantyId) {
        WarrantyVO warrantyVOResult = warrantyService.findWarranty(warrantyId);
        return ResponseResultBuild.successBuild(warrantyVOResult);
    }


    /***
     * @description 保费计算
     * @param doInsureVo 投保试算对象
     * @return  投入型：最终本期投保金额，返回0则表示不符合条件
     */
    @PostMapping("do-premium")
    @ApiOperation(value = "保费计算",notes = "保费计算")
    @ApiImplicitParam(name = "doInsureVo",value = "投保试算对象",required = true,dataType = "DoInsureVo")
    public ResponseResult<String> doPremium(@RequestBody DoInsureVo doInsureVo) {
        String priceBuy= insuranceService.doPremium(doInsureVo);
        return ResponseResultBuild.successBuild(priceBuy);
    }

    /***
     * @description 理财收益
     * @param doInsureVo 投保试算对象
     * @return  理财型：最终收益，返回0则表示不符合条件
     */
    @PostMapping("do-earnings")
    @ApiOperation(value = "收益计算",notes = "收益计算")
    @ApiImplicitParam(name = "doInsureVo",value = "投保试算对象",required = true,dataType = "DoInsureVo")
    public ResponseResult<EarningVO> doEarnings(@RequestBody DoInsureVo doInsureVo) {
        EarningVO earningVO= insuranceService.doEarnings(doInsureVo);
        return ResponseResultBuild.successBuild(earningVO);
    }

    /***
     * @description 保险支付:一次性支付
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return  支付会话签名
     */
    @PostMapping("do-payment/{warrantyOrderId}/{tradingChannel}")
    @ApiOperation(value = "一次性支付",notes = "一次性支付")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyOrderId",value = "合同保单Id",required = true,dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "支付渠道",required = true,dataType = "String")
    })
    public ResponseResult<TradeVO> doPayment(@PathVariable("warrantyOrderId")String warrantyOrderId,
            @PathVariable("tradingChannel")String tradingChannel) {
        return ResponseResultBuild.successBuild(warrantyOrderService.doPayment(warrantyOrderId,tradingChannel));
    }

    /***
     * @description 关闭订单
     * @param tradeOrderNo 交易订单号
     * @param tradingChannel 支付渠道
     * @return  支付会话签名
     */
    @PostMapping("do-close/{tradeOrderNo}/{tradingChannel}")
    @ApiOperation(value = "关闭订单",notes = "关闭订单")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "tradeOrderNo",value = "交易订单号",required = true,dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "支付渠道",required = true,dataType = "String")
    })
    public ResponseResult<TradeVO> doClose(@PathVariable("tradeOrderNo")String tradeOrderNo,
            @PathVariable("tradingChannel")String tradingChannel) {
        return ResponseResultBuild.successBuild(warrantyOrderService.doClose(tradeOrderNo,tradingChannel));
    }

    /***
     * @description 保险支付:独立签约
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return  签约唤醒签名
     */
    @PostMapping("sign-contract/{warrantyOrderId}/{tradingChannel}")
    @ApiOperation(value = "独立签约",notes = "独立签约")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyOrderId",value = "合同保单Id",required = true,dataType = "Long"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "支付渠道",required = true,dataType = "String")
    })
    public ResponseResult<String> signContract(@PathVariable("warrantyOrderId")String warrantyOrderId,
            @PathVariable("tradingChannel")String tradingChannel) {
        return ResponseResultBuild.successBuild(warrantyOrderService.signContract(warrantyOrderId,tradingChannel));
    }

    /***
     * @description 保险支付:签约同步，并首次扣款
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @param agreementNo 支付签约号
     * @return  签约唤醒签名
     */
    @PostMapping("sign-contract-sync/{warrantyOrderId}/{tradingChannel}/{agreementNo}")
    @ApiOperation(value = "签约同步",notes = "签约同步")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyOrderId",value = "合同保单Id",required = true,dataType = "Long"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "支付渠道",required = true,dataType = "String"),
        @ApiImplicitParam(paramType = "path",name = "agreementNo",value = "支付签约号",required = true,dataType = "String")
    })
    public ResponseResult<Boolean> signContractSync(@PathVariable("warrantyOrderId")String warrantyOrderId,
            @PathVariable("tradingChannel")String tradingChannel,
            @PathVariable("agreementNo")String agreementNo) {
        return ResponseResultBuild.successBuild(warrantyOrderService.signContractSync(warrantyOrderId,tradingChannel,agreementNo));
    }

    /***
     * @description  保险支付:关闭签约
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return  支付会话签名
     */
    @PostMapping("do-close-sign-contract/{warrantyOrderId}/{tradingChannel}")
    @ApiOperation(value = "关闭签约",notes = "关闭签约")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "warrantyOrderId",value = "合同保单Id",required = true,dataType = "Long"),
        @ApiImplicitParam(paramType = "path",name = "tradingChannel",value = "支付渠道",required = true,dataType = "String")
    })
    public ResponseResult<Boolean> doCloseSignContract(@PathVariable("warrantyOrderId")String warrantyOrderId,
            @PathVariable("tradingChannel")String tradingChannel) {
        return ResponseResultBuild.successBuild(warrantyOrderService.doCloseSignContract(warrantyOrderId,tradingChannel));
    }
}
