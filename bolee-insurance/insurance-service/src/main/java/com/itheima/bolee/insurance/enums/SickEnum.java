package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName SickEnum.java
* @Description 疾病表枚举
*/

public enum SickEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询疾病表分页失败"),
    LIST_FAIL(53002, "查询疾病表列表失败"),
    FIND_ONE_FAIL(53003, "查询疾病表对象失败"),
    SAVE_FAIL(53004, "保存疾病表失败"),
    UPDATE_FAIL(53005, "修改疾病表失败"),
    DEL_FAIL(53006, "删除疾病表失败"),
    SICK_TYPE_FAIL(53007, "查询疾病类型列表失败"),

    ADVICE_SICK_DATA_FAIL(53008, "健康咨询疾病数据异常，数据不能为空"),
    ;

    private Integer code;

    private String msg;

    SickEnum(Integer code, String msg) {
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
