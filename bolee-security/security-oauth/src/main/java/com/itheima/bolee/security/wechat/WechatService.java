package com.itheima.bolee.security.wechat;

/**
 * @ClassName wechatServer.java
 * @Description 微信接口服务
 */
public interface WechatService {

    /***
     * @description 查询用户openId
     * @param appId 应用
     * @param appSecret
     * @param code
     * @return
     */
     String openId(String appId,String appSecret,String code);


}
