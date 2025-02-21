package com.itheima.bolee.file.handler.qiniu.config;

import com.itheima.bolee.file.handler.qiniu.properties.QiniuProperties;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Qiniu服务配置类
 * </p>
 *     声明创建Qiniu核心配置对象
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuConfig {

    @Autowired
    private QiniuProperties qiniuConfigProperties;

    private static Map<String, Region> regions =new HashMap<>();

    static {
        regions.put(QiniuRegion.REGION_HUADONG,Region.huadong());
        regions.put(QiniuRegion.REGION_HUABEI,Region.huabei());
        regions.put(QiniuRegion.REGION_HUANAN,Region.huanan());
        regions.put(QiniuRegion.REGION_BEIMEI,Region.beimei());
        regions.put(QiniuRegion.REGION_DONGNANYA,Region.xinjiapo());
    }

    @Bean("qiniuConfiguration")
    public com.qiniu.storage.Configuration qiniuConfiguration() {

        // 设置存储区域
        String regionString = qiniuConfigProperties.getKodo().getRegion();

        Region region = regions.get(regionString);

        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(region);
        // 使用http协议
        configuration.useHttpsDomains = false;

        //上传限流,超过后上传会自动转为分片上传
        configuration.resumableUploadAPIV2BlockSize = qiniuConfigProperties.getUplimitSpeed();

        return configuration;
    }

    @Bean("qiniuAuth")
    public Auth qiniuAuth() {
        Auth auth = Auth.create(qiniuConfigProperties.getAccessKey(),
                qiniuConfigProperties.getSecretKey());
        return auth;
    }

    @Bean("bucketManager")
    public BucketManager bucketManager(@Qualifier("qiniuConfiguration") com.qiniu.storage.Configuration configuration,
                                   @Qualifier("qiniuAuth") Auth auth
                                   ) {
        BucketManager bucketManager = new BucketManager(auth, configuration);
        return bucketManager;
    }



    /**
     * <p>
     *     七牛服务区域标识
     * </p>
     *     此标识是由七牛的kodo对象存储中定义，参考 {@link Region}
     */
    public interface QiniuRegion {

        /**
         * z0 华东
         */
        String REGION_HUADONG = "z0";
        /**
         * z1 华北
         */
        String REGION_HUABEI = "z1";
        /**
         * z2 华南
         */
        String REGION_HUANAN = "z2";
        /**
         * na0 北美
         */
        String REGION_BEIMEI = "na0";
        /**
         * as0 东南亚
         */
        String REGION_DONGNANYA = "as0";
    }

}
