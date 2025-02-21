package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName SelfSelectionEnum.java
* @Description 自选保险枚举
*/

public enum SelfSelectionEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询自选保险分页失败"),
    LIST_FAIL(53002, "查询自选保险列表失败"),
    FIND_ONE_FAIL(53003, "查询自选保险对象失败"),
    SAVE_FAIL(53004, "保存自选保险失败"),
    UPDATE_FAIL(53005, "修改自选保险失败"),
    DEL_FAIL(53006, "删除自选保险失败"),
    EXIST_FAIL(53007, "重复的数据"),
    ;

    private Integer code;

    private String msg;

    SelfSelectionEnum(Integer code, String msg) {
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
