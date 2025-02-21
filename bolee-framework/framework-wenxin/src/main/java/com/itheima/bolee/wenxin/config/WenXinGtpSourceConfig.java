package com.itheima.bolee.wenxin.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WenXinGtpSourceConfig
 *
 * @author: wgl
 * @describe: 文心一言配置类
 * @date: 2022/12/28 10:10
 */
@Data
@ConfigurationProperties(prefix = "baidu")
public class WenXinGtpSourceConfig {


    @Value("${baidu.wenxin.appid:38982621}")
    private String appId;

    /**
     * 当前应用的ak
     */
    @Value("${baidu.wenxin.apikey:2369urj0vruWDklhCtjWoKH5}")
    private String apiKey;


    /**
     * 当前应用的sk
     */
    @Value("${baidu.wenxin.secretKey:XoNQwu3AKmagGWoEwZG1TXwV9U95w0jd}")
    private String secretKey;

}