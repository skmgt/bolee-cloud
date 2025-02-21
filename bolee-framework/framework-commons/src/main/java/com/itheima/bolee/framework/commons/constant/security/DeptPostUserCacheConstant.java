package com.itheima.bolee.framework.commons.constant.security;

import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;

/**
 * @ClassName DeptPostUserCacheConstant.java
 * @Description 部门职位人员缓存常量
 */
public class DeptPostUserCacheConstant extends CacheConstant {

    //缓存父包
    public static final String PREFIX= "dept-post-user:";

    //缓存父包
    public static final String BASIC= PREFIX+"basic";

    //分布式锁前缀
    public static final String LOCK_PREFIX = PREFIX+"lock:";

    //page分页
    public static final String PAGE= PREFIX+"page";

    //list下拉框
    public static final String LIST= PREFIX+"list";

    //DeptPostUser-list
    public static final String DEPT_POST_USER_VO= PREFIX+"vo";
}
