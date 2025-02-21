package com.itheima.bolee.framework.gateway.util;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.basic.IBaseEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import io.netty.util.CharsetUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/***
 * @description web工具类
 */
public class WebUtils {

    public static Mono writeFailedToResponse(ServerHttpResponse response, IBaseEnum basicEnum){
        //应答状态
        response.setStatusCode(HttpStatus.OK);
        //响应格式
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        //返回结果封装
        ResponseResult<Boolean> responseWrap = ResponseResultBuild.build(basicEnum, false);
        String result = JSONObject.toJSONString(responseWrap);
        DataBuffer buffer = response.bufferFactory().wrap(result.getBytes(CharsetUtil.UTF_8));
        //写入响应结果
        return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
    }

}
