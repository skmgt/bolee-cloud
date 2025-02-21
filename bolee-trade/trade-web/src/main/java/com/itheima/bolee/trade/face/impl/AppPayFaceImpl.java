package com.itheima.bolee.trade.face.impl;

import com.itheima.bolee.trade.adapter.AppPayAdapter;
import com.itheima.bolee.framework.commons.constant.trade.TradeCacheConstant;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.trade.face.AppPayFace;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName AppPayFaceImpl.java
 * @Description 手机网页支付Face接口实现
 */
@Slf4j
@Component
public class AppPayFaceImpl implements AppPayFace {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AppPayAdapter appPayAdapter;

    @Override
    public TradeVO appTrade(TradeVO tradeVO) {
        //防止并发：使用订单号，对本次交易加锁
        String key = TradeCacheConstant.CREATE_PAY + tradeVO.getProductOrderNo();
        RLock lock = redissonClient.getFairLock(key);
        try {
            if (lock.tryLock(TradeCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
                return appPayAdapter.appTrade(tradeVO);
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
}
