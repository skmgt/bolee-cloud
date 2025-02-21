package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName ClaimEnum.java
* @Description 理赔表单项枚举
*/

public enum ClaimEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询理赔表单项分页失败"),
    LIST_FAIL(53002, "查询理赔表单项列表失败"),
    FIND_ONE_FAIL(53003, "查询理赔表单项对象失败"),
    SAVE_FAIL(53004, "保存理赔表单项失败"),
    UPDATE_FAIL(53005, "修改理赔表单项失败"),
    DEL_FAIL(53006, "删除理赔表单项失败")
    ;

    private Integer code;

    private String msg;

    ClaimEnum(Integer code, String msg) {
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
