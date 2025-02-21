package com.itheima.bolee.framework.redis.resolver;

import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.framework.redis.properties.DataSecurityCacheProperties;
import com.itheima.bolee.framework.redis.properties.TenantCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CustomCacheManager.java
 * @Description 自定义缓存管理者
 */
public class CustomRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private TenantCacheProperties tenantCacheProperties;
    private DataSecurityCacheProperties dataSecurityCacheProperties;

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public CustomRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public CustomRedisCacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration cacheConfiguration, TenantCacheProperties tenantCacheProperties, DataSecurityCacheProperties dataSecurityCacheProperties) {
        this(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),cacheConfiguration);
        this.tenantCacheProperties =tenantCacheProperties;
        this.dataSecurityCacheProperties=dataSecurityCacheProperties;
    }

    /**
     * 覆盖父类创建RedisCache，采用自定义的RedisCacheResolver
     * @Title: createRedisCache
     * @Description: TODO
     * @param  @param name
     * @param  @param cacheConfig
     * @param  @return
     * @throws
     *
     */
    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        //自定义缓存时间例如
        //@Cacheable(value = DataDictCacheConstant.PARENT_KEY+"=-1",key = "#parentKey")
        String[] split = name.split("&");
        if (split.length==1){
            name=split[0];
        }else if (split.length==2){
            name=split[0];
            long ttl = Long.valueOf(split[1].replaceAll("ttl=",""));
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }else {
            throw  new RuntimeException("自定义缓存失效时间异常");
        }
        return new CustomRedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    /**
     * 从上下文中获取租户ID，重写key值添加多租户模式
     * @param name
     * @return
     */
    @Override
    public Cache getCache(String name) {
        //多租户及数据权限缓存key处理
        String companyNo = SubjectContent.getCompanyNo();
        String dataSecurity = null;
        DataSecurityVO dataSecurityVO = SubjectContent.getDataSecurityVO();
        //查看个人数据
        if (!EmptyUtil.isNullOrEmpty(dataSecurityVO)&&dataSecurityVO.getYouselfData()){
            dataSecurity = String.valueOf(SubjectContent.getUserVO().getId().hashCode());
        }
        //查看授权数据
        if (!EmptyUtil.isNullOrEmpty(dataSecurityVO)&&!dataSecurityVO.getYouselfData()){
            dataSecurity = dataSecurity+":"+SubjectContent.getUserVO().getDataSecurityVO().hashCode();
        }
        String[] nameTime = name.split("&");
        String targetTab = "tab_"+name.split(":")[0].replace("-","_");
        if (!EmptyUtil.isNullOrEmpty(companyNo)&&
            !tenantCacheProperties.getDefaultCompanyNo().equals(companyNo)){
            boolean isIgnoreCompanyNo = false;
            if (!EmptyUtil.isNullOrEmpty(tenantCacheProperties.getIgnoreCompanyTables())){
                isIgnoreCompanyNo =  !tenantCacheProperties.getIgnoreCompanyTables().contains(targetTab);
            }
            if (isIgnoreCompanyNo){
                nameTime[0] = nameTime[0]+":"+companyNo;
            }
        }
        boolean isIgnoreDataSecurity = false;
        if (!EmptyUtil.isNullOrEmpty(dataSecurityCacheProperties.getIgnoreDataSecurityTables())){
            isIgnoreDataSecurity =  !dataSecurityCacheProperties.getIgnoreDataSecurityTables().contains(targetTab);
        }
        if (isIgnoreDataSecurity){
            nameTime[0] = nameTime[0]+":"+dataSecurity;
        }
        if (nameTime.length==2){
            name = nameTime[0]+"&"+nameTime[1];
        }else{
            name = nameTime[0];
        }
        //默认企业则不做缓存租户处理
        return super.getCache(name);
    }

    @Override
    public Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(getCacheNames().size());
        getCacheNames().forEach(it -> {
            RedisCache cache = CustomRedisCache.class.cast(lookupCache(it));
            configurationMap.put(it, cache != null ? cache.getCacheConfiguration() : null);
        });
        return Collections.unmodifiableMap(configurationMap);
    }
}

