package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WarrantyRenewalEnum.java
* @Description 合同续期枚举
*/

public enum WarrantyRenewalEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询合同续期分页失败"),
    LIST_FAIL(53002, "查询合同续期列表失败"),
    FIND_ONE_FAIL(53003, "查询合同续期对象失败"),
    SAVE_FAIL(53004, "保存合同续期失败"),
    UPDATE_FAIL(53005, "修改合同续期失败"),
    DEL_FAIL(53006, "删除合同续期失败")
    ;

    private Integer code;

    private String msg;

    WarrantyRenewalEnum(Integer code, String msg) {
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
