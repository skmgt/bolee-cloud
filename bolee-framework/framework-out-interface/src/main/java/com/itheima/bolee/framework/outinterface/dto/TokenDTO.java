package com.itheima.bolee.framework.outinterface.dto;

import lombok.Data;

/**
 * TokenDTO
 *
 * @author: wgl
 * @describe: 请求百度云时的Token传输对象
 * @date: 2022/12/28 10:10
 */
@Data
public class TokenDTO {

    private String refresh_token;

    private String expires_in;

    private String session_key;

    private String access_token;

    private String scope;

    private String session_secret;
}
