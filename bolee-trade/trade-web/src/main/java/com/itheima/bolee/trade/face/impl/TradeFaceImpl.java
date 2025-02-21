package com.itheima.bolee.trade.face.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.enums.trade.PayChannelEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.face.TradeFace;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.service.IRefundRecordService;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TradeFace.java
 * @Description 交易Face接口
 */
@Slf4j
@Component
public class TradeFaceImpl implements TradeFace {

    @Autowired
    ITradeService tradeService;

    @Autowired
    IRefundRecordService refundRecordService;

    @Override
    public List<TradeVO> findTradeByTradeState(String tradeState) {
        List<Trade>  trades = tradeService.findTradeByTradeState(tradeState);
        return BeanConv.toBeanList(trades,TradeVO.class);
    }

    @Override
    public Page<TradeVO> findTradeVOPage(TradeVO tradeVO, int pageNum, int pageSize) {
        try {
            Page<Trade> page = tradeService.findPayChannelVOPage(tradeVO, pageNum, pageSize);
            Page<TradeVO> pageVO = BeanConv.toPage(page,TradeVO.class);
            if (!EmptyUtil.isNullOrEmpty(pageVO.getRecords())){
                //对应退款单
                List<Long> productOrderNos = pageVO.getRecords().stream()
                        .map(TradeVO::getProductOrderNo).collect(Collectors.toList());
                List<RefundRecordVO> refundRecordVOList = refundRecordService
                        .findRefundRecordInProductOrderNo(productOrderNos);
                pageVO.getRecords().forEach(n->{
                    //装备退款单
                    List<RefundRecordVO> refundRecordVOListHandler = Lists.newArrayList();
                    refundRecordVOList.forEach(d->{
                        if (n.getProductOrderNo().equals(d.getProductOrderNo())){
                            refundRecordVOListHandler.add(d);
                        }
                    });
                    n.setRefundRecordVOList(refundRecordVOListHandler);
                });
            }
            return pageVO;
        } catch (Exception e) {
            log.error("查询支付通道列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PayChannelEnum.PAGE_FAIL);
        }
    }
}
