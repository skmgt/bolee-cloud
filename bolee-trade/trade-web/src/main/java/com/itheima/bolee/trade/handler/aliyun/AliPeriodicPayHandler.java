package com.itheima.bolee.trade.handler.aliyun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayUserAgreementPageSignRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayUserAgreementPageSignResponse;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.trade.SignContractConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.config.AliPayConfig;
import com.itheima.bolee.trade.handler.PeriodicPayHandler;
import com.itheima.bolee.trade.pojo.SignContract;
import com.itheima.bolee.trade.pojo.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * @ClassName AliPeriodicPayHandler.java
 * @Description 周期性扣款实现
 */
@Slf4j
@Component
public class AliPeriodicPayHandler extends AliCommonPayHandler implements PeriodicPayHandler {

    @Autowired
    AliPayConfig aliPayConfig;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Override
    public TradeVO appPaySign(TradeVO tradeVO) {
        //交易前置处理：参数校验
        Boolean flag = beforePayHandler.checkeAppPaySign(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_DEDUCTION_FAIL);
        }
        //签约前置处理：幂等性处理
        beforePayHandler.idempotentSignContract(tradeVO);
        //交易前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrade(tradeVO);
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(tradeVO.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", tradeVO.getTradeOrderNo());
        //订单总金额。首次支付金额，不算在周期扣总金额里面。
        bizContent.put("total_amount", tradeVO.getTradeAmount());
        bizContent.put("subject", tradeVO.getMemo());
        bizContent.put("product_code", "QUICK_MSECURITY_PAY");
        //签约参数
        JSONObject agreement_sign_params = new JSONObject();
        agreement_sign_params.put("product_code", "GENERAL_WITHHOLDING");
        agreement_sign_params.put("personal_product_code", "CYCLE_PAY_AUTH_P");
        //扣款场景
        agreement_sign_params.put("sign_scene", tradeVO.getAliPeriodicVO().getSignScene());
        //商家签约号，代扣协议中标示用户的唯一签约号（确保在商家系统中唯一）。用户支持一对多
        agreement_sign_params.put("external_agreement_no", identifierGenerator.nextId(tradeVO));
        //签约成功异步通知地址。支付后签约场景中，用户支付成功及签约成功都会触发异步通知
        agreement_sign_params.put("sign_notify_url", tradeVO.getAliPeriodicVO().getSignNotifyUrl());
        //周期规则参数，必填
        JSONObject period_rule_params = new JSONObject();
        //周期类型 ，枚举值为 DAY 和 MONTH。周期类型使用MONTH的时候，计划扣款时间 execute_time不允许传 28 日之后的日期（可以传 28 日），以此避免有些月份可能不存在对应日期的情况。
        period_rule_params.put("period_type", tradeVO.getAliPeriodicVO().getRulePeriodType());
        //必填，周期数，与 period_type 组合使用确定扣款周期，例如 period_type 为 DAY，period = 90，则扣款周期为 90 天。
        period_rule_params.put("period", tradeVO.getAliPeriodicVO().getRulePeriod());
        //下次扣款的时间。非支付并签约的成功时间，必填。精确到日，格式为 yyyy-MM-dd。
        period_rule_params.put("execute_time", tradeVO.getAliPeriodicVO().getRuleExecuteTime());
        //单次扣款最大金额，必填，即每次发起扣款时限制的最大金额，单位为元。商家每次发起扣款都不允许大于此金额。
        period_rule_params.put("single_amount", tradeVO.getAliPeriodicVO().getRuleSingleAmount());
        //周期内允许扣款的总金额。
        period_rule_params.put("total_amount", tradeVO.getAliPeriodicVO().getRuleTotalAmount());
        //总扣款次数
        period_rule_params.put("total_payments", tradeVO.getAliPeriodicVO().getRuleTotalPayments());
        agreement_sign_params.put("period_rule_params", period_rule_params);
        JSONObject access_params = new JSONObject();
        //channel：目前支持以下值：ALIPAYAPP ：支付宝客户端 H5 页面签约。QRCODE：扫码签约。QRCODEORSMS：扫码签约或者短信签约。
        access_params.put("channel", tradeVO.getAliPeriodicVO().getAccessChannel());
        agreement_sign_params.put("access_params", access_params);
        bizContent.put("agreement_sign_params", agreement_sign_params);
        request.setBizContent(bizContent.toString());
        AlipayClient alipayClient = aliPayConfig.createAlipayClient(tradeVO.getCompanyNo());
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            boolean success = response.isSuccess();
            if (success){
                //保存签约合同记录
                SignContract signContract = SignContract.builder()
                    .contractNo(tradeVO.getAliPeriodicVO().getContractNo())
                    .externalAgreementNo(tradeVO.getAliPeriodicVO().getExternalAgreementNo())
                    .signState(SignContractConstant.SIGNSTATE_TEMP)
                    .tradeChannel(tradeVO.getTradeChannel())
                    .rulePeriodType(tradeVO.getAliPeriodicVO().getRulePeriodType())
                    .rulePeriod(Long.valueOf(tradeVO.getAliPeriodicVO().getRulePeriod()))
                    .ruleTotalAmount(new BigDecimal(tradeVO.getAliPeriodicVO().getRuleTotalAmount()))
                    .ruleSingleAmount(new BigDecimal(tradeVO.getAliPeriodicVO().getRuleSingleAmount()))
                    .ruleTotalPayments(Long.valueOf(tradeVO.getAliPeriodicVO().getRuleTotalPayments()))
                    .build();
                signContractService.save(signContract);
                //保存支付记录
                tradeVO.setPlaceOrderCode(TradeConstant.ALI_SUCCESS_CODE);
                tradeVO.setPlaceOrderMsg(TradeConstant.ALI_SUCCESS_MSG);
                tradeVO.setPlaceOrderJson(response.getBody());
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
                return BeanConv.toBean(trade, TradeVO.class);
            }else {
                log.error("网关：App支付并签约：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(response));
                throw new RuntimeException("网关：支付宝App支付并签约创建失败！");
            }
        }catch (Exception e){
            log.error("支付宝App支付并签约创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("支付宝App支付并签约创建失败!");
        }
    }

    @Override
    public TradeVO h5SignContract(TradeVO tradeVO) {
        //交易前置处理：参数校验
        Boolean flag = beforePayHandler.checkeH5Sign(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_DEDUCTION_FAIL);
        }
        //签约前置处理：幂等性处理
        beforePayHandler.idempotentSignContract(tradeVO);
        //构建请求对象
        AlipayUserAgreementPageSignRequest request = new AlipayUserAgreementPageSignRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("product_code", "GENERAL_WITHHOLDING");
        //商家扣款场景固定为 GENERAL_WITHHOLDING
        bizContent.put("personal_product_code", "CYCLE_PAY_AUTH_P");
        //商户签约号
        bizContent.put("external_agreement_no", tradeVO.getAliPeriodicVO().getExternalAgreementNo());
        //签约场景
        bizContent.put("sign_scene", tradeVO.getAliPeriodicVO().getSignScene());
        //周期规则参数，必填
        JSONObject period_rule_params = new JSONObject();
        //周期类型 ，枚举值为 DAY 和 MONTH。周期类型使用MONTH的时候，计划扣款时间 execute_time不允许传 28 日之后的日期（可以传 28 日），以此避免有些月份可能不存在对应日期的情况。
        period_rule_params.put("period_type", tradeVO.getAliPeriodicVO().getRulePeriodType());
        //必填，周期数，与 period_type 组合使用确定扣款周期，例如 period_type 为 DAY，period = 90，则扣款周期为 90 天。
        period_rule_params.put("period", tradeVO.getAliPeriodicVO().getRulePeriod());
        //首次扣款的时间。非支付并签约的成功时间，必填。精确到日，格式为 yyyy-MM-dd。
        period_rule_params.put("execute_time", tradeVO.getAliPeriodicVO().getRuleExecuteTime());
        //单次扣款最大金额，必填，即每次发起扣款时限制的最大金额，单位为元。商家每次发起扣款都不允许大于此金额。
        period_rule_params.put("single_amount", tradeVO.getAliPeriodicVO().getRuleSingleAmount());
        //周期内允许扣款的总金额。
        period_rule_params.put("total_amount", tradeVO.getAliPeriodicVO().getRuleTotalAmount());
        //总扣款次数
        period_rule_params.put("total_payments", tradeVO.getAliPeriodicVO().getRuleTotalPayments());
        JSONObject access_params = new JSONObject();
        //channel：目前支持以下值：ALIPAYAPP ：支付宝客户端 H5 页面签约。QRCODE：扫码签约。QRCODEORSMS：扫码签约或者短信签约。
        access_params.put("channel", tradeVO.getAliPeriodicVO().getAccessChannel());
        bizContent.put("access_params", access_params);
        bizContent.put("period_rule_params", period_rule_params);
        String bizContentJsonString = bizContent.toJSONString();
        request.setBizContent(bizContentJsonString);
        request.setNotifyUrl(tradeVO.getNotifyUrl());//设置异步通知地址
        request.setReturnUrl(tradeVO.getReturnUrl());//跳转商家处理地址
        AlipayClient alipayClient = aliPayConfig.createAlipayClient(tradeVO.getCompanyNo());
        try {
            // 商家扣款场景请求，生成form表单
            AlipayUserAgreementPageSignResponse response = alipayClient.pageExecute(request);
            boolean success = response.isSuccess();
            if (success){
                //保存签约合同记录
                SignContract signContract = SignContract.builder()
                    .contractNo(tradeVO.getAliPeriodicVO().getContractNo())
                    .externalAgreementNo(tradeVO.getAliPeriodicVO().getExternalAgreementNo())
                    .signState(SignContractConstant.SIGNSTATE_TEMP)
                    .tradeChannel(tradeVO.getTradeChannel())
                    .rulePeriodType(tradeVO.getAliPeriodicVO().getRulePeriodType())
                    .rulePeriod(Long.valueOf(tradeVO.getAliPeriodicVO().getRulePeriod()))
                    .ruleTotalAmount(new BigDecimal(tradeVO.getAliPeriodicVO().getRuleTotalAmount()))
                    .ruleSingleAmount(new BigDecimal(tradeVO.getAliPeriodicVO().getRuleSingleAmount()))
                    .ruleTotalPayments(Long.valueOf(tradeVO.getAliPeriodicVO().getRuleTotalPayments()))
                    .build();
                signContractService.save(signContract);
                //返回签约唤醒
                tradeVO.setPlaceOrderCode(TradeConstant.ALI_SUCCESS_CODE);
                tradeVO.setPlaceOrderMsg(TradeConstant.ALI_SUCCESS_MSG);
                tradeVO.setPlaceOrderJson(response.getBody());
                return tradeVO;
            }else {
                log.error("网关：H5先签约：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(response));
                throw new RuntimeException("网关：支付宝H5先签约创建失败！");
            }
        } catch (Exception e) {
            log.error("支付宝H5先签约创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("支付宝H5先签约创建失败!");
        }
    }

    @Override
    public TradeVO h5PeriodicPay(TradeVO tradeVO) {
        //交易前置处理：参数校验
        Boolean flag = beforePayHandler.checkeH5PeriodicPay(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_DEDUCTION_FAIL);
        }
        //签约前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrade(tradeVO);
        //周期扣款
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", tradeVO.getTradeOrderNo());
        bizContent.put("product_code", "GENERAL_WITHHOLDING");//商家扣款产品码固定为GENERAL_WITHHOLDING
        bizContent.put("total_amount", tradeVO.getTradeAmount());
        bizContent.put("subject", tradeVO.getMemo());
        JSONObject agreement_params = new JSONObject();
        agreement_params.put("agreement_no", tradeVO.getAliPeriodicVO().getAgreementNo());
        bizContent.put("agreement_params", agreement_params);
        request.setBizContent(bizContent.toJSONString());
        AlipayClient alipayClient = aliPayConfig.createAlipayClient(tradeVO.getCompanyNo());
        try {
            AlipayTradePayResponse response = alipayClient.execute(request);
            boolean success = response.isSuccess();
            tradeVO.setResultCode("10000");//因无资质，所以此处为模拟结果
            tradeVO.setResultMsg("SUCCESS");//因无资质，所以此处为模拟结果
            tradeVO.setResultJson(JSONObject.toJSONString(response));//因无资质，所以此处为模拟结果
            tradeVO.setTradeState(TradeConstant.TRADE_SUCCESS);//因无资质，所以此处为模拟结果
            Trade trade = BeanConv.toBean(tradeVO, Trade.class);
            tradeService.save(trade);
            return BeanConv.toBean(trade, TradeVO.class);
        } catch (AlipayApiException e) {
            log.error("支付宝H5代扣失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("支付宝H5代扣失败!");
        }
    }
}
