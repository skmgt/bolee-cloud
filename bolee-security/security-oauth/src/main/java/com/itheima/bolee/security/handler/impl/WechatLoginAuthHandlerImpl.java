package com.itheima.bolee.security.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.security.CompanyConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.*;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.security.feign.UserFeign;
import com.itheima.bolee.security.handler.CacheTokenHandler;
import com.itheima.bolee.security.handler.LoginAuthHandler;
import com.itheima.bolee.security.wechat.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName WechatLoginAuthHandlerImpl.java
 * @Description 微信登录处理
 */
@Component("wechatLoginAuthHandler")
public class WechatLoginAuthHandlerImpl implements LoginAuthHandler {


    //调用RPC原创服务
    @Autowired
    UserFeign userFeign;

    //调用RPC原创服务
    @Autowired
    CustomerFeign customerFeign;

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    CacheTokenHandler cacheTokenHandler;

    @Autowired
    WechatService wechatService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserVO loginHandler(Principal principal, Map<String, String> parameters,
                                String loginBeanName, CompanyVO companyVO)
            throws HttpRequestMethodNotSupportedException {
        String code = parameters.get(OauthConstant.CODE_KEY);
        String clientId = parameters.get(OauthConstant.CLIENT_ID_KEY);
        String loginType = parameters.get(OauthConstant.LOGIN_TYPE_KEY);
        //code兑换openId
        AuthChannelVO authChannelVO = companyVO.getAuthChannelVOs().stream().filter(n -> {
            return n.getChannelLabel().equals(CompanyConstant.CHANNEL_LABEL_WECHAT);
        }).findFirst().get();
        //微信拿openId
        String openId = wechatService.openId(authChannelVO.getAppId(), authChannelVO.getAppSecret(), code);
        UsernameVO usernameVO = UsernameVO.builder()
            .username(openId)
            .clientId(clientId)
            .companyNo(companyVO.getCompanyNo())
            .loginType(loginType)
            .loginBeanName(loginBeanName)
            .build();
        String username = JSONObject.toJSONString(usernameVO);
        parameters.put(OauthConstant.USER_NAME_KEY,username);
        parameters.put(OauthConstant.PASSWORD_KEY,openId);
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return cacheTokenHandler.cacheToken(oAuth2AccessToken);
    }

    @Override
    public UserVO findUserDetail(String loginType, String openId,String companyNo) {
        //处理登录
        UserVO userVO = null;
        String password = bCryptPasswordEncoder.encode(openId);
        switch (loginType){
            case OauthConstant.USER_WECHAT:
                userVO = userFeign.wechatLogin(openId,companyNo);
                if (!EmptyUtil.isNullOrEmpty(userVO)){
                    userVO.setPassword(password);
                    userVO.setOnlyAuthenticate(false);
                }
                return userVO;
            case OauthConstant.CUSTOMER_WECHAT:
                userVO = customerFeign.wechatLogin(openId,companyNo);
                //如果用户不存在则注册用户
                if (EmptyUtil.isNullOrEmpty(userVO)){
                    CustomerVO customerVO = CustomerVO.builder()
                        .username(openId)
                        .openId(openId)
                        .password(password)
                        .build();
                    return customerFeign.registerUser(customerVO);
                }
                userVO.setPassword(password);
                userVO.setOnlyAuthenticate(true);
                return userVO;
            default:
                throw new ProjectException(AuthEnum.LOGIN_FAIL);
        }
    }
}
