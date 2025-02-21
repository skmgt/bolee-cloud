package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DnuEnum.java
* @Description 日新增用户数枚举
*/

public enum DnuEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询日新增用户数分页失败"),
    LIST_FAIL(53002, "查询日新增用户数列表失败"),
    FIND_ONE_FAIL(53003, "查询日新增用户数对象失败"),
    SAVE_FAIL(53004, "保存日新增用户数失败"),
    UPDATE_FAIL(53005, "修改日新增用户数失败"),
    DEL_FAIL(53006, "删除日新增用户数失败")
    ;

    private Integer code;

    private String msg;

    DnuEnum(Integer code, String msg) {
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
