package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName InsuranceSievingEnum.java
* @Description 初筛结果枚举
*/

public enum InsuranceSievingEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询初筛结果分页失败"),
    LIST_FAIL(53002, "查询初筛结果列表失败"),
    FIND_ONE_FAIL(53003, "查询初筛结果对象失败"),
    SAVE_FAIL(53004, "保存初筛结果失败"),
    UPDATE_FAIL(53005, "修改初筛结果失败"),
    DEL_FAIL(53006, "删除初筛结果失败")
    ;

    private Integer code;

    private String msg;

    InsuranceSievingEnum(Integer code, String msg) {
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
