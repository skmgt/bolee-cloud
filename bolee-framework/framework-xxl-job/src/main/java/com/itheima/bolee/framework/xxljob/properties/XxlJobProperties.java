package com.itheima.bolee.framework.xxljob.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @Description：自定义rabbit配置文件
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "itheima.framework.xxl-job.executor")
public class XxlJobProperties implements Serializable {

    public String adminAddresses;

    public String appName;

    public String ip;

    public Integer port;

    private String accessToken;

    private String logPath;

    private int logRetentionDays;

}
