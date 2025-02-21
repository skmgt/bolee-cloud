package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsSendRecordEnum.java
 * @Description 短信发送记录
 */
public enum SmsSendRecordEnum implements IBaseEnum {

    PAGE_FAIL(49001, "查询发送记录列表失败"),
    CREATE_FAIL(49002, "保存发送记录失败"),
    UPDATE_FAIL(49003, "修改发送记录失败"),
    DELETE_FAIL(49004, "修改发送记录失败"),
    SELECT_FAIL(49005, "查询发送记录失败"),
    SEND_RECORD_FAIL(49006, "重发失败");

    private Integer code;
    private String msg;

    SmsSendRecordEnum(Integer code, String msg) {
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
