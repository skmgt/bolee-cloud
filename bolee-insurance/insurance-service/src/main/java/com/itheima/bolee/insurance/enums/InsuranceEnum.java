package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName InsuranceEnum.java
* @Description 保险产品枚举
*/

public enum InsuranceEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询保险产品分页失败"),
    LIST_FAIL(53002, "查询保险产品列表失败"),
    FIND_ONE_FAIL(53003, "查询保险产品对象失败"),
    SAVE_FAIL(53004, "保存保险产品失败"),
    UPDATE_FAIL(53005, "修改保险产品失败"),
    DEL_FAIL(53006, "删除保险产品失败"),
    CHECK_ROUTE_FAIL(53007, "检测路由获取失败"),
    CALCULATION_FAIL(53008, "保费计算失败"),
    INCOME_FAIL(53008, "收益计算失败"),
    ;

    private Integer code;

    private String msg;

    InsuranceEnum(Integer code, String msg) {
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
