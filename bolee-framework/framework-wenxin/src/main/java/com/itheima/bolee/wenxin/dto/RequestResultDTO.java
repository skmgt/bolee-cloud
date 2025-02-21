package com.itheima.bolee.wenxin.dto;

import lombok.Data;

/**
 * RequestDTO
 *
 * @author: wgl
 * @describe: 请求DTO
 * @date: 2022/12/28 10:10
 */
@Data
public class RequestResultDTO {

    private String id;

    private String object;

    private String created;

    private String result;

    private Boolean is_truncated;

    private Boolean need_clear_history;
}