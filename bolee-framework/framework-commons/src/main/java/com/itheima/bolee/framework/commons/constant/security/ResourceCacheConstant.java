package com.itheima.bolee.framework.commons.constant.security;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
* @Description：权限表缓存常量
*/
public class ResourceCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "resource:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";

    //tree树形
    public static final String TREE = PREFIX+"tree";

    //菜单
    public static final String MENUS= PREFIX+"menus";

}
