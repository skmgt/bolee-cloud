package com.itheima.bolee.trade.face.impl;

import com.itheima.bolee.framework.commons.constant.trade.TradeCacheConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.adapter.PeriodicPayAdepter;
import com.itheima.bolee.trade.adapter.WapPayAdapter;
import com.itheima.bolee.trade.face.PeriodicPayFace;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName PeriodicPayFaceImpl.java
 * @Description 周期性扣款
 */
@Slf4j
@Component
public class PeriodicPayFaceImpl implements PeriodicPayFace {

    @Autowired
    PeriodicPayAdepter periodicPayAdepter;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public TradeVO appPaySign(TradeVO tradeVO) {
        try {
            return periodicPayAdepter.appPaySign(tradeVO);
        } catch (Exception e) {
            log.error("签约预创建异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_SIGN_CONTRACT_FAIL);
        }
    }

    @Override
    public TradeVO h5SignContract(TradeVO tradeVO) {
        try {
            return periodicPayAdepter.h5SignContract(tradeVO);
        } catch (Exception e) {
            log.error("签约预创建异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_SIGN_CONTRACT_FAIL);
        }
    }

    @Override
    public TradeVO h5PeriodicPay(TradeVO tradeVO) {
        //1、对交易订单加锁
        Long productOrderNo = tradeVO.getProductOrderNo();
        String key = TradeCacheConstant.CREATE_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
        if (lock.tryLock(TradeCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
            return periodicPayAdepter.h5PeriodicPay(tradeVO);
        }else {
            throw new ProjectException(TradeEnum.TRAD_DEDUCTION_FAIL);
        }
        } catch (Exception e) {
            log.error("周期支付异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_DEDUCTION_FAIL);
        }finally {
            //释放锁
            lock.unlock();
        }
    }
}
