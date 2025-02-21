package com.itheima.bolee.insurance.constant;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
* @Description：保险分类缓存常量
*/
public class CategoryCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "category:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";

    //树形
    public static final String TREE = PREFIX+"tree";;
}
