package com.itheima.bolee.trade.handler.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.kernel.Config;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.TradeSource;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.client.alipay.Factory;
import com.itheima.bolee.trade.config.AliPayConfig;
import com.itheima.bolee.trade.handler.PayNotifyHandler;
import com.itheima.bolee.trade.pojo.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AliNotifyHandler.java
 * @Description 支付通知接口
 */
@Component
public class AliPayNotifyHandler implements PayNotifyHandler {

    @Autowired
    AliPayConfig aliPayConfig;

    @Autowired
    ITradeService tradeService;

    @Autowired
    TradeSource tradeSource;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Override
    public String notify(HttpServletRequest request, HttpEntity<String> httpEntity,String companyNo) {
        //获取支付结果参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        for (String key : parameterMap.keySet()) {
            paramMap.put(key, parameterMap.get(key)[0]);
        }
        try {
            //1、获得支付宝配置文件
            Config config = aliPayConfig.config(companyNo);
            //2、配置如果为空，抛出异常
            if (EmptyUtil.isNullOrEmpty(config)){
                throw new ProjectException(TradeEnum.CONFIG_ERROR);
            }
            //3、使用配置
            Factory factory = new Factory();
            factory.setOptions(config);
            //4、验证签名
            boolean verifyResult = factory.Common().verifyNotify(paramMap);
            if (!verifyResult) {
                return "verify sign fail";
            }
            //5、校验支付状态
            if (TradeConstant.ALI_TRADE_SUCCESS.equals(paramMap.get("trade_status"))) {
                Trade trade = tradeService.findTradByTradeOrderNo(Long.valueOf(paramMap.get("out_trade_no")));
                trade.setResultCode(paramMap.get("trade_status"));
                trade.setResultMsg(TradeConstant.ALI_SUCCESS_MSG);
                trade.setResultJson(JSONObject.toJSONString(paramMap));
                trade.setTradeState(TradeConstant.TRADE_SUCCESS);
                tradeService.updateById(trade);
                //发送同步业务信息的MQ信息
                Long messageId = (Long) identifierGenerator.nextId(trade);
                MqMessage mqMessage = MqMessage.builder()
                    .id(messageId)
                    .title("trade-message")
                    .content(JSONObject.toJSONString(BeanConv.toBean(trade, TradeVO.class)))
                    .messageType("trade-project-sync")
                    .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                    .sender("system")
                    .build();
                //指定通知的企业
                Message<MqMessage> message = MessageBuilder.withPayload(mqMessage)
                    .setHeader("type", "trade-key").build();
                boolean flag = tradeSource.tradeOutput().send(message);
                return "success";
            }else if(TradeConstant.ALI_TRADE_CLOSED.equals(paramMap.get("trade_status"))){
                Trade trade = tradeService.findTradByTradeOrderNo(Long.valueOf(paramMap.get("out_trade_no")));
                trade.setResultCode(paramMap.get("trade_status"));
                trade.setResultMsg(TradeConstant.ALI_SUCCESS_MSG);
                trade.setResultJson(JSONObject.toJSONString(paramMap));
                trade.setTradeState(TradeConstant.TRADE_CLOSED);
                tradeService.updateById(trade);
                //发送同步业务信息的MQ信息
                Long messageId = (Long) identifierGenerator.nextId(trade);
                MqMessage mqMessage = MqMessage.builder()
                    .id(messageId)
                    .title("trade-message")
                    .content(JSONObject.toJSONString(BeanConv.toBean(trade, TradeVO.class)))
                    .messageType("trade-project-sync")
                    .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                    .sender("system")
                    .build();
                //指定通知的企业
                Message<MqMessage> message = MessageBuilder.withPayload(mqMessage)
                    .setHeader("type", "trade-key").build();
                tradeSource.tradeOutput().send(message);
                return "success";
            }else {
                return "fail";
            }
        } catch (Exception e) {
            //6、异常返回 fail 给支付宝
            return "fail";
        }
    }
}
