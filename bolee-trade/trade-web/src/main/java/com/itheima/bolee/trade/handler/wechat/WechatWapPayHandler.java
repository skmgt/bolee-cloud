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
import com.itheima.bolee.trade.client.wechat.response.WapPayResponse;
import com.itheima.bolee.trade.handler.WapPayHandler;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.utils.ResponseChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName WechatWapPayHandler.java
 * @Description Jsapi支付处理
 */
@Slf4j
@Component
public class WechatWapPayHandler extends WechatCommonPayHandler implements WapPayHandler {

    @Override
    public TradeVO wapTrade(TradeVO tradeVO){
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
            throw new RuntimeException("微信支付配置为空！");
        }
        //5、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //6、调用接口
            WapPayResponse wapPayResponse = factory.Wap().pay(
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()),
                tradeVO.getMemo(),
                tradeVO.getOpenId());
            //7、检查网关响应结果
            boolean success = ResponseChecker.success(wapPayResponse);
            if (success&&!EmptyUtil.isNullOrEmpty(wapPayResponse.getPrepayId())){
                tradeVO.setPlaceOrderCode(wapPayResponse.getCode());
                tradeVO.setPlaceOrderMsg(wapPayResponse.getMessage());
                tradeVO.setPlaceOrderJson(wapPayResponse.getPrepayId());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
                return BeanConv.toBean(trade, TradeVO.class);
            }else {
                log.error("网关：支付宝手机网页支付统一下单创建：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(wapPayResponse));
                throw new RuntimeException("网关：微信手机网页支付统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("微信手机网页支付统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("微信手机网页支付统一下单创建失败");
        }
    }

}
