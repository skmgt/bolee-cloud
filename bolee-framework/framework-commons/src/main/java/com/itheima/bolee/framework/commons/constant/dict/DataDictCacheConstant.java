package com.itheima.bolee.framework.commons.constant.dict;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName DataDictCacheConstant.java
 * @Description 数字字典缓存常量
 */
public class DataDictCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "data_dict:";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //所有父key
    public static final String PARENT_KEY= PREFIX+"parent_key&ttl=-1";

    //所有dataKey
    public static final String DATA_KEY= PREFIX+"data_key&ttl=-1";

    //page分页
    public static final String PAGE= PREFIX+"page";


    public static final String QUESTION = PREFIX+"question";

}
