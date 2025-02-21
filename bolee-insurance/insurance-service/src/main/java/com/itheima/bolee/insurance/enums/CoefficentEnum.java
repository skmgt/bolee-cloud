package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CoefficentEnum.java
* @Description 系数项枚举
*/

public enum CoefficentEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询系数项分页失败"),
    LIST_FAIL(53002, "查询系数项列表失败"),
    FIND_ONE_FAIL(53003, "查询系数项对象失败"),
    SAVE_FAIL(53004, "保存系数项失败"),
    UPDATE_FAIL(53005, "修改系数项失败"),
    DEL_FAIL(53006, "删除系数项失败")
    ;

    private Integer code;

    private String msg;

    CoefficentEnum(Integer code, String msg) {
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
