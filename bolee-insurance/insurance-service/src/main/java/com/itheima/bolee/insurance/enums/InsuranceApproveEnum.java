package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName InsuranceApproveEnum.java
* @Description 保批信息枚举
*/

public enum InsuranceApproveEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询保批信息分页失败"),
    LIST_FAIL(53002, "查询保批信息列表失败"),
    FIND_ONE_FAIL(53003, "查询保批信息对象失败"),
    SAVE_FAIL(53004, "保存保批信息失败"),
    UPDATE_FAIL(53005, "修改保批信息失败"),
    DEL_FAIL(53006, "删除保批信息失败")
    ;

    private Integer code;

    private String msg;

    InsuranceApproveEnum(Integer code, String msg) {
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
