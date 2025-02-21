package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName ${entity}Enum.java
* @Description ${table.comment!}枚举
*/

public enum ${entity}Enum implements IBaseEnum {

    PAGE_FAIL(53001, "查询${table.comment!}分页失败"),
    LIST_FAIL(53002, "查询${table.comment!}列表失败"),
    FIND_ONE_FAIL(53003, "查询${table.comment!}对象失败"),
    SAVE_FAIL(53004, "保存${table.comment!}失败"),
    UPDATE_FAIL(53005, "修改${table.comment!}失败"),
    DEL_FAIL(53006, "删除${table.comment!}失败")
    ;

    private Integer code;

    private String msg;

    ${entity}Enum(Integer code, String msg) {
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
