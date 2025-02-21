package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName UserEnum.java
* @Description 用客户枚举
*/

public enum CustomerEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(58001, "分页查询用客户列表失败"),
    FIND_FAIL(58002, "查询用客户失败"),
    SAVE_FAIL(58003, "保存用客户失败"),
    UPDATE_FAIL(58004, "修改用客户失败"),
    DEL_FAIL(58005, "删除用客户失败"),
    LIST_FAIL(58006, "查询用客户失败"),
    RESET_PASSWORD_FAIL(58007,"充值密码失败")
    ;

    private Integer code;

    private String msg;

    CustomerEnum(Integer code, String msg) {
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
