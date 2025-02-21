package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CustomerInfoEnum.java
* @Description 客户信息表枚举
*/

public enum CustomerInfoEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询客户信息表分页失败"),
    LIST_FAIL(53002, "查询客户信息表列表失败"),
    FIND_ONE_FAIL(53003, "查询客户信息表对象失败"),
    SAVE_FAIL(53004, "保存客户信息表失败"),
    UPDATE_FAIL(53005, "修改客户信息表失败"),
    DEL_FAIL(53006, "删除客户信息表失败")
    ;

    private Integer code;

    private String msg;

    CustomerInfoEnum(Integer code, String msg) {
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
