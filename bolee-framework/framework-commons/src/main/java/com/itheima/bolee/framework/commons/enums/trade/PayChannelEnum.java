package com.itheima.bolee.framework.commons.enums.trade;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;


/**
 * @ClassName PayChannelEnum.java
 * @Description 支付枚举类
 */
public enum PayChannelEnum implements IBaseEnum {

    PAGE_FAIL(51001, "查询支付通道列表失败"),
    CREATE_FAIL(51002, "保存支付通道失败"),
    UPDATE_FAIL(51003, "修改支付通道失败"),
    DELETE_FAIL(51004, "修改支付通道失败"),
    SELECT_FAIL(51005, "查询支付通道失败"),
    CHANNEL_FAIL(51006, "交易渠道不存在"),
    ;

    private Integer code;
    private String msg;

    PayChannelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
