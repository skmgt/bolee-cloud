package com.itheima.bolee.trade.web;

import com.itheima.bolee.trade.handler.PayNotifyHandler;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AliPayNotifyController.java
 * @Description TODO
 */
@RestController
@RequestMapping("pay-notify")
@Slf4j
@Api(tags = "支付通道回调")
public class PayNotifyController {

    @Autowired
    PayNotifyHandler aliPayNotifyHandler;

    @Autowired
    PayNotifyHandler wechatPayNotifyHandler;

    /***
     * @description 阿里支付回调
     * @param request http请求对象：支付宝
     * @param httpEntity http请求对象：微信
     */
    @PostMapping("ali-pay-notify/{companyNo}")
    String aliPayNotify(HttpServletRequest request, HttpEntity<String> httpEntity,
                        @PathVariable("companyNo") String companyNo){
        log.info("==========支付宝通知接口companyNo：{}",companyNo);
        return aliPayNotifyHandler.notify(request,httpEntity,companyNo);
    }

    /***
     * @description 微信支付回调
     * @param request http请求对象：支付宝
     * @param httpEntity http请求对象：微信
     */
    @PostMapping("wechat-pay-notify/{companyNo}")
    String wechatPayNotify(HttpServletRequest request, HttpEntity<String> httpEntity,
                           @PathVariable("companyNo") String companyNo){
        log.info("===============微信通知接口companyNo：{}",companyNo);
        return wechatPayNotifyHandler.notify(request,httpEntity,companyNo);
    }
}
