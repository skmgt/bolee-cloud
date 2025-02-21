package com.itheima.bolee.file.handler.qiniu.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description 阿里云OSS上传配置类
 */

@Data
@NoArgsConstructor
@ConfigurationProperties("spring.cloud.qiniu")
public class QiniuProperties {

    //访问key
    private String accessKey ;

    //密钥key
    private String secretKey ;

    //是否限流
    private Boolean islimitSpeed = true;

    //上传限流,超过后上传会自动转为分片上传
    private int uplimitSpeed = 1024 * 1024 * 8;


    private KodoProperties kodo;


    public static class KodoProperties {

        /**
         * 存款空间区域标识，参考 {@link com.qiniu.storage.Region} 中的region属性值
         */
        private String region;

        /**
         * 存储空间的名称
         */
        private String bucketName;

        /**
         * 存款空间访问域名
         */
        private String endpoint;

        public KodoProperties() {
        }

        public KodoProperties(String region, String bucketName) {
            this.region = region;
            this.bucketName = bucketName;
        }
        public KodoProperties(String region, String bucketName, String endpoint) {
            this.region = region;
            this.bucketName = bucketName;
            this.endpoint = endpoint;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }

}
