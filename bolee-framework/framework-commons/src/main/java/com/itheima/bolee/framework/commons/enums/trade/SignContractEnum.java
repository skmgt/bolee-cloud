package com.itheima.bolee.framework.commons.enums.trade;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName TradeEnum.java
 * @Description 签约合同枚举
 */
public enum SignContractEnum implements IBaseEnum {

    PAGE_FAIL(25001, "查询签约合同失败"),
    SAVE_FAIL(25002, "保存签约合同失败"),
    UPDATE_FAIL(25003, "修改签约合同失败"),
    DELETE_FAIL(25004, "修改签约合同失败"),
    LIST_FAIL(25004, "查询签约合同失败");

    private Integer code;
    private String msg;

    SignContractEnum(Integer code, String msg) {
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
