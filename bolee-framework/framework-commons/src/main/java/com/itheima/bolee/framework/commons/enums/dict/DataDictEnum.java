package com.itheima.bolee.framework.commons.enums.dict;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @Description：数字字典枚举
 */
public enum DataDictEnum implements IBaseEnum {
    PAGE_FAIL(25001, "查询数据字典列表失败"),
    SAVE_FAIL(25002, "保存字典数据失败"),
    UPDATE_FAIL(25003, "修改字典数据失败"),
    FIND_DATADICTVO_DATAKEY(25004, "据dataKey获取DataDictVO失败"),
    FIND_DATADICTVO_PARENTKEY(25005, "获取ParentKey下的数据失败"),
    FIND_PARENTKEY_ALL(25006, "根据parentKey查询失败"),
    FIND_DATAKEY_ALL(25007, "根据dataKey查询失败"),
    CHECK_FALL(25008, "检查数字字典重复性异常");
    ;

    private Integer code;
    private String msg;

    DataDictEnum(Integer code, String msg) {
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
