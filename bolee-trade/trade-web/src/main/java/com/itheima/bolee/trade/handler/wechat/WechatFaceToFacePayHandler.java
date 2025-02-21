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
import com.itheima.bolee.trade.client.wechat.response.PreCreateResponse;
import com.itheima.bolee.trade.handler.FaceToFacePayHandler;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.utils.ResponseChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName WechatNativePayHandler.java
 * @Description Native支付方式：商户生成二维码，用户扫描支付
 */
@Slf4j
@Component
public class WechatFaceToFacePayHandler extends WechatCommonPayHandler implements FaceToFacePayHandler {

    @Override
    public TradeVO payTrade(TradeVO tradeVO)  {
        //因微信商户扫用户模式新版本未出，还是V2版本，因此未接入，等待更新
        throw new RuntimeException("因微信商户扫用户模式新版本未出，还是V2版本，因此未接入，等待更新");
    }

    @Override
    public TradeVO precreateTrade(TradeVO tradeVO) {
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
        //5、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //4、调用接口
            PreCreateResponse preCreateResponse =factory.FaceToFace().precreate(
                String.valueOf(tradeVO.getTradeOrderNo()),
                String.valueOf(tradeVO.getTradeAmount()),
                tradeVO.getMemo());
            //7、受理结果【只表示请求是否成功，而不是支付是否成功】
            boolean success = ResponseChecker.success(preCreateResponse);
            //8、受理成功：修改交易单
            if (success&&!EmptyUtil.isNullOrEmpty(preCreateResponse.getCodeUrl())){
                tradeVO.setPlaceOrderCode(preCreateResponse.getCode());
                tradeVO.setPlaceOrderMsg(preCreateResponse.getMessage());
                tradeVO.setPlaceOrderJson(preCreateResponse.getCodeUrl());
                tradeVO.setTradeState(TradeConstant.TRADE_WAIT);
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.save(trade);
            }else {
                log.error("网关：precreateResponse：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(preCreateResponse));
                throw new RuntimeException("网关：微信商家扫用户统一下单创建失败！");
            }
        } catch (Exception e) {
            log.error("微信用户扫商家统一下单创建失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("微信商家扫用户统一下单创建失败！");
        }
        return tradeVO;
    }

}
