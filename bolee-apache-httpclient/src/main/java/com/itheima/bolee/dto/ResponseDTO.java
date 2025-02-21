package com.itheima.bolee.dto;

import lombok.Data;

import java.util.Map;

/**
 * ResponseDTO
 *
 * @author: wgl
 * @describe: 返回结果对象
 * @date: 2022/12/28 10:10
 */
@Data
public class ResponseDTO {

    private String msg;

    private String code;

    private Map data;
}
