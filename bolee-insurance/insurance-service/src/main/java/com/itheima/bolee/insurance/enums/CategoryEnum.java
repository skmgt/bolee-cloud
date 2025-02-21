package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CategoryEnum.java
* @Description 保险分类枚举
*/

public enum CategoryEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询保险分类分页失败"),
    LIST_FAIL(53002, "查询保险分类列表失败"),
    FIND_ONE_FAIL(53003, "查询保险分类对象失败"),
    SAVE_FAIL(53004, "保存保险分类失败"),
    UPDATE_FAIL(53005, "修改保险分类失败"),
    DEL_FAIL(53006, "删除保险分类失败"),
    TREE_FAIL(53007, "保险分类树查询失败"),
    ;

    private Integer code;

    private String msg;

    CategoryEnum(Integer code, String msg) {
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
