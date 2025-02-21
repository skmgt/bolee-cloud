package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName RoleEnum.java
* @Description 角色表枚举
*/

public enum RoleEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(54001, "分页查询角色表列表失败"),
    FIND_FAIL(54002, "查询角色表失败"),
    SAVE_FAIL(54003, "保存角色表失败"),
    UPDATE_FAIL(54004, "修改角色表失败"),
    DEL_FAIL(54005, "删除角色表失败"),
    LIST_FAIL(54006, "查询角色表失败")
    ;

    private Integer code;

    private String msg;

    RoleEnum(Integer code, String msg) {
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
