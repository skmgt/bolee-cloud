package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName DeptPostUserEnum.java
 * @Description 部门职位人员枚举
 */
public enum DeptPostUserEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(63001, "查询部门职位人员表列表失败"),
    SAVE_FAIL(63002, "保存部门职位人员表失败"),
    UPDATE_FAIL(63003, "修改部门职位人员表失败"),
    DEL_FAIL(63004, "删除部门职位人员表失败"),
    LIST_FAIL(63005, "查询部门职位人员表失败"),
    TREE_FAIL(63006, "查询部门职位人员树失败")
    ;

    private Integer code;

    private String msg;

    DeptPostUserEnum(Integer code, String msg) {
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
