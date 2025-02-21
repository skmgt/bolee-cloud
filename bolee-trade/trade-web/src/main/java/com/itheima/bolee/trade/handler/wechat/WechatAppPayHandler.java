package com.itheima.bolee.trade.handler.wechat;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.client.wechat.Config;
import com.itheima.bolee.trade.client.wechat.Factory;
import com.itheima.bolee.trade.client.wechat.response.AppPayResponse;
import com.itheima.bolee.trade.handler.AppPayHandler;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.utils.ResponseChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName WechatAppPayHandler.java
 * @Description 原生APP支付处理
 */
@Component
@Slf4j
public class WechatAppPayHandler extends WechatCommonPayHandler implements AppPayHandler {

    @Override
    public TradeVO appTrade(TradeVO tradeVO){
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        //2、交易前置处理：幂等性处理
        beforePayHandler.idempotentCreateTrade(tradeVO);
        //3、获得微信客户端
        Config config = wechatPayConfig.config(tradeVO.getCompanyNo());
        //4、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //5、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            AppPayResponse appPayResponse = factory.App().pay(
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()),
                tradeVO.getMemo());
            //6、检查网关响应结果
            boolean success = ResponseChecker.success(appPayResponse);
            //7、网关响应成功且appPayResponse有返回结果
            if (success&&!EmptyUtil.isNullOrEmpty(appPayResponse.getPrepayId())){
                tradeVO.setPlaceOrderCode(appPayResponse.getCode());
                tradeVO.setPlaceOrderMsg(appPayResponse.getMessage());
                tradeVO.setPlaceOrderJson(appPayResponse.getPrepayId());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
                return BeanConv.toBean(trade, TradeVO.class);
            }else {
                log.error("网关：微信APP统一下单：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(appPayResponse));
                throw new RuntimeException("网关：微信APP统一下单创建失败!");
            }
        } catch (Exception e) {
            log.error("微信APP统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("微信APP统一下单创建失败!");
        }
    }

}
