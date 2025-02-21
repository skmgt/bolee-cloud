package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName PostEnum.java
* @Description 公司表枚举
*/

public enum CompanyEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(74001, "查询公司表列表失败"),
    SAVE_FAIL(74002, "保存公司表失败"),
    UPDATE_FAIL(74003, "修改公司表失败"),
    DEL_FAIL(74004, "删除公司表失败"),
    LIST_FAIL(74005, "查询公司表失败"),
    CREATE_POST_NO_FAIL(74006, "创建公司编号失败")
    ;

    private Integer code;

    private String msg;

    CompanyEnum(Integer code, String msg) {
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
