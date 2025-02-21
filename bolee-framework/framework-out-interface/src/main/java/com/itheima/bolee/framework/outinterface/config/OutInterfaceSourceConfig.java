package com.itheima.bolee.framework.outinterface.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * BaiduConfig
 *
 * @author: wgl
 * @describe: 外部数据接口配置中心
 * @date: 2022/12/28 10:10
 */
@Data
@ConfigurationProperties(prefix = "baidu")
public class OutInterfaceSourceConfig {


    /**
     * 当前应用的ak
     */
    @Value("${baidu.apikey}")
    private String apiKey;


    /**
     * 当前应用的sk
     */
    @Value("${baidu.secretKey}")
    private String secretKey;

}
