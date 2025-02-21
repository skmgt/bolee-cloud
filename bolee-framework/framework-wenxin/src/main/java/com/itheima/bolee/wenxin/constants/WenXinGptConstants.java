package com.itheima.bolee.wenxin.constants;

import com.itheima.bolee.wenxin.config.WenXinGtpSourceConfig;

/**
 * GptConstants
 *
 * @author: wgl
 * @describe: TODO
 * @date: 2022/12/28 10:10
 */
public class WenXinGptConstants {
    /**
     * 文档地址：https://cloud.baidu.com/doc/WENXINWORKSHOP/s/Nlks5zkzu
     */
    private final static String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s";

    /**
     * https://cloud.baidu.com/doc/WENXINWORKSHOP/s/jlil56u11
     */
    public final static String ERNIE_BOT = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";


    /**
     * 获取基础的文心一言大模型请求的的url
     * @param accessToken
     * @return
     */
    public static String getGptUrl(String accessToken){
        return ERNIE_BOT+"?access_token="+accessToken;
    }

    /**
     * 获取Token接口
     * @param wenXinGtpConfig
     * @return
     */
    public static String getTokenUrl(WenXinGtpSourceConfig wenXinGtpConfig){
        return String.format(TOKEN_URL,wenXinGtpConfig.getApiKey(),wenXinGtpConfig.getSecretKey());
    }
}
