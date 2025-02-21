package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName SafeguardEnum.java
* @Description 保障项枚举
*/

public enum SafeguardEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询保障项分页失败"),
    LIST_FAIL(53002, "查询保障项列表失败"),
    FIND_ONE_FAIL(53003, "查询保障项对象失败"),
    SAVE_FAIL(53004, "保存保障项失败"),
    UPDATE_FAIL(53005, "修改保障项失败"),
    DEL_FAIL(53006, "删除保障项失败")
    ;

    private Integer code;

    private String msg;

    SafeguardEnum(Integer code, String msg) {
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
