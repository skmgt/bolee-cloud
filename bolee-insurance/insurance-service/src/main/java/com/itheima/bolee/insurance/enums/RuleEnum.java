package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * 规则异常枚举类
 */
public enum RuleEnum implements IBaseEnum {

    DATA_ERROR_PER_ERROR(53001, "数据异常:未找到个人信息"),
    WORRRY_FREE_INSURANCE_ERROR(53002, "省心配模块异常:无法计算"),
    LOCK_NOT_ACQUIRED_ERROR(53003,"当前用户正在计算中"),

    SICK_ERROR_INSURANCE_ERROR(53004, "初筛，疾病问卷模块异常：无法计算"),

    ;

    private Integer code;

    private String msg;

    RuleEnum(Integer code, String msg) {
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