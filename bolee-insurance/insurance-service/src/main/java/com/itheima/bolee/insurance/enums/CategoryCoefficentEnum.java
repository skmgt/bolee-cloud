package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CategoryCoefficentEnum.java
* @Description 分类系数项枚举
*/

public enum CategoryCoefficentEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询分类系数项分页失败"),
    LIST_FAIL(53002, "查询分类系数项列表失败"),
    FIND_ONE_FAIL(53003, "查询分类系数项对象失败"),
    SAVE_FAIL(53004, "保存分类系数项失败"),
    UPDATE_FAIL(53005, "修改分类系数项失败"),
    DEL_FAIL(53006, "删除分类系数项失败")
    ;

    private Integer code;

    private String msg;

    CategoryCoefficentEnum(Integer code, String msg) {
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
