package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WarrantyOrderEnum.java
* @Description 合同订单枚举
*/

public enum WarrantyOrderEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询合同订单分页失败"),
    LIST_FAIL(53002, "查询合同订单列表失败"),
    FIND_ONE_FAIL(53003, "查询合同订单对象失败"),
    SAVE_FAIL(53004, "保存合同订单失败"),
    UPDATE_FAIL(53005, "修改合同订单失败"),
    DEL_FAIL(53006, "删除合同订单失败"),
    SYNC_PAYMENT_FAIL(53007, "同步订单结果失败"),
    ;

    private Integer code;

    private String msg;

    WarrantyOrderEnum(Integer code, String msg) {
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
