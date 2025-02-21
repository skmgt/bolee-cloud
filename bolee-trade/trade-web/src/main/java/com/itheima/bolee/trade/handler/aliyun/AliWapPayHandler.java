package com.itheima.bolee.trade.handler.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.handler.WapPayHandler;
import com.itheima.bolee.trade.client.alipay.Factory;
import com.itheima.bolee.trade.pojo.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @ClassName AliWapPayHandler.java
 * @Description 手机网页支付
 */
@Slf4j
@Component
public class AliWapPayHandler extends AliCommonPayHandler implements WapPayHandler {

    @Override
    public TradeVO wapTrade(TradeVO tradeVO) {
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
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
            //6、调用接口
            AlipayTradeWapPayResponse wapPayResponse = factory.Wap().pay(tradeVO.getMemo(),
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(new BigDecimal("0.01")), tradeVO.getQuitUrl(), tradeVO.getReturnUrl());
            //7、检查网关响应结果
            boolean isSuccess = ResponseChecker.success(wapPayResponse);
            if (isSuccess&&!EmptyUtil.isNullOrEmpty(wapPayResponse.getBody())){
                tradeVO.setPlaceOrderCode(TradeConstant.ALI_SUCCESS_CODE);
                tradeVO.setPlaceOrderMsg(TradeConstant.ALI_SUCCESS_MSG);
                tradeVO.setPlaceOrderJson(wapPayResponse.getBody());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
                return BeanConv.toBean(trade, TradeVO.class);
            }else {
                log.error("网关：支付宝手机网页支付统一下单创建：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(wapPayResponse));
                throw new RuntimeException("网关：支付宝手机网页支付统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("支付宝手机网页支付统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("支付宝手机网页支付统一下单创建失败");
        }
    }
}
