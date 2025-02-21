package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * OCR识别异常枚举
 */
public enum OutDataInfoOcrEnum implements IBaseEnum {

    LOGIN_FAIL(53001, "进行OCR认证需要先登录"),

    BANK_OCR_OUT_INTERFACE_ERROR(53002,"银行卡OCR接口调用失败"),

    TOKEN_ERROR(53003, "百度云获取token失败"),
    ;

    private Integer code;

    private String msg;

    OutDataInfoOcrEnum(Integer code, String msg) {
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