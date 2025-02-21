package com.itheima.bolee.insurance.enums;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
* @ClassName WorryFreeFlowNodeEnum.java
* @Description 省心配流程节点记录枚举
*/

public enum WorryFreeFlowNodeEnum implements IBaseEnum {

    PAGE_FAIL(53001, "查询省心配流程节点记录分页失败"),
    LIST_FAIL(53002, "查询省心配流程节点记录列表失败"),
    FIND_ONE_FAIL(53003, "查询省心配流程节点记录对象失败"),
    SAVE_FAIL(53004, "保存省心配流程节点记录失败"),
    UPDATE_FAIL(53005, "修改省心配流程节点记录失败"),
    DEL_FAIL(53006, "删除省心配流程节点记录失败")
    ;

    private Integer code;

    private String msg;

    WorryFreeFlowNodeEnum(Integer code, String msg) {
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
