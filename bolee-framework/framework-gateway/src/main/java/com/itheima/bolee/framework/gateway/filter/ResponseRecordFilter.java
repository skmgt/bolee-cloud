package com.itheima.bolee.framework.gateway.filter;

import com.itheima.bolee.framework.commons.utils.SnowflakeIdWorker;
import com.itheima.bolee.framework.gateway.decorator.CacheServerHttpResponseDecorator;
import com.itheima.bolee.framework.gateway.properties.LogProperties;
import com.itheima.bolee.framework.gateway.util.RequestHelper;
import com.itheima.bolee.framework.rabbitmq.source.LogSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @ClassName ResponseRecordFilter.java
 * @Description Response记录过滤
 */
@Slf4j
@Component
public class ResponseRecordFilter implements GlobalFilter, Ordered {

    @Autowired
    private LogSource logSource;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    LogProperties logProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String port;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //处理文件上传:如果是文件上传则不记录日志
        MediaType mediaType =exchange.getRequest().getHeaders().getContentType();
        boolean flag = RequestHelper.isUploadFile(mediaType);
        //忽略路径处理:获得请求路径然后与logProperties的路进行匹配，匹配上则不记录日志
        String path = exchange.getRequest().getURI().getPath();
        List<String> ignoreTestUrl = logProperties.getIgnoreUrl();
        for (String testUrl : ignoreTestUrl) {
            if (antPathMatcher.match(testUrl, path)){
                flag = true;
                break;
            }
        }
        //无需记录日志：直接放过请求
        if (flag){
            return chain.filter(exchange);

        }
        //需记录日志:对ServerHttpResponse进行二次封装
        CacheServerHttpResponseDecorator serverHttpResponseDecorator =
            new CacheServerHttpResponseDecorator(
                exchange,
                logSource,
                snowflakeIdWorker.nextId(),
                applicationName+":"+port);
        //把当前的应答体进行改变，用于传递新放入的response中
        return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
