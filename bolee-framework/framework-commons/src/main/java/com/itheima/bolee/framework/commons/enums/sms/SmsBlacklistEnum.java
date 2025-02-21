package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsBlacklistEnum.java
 * @Description 短信黑名单
 */
public enum SmsBlacklistEnum implements IBaseEnum {

    PAGE_FAIL(48001, "查询黑名单列表失败"),
    CREATE_FAIL(48002, "保存黑名单失败"),
    UPDATE_FAIL(48003, "修改黑名单失败"),
    DELETE_FAIL(48004, "修改黑名单失败"),
    SELECT_FAIL(48005, "查询黑名单失败");

    private Integer code;
    private String msg;

    SmsBlacklistEnum(Integer code, String msg) {
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
