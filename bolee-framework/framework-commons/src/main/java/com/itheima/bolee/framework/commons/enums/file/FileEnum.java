package com.itheima.bolee.framework.commons.enums.file;

import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;

/**
 * @ClassName FileEnum.java
 * @Description 文件上传
 */
public enum FileEnum implements IBaseEnum {

    DOWNLOAD_FAIL(21001, "下载文件失败"),
    UPLOAD_FAIL(21002, "上传文件失败"),
    SELECT_FILE_BUSINESSID_FAIL(21003, "查询业务对应附件失败"),
    DELETE_FILE_BUSINESSID_FAIL(21004, "删除业务对应附件失败"),
    PAGE_FAIL(21005, "查询附件列表失败"),
    DELETE_FAIL(21006, "删除附件失败"),
    SELECT_BUSUBBESSID_FAIL(21007, "查询附件所有业务ID失败"),
    BIND_FAIL(21008, "业务绑定文件失败"),
    FILE_NAME_NOTFOUND(21009, "上传文件名称为空"),
    BUCKET_NAME_NULL(21010, "存储空间名称为空"),
    STORE_FLAG_NULL(21011, "存储标识为空"),
    FILE_PREFIX_NOT_FOUND(21012, "文件路径前缀标识没找到"),
    FILE_OPERATE(21013, "操作文件数据失败"),
    FILE_DATA_LOSE(21014, "文件操作关键数据异常"),
    INIT_UPLOAD_FAIL(21015, "分片上传文件初始化失败"),
    UPLOAD_PART_FAIL(21016, "分片上传文件失败"),
    COMPLETE_PART_FAIL(21017, "分片上传合并文件失败"),
    CLEAR_FILE_TASK_FAIL(21018, "定时清理失败"),
    ;

    private Integer code;
    private String msg;

    FileEnum(Integer code, String msg) {
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
