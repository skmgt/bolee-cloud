package com.itheima.bolee.security.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.security.handler.CacheTokenHandler;
import com.itheima.bolee.security.handler.LoginAuthHandler;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.security.feign.UserFeign;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.dto.security.UsernameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.Map;

/**
 * @ClassName UserpasswordLoginAdapter.java
 * @Description 密码登录处理器实现
 */
@Slf4j
@Component("usernameLoginAuthHandler")
public class UsernameLoginAuthHandlerImpl implements LoginAuthHandler {

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    CacheTokenHandler cacheTokenHandler;

    //调用RPC原创服务
    @Autowired
    UserFeign userFeign;

    //调用RPC原创服务
    @Autowired
    CustomerFeign customerFeign;

    @Override
    public UserVO loginHandler(Principal principal, Map<String, String> parameters,
                                String loginBeanName, CompanyVO companyVO)
            throws HttpRequestMethodNotSupportedException {
        String username = parameters.get(OauthConstant.USER_NAME_KEY);
        String clientId = parameters.get(OauthConstant.CLIENT_ID_KEY);
        String loginType = parameters.get(OauthConstant.LOGIN_TYPE_KEY);
        UsernameVO usernameVO = UsernameVO.builder()
            .username(username)
            .companyNo(companyVO.getCompanyNo())
            .clientId(clientId)
            .loginType(loginType)
            .loginBeanName(loginBeanName)
            .build();
        username = JSONObject.toJSONString(usernameVO);
        parameters.put(OauthConstant.USER_NAME_KEY,username);
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return cacheTokenHandler.cacheToken(oAuth2AccessToken);
    }

    @Override
    public UserVO findUserDetail(String loginType, String username,String companyNo) {
        UserVO userVO = null;
        //处理登录
        switch (loginType){
            case OauthConstant.USER_USERNAME:
                userVO = userFeign.usernameLogin(username,companyNo);
                if (!EmptyUtil.isNullOrEmpty(userVO)){
                    userVO.setOnlyAuthenticate(false);
                }
                return userVO;
            case OauthConstant.CUSTOMER_USERNAME:
                userVO = customerFeign.usernameLogin(username,companyNo);
                if (!EmptyUtil.isNullOrEmpty(userVO)){
                    userVO.setOnlyAuthenticate(true);
                }
                return userVO;
            default:
                throw new ProjectException(AuthEnum.LOGIN_FAIL);
        }
    }
}
