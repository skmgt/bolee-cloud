package com.itheima.bolee.framework.commons.constant.sms;

/**
 * @ClassName SmsConstant.java
 * @Description 短信常量类
 */
public class SmsConstant {

    //阿里云短信
    public static final String ALIYUN_SMS = "ALIYUN_SMS";
    //腾讯云短信
    public static final String TENCENT_SMS = "TENCENT_SMS";
    //百度云短信
    public static final String BAIDU_SMS = "BAIDU_SMS";

    //邮件负载均衡
    public static final String HASH = "HASH";
    public static final String RANDOM = "RANDOM ";
    public static final String ROUND_ROBIN ="ROUND_ROBIN" ;
    public static final String WEIGHT_RANDOM = "WEIGHT_RANDOM";
    public static final String WEIGHT_ROUND_ROBIN = "WEIGHT_ROUND_ROBIN";

    //发送状态:发送成功
    public static final String STATUS_SEND_0 ="0" ;
    //发送状态:发送失败
    public static final String STATUS_SEND_1 ="1" ;
    //发送状态:发送中
    public static final String STATUS_SEND_2 ="2" ;

    //受理状态:受理成功
    public static final String STATUS_ACCEPT_0 ="0" ;
    //受理状态:受理失败
    public static final String STATUS_ACCEPT_1 ="1" ;
    //受理状态:受理中
    public static final String STATUS_ACCEPT_2 ="2" ;

    //审核状态:审核成功
    public static final String STATUS_AUDIT_0 = "0";
    //审核状态:审核失败
    public static final String STATUS_AUDIT_1 = "1";
    //审核状态:审核中
    public static final String STATUS_AUDIT_2 = "2";

}
