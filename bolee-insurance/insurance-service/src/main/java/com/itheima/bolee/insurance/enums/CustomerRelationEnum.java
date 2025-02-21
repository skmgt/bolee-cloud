package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName CustomerRelationEnum.java
* @Description 客户关系表枚举
*/

public enum CustomerRelationEnum implements IBaseEnum {


    PAGE_FAIL(53001, "查询客户关系表分页失败"),
    LIST_FAIL(53002, "查询客户关系表列表失败"),
    FIND_ONE_FAIL(53003, "查询客户关系表对象失败"),
    SAVE_FAIL(53004, "保存客户关系表失败"),
    UPDATE_FAIL(53005, "修改客户关系表失败"),
    DEL_FAIL(53006, "删除客户关系表失败"),
    UN_LOGIN(530007,"当前操作未登录"),
    RELATION_FIELD_ERROR(530008,"关系标志位内容错误"),
    ID_CARD_ERROR(530009, "当前用户不存在身份证号，数据异常");

    private Integer code;

    private String msg;

    CustomerRelationEnum(Integer code, String msg) {
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
