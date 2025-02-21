package com.itheima.easy.enums;

import com.itheima.easy.basic.IBasicEnum;

/**
* @ClassName ${entity}Enum.java
* @Description ${table.comment!}枚举
*/

public enum ${entity}Enum implements IBasicEnum {

    SUCCEED("200","操作成功"),
    FAIL("1000","操作失败"),
    PAGE_FAIL("53001", "查询${table.comment!}列表失败"),
    SAVE_FAIL("53002", "保存${table.comment!}失败"),
    UPDATE_FAIL("53003", "修改${table.comment!}失败"),
    DEL_FAIL("53004", "删除${table.comment!}失败"),
    LIST_FAIL("53005", "查询${table.comment!}失败")
    ;

    private String code;

    private String msg;

    ${entity}Enum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
