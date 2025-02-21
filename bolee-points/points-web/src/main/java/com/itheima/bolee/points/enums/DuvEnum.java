package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DuvEnum.java
* @Description 日唯一访问量枚举
*/

public enum DuvEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询日唯一访问量分页失败"),
    LIST_FAIL(53002, "查询日唯一访问量列表失败"),
    FIND_ONE_FAIL(53003, "查询日唯一访问量对象失败"),
    SAVE_FAIL(53004, "保存日唯一访问量失败"),
    UPDATE_FAIL(53005, "修改日唯一访问量失败"),
    DEL_FAIL(53006, "删除日唯一访问量失败")
    ;

    private Integer code;

    private String msg;

    DuvEnum(Integer code, String msg) {
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
