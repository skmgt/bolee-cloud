package com.itheima.easy.constant;

/**
* @Description：${table.comment!}缓存常量
*/
public class ${entity}CacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "${controllerMappingHyphen}:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";


}
