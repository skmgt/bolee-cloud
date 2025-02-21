package com.itheima.bolee.framework.commons.constant.sms;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName DataDictCacheConstant.java
 * @Description 短信缓存常量
 */
public class SmsCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "sms-";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //blacklist缓存父包
    public static final String PREFIX_BLACKLIST= PREFIX+"blacklist";

    //Channel缓存父包
    public static final String PREFIX_CHANNEL= PREFIX+"channel";

    //发送记录缓存父包
    public static final String PREFIX_SEND_RECORD= PREFIX+"send_record";

    //签名缓存父包
    public static final String PREFIX_SIGN= PREFIX+"sign";

    //模板缓存父包
    public static final String PREFIX_TEMPLATE= PREFIX+"template";

    //黑名单page分页
    public static final String PAGE_BLACKLIST= PREFIX_BLACKLIST+"page";

    //channel的page分页
    public static final String PAGE_CHANNEL= PREFIX_CHANNEL+"page";

    //channel的page分页
    public static final String CHANNEL_LABEL= PREFIX_CHANNEL+"label&ttl=-1";

    //sign的page分页
    public static final String PAGE_SIGN= PREFIX_SIGN+"page";

    //channel的page分页
    public static final String PAGE_SEND_RECORD = PREFIX_SEND_RECORD+"page";

    //template的page分页
    public static final String PAGE_TEMPLATE = PREFIX_TEMPLATE+"page";

    //Channel缓存list
    public static final String CHANNEL_LIST= PREFIX+"channel_list:";

    //sign缓存list
    public static final String SIGN_LIST =PREFIX+"sign_list" ;

}
