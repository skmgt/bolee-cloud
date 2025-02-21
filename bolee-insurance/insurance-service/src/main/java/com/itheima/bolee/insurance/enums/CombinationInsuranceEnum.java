package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CombinationInsuranceEnum.java
* @Description 组合保险枚举
*/

public enum CombinationInsuranceEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询组合保险分页失败"),
    LIST_FAIL(53002, "查询组合保险列表失败"),
    FIND_ONE_FAIL(53003, "查询组合保险对象失败"),
    SAVE_FAIL(53004, "保存组合保险失败"),
    UPDATE_FAIL(53005, "修改组合保险失败"),
    DEL_FAIL(53006, "删除组合保险失败")
    ;

    private Integer code;

    private String msg;

    CombinationInsuranceEnum(Integer code, String msg) {
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
