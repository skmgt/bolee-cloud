package com.itheima.bolee.trade.client.alipay;

import com.alipay.easysdk.kernel.AlipayConstants;
import com.alipay.easysdk.kernel.Client;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.Context;
import com.alipay.easysdk.kms.aliyun.AliyunKMSClient;
import com.alipay.easysdk.kms.aliyun.AliyunKMSSigner;
import com.aliyun.tea.TeaModel;

import java.lang.reflect.Constructor;

/**
 * @ClassName Factory.java
 * @Description 基于ali工厂改造：在多租户下，用户上下文对象并发问题
 */
public class Factory {

    public final String SDK_VERSION = "alipay-easysdk-java-2.1.2";

    /**
     * 将一些初始化耗时较多的信息缓存在上下文中
     */
    private Context context;

    /**
     * 设置客户端参数，只需设置一次，即可反复使用各种场景下的API Client
     *
     * @param options 客户端参数对象
     */
    public  void setOptions(Config options) {
        try {
            context = new Context(options, SDK_VERSION);

            if (AlipayConstants.AliyunKMS.equals(context.getConfig(AlipayConstants.SIGN_PROVIDER_CONFIG_KEY))) {
                context.setSigner(new AliyunKMSSigner(new AliyunKMSClient(TeaModel.buildMap(options))));
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取调用OpenAPI所需的客户端实例
     * 本方法用于调用SDK扩展包中的API Client下的方法
     *
     * 注：返回的实例不可重复使用，只可用于单次调用
     *
     * @param client API Client的类型对象
     * @return client实例，用于发起单次调用
     */
    public  <T> T getClient(Class<T> client) {
        try {
            Constructor<T> constructor = client.getConstructor(Client.class);
            context.setSdkVersion(getSdkVersion(client));
            return constructor.newInstance(new Client(context));
        } catch (Exception e) {
            throw new RuntimeException("" + e.getMessage(), e);
        }
    }

    private  <T> String getSdkVersion(Class<T> client) {
        return context.getSdkVersion() + "-" + client.getCanonicalName()
                .replace("com.alipay.easysdk.", "")
                .replace(".Client", "")
                .replace(".", "-");
    }

    /**
     * 获取支付通用API Client
     *
     * @return 支付通用API Client
     */
    public  com.alipay.easysdk.payment.common.Client Common() throws Exception {
        return new com.alipay.easysdk.payment.common.Client(new Client(context));
    }

    /**
     * 获取当面付相关API Client
     *
     * @return 当面付相关API Client
     */
    public  com.alipay.easysdk.payment.facetoface.Client FaceToFace() throws Exception {
        return new com.alipay.easysdk.payment.facetoface.Client(new Client(context));
    }

    /**
     * 获取电脑网站支付相关API Client
     *
     * @return 电脑网站支付相关API Client
     */
    public  com.alipay.easysdk.payment.page.Client Page() throws Exception {
        return new com.alipay.easysdk.payment.page.Client(new Client(context));
    }

    /**
     * 获取手机网站支付相关API Client
     *
     * @return 手机网站支付相关API Client
     */
    public  com.alipay.easysdk.payment.wap.Client Wap() throws Exception {
        return new com.alipay.easysdk.payment.wap.Client(new Client(context));
    }

    /**
     * 获取手机APP支付相关API Client
     *
     * @return 手机APP支付相关API Client
     */
    public  com.alipay.easysdk.payment.app.Client App() throws Exception {
        return new com.alipay.easysdk.payment.app.Client(new Client(context));
    }

}
