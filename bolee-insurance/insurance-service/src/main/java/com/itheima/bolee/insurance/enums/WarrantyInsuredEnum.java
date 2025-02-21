package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WarrantyInsuredEnum.java
* @Description 合同被保人枚举
*/

public enum WarrantyInsuredEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询合同被保人分页失败"),
    LIST_FAIL(53002, "查询合同被保人列表失败"),
    FIND_ONE_FAIL(53003, "查询合同被保人对象失败"),
    SAVE_FAIL(53004, "保存合同被保人失败"),
    UPDATE_FAIL(53005, "修改合同被保人失败"),
    DEL_FAIL(53006, "删除合同被保人失败")
    ;

    private Integer code;

    private String msg;

    WarrantyInsuredEnum(Integer code, String msg) {
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
