package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

public enum WarrantyApplicantEnum implements IBaseEnum {


    APPLICANT_ERROR(53001,"投保人信息构建失败" ),
    INSURED_ERROR(53002,"被保人信息构建失败"),
    ;

    private Integer code;

    private String msg;

    WarrantyApplicantEnum(Integer code, String msg) {
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