package com.itheima.bolee.framework.gateway.decorator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.log.LogBusinessVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.basic.BaseEnum;
import com.itheima.bolee.framework.commons.utils.CityUtil;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.LogSource;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName CacheServerHttpResponseDecorator.java
 * @Description 缓存应答装饰器
 */
@Slf4j
public class CacheServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    LogSource logSource;

    ServerWebExchange exchange;

    private Long messageId;

    private String sender;

    private byte[] bytes;

    public CacheServerHttpResponseDecorator(ServerWebExchange exchange,
                                            LogSource logSource,
                                            Long messageId,
                                            String sender) {
        super(exchange.getResponse());
        this.logSource = logSource;
        this.exchange = exchange;
        this.messageId = messageId;
        this.sender =sender;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        AtomicReference<String> bodyRef = new AtomicReference<>();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                DataBuffer join = dataBufferFactory.join(dataBuffer);
                byte[] content = new byte[join.readableByteCount()];
                join.read(content);
                // 释放掉内存
                DataBufferUtils.release(join);
                String resultString = new String(content, Charset.forName("UTF-8"));
                //返回结果处理
                ResponseResult responseResult = JSON.parseObject(resultString, ResponseResult.class);
                // 服务访问记录-ResponseResult类型
                this.trace(exchange.getRequest(),responseResult);
                // 返回响应数据
                byte[] uppedContent = resultString.getBytes();
                return getDelegate().bufferFactory().wrap(uppedContent);
            }));
        }
        return super.writeWith(body);
    }

    /***
     * @description 服务访问记录-ResponseResult类型
     */
    private void trace(ServerHttpRequest request, ResponseResult responseResult) {
        //创建请求日志记录
        String logJsonString = createLogJsonString(request, responseResult);
        //发送队列延迟信息
        sendLogJsonString(logJsonString);
    }

    /**
     * 发送日志报文到mq
     * @param logBusinessVOJsonString
     */
    private void sendLogJsonString(String logBusinessVOJsonString) {
        //发送队列信息
        MqMessage mqMessage = MqMessage.builder()
            .id(messageId)
            .title("log-message")
            .content(logBusinessVOJsonString)
            .messageType("log-request")
            .produceTime(Timestamp.valueOf(LocalDateTime.now()))
            .sender(sender)
            .build();
        Message<MqMessage> message = MessageBuilder.withPayload(mqMessage).setHeader("type", "log-key").build();
        boolean flag = logSource.logOutput().send(message);
        log.info("发送：{}结果：{}",mqMessage.toString(),flag);
    }

    /**
     * 构建日志记录
     * @param request         ServerHttpRequest-请求对象
     * @param responseResult  返回的响应结果
     * @return
     */
    private String createLogJsonString(ServerHttpRequest request,ResponseResult responseResult ) {
        //请求IP
        String hostAddress = request.getRemoteAddress().getAddress().getHostAddress();
        //请求host
        String host = request.getURI().getHost();
        //请求路径
        String requestUri = request.getURI().getPath();
        //请求方式
        String method = request.getMethodValue().toUpperCase();
        //请求id
        String requestId = request.getId();
        //请求体
        String requestBody = request.getHeaders().getFirst("requestBody");
        //业务类型
        String businessType = request.getHeaders().getFirst("businessType");
        //设备号
        String deviceNumber = request.getHeaders().getFirst("deviceNumber");
        //省份数据
        String province = request.getHeaders().getFirst("province");
        //市区数据
        String city = request.getHeaders().getFirst("city");
        //日志对象封装
        LogBusinessVO logBusinessVO = LogBusinessVO.builder()
            .requestId(requestId)
            .host(host)
            .hostAddress(hostAddress)
            .requestUri(requestUri)
            .requestBody(requestBody)
            .requestMethod(method)
            .responseBody(JSONObject.toJSONString(responseResult.getData()))
            .responseCode(responseResult.getCode())
            .responseMsg(responseResult.getMsg())
            .businessType(businessType)
            .deviceNumber(deviceNumber)
            .province(province)
            .userId(responseResult.getOperatorId())
            .userName(responseResult.getOperatorName())
            .sex(responseResult.getOperatorSex())
            .city(city)
            .build();
        CityUtil.handlerCity(logBusinessVO);
        String logBusinessVOJsonString = JSONObject.toJSONString(logBusinessVO);
        log.info("================logBusinessVOJsonString:{}",logBusinessVOJsonString);
        return logBusinessVOJsonString;
    }
}
