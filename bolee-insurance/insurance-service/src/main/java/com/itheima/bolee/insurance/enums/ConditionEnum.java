package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName ConditionEnum.java
* @Description 筛选项枚举
*/

public enum ConditionEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询筛选项分页失败"),
    LIST_FAIL(53002, "查询筛选项列表失败"),
    FIND_ONE_FAIL(53003, "查询筛选项对象失败"),
    SAVE_FAIL(53004, "保存筛选项失败"),
    UPDATE_FAIL(53005, "修改筛选项失败"),
    DEL_FAIL(53006, "删除筛选项失败")
    ;

    private Integer code;

    private String msg;

    ConditionEnum(Integer code, String msg) {
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
