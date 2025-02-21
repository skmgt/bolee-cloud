package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DoInsureConversionDpvEnum.java
* @Description 投保转换率枚举
*/

public enum DoInsureConversionDpvEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询投保转换率分页失败"),
    LIST_FAIL(53002, "查询投保转换率列表失败"),
    FIND_ONE_FAIL(53003, "查询投保转换率对象失败"),
    SAVE_FAIL(53004, "保存投保转换率失败"),
    UPDATE_FAIL(53005, "修改投保转换率失败"),
    DEL_FAIL(53006, "删除投保转换率失败")
    ;

    private Integer code;

    private String msg;

    DoInsureConversionDpvEnum(Integer code, String msg) {
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
