package com.itheima.bolee.points.constant;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
* @Description：日投保访问页面量缓存常量
*/
public class DoInsureDpvCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "do-insure-dpv:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";


}
