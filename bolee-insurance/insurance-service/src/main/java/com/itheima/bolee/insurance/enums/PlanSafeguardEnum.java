package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName PlanSafeguardEnum.java
* @Description 方案保障项枚举
*/

public enum PlanSafeguardEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询方案保障项分页失败"),
    LIST_FAIL(53002, "查询方案保障项列表失败"),
    FIND_ONE_FAIL(53003, "查询方案保障项对象失败"),
    SAVE_FAIL(53004, "保存方案保障项失败"),
    UPDATE_FAIL(53005, "修改方案保障项失败"),
    DEL_FAIL(53006, "删除方案保障项失败")
    ;

    private Integer code;

    private String msg;

    PlanSafeguardEnum(Integer code, String msg) {
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
