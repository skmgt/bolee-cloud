package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DauTimeEnum.java
* @Description 用户活跃数时段枚举
*/

public enum DauTimeEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询用户活跃数时段分页失败"),
    LIST_FAIL(53002, "查询用户活跃数时段列表失败"),
    FIND_ONE_FAIL(53003, "查询用户活跃数时段对象失败"),
    SAVE_FAIL(53004, "保存用户活跃数时段失败"),
    UPDATE_FAIL(53005, "修改用户活跃数时段失败"),
    DEL_FAIL(53006, "删除用户活跃数时段失败")
    ;

    private Integer code;

    private String msg;

    DauTimeEnum(Integer code, String msg) {
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
