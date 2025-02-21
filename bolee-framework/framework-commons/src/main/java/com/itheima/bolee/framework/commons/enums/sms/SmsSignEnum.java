package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsSignEnum.java
 * @Description 签名枚举类
 */
public enum SmsSignEnum implements IBaseEnum {

    PAGE_FAIL(46001, "查询签名列表失败"),
    CREATE_FAIL(46002, "保存签名失败"),
    UPDATE_FAIL(46003, "修改签名失败"),
    DELETE_FAIL(46004, "删除签名失败"),
    SELECT_FAIL(46005, "查询签名失败");

    private Integer code;
    private String msg;

    SmsSignEnum(Integer code, String msg) {
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
