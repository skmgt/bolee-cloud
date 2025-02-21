package com.itheima.bolee.security.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.security.wechat.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName WechatServiceImpl.java
 * @Description TODO
 */
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String openId(String appId, String appSecret, String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+
                "&secret="+appSecret+
                "&code="+code+
                "&grant_type=authorization_code" ;
        String wechatString = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(wechatString);
        return jsonObject.getString("openid");
    }
}
