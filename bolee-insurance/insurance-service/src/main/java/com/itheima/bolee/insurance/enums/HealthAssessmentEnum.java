package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName HealthAssessmentEnum.java
* @Description 评估类目枚举
*/

public enum HealthAssessmentEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询评估类目分页失败"),
    LIST_FAIL(53002, "查询评估类目列表失败"),
    FIND_ONE_FAIL(53003, "查询评估类目对象失败"),
    SAVE_FAIL(53004, "保存评估类目失败"),
    UPDATE_FAIL(53005, "修改评估类目失败"),
    DEL_FAIL(53006, "删除评估类目失败")
    ;

    private Integer code;

    private String msg;

    HealthAssessmentEnum(Integer code, String msg) {
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
