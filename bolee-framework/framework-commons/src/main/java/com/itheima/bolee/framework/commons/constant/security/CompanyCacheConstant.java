package com.itheima.bolee.framework.commons.constant.security;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
* @Description：岗位表缓存常量
*/
public class CompanyCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "company:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";

    //站点
    public static final String WEBSITE = PREFIX+ "web-site:";


}
