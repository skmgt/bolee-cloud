package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName InsurancePlanEnum.java
* @Description 保险方案枚举
*/

public enum InsurancePlanEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询保险方案分页失败"),
    LIST_FAIL(53002, "查询保险方案列表失败"),
    FIND_ONE_FAIL(53003, "查询保险方案对象失败"),
    SAVE_FAIL(53004, "保存保险方案失败"),
    UPDATE_FAIL(53005, "修改保险方案失败"),
    DEL_FAIL(53006, "删除保险方案失败"),
    DATA_ERROR(53007,"数据异常"),
    ;

    private Integer code;

    private String msg;

    InsurancePlanEnum(Integer code, String msg) {
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
