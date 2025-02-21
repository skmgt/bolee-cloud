package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsChannelEnum.java
 * @Description TODO
 */
public enum SmsChannelEnum implements IBaseEnum {

    PAGE_FAIL(47001, "查询通道列表失败"),
    CREATE_FAIL(47002, "保存通道失败"),
    UPDATE_FAIL(47003, "修改通道失败"),
    DELETE_FAIL(47004, "修改通道失败"),
    SELECT_FAIL(47005, "查询通道失败");

    private Integer code;
    private String msg;

    SmsChannelEnum(Integer code, String msg) {
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
