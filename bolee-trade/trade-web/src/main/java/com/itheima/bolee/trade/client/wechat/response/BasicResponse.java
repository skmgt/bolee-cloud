package com.itheima.bolee.trade.client.wechat.response;

import lombok.Data;

/**
 * @ClassName BasicResponse.java
 * @Description 返回基础对象
 */
@Data
public class BasicResponse {

    //网关请求返回编码
    public String code;

    //网关请求返回信息
    public String message;

    //业务请求返回编码
    public String subCode;

    //业务请求返回信息
    public String subMessage;
}
