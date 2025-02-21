package com.itheima.bolee.framework.commons.constant.dict;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName PlacesCacheConstant.java
 * @Description 区域缓存常量
 */
public class PlacesCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "places:";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //获得所有不重复的ParentKey的集合
    public static final String LIST= PREFIX+"list&ttl=-1";

}
