package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName PlanEarningsEnum.java
* @Description 方案给付枚举
*/

public enum PlanEarningsEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询方案给付分页失败"),
    LIST_FAIL(53002, "查询方案给付列表失败"),
    FIND_ONE_FAIL(53003, "查询方案给付对象失败"),
    SAVE_FAIL(53004, "保存方案给付失败"),
    UPDATE_FAIL(53005, "修改方案给付失败"),
    DEL_FAIL(53006, "删除方案给付失败")
    ;

    private Integer code;

    private String msg;

    PlanEarningsEnum(Integer code, String msg) {
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
