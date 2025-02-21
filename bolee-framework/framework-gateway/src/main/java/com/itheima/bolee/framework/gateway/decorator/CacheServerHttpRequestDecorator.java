package com.itheima.bolee.framework.gateway.decorator;

import com.itheima.bolee.framework.gateway.util.RequestHelper;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @ClassName RecorderServerHttpRequestDecorator.java
 * @Description 对ServerHttpRequest进行二次封装，解决requestBody只能读取一次的问题
 */
@Slf4j
public class CacheServerHttpRequestDecorator extends ServerHttpRequestDecorator {

    byte[] bytes;

    public CacheServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    /***
     * @description 改下body的获得方式，传递给下游业务系统使用
     * @return
     * @return: reactor.core.publisher.Flux<org.springframework.core.io.buffer.DataBuffer>
     */
    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody()   //获得父类获得请求体信息
            .publishOn(Schedulers.single()) //切换单线程防止阻塞
            .map(this::cache) //重写body可多次读取
            .doOnComplete(() -> addHeaders(getDelegate(),bytes));//读取请求体放入当前请求头便于后续处理
    }


    private DataBuffer cache(DataBuffer dataBuffer) {
        //先构建一个与dataBuffer长度相同的byty数组
        bytes = new byte[dataBuffer.readableByteCount()];
        //从dataBuffer中读出数据存入当前bytes中
        dataBuffer.read(bytes);
        //释放掉内存
        DataBufferUtils.release(dataBuffer);
        //放回到当前的NettyDataBufferFactory，用于下游系统的使用
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
        return nettyDataBufferFactory.wrap(bytes);
    }

    private void addHeaders(ServerHttpRequest request,byte[] bytes) {
        //请求体信息
        String requesBody = RequestHelper.readRequestBody(request,bytes);
        String replaceString = requesBody.replaceAll("\t|\n|\r", "").replaceAll(" ","");
        request.mutate().header("requesBody", replaceString).build();
    }

}
