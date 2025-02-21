package com.itheima.bolee.framework.redis.resolver;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @ClassName CustomRedisCache.java
 * @Description redis缓存自定义处理
 */
public class CustomRedisCache extends RedisCache {
    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final ConversionService conversionService;

    protected CustomRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.conversionService = cacheConfig.getConversionService();
    }

    /**
     *
     * @Title: evict
     * @Description: 重写删除的方法
     * @param  @param key
     * @throws
     *
     */
    @Override
    public void evict(Object key) {

        if (key instanceof String) {
            String keyString = key.toString();
            // 后缀删除
            if (keyString.endsWith("*")) {
                evictLikeSuffix(keyString);
                return;
            }
        }
        // 删除指定的key
        super.evict(key);
    }

    /**
     * 后缀匹配匹配
     *
     * @param key
     */
    private void evictLikeSuffix(String key) {
        byte[] pattern = this.conversionService.convert(this.createCacheKey(key), byte[].class);
        this.cacheWriter.clean(this.name, pattern);
    }
}
