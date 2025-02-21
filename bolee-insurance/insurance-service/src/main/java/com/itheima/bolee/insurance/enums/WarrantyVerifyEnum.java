package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WarrantyVerifyEnum.java
* @Description 合同核保枚举
*/

public enum WarrantyVerifyEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询合同核保分页失败"),
    LIST_FAIL(53002, "查询合同核保列表失败"),
    FIND_ONE_FAIL(53003, "查询合同核保对象失败"),
    SAVE_FAIL(53004, "保存合同核保失败"),
    UPDATE_FAIL(53005, "修改合同核保失败"),
    DEL_FAIL(53006, "删除合同核保失败")
    ;

    private Integer code;

    private String msg;

    WarrantyVerifyEnum(Integer code, String msg) {
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
