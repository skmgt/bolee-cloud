package com.itheima.bolee.trade.adapter.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.itheima.bolee.trade.handler.FaceToFacePayHandler;
import com.itheima.bolee.trade.adapter.FaceToFacePayAdapter;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FaceToFacePayAdapterImpl.java
 * @Description 面对面适配实现
 */
@Slf4j
@Component
public class FaceToFacePayAdapterImpl implements FaceToFacePayAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    RedissonClient redissonClient;

    private static Map<String,String> faceToFacePayHandlers =new HashMap<>();

    static {
        faceToFacePayHandlers.put(TradeConstant.TRADE_CHANNEL_ALI_PAY,"aliFaceToFacePayHandler");
        faceToFacePayHandlers.put(TradeConstant.TRADE_CHANNEL_WECHAT_PAY,"wechatFaceToFacePayHandler");
    }

    @Override
    public TradeVO payTrade(TradeVO tradeVO) {
        //2、从IOC容器中找到FaceToFacePayHandler实现
        String faceToFacePayHandlerString = faceToFacePayHandlers.get(tradeVO.getTradeChannel());
        FaceToFacePayHandler faceToFacePayHandler = registerBeanHandler
                .getBean(faceToFacePayHandlerString, FaceToFacePayHandler.class);
        //3、payTrade支付交易处理
        TradeVO tradeVOResult = faceToFacePayHandler.payTrade(tradeVO);
        return tradeVOResult;
    }

    @Override
    public TradeVO precreateTrade(TradeVO tradeVO) throws ProjectException {
        //2、从IOC容器中找到FaceToFacePayHandler实现
        String faceToFacePayHandlerString = faceToFacePayHandlers.get(tradeVO.getTradeChannel());
        FaceToFacePayHandler faceToFacePayHandler = registerBeanHandler
                .getBean(faceToFacePayHandlerString, FaceToFacePayHandler.class);
        //3、precreateTrade支付交易处理
        TradeVO tradeVOResult = faceToFacePayHandler.precreateTrade(tradeVO);
        if (EmptyUtil.isNullOrEmpty(tradeVOResult.getPlaceOrderMsg())) {
            throw new ProjectException(TradeEnum.TRAD_QRCODE_FAIL);
        }
        //4、构建支付二维码
        BufferedImage image = QrCodeUtil.generate(tradeVOResult.getPlaceOrderMsg(), 480, 480);
        //输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch (IOException e) {
            throw new ProjectException(TradeEnum.TRAD_QRCODE_FAIL);
        }
        String base64Image = Base64.encode(stream.toByteArray());
        tradeVOResult.setQrCodeImageBase64(base64Image);
        return tradeVOResult;
    }
}
