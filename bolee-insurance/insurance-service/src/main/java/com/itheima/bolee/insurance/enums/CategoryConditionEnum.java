package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CategoryConditionEnum.java
* @Description 分类筛选项枚举
*/

public enum CategoryConditionEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询分类筛选项分页失败"),
    LIST_FAIL(53002, "查询分类筛选项列表失败"),
    FIND_ONE_FAIL(53003, "查询分类筛选项对象失败"),
    SAVE_FAIL(53004, "保存分类筛选项失败"),
    UPDATE_FAIL(53005, "修改分类筛选项失败"),
    DEL_FAIL(53006, "删除分类筛选项失败")
    ;

    private Integer code;

    private String msg;

    CategoryConditionEnum(Integer code, String msg) {
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
