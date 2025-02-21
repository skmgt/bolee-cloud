package com.itheima.bolee.dto;

import lombok.Data;

/**
 * EncodeDTO
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
@Data
public class EncodeDTO {

    /**
     * 原始body参数
     */
    private String body;

    /**
     * 加密后的密文
     */
    private String encodeBody;

    /**
     * head中rsa加密后的aes密钥
     */
    private String headAESSecurityKey;
}
