package com.itheima.bolee.trade.handler;

import org.springframework.http.HttpEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName NotifyHandler.java
 * @Description 支付回调接口
 */
public interface PayNotifyHandler {

    /***
     * @description 支付回调
     * @param request http请求对象：支付宝
     * @param httpEntity http请求对象：微信
     * @param companyNo 企业号
     */
    String notify(HttpServletRequest request, HttpEntity<String> httpEntity,String companyNo);

}
