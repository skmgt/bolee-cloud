package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DoInsureGenderDuvEnum.java
* @Description 性别日投保用户访问数枚举
*/

public enum DoInsureGenderDuvEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询性别日投保用户访问数分页失败"),
    LIST_FAIL(53002, "查询性别日投保用户访问数列表失败"),
    FIND_ONE_FAIL(53003, "查询性别日投保用户访问数对象失败"),
    SAVE_FAIL(53004, "保存性别日投保用户访问数失败"),
    UPDATE_FAIL(53005, "修改性别日投保用户访问数失败"),
    DEL_FAIL(53006, "删除性别日投保用户访问数失败")
    ;

    private Integer code;

    private String msg;

    DoInsureGenderDuvEnum(Integer code, String msg) {
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
