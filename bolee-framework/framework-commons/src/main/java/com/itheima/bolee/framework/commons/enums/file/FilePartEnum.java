package com.itheima.bolee.framework.commons.enums.file;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName FilePartEnum.java
* @Description 文件分片枚举
*/

public enum FilePartEnum implements IBaseEnum {

    PAGE_FAIL(99001, "文件分片查询列表失败"),
    SAVE_FAIL(99002, "文件分片保存失败"),
    UPDATE_FAIL(99003, "文件分片修改失败"),
    DEL_FAIL(99004, "文件分片删除失败"),
    LIST_FAIL(99005, "文件分片查询失败");

    private Integer code;
    private String msg;

    FilePartEnum(Integer code, String msg) {
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
