package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsSend.java
 * @Description 短信发送枚举
 */
public enum SmsSendEnum implements IBaseEnum {

    PEXCEED_THE_LIMIT (39001, "超过发送上限"),
    SEND_FAIL(39002, "发送短信失败"),
    SEND_SUCCEED(39004, "此短信已送达"),
    QUERY_FAIL(39003, "查询短信发送情况失败"),
    RETRY_FAIL(39004, "重新短信发送失败"),

    ;

    private Integer code;
    private String msg;

    SmsSendEnum(Integer code, String msg) {
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
