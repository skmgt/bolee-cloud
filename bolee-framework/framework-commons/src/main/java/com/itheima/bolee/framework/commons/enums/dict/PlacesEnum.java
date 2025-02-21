package com.itheima.bolee.framework.commons.enums.dict;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName PlacesEnum.java
 * @Description 地区地域
 */
public enum PlacesEnum implements IBaseEnum {
    LIST_FAIL(34001, "查询省市区失败");

    private Integer code;
    private String msg;

    PlacesEnum(Integer code, String msg) {
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
