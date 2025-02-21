package com.itheima.bolee.trade.adapter.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.trade.adapter.CommonPayAdapter;
import com.itheima.bolee.trade.handler.CommonPayHandler;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.pojo.Trade;
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
 * @ClassName CommonPayAdapterImpl.java
 * @Description 基础交易适配
 */
@Slf4j
@Component
public class CommonPayAdapterImpl implements CommonPayAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ITradeService tradeService;

    private static Map<String,String> commonPayHandlers =new HashMap<>();

    static {
        commonPayHandlers.put(TradeConstant.TRADE_CHANNEL_ALI_PAY,"aliCommonPayHandler");
        commonPayHandlers.put(TradeConstant.TRADE_CHANNEL_WECHAT_PAY,"wechatCommonPayHandler");
    }

    @Override
    public TradeVO queryTrade(TradeVO tradeVO) throws ProjectException {
        //从IOC容器中找到AppPayHandler实现
        String commonPayHandlerString = commonPayHandlers.get(tradeVO.getTradeChannel());
        CommonPayHandler commonPayHandler = registerBeanHandler
            .getBean(commonPayHandlerString, CommonPayHandler.class);
        //queryTrade查询订单结果
        return commonPayHandler.queryTrade(tradeVO);
    }

    @Override
    public TradeVO refundTrade(TradeVO tradeVO) throws ProjectException {
        //从IOC容器中找到AppPayHandler实现
        String commonPayHandlerString = commonPayHandlers.get(tradeVO.getTradeChannel());
        CommonPayHandler commonPayHandler = registerBeanHandler
            .getBean(commonPayHandlerString, CommonPayHandler.class);
        //refundTrade处理退款
        return commonPayHandler.refundTrade(tradeVO);
    }

    @Override
    public RefundRecordVO queryRefundTrade(RefundRecordVO refundRecordVO) throws ProjectException {
        String commonPayHandlerString = commonPayHandlers.get(refundRecordVO.getTradeChannel());
        CommonPayHandler commonPayHandler = registerBeanHandler
                .getBean(commonPayHandlerString, CommonPayHandler.class);
        return commonPayHandler.queryRefundTrade(refundRecordVO);
    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        String commonPayHandlerString = commonPayHandlers.get(tradeVO.getTradeChannel());
        CommonPayHandler commonPayHandler = registerBeanHandler
                .getBean(commonPayHandlerString, CommonPayHandler.class);
        return commonPayHandler.closeTrade(tradeVO);
    }

    @Override
    public String queryQrCode(TradeVO tradeVO) {
        Trade trade = tradeService.findTradByProductOrderNo(tradeVO.getProductOrderNo());
        //4、构建支付二维码
        BufferedImage image = QrCodeUtil.generate(trade.getPlaceOrderMsg(), 480, 480);
        //输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch (IOException e) {
            throw new ProjectException(TradeEnum.TRAD_QRCODE_FAIL);
        }
        return Base64.encode(stream.toByteArray());
    }

    @Override
    public TradeVO downLoadBill(TradeVO tradeVO) {
        String commonPayHandlerString = commonPayHandlers.get(tradeVO.getTradeChannel());
        CommonPayHandler commonPayHandler = registerBeanHandler
                .getBean(commonPayHandlerString, CommonPayHandler.class);
        return commonPayHandler.downLoadBill(tradeVO);
    }

}
