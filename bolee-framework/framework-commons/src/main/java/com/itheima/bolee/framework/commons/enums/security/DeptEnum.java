package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DeptEnum.java
* @Description 部门表枚举
*/

public enum DeptEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(53001, "查询部门表列表失败"),
    SAVE_FAIL(53002, "保存部门表失败"),
    UPDATE_FAIL(53003, "修改部门表失败"),
    DEL_FAIL(53004, "删除部门表失败"),
    LIST_FAIL(53005, "查询部门表失败"),
    TREE_FAIL(53006, "查询部门树失败"),
    CREATE_DEPT_NO_FAIL(53007, "创建部门编号失败"),
    ;

    private Integer code;

    private String msg;

    DeptEnum(Integer code, String msg) {
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
