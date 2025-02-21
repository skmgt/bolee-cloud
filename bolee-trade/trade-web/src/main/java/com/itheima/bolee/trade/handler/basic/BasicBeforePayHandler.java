package com.itheima.bolee.trade.handler.basic;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.trade.SignContractConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.trade.handler.BeforePayHandler;
import com.itheima.bolee.trade.pojo.SignContract;
import com.itheima.bolee.trade.service.IRefundRecordService;
import com.itheima.bolee.trade.service.ISignContractService;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.pojo.RefundRecord;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName BeforePayHandlerImpl.java
 * @Description 阿里交易前置处理接口实现
 */
@Component("beforePayHandler")
public class BasicBeforePayHandler implements BeforePayHandler {

    @Autowired
    ITradeService tradeService;

    @Autowired
    IRefundRecordService refundRecordService;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Override
    public void idempotentCreateTrade(TradeVO tradeVO) throws ProjectException {
        //查询当前订单对应的最近交易单
        Trade trade = tradeService.findTradByProductOrderNo(tradeVO.getProductOrderNo());
        //交易单为空：之前未对此订单做过支付
        if (EmptyUtil.isNullOrEmpty(trade)) {
            tradeVO.setTradeOrderNo((Long)identifierGenerator.nextId(tradeVO));
            return;
        }
        //交易单不为空：判定交易单状态
        switch (trade.getTradeState()){
            //待支付:交易创建，等待买家付款
            case TradeConstant.TRADE_WAIT:
                throw new ProjectException(TradeEnum.TRADE_STATE_PAYING);
            //已结算:直接抛出重复支付异常
            case TradeConstant.TRADE_SUCCESS:
                throw new ProjectException(TradeEnum.TRADE_STATE_SUCCEED);
            //已关闭:付款交易超时关闭或支付失败,重新发起交易
            case TradeConstant.TRADE_CLOSED:
                tradeVO.setTradeOrderNo((Long)identifierGenerator.nextId(tradeVO));break;
            default:
                throw new ProjectException(TradeEnum.PAYING_TRADE_FAIL);
        }

    }

    @Override
    public Boolean checkeAppPaySign(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //订单号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getProductOrderNo())){
            flag = false;
        //订单总金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeAmount())){
            flag = false;
        //规则为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO())){
            flag = false;
        //签约场景为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getSignScene())){
            flag = false;
        //步通知地址为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getSignNotifyUrl())){
            flag = false;
        //周期类型为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRulePeriodType())){
            flag = false;
        //周期数为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRulePeriod())){
            flag = false;
        //下次扣款的时间为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleExecuteTime())){
            flag = false;
        //channel为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getAccessChannel())){
            flag = false;
        //周期内允许扣款的总金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleTotalAmount())){
            flag = false;
        //总扣款次数
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleTotalPayments())){
            flag = false;
        //支付渠道为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeChannel())){
            flag=false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeH5Sign(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //订单号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getProductOrderNo())){
            flag = false;
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO())){
            flag = false;
        //签约场景为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getSignScene())){
            flag = false;
        //步通知地址为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getNotifyUrl())){
            flag = false;
        //步通知地址为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getReturnUrl())){
            flag = false;
        //周期类型为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRulePeriodType())){
            flag = false;
        //周期数为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRulePeriod())){
            flag = false;
        //扣款的时间为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleExecuteTime())){
            flag = false;
        //channel为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getAccessChannel())){
            flag = false;
        //周期内允许扣款的总金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleTotalAmount())){
            flag = false;
        //签约业务编号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getContractNo())){
            flag = false;
        //总扣款次数
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getRuleTotalPayments())){
            flag = false;
        //支付渠道为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeChannel())){
            flag=false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeH5PeriodicPay(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //订单号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getProductOrderNo())){
            flag = false;
        //订单交易金额
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeAmount())){
            flag = false;
        //订单签约号
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getAliPeriodicVO().getAgreementNo())){
            flag=false;
        //订单交易渠道
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeChannel())){
            flag=false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public void idempotentRefundTrade(TradeVO tradeVO) {
        //查询交易单是否为以结算订单
        Trade trade = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        //交易单不存在，或者不为已结算状态：抛出退款失败异常
        if (EmptyUtil.isNullOrEmpty(trade)|| !TradeConstant.TRADE_SUCCESS.equals(trade.getTradeState())){
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }else {
            tradeVO.setTradeOrderNo(trade.getTradeOrderNo());
            tradeVO.setId(trade.getId());
            tradeVO.setTradeAmount(tradeVO.getOperTionRefund());
        }
        //查询是否有退款中的退款记录
        RefundRecord refundRecord = refundRecordService
                .findRefundRecordByProductOrderNoAndSending(tradeVO.getProductOrderNo());
        if (!EmptyUtil.isNullOrEmpty(refundRecord)){
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }
    }

    @Override
    public Boolean checkeCreateTrade(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        }else if (TradeConstant.TRADE_CHANNEL_WECHAT_PAY.equals(tradeVO.getTradeChannel())
                &&EmptyUtil.isNullOrEmpty(tradeVO.getOpenId())){
            flag = false;
        //订单号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getProductOrderNo())){
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getCompanyNo())){
            flag = false;
        //交易金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeAmount())){
            flag = false;
        //支付渠道为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeChannel())){
            flag=false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeQueryTrade(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getCompanyNo())){
            flag = false;
         //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeOrderNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeRefundTrade(TradeVO tradeVO) {

        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getCompanyNo())){
            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeOrderNo())){
            flag = false;
         //退款请求号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getOutRequestNo())){
            flag = false;
        //当前退款金额为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getOperTionRefund())){
            flag = false;
        //退款总金额不可超剩余实付总金额
        }else if (tradeVO.getRefund()
            .add(tradeVO.getOperTionRefund()).compareTo(tradeVO.getTradeAmount()) > 0){
            flag = false;
        }else {
            flag = true;
        }

        return flag;
    }

    @Override
    public Boolean checkeQueryRefundTrade(RefundRecordVO refundRecordVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(refundRecordVO)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVO.getCompanyNo())){
            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVO.getTradeOrderNo())){
            flag = false;
         //退款请求号为空
        }else if (EmptyUtil.isNullOrEmpty(refundRecordVO.getRefundNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeCloseTrade(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //交易号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getTradeOrderNo())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean checkeDownLoadBill(TradeVO tradeVO) {
        Boolean flag =null;
        //订单为空
        if (EmptyUtil.isNullOrEmpty(tradeVO)) {
            flag = false;
        //企业号为空
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getCompanyNo())){
            flag = false;
        //账单日期
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getBillDate())){
            flag = false;
        //账单账单类型
        }else if (EmptyUtil.isNullOrEmpty(tradeVO.getBillType())){
            flag = false;
        }else {
            flag = true;
        }
        return flag;
    }
    @Autowired
    ISignContractService signContractService;

    @Override
    public void idempotentSignContract(TradeVO tradeVO) {
        //查询当前业务对应的签约信息
        SignContract signContract = signContractService.findSignContractByContract_no(tradeVO.getAliPeriodicVO().getContractNo());
        //签约合同为空：当前业务签约无有效签约号
        if (EmptyUtil.isNullOrEmpty(signContract)) {
            tradeVO.getAliPeriodicVO().setExternalAgreementNo(String.valueOf(identifierGenerator.nextId(tradeVO)));
            return;
        }
        //签约合同单不为空：判定签约状态
        switch (signContract.getSignState()){
            //TEMP：暂存，协议未生效过
            case SignContractConstant.SIGNSTATE_TEMP:
                throw new RuntimeException("暂存");
            //NORMAL：正常
            case SignContractConstant.SIGNSTATE_NORMAL:
                throw new RuntimeException("正常");
                //已关闭:付款交易超时关闭或支付失败,重新发起交易
            default:
                throw new RuntimeException("签约合同异常");
        }
    }
}
