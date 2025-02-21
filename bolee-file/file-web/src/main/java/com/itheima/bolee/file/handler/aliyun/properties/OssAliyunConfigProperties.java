package com.itheima.bolee.file.handler.aliyun.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 阿里云OSS上传配置类
 */

@Data
@NoArgsConstructor
@ConfigurationProperties("spring.cloud.alicloud.oss")
public class OssAliyunConfigProperties {

    //区域
    String region;

    //秘钥ID
    String accessKeyId;

    //秘钥
    String accessKeySecret;

    //角色
    String roleArn;

    //桶名称
    private String bucketName ;

    //访问终端域名地址
    private String endpoint;

    //是否限流
    private Boolean islimitSpeed = true;

    //上传限流
    private int uplimitSpeed = 100 * 1024 * 1024 * 2;

    //下载限流
    private int downlimitSpeed = 100 * 1024 * 8;

}
