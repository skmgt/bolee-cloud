package com.itheima.bolee.wenxin.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * WenXinGptException
 *
 * @author: wgl
 * @describe: 文心一言调用异常
 * @date: 2022/12/28 10:10
 */
public class WenXinGptException extends RuntimeException {

    //错误编码
    private int code;

    //提示信息
    private String message;

    public WenXinGptException(WenXinExcpetionEnum wenXinExcpetionEnum) {
        this.code = wenXinExcpetionEnum.code;
        this.message = wenXinExcpetionEnum.msg;
    }

    @AllArgsConstructor
    @Getter
    public enum WenXinExcpetionEnum{
        WEN_XIN_REQUEST_ERROR(50000,"文心一言调用失败"),
        WEN_XIN_TOKEN_REQUEST_ERROR(50001,"文心一言Token获取失败"),

        WEN_XIN_TOKEN_QUESTION_ERROR(50002,"文心一言T提问失败"),
        ;
        private int code;

        private String msg;

    }
}

