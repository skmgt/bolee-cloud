package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName InsuranceTopEnum.java
* @Description 人气保险枚举
*/

public enum InsuranceTopEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询人气保险分页失败"),
    LIST_FAIL(53002, "查询人气保险列表失败"),
    FIND_ONE_FAIL(53003, "查询人气保险对象失败"),
    SAVE_FAIL(53004, "保存人气保险失败"),
    UPDATE_FAIL(53005, "修改人气保险失败"),
    DEL_FAIL(53006, "删除人气保险失败"),
    DATA_FAIL(50337,"数据异常")
    ;

    private Integer code;

    private String msg;

    InsuranceTopEnum(Integer code, String msg) {
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
