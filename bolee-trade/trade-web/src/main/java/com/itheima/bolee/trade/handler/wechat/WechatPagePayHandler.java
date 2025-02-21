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
import com.itheima.bolee.trade.client.wechat.response.PagePayResponse;
import com.itheima.bolee.trade.handler.PagePayHandler;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.utils.ResponseChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName WechatPagePayHandler.java
 * @Description PC网页支付处理实现
 */
@Slf4j
@Component
public class WechatPagePayHandler extends WechatCommonPayHandler implements PagePayHandler {

    @Override
    public TradeVO pageTrade(TradeVO tradeVO){
        //1、交易前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeCreateTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_PAY_FAIL);
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
            PagePayResponse pagePayResponse = factory.Page().pay(
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()),
                tradeVO.getMemo(),
                tradeVO.getOpenId());
            //7、受理结果【只表示请求是否成功，而不是支付是否成功】
            boolean success = ResponseChecker.success(pagePayResponse);
            if (success&&!EmptyUtil.isNullOrEmpty(pagePayResponse.getPrepayId())){
                tradeVO.setPlaceOrderCode(pagePayResponse.getCode());
                tradeVO.setPlaceOrderMsg(pagePayResponse.getMessage());
                tradeVO.setPlaceOrderJson(pagePayResponse.getPrepayId());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
            }else {
                log.error("网关：微信Page支付preCreate：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(pagePayResponse));
                throw new RuntimeException("网关：微信Page支付preCreate发生异常!");
            }
        } catch (Exception e) {
            log.error("微信用户扫商家统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("微信商家扫用户统一下单创建失败！");
        }
        return tradeVO;
    }

}
