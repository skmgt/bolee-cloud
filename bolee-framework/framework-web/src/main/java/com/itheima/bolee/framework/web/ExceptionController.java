package com.itheima.bolee.framework.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ClassName BaseController.java
 * @Description 基础的controller
 * ControllerAdvice：对controller层的增强，其他的controller则不需要继承，也会被拦截处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionController {

    /**
     * 对于项目中业务异常进行处理
     *   由开发人员在处理业务代码时主动抛出的异常信息
     * */
    @ExceptionHandler(ProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> prodException(ProjectException e) {
        log.error("【业务操作异常】{}",e.getMessage());
        return ResponseResultBuild.build(e.getBaseEnum(), e.getMessage());
    }

    /**
     * 对于项目中Validation数据校验异常处理
     *    校验Controller层接收数据的合法性
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> vaildDataException(MethodArgumentNotValidException manvExceptionex) {
        log.warn("【数据校验异常】:{} ", manvExceptionex.getMessage());
        return ResponseResultBuild.build(BaseEnum.VALID_EXCEPTION, manvExceptionex.getMessage());
    }

    /**
     * 项目中框架级别或没有指定的异常处理
     * */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> topException(Exception ex) {
        log.error("【系统运行异常】：{}", ExceptionsUtil.getStackTraceAsString(ex));
        return ResponseResultBuild.build(BaseEnum.SYSYTEM_FAIL, ex.getMessage());
    }

}
