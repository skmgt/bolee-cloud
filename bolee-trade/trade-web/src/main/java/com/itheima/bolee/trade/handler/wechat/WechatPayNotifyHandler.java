package com.itheima.bolee.trade.handler.wechat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.config.WechatPayConfig;
import com.itheima.bolee.trade.handler.PayNotifyHandler;
import com.itheima.bolee.trade.pojo.Trade;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName AliNotifyHandler.java
 * @Description 支付通知接口
 */
@Component
public class WechatPayNotifyHandler implements PayNotifyHandler {

    @Autowired
    WechatPayConfig wechatPayConfig;

    @Autowired
    ITradeService tradeService;

    @Override
    public String notify(HttpServletRequest request, HttpEntity<String> httpEntity,String companyNo) {
        //获取请求头
        HttpHeaders headers = httpEntity.getHeaders();
        //构建微信请求数据对象
        NotificationRequest notificationRequest = new NotificationRequest.Builder()
            .withSerialNumber(headers.getFirst("Wechatpay-Serial")) //证书序列号（微信平台）
            .withNonce(headers.getFirst("Wechatpay-Nonce"))  //随机串
            .withTimestamp(headers.getFirst("Wechatpay-Timestamp")) //时间戳
            .withSignature(headers.getFirst("Wechatpay-Signature")) //签名字符串
            .withBody(httpEntity.getBody())
            .build();
        //验证签名，确保请求来自微信
        JSONObject jsonData;
        try {
            //确保在管理器中存在自动更新的商户证书
            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            Verifier verifier = certificatesManager.getVerifier(wechatPayConfig.config(companyNo).getMchId());
            //验签和解析请求数据
            NotificationHandler notificationHandler = new NotificationHandler(verifier, wechatPayConfig.config(companyNo)
                    .getApiV3Key().getBytes(StandardCharsets.UTF_8));
            Notification notification = notificationHandler.parse(notificationRequest);
            if (!StrUtil.equals("TRANSACTION.SUCCESS", notification.getEventType())) {
                return "verify sign fail";
            }
            //获取解密后的数据
            jsonData = JSONUtil.parseObj(notification.getDecryptData());
        } catch (Exception e) {
            throw new RuntimeException("微信通知：验签失败");
        }
        if (!StrUtil.equals(jsonData.getStr("trade_state"), TradeConstant.WECHAT_TRADE_SUCCESS)) {
            Trade trade = tradeService.findTradByTradeOrderNo(jsonData.getLong("out_trade_no"));
            trade.setResultCode(jsonData.getStr("trade_state"));
            trade.setResultMsg(TradeConstant.ALI_SUCCESS_MSG);
            trade.setResultJson(com.alibaba.fastjson.JSONObject.toJSONString(jsonData));
            trade.setTradeState(TradeConstant.TRADE_WAIT);
            tradeService.updateById(trade);
            return "SUCCESS";
        }else {
            return "FAIL";
        }
    }
}
