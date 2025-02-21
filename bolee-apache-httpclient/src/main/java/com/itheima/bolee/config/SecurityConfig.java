package com.itheima.bolee.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SecurityConfig
 *
 * @author: wgl
 * @describe: 客户端秘钥配置
 * @date: 2022/12/28 10:10
 */
@ConfigurationProperties(prefix = "itheima.security")
@Data
public class SecurityConfig {

    /**
     * 客户appId
     */
    @Value("${itheima.security.client.appid}")
    private String appId;

    /**
     * 客户端私钥
     */
    @Value("${itheima.security.client.privateKey}")
    private String privateKey;


    /**
     * 服务端公钥
     */
    @Value("${itheima.security.server.publicKey}")
    private String publicKey;
}
