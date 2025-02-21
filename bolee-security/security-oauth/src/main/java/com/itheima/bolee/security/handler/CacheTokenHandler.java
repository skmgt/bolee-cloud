package com.itheima.bolee.security.handler;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @ClassName CacheTokenHandler.java
 * @Description 缓存token处理器
 */
public interface CacheTokenHandler {


    /***
     * @description 缓存token
     * @param oAuth2AccessToken
     * @return
     */
    UserVO cacheToken(OAuth2AccessToken oAuth2AccessToken);

    /***
     * @description 缓存token
     * @param userVO 用户
     * @return
     */
    Boolean deleteToken(UserVO userVO);
}
