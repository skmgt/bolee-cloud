package com.itheima.bolee.framework.commons.constant.file;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName DataDictCacheConstant.java
 * @Description 附件缓存常量
 */
public class FileCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "file:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //业务key前缀
    public static final String BUSINESS_KEY = PREFIX+"business_key&ttl=-1";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list分页
    public static final String LIST = PREFIX+"list";;
}
