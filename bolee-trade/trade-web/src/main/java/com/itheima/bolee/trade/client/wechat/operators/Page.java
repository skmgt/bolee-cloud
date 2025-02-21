package com.itheima.bolee.trade.client.wechat.operators;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itheima.bolee.trade.client.wechat.WechatPayHttpClient;
import com.itheima.bolee.trade.client.wechat.Config;
import com.itheima.bolee.trade.client.wechat.response.PagePayResponse;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @ClassName Page.java
 * @Description TODO
 */
@Slf4j
public class Page {

    private Config config;

    public Page(Config config) {
        this.config=config;
    }

    /***
     * @description 生成交易表单，渲染后自动跳转支付宝或者微信引导用户完成支付
     * @param outTradeNo  外部订单【业务系统中间的交易单号】
     * @param amount    交易的金额【单位：分】
     * @param description   商品的描述
     * @param openId    用户在当前公众号下的OpenID
     * @return TradeVo对象中的placeOrderJson：阿里云form表单字符串、微信prepay_id标识
     */
    public PagePayResponse pay(String outTradeNo, String amount,
                               String description, String openId) throws IOException {
        //请求地址
        String uri ="/v3/pay/transactions/jsapi";
        //发起对应的请求
        WechatPayHttpClient httpClient = WechatPayHttpClient.builder()
            .mchId(config.getMchId())
            .mchSerialNo(config.getMchSerialNo())
            .apiV3Key(config.getApiV3Key())
            .privateKey(config.getPrivateKey())
            .domain(config.getDomain()+uri)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyParams = objectMapper.createObjectNode();
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        bodyParams.put("mchid",config.getMchId())
            .put("appid",config.getAppid() )
            .put("description", description)
            .put("notify_url", config.getNotifyUrl())
            .put("out_trade_no", outTradeNo);
        //交易金额
        bodyParams.putObject("amount").put("total", multiply.intValue());
        //付款者OpenId信息
        bodyParams.putObject("payer").put("openid",openId);
        String body =  httpClient.doPost(bodyParams);
        PagePayResponse pagePayResponse = JSONObject.parseObject(body, PagePayResponse.class);
        if (!EmptyUtil.isNullOrEmpty(pagePayResponse.getPrepayId())){
            pagePayResponse.setCode("200");
            pagePayResponse.setMessage("网关请求成功");
            pagePayResponse.setSubCode("10000");
            pagePayResponse.setSubMessage("微信Page支付PrepayId生成成功");
        }
        return pagePayResponse;
    }


}
