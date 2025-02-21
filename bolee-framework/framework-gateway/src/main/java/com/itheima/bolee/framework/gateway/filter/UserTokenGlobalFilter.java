package com.itheima.bolee.framework.gateway.filter;

import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName TenantFilter.java
 * @Description 多租户过滤器
 */
@Slf4j
@Component
@Order(-99)
public class UserTokenGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //放入下游请求头中
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().build();

        //获得请求头中的userToken信息
        String userToken = exchange.getRequest().getHeaders().getFirst(SecurityConstant.USER_TOKEN);
        //已登录从头部中拿到userToken
        if (!EmptyUtil.isNullOrEmpty(userToken)){
            serverHttpRequest = serverHttpRequest.mutate().header(SecurityConstant.USER_TOKEN, userToken).build();
        }

        //把新的exchange放回到过滤链
        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }
}
