package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName UserEnum.java
* @Description 用户表枚举
*/

public enum UserEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(56001, "分页查询用户表列表失败"),
    FIND_FAIL(56002, "查询用户表失败"),
    SAVE_FAIL(56003, "保存用户表失败"),
    UPDATE_FAIL(56004, "修改用户表失败"),
    DEL_FAIL(56005, "删除用户表失败"),
    LIST_FAIL(56006, "查询用户表失败"),

    POST_NOT_BE_NULL(56007, "职位不能为空失败"),

    ROLE_NOT_BE_NULL(56008, "角色不能为空失败"),
    ;

    private Integer code;

    private String msg;

    UserEnum(Integer code, String msg) {
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
