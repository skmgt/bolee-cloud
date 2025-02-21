package com.itheima.bolee.framework.commons.enums.sms;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SmsTemplateEnum.java
 * @Description 签名模板枚举
 */
public enum  SmsTemplateEnum implements IBaseEnum {

    PAGE_FAIL(48001, "查询模板列表失败"),
    CREATE_FAIL(48002, "保存模板失败"),
    UPDATE_FAIL(48003, "修改模板失败"),
    DELETE_FAIL(48004, "修改模板失败"),
    SELECT_FAIL(48005, "查询模板失败");

    private Integer code;
    private String msg;

    SmsTemplateEnum(Integer code, String msg) {
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
