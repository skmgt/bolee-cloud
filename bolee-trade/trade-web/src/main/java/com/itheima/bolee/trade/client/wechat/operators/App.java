package com.itheima.bolee.trade.client.wechat.operators;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itheima.bolee.trade.client.wechat.WechatPayHttpClient;
import com.itheima.bolee.trade.client.wechat.Config;
import com.itheima.bolee.trade.client.wechat.response.AppPayResponse;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ClassName Appprecreate.java
 * @Description app的支付
 */
@Slf4j
public class App  {

    private Config config;

    public App(Config config) {
        this.config=config;
    }

    /***
     * @description 统一收单线下交易预创建
     * @param outTradeNo 发起支付传递的交易单号
     * @param amount 交易金额【元】
     * @param description 商品描述
     * @return
     */
    public AppPayResponse pay(String outTradeNo, String amount, String description) throws Exception {
        //请求地址
        String uri ="/v3/pay/transactions/app";
        //系统参数封装
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        //业务数据的封装
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyParams = objectMapper.createObjectNode();
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        bodyParams.put("mchid",config.getMchId())
            .put("appid",config.getAppid() )
            .put("description", description)
            .put("notify_url", config.getNotifyUrl())
            .put("out_trade_no", outTradeNo);
        bodyParams.putObject("amount")
            .put("total", multiply.intValue());
        String body = httpClient.doPost(bodyParams);
        AppPayResponse appPayResponse = JSONObject.parseObject(body, AppPayResponse.class);
        if (!EmptyUtil.isNullOrEmpty(appPayResponse.getPrepayId())){
            appPayResponse.setCode("200");
            appPayResponse.setMessage("网关请求成功");
            appPayResponse.setSubCode("10000");
            appPayResponse.setSubMessage("App支付PrepayId生成成功");
        }
        return appPayResponse;
    }
}
