package com.itheima.bolee.trade.handler.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.client.alipay.Factory;
import com.itheima.bolee.trade.handler.FaceToFacePayHandler;
import com.itheima.bolee.trade.pojo.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName AliFaceToFacePayHandler.java
 * @Description 支付宝扫码付支付实现
 */
@Slf4j
@Component
public class AliFaceToFacePayHandler extends AliCommonPayHandler implements FaceToFacePayHandler {

    @Override
    public TradeVO payTrade(TradeVO tradeVO) {
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_PAY_FAIL);
        }
        //2、交易前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrade(tradeVO);
        //3、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //4、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //5、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //6、调用支付宝API
            AlipayTradePayResponse payResponse = factory.FaceToFace().pay(tradeVO.getMemo(),
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()),
                tradeVO.getAuthCode()
            );
            //7、受理结果【只表示请求是否成功，而不是支付是否成功】
            boolean isSuccess = ResponseChecker.success(payResponse);
            //8、受理成功：修改交易单
            if (isSuccess&&String.valueOf(tradeVO.getTradeOrderNo()).equals(payResponse.getOutTradeNo())){
                tradeVO.setResultCode(payResponse.getCode());
                tradeVO.setResultMsg(payResponse.getMsg());
                tradeVO.setResultJson(JSONObject.toJSONString(payResponse));
                tradeVO.setTradeState(TradeConstant.TRADE_SUCCESS);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
            }else {
                log.error("网关：支付宝商家扫用户统一下单创建：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(payResponse));
                throw new RuntimeException("网关：支付宝商家扫用户统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("支付宝商家扫用户统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.PAYING_TRADE_FAIL);
        }
        return tradeVO;
    }

    @Override
    public TradeVO precreateTrade(TradeVO tradeVO)  {
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_PAY_FAIL);
        }
        //2、交易前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrade(tradeVO);
        //3、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //4、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //5、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //6、调用支付宝API面对面支付
            AlipayTradePrecreateResponse precreateResponse = factory.FaceToFace()
                    .preCreate(
                tradeVO.getMemo(),
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()));
            //7、受理结果【只表示请求是否成功，而不是支付是否成功】
            boolean isSuccess = ResponseChecker.success(precreateResponse);
            //8、受理成功：修改交易单
            if (isSuccess&&!EmptyUtil.isNullOrEmpty(precreateResponse.getQrCode())){
                tradeVO.setPlaceOrderCode(precreateResponse.getCode());
                tradeVO.setPlaceOrderMsg(precreateResponse.getMsg());
                tradeVO.setPlaceOrderJson(precreateResponse.getQrCode());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
            }else {
                log.error("网关：支付宝商家扫用户统一下单创建：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(precreateResponse));
                throw new RuntimeException("网关：支付宝商家扫用户统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("支付宝用户扫商家统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.PAYING_TRADE_FAIL);
        }
        return tradeVO;
    }

}
