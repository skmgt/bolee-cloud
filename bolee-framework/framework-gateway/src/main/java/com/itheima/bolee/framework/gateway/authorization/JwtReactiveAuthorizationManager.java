package com.itheima.bolee.framework.gateway.authorization;

import com.itheima.bolee.framework.commons.constant.security.OauthCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.OauthConstant;
import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @ClassName AuthorizationManager.java
 * @Description 鉴权管理器
 */
@Slf4j
@Component
public class JwtReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ReactiveJwtDecoder reactiveJwtDecoder;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication,
                                             AuthorizationContext authorizationContext) {
        //目标路径：获得url路径
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        String path = request.getURI().getPath();
        path = path.substring(path.indexOf("/",1));
        String methodValue = request.getMethodValue();
        String tagetResource = methodValue+path;
        log.info("===============进入鉴权tagetResource路径:{}==========",tagetResource);
        //校验userToken：如果当前的tooken获得为空，则认为鉴权失败
        String userToken = request.getHeaders().getFirst(SecurityConstant.USER_TOKEN);
        if (EmptyUtil.isNullOrEmpty(userToken)){
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        //兑换userVO:使用当前usertoken获得缓存中userVO信息
        RBucket<UserVO> userVOBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN + userToken);
        UserVO userVO = userVOBucket.get();
        if (EmptyUtil.isNullOrEmpty(userVO)){
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        //剔除处理：如果页面传入usetToken不等于缓存中的usetToken,则表示当前usetToken被剔除
        RBucket<String> userTokenBindRBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN_BIND + userVO.getUsername());
        String userTokenBind = userTokenBindRBucket.get();
        if (!userToken.equals(userTokenBind)){
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        //兑换accessToken：使用userToken兑换去缓存中兑换accessToken
        RBucket<String> accessTokenBucket = redissonClient.getBucket(OauthCacheConstant.ACCESS_TOKEN + userTokenBind);
        String accessToken = accessTokenBucket.get();
        //校验accessToken：令牌过期获得为空，校验失败
        if (EmptyUtil.isNullOrEmpty(accessToken)){
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        //解析accessToken：调用rsa接口进行验证签名
        Mono<Jwt> decode = reactiveJwtDecoder.decode(accessToken);
        Jwt jwt = null;
        try {
            jwt = decode.toFuture().get();
        } catch (Exception e) {
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        if (EmptyUtil.isNullOrEmpty(jwt)){
            return Mono.justOrEmpty(new AuthorizationDecision(false));
        }
        //权限校验：当前用户不为非只认证类型，我们会从jwt中获得载荷，然后判断载荷中的权限是否包含访问路径权限
        if (!userVO.getOnlyAuthenticate()){
            List<String> authorities = (List<String>)jwt.getClaims().get(OauthConstant.AUTHORITIES_KEY);
            for (String authority : authorities) {
                boolean isMatch = antPathMatcher.match(authority, tagetResource);
                if (isMatch){
                    log.info("用户:{}拥有tagetResource权限:{}==========",userVO.getUsername(),tagetResource);
                    return Mono.just(new AuthorizationDecision(true));
                }
            }
            log.info("用户:{}不拥有tagetResource权限:{}==========",userVO.getUsername(),tagetResource);
            return Mono.just(new AuthorizationDecision(false));
        }
        return Mono.just(new AuthorizationDecision(true));
    }

}
