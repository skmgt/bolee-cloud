package com.itheima.bolee.trade.client.wechat;

import com.itheima.bolee.trade.client.wechat.operators.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @ClassName Factory.java
 * @Description 封装对于微信支付工程类
 */
@Data
@Log4j2
public class Factory {

    Config config;

    public void setOptions(Config config) {
        this.config = config;
    }

    //基础服务
    public Common Common(){
        return new Common(config);
    }

    //面对面支付
    public FaceToFace FaceToFace(){
        return new FaceToFace(config);
    }

    //PC网页支付
    public Page Page(){
        return new Page(config);
    }

    //手机网页支付
    public Wap Wap(){
        return new Wap(config);
    }

    //APP网页支付
    public App App(){
        return new App(config);
    }

    //H5
    public H5 H5(){
        return new H5(config);
    }
}
