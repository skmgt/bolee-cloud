package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName SalesEnum.java
 * @Description TODO
 */
public enum SalesEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(55001, "分页查询销售表列表失败"),
    FIND_FAIL(55002, "查询销售表失败"),
    SAVE_FAIL(55003, "保存销售表失败"),
    UPDATE_FAIL(55004, "修改销售表失败"),
    DEL_FAIL(55005, "删除销售表失败"),
    LIST_FAIL(55006, "查询销售表失败")
    ;

    private Integer code;

    private String msg;

    SalesEnum(Integer code, String msg) {
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
