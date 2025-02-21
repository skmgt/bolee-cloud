package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName SickSearchRecordEnum.java
* @Description 疾病搜索记录枚举
*/

public enum SickSearchRecordEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询疾病搜索记录分页失败"),
    LIST_FAIL(53002, "查询疾病搜索记录列表失败"),
    FIND_ONE_FAIL(53003, "查询疾病搜索记录对象失败"),
    SAVE_FAIL(53004, "保存疾病搜索记录失败"),
    UPDATE_FAIL(53005, "修改疾病搜索记录失败"),
    DEL_FAIL(53006, "删除疾病搜索记录失败")
    ;

    private Integer code;

    private String msg;

    SickSearchRecordEnum(Integer code, String msg) {
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
