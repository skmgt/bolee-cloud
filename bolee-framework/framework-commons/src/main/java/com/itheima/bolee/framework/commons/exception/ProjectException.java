package com.itheima.bolee.framework.commons.exception;


import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description：自定义异常
 */
@Data
@NoArgsConstructor
@ToString
public class ProjectException extends RuntimeException {

    //错误编码
    private int code;

    //提示信息
    private String message;

    //异常接口
    private IBaseEnum baseEnum;

    public ProjectException(IBaseEnum baseEnum) {
        this.baseEnum = baseEnum;
    }
}
