package com.itheima.bolee.framework.commons.constant.log;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName DataDictCacheConstant.java
 * @Description 数字字典缓存常量
 */
public class LogCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "log:";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

}
