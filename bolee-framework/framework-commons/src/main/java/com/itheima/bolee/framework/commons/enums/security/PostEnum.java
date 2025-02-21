package com.itheima.bolee.framework.commons.enums.security;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName PostEnum.java
* @Description 岗位表枚举
*/

public enum PostEnum implements IBaseEnum {

    SUCCEED(200,"操作成功"),
    FAIL(1000,"操作失败"),
    PAGE_FAIL(73001, "查询岗位表列表失败"),
    SAVE_FAIL(73002, "保存岗位表失败"),
    UPDATE_FAIL(73003, "修改岗位表失败"),
    DEL_FAIL(73004, "删除岗位表失败"),
    LIST_FAIL(73005, "查询岗位表失败"),
    CREATE_POST_NO_FAIL(73006, "创建岗位编号失败")
    ;

    private Integer code;

    private String msg;

    PostEnum(Integer code, String msg) {
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
