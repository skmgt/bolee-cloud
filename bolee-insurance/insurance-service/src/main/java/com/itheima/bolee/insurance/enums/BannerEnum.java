package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName BannerEnum.java
* @Description 枚举
*/

public enum BannerEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询分页失败"),
    LIST_FAIL(53002, "查询列表失败"),
    FIND_ONE_FAIL(53003, "查询对象失败"),
    SAVE_FAIL(53004, "保存失败"),
    UPDATE_FAIL(53005, "修改失败"),
    DEL_FAIL(53006, "删除失败")
    ;

    private Integer code;

    private String msg;

    BannerEnum(Integer code, String msg) {
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
