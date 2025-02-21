package com.itheima.bolee.security.handler.impl;

import com.itheima.bolee.framework.commons.constant.security.OauthCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.handler.CacheTokenHandler;
import com.itheima.bolee.security.handler.RefreshTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RefreshTokenHandlerImpl.java
 * @Description 刷新令牌处理实现
 */
@Slf4j
@Component
public class RefreshTokenHandlerImpl implements RefreshTokenHandler {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    CacheTokenHandler cacheTokenHandler;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Override
    public UserVO refreshToken(Principal principal, Map<String, String> parameters) {
        Boolean flag = false;
        //通过jti获得refreshToken
        String userToken = parameters.get(OauthConstant.JTI_KEY);
        RBucket<String> refreshTokenBucket = redissonClient.getBucket(OauthCacheConstant.REFRESH_TOKEN + userToken);
        String refreshToken = refreshTokenBucket.get();
        if (EmptyUtil.isNullOrEmpty(refreshToken)){
            return null;
        }
        //使用refreshToken再次获得OAuth2AccessToken
        parameters.put("refresh_token",refreshToken);
        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            log.error("刷新令牌出错：{}", ExceptionsUtil.getErrorMessageWithNestedException(e));
            return null;
        }
        //重新构建jwt增强信息
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;
        RBucket<UserVO> jtiUserBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN + userToken);
        UserVO userVO = jtiUserBucket.get();
        Map<String, Object> map = new HashMap<>(11);
        map.put(OauthConstant.USER_ID_KEY, userVO.getId());
        map.put(OauthConstant.CLIENT_ID_KEY, userVO.getClientId());
        map.put(OauthConstant.USER_NAME_KEY,userVO.getUsername());
        map.put(OauthConstant.RESOURCS_KEY,userVO.getResourceRequestPaths());
        map.put(OauthConstant.ROLES_KEY,userVO.getRoleLabels());
        map.put(OauthConstant.OPEN_ID_KEY,userVO.getOpenId());
        map.put(OauthConstant.DEPT_NO_KEY,userVO.getDeptNo());
        map.put(OauthConstant.POST_NO_KEY,userVO.getPostNo());
        map.put(OauthConstant.MOBILE_KEY,userVO.getMobile());
        map.put(OauthConstant.COMPANY_NO_KEY,userVO.getCompanyNo());
        map.put(OauthConstant.DATA_SECURITY_KEY,userVO.getDataSecurityVO());
        //令牌自定过期时间
        map.put(OauthConstant.EXPIRES_IN_KEY,securityConfigProperties.getAccessTokenValiditySeconds());
        defaultOAuth2AccessToken.setAdditionalInformation(map);
        //处理缓存信息
        return cacheTokenHandler.cacheToken(defaultOAuth2AccessToken);
    }
}
