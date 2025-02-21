package com.itheima.bolee.insurance.constant;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
* @Description：方案保障项缓存常量
*/
public class PlanSafeguardCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "plan-safeguard:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";


}
