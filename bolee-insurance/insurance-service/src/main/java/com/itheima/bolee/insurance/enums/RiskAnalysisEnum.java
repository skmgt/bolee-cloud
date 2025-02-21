package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName RiskAnalysisEnum.java
* @Description 风险类目枚举
*/

public enum RiskAnalysisEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询风险类目分页失败"),
    LIST_FAIL(53002, "查询风险类目列表失败"),
    FIND_ONE_FAIL(53003, "查询风险类目对象失败"),
    SAVE_FAIL(53004, "保存风险类目失败"),
    UPDATE_FAIL(53005, "修改风险类目失败"),
    DEL_FAIL(53006, "删除风险类目失败")
    ;

    private Integer code;

    private String msg;

    RiskAnalysisEnum(Integer code, String msg) {
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
