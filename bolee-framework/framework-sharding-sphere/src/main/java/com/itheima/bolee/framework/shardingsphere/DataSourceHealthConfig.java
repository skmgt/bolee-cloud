package com.itheima.bolee.framework.shardingsphere;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DataSourceHealthConfig.java
 * @Description 数据源健康检查
 */
@Configuration
public class DataSourceHealthConfig extends DataSourceHealthContributorAutoConfiguration {

    public DataSourceHealthConfig(ObjectProvider<DataSourcePoolMetadataProvider> metadataProviders) {
        super(metadataProviders);
    }
}
