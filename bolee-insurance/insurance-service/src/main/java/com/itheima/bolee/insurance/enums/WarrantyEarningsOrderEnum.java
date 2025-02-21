package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WarrantyEarningsOrderEnum.java
* @Description 给付计划订单枚举
*/

public enum WarrantyEarningsOrderEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询给付计划订单分页失败"),
    LIST_FAIL(53002, "查询给付计划订单列表失败"),
    FIND_ONE_FAIL(53003, "查询给付计划订单对象失败"),
    SAVE_FAIL(53004, "保存给付计划订单失败"),
    UPDATE_FAIL(53005, "修改给付计划订单失败"),
    DEL_FAIL(53006, "删除给付计划订单失败")
    ;

    private Integer code;

    private String msg;

    WarrantyEarningsOrderEnum(Integer code, String msg) {
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
