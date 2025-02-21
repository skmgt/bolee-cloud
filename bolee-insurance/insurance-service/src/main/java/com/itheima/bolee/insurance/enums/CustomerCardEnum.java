package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CustomerCardEnum.java
* @Description 绑卡信息枚举
*/

public enum CustomerCardEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询绑卡信息分页失败"),
    LIST_FAIL(53002, "查询绑卡信息列表失败"),
    FIND_ONE_FAIL(53003, "查询绑卡信息对象失败"),
    SAVE_FAIL(53004, "保存绑卡信息失败"),
    UPDATE_FAIL(53005, "修改绑卡信息失败"),
    DEL_FAIL(53006, "删除绑卡信息失败"),

    ONLY_ONE(53007, "只有一张银行卡不能解绑"),
    ;

    private Integer code;

    private String msg;

    CustomerCardEnum(Integer code, String msg) {
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
