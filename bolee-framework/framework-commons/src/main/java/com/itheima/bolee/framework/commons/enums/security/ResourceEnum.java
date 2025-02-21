package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName ResourceEnum.java
* @Description 权限表枚举
*/

public enum ResourceEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(83001, "分页查询权限表列表失败"),
    FIND_FAIL(83002, "查询权限表失败"),
    SAVE_FAIL(83003, "保存权限表失败"),
    UPDATE_FAIL(83004, "修改权限表失败"),
    DEL_FAIL(83005, "删除权限表失败"),
    LIST_FAIL(83006, "查询权限表失败"),
    TREE_FAIL(83007, "权限表tree失败"),
    MENUS_FAIL(83008, "菜单加载失败")
    ;

    private Integer code;

    private String msg;

    ResourceEnum(Integer code, String msg) {
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
