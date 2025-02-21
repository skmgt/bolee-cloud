package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName BrowsingHistoryEnum.java
* @Description 浏览记录枚举
*/

public enum BrowsingHistoryEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询浏览记录分页失败"),
    LIST_FAIL(53002, "查询浏览记录列表失败"),
    FIND_ONE_FAIL(53003, "查询浏览记录对象失败"),
    SAVE_FAIL(53004, "保存浏览记录失败"),
    UPDATE_FAIL(53005, "修改浏览记录失败"),
    DEL_FAIL(53006, "删除浏览记录失败")
    ;

    private Integer code;

    private String msg;

    BrowsingHistoryEnum(Integer code, String msg) {
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
