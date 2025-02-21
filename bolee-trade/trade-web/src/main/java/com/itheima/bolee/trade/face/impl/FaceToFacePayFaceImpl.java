package com.itheima.bolee.trade.face.impl;

import com.itheima.bolee.trade.adapter.FaceToFacePayAdapter;
import com.itheima.bolee.trade.face.FaceToFacePayFace;
import com.itheima.bolee.framework.commons.constant.trade.TradeCacheConstant;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName FaceToFacePayFaceImpl.java
 * @Description 面对面支付Face接口实现
 */
@Slf4j
@Component
public class FaceToFacePayFaceImpl implements FaceToFacePayFace {

    @Autowired
    FaceToFacePayAdapter faceToFacePayAdapter;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public TradeVO payTrade(TradeVO tradeVO) {
        //1、对交易订单加锁
        Long productOrderNo = tradeVO.getProductOrderNo();
        String key = TradeCacheConstant.CREATE_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            if (lock.tryLock(TradeCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
                return faceToFacePayAdapter.payTrade(tradeVO);
            }else {
                throw new ProjectException(TradeEnum.TRAD_PAY_FAIL);
            }
        } catch (Exception e) {
            log.error("统一收单线下交易预创建异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_PAY_FAIL);
        }finally {
            //释放锁
            lock.unlock();
        }

    }

    @Override
    @GlobalTransactional
    public TradeVO precreateTrade(TradeVO tradeVO) {
        //1、对交易订单加锁
        Long productOrderNo = tradeVO.getProductOrderNo();
        String key = TradeCacheConstant.CREATE_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            if (lock.tryLock(TradeCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
                return faceToFacePayAdapter.precreateTrade(tradeVO);
            }else {
                throw new ProjectException(TradeEnum.TRAD_QRCODE_FAIL);
            }
        } catch (Exception e) {
            log.error("统一收单线下交易预创建异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_QRCODE_FAIL);
        }finally {
            //释放锁
            lock.unlock();
        }
    }

}
