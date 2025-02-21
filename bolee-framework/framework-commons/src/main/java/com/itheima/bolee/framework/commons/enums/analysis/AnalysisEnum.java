package com.itheima.bolee.framework.commons.enums.analysis;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

public enum AnalysisEnum implements IBaseEnum {

    TIME_BUILD_FAIL(53001, "查询日期转换失败"),
    ANALYSIS_CUSTOMER_CITY(53002,"统计分析：保单合同关联城市异常"),

    ANALYSIS_CUSTOMER_ACTIVE(53002,"统计分析：客户活跃度统计异常"),

    ANALYSIS_CUSTOMER_INSTANCE(53003,"统计分析：客户合同金额统计异常"),
    ANALYSIS_CUSTOMER_SEX(53004, "统计分析：投保人性别统计异常"),

    ANALYSIS_CUSTOMER_INSURANCE_TYPE(53006, "统计分析：投保分类统计异常"),

    ANALYSIS_PV_UV(53007, "统计分析：PV_UV统计分析失败"),
    ;

    private Integer code;

    private String msg;

    AnalysisEnum(Integer code, String msg) {
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