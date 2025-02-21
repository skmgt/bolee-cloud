package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName AuthEnum.java
 * @Description 认证枚举
 */
public enum AuthEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    AUTH_FAIL(22001, "鉴权失败"),
    HSOT_FAIL(22002, "host校验失败"),
    NEED_LOGIN(22003, "请先登陆"),
    LOGIN_FAIL(22004, "登陆失败"),
    CODE_FAIL(22005, "验证码过期");

    private Integer code;
    private String msg;

    AuthEnum(Integer code, String msg) {
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
