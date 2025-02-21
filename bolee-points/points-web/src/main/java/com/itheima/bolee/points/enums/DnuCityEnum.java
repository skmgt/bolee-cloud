package com.itheima.bolee.points.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName DnuCityEnum.java
* @Description 新增用户所属城市枚举
*/

public enum DnuCityEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询新增用户所属城市分页失败"),
    LIST_FAIL(53002, "查询新增用户所属城市列表失败"),
    FIND_ONE_FAIL(53003, "查询新增用户所属城市对象失败"),
    SAVE_FAIL(53004, "保存新增用户所属城市失败"),
    UPDATE_FAIL(53005, "修改新增用户所属城市失败"),
    DEL_FAIL(53006, "删除新增用户所属城市失败")
    ;

    private Integer code;

    private String msg;

    DnuCityEnum(Integer code, String msg) {
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
