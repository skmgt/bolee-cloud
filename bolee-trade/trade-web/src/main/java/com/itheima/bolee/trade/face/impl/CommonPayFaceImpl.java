package com.itheima.bolee.trade.face.impl;

import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.adapter.CommonPayAdapter;
import com.itheima.bolee.trade.face.CommonPayFace;
import com.itheima.bolee.framework.commons.constant.trade.TradeCacheConstant;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.service.impl.TradeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CommonPayFaceImpl.java
 * @Description 基础交易face接口实现
 */
@Component
@Slf4j
public class CommonPayFaceImpl implements CommonPayFace {

    @Autowired
    CommonPayAdapter commonPayAdapter;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ITradeService tradeService;

    @Resource(name = "syncPaymentExecutor")
    Executor periodicPayExecutor;

    @Override
    public String queryQrCode(TradeVO tradeVO) {
        return commonPayAdapter.queryQrCode(tradeVO);
    }

    @Override
    @Transactional
    public TradeVO queryTrade(TradeVO tradeVO) throws ProjectException {
        return commonPayAdapter.queryTrade(tradeVO);
    }

    @Override
    @Transactional
    public TradeVO refundTrade(TradeVO tradeVO) throws ProjectException {
        //1、对交易订单加锁
        Long productOrderNo = tradeVO.getProductOrderNo();
        String key = TradeCacheConstant.REFUND_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            boolean flag = lock.tryLock(TradeCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS);
            if (flag){
                return commonPayAdapter.refundTrade(tradeVO);
            }else {
                throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
            }
        } catch (Exception e) {
            log.error("统一收单交易退款接口异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }finally {
            lock.unlock();
        }

    }

    @Override
    @Transactional
    public RefundRecordVO queryRefundTrade(RefundRecordVO refundRecordVO) throws ProjectException {
        return commonPayAdapter.queryRefundTrade(refundRecordVO);
    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        return commonPayAdapter.closeTrade(tradeVO);
    }

    @Override
    public TradeVO downLoadBill(TradeVO tradeVO) {
        return commonPayAdapter.downLoadBill(tradeVO);
    }

    @Override
    public Boolean syncPaymentJob() {
        //查询最近10分钟内待支付订单
        List<Trade> trades = tradeService.findTradeByTradeState(TradeConstant.TRADE_WAIT);
        if (EmptyUtil.isNullOrEmpty(trades)){
            return true;
        }
        //线程处周期性扣款
        for (Trade trade: trades) {
            periodicPayExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    queryTrade(BeanConv.toBean(trade,TradeVO.class));
                }
            });
        }
        return true;
    }
}
