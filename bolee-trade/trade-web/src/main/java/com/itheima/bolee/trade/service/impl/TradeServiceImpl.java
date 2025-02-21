package com.itheima.bolee.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.mapper.TradeMapper;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.service.ITradeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description：交易订单表 服务实现类
 */
@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements ITradeService {

    @Override
    public Trade findTradByTradeOrderNo(Long tradeOrderNo) {
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Trade::getTradeOrderNo,tradeOrderNo);
        return getOne(queryWrapper);
    }

    @Override
    public Trade findTradByProductOrderNo(Long productOrderNo) {
        LambdaQueryWrapper<Trade> queryWrapper = Wrappers.<Trade>lambdaQuery();
        queryWrapper.eq(Trade::getProductOrderNo,productOrderNo).orderByDesc(Trade::getCreateTime);
        List<Trade> list = list(queryWrapper);
        if (!EmptyUtil.isNullOrEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Trade> findTradeByTradeState(String tradeState) {
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();
        LocalDateTime localDateTime =  LocalDateTime.now().minusSeconds(600);
        queryWrapper.lambda().eq(Trade::getTradeState,tradeState)
            .gt(Trade::getCreateTime, localDateTime)
            .eq(Trade::getDataState, SuperConstant.DATA_STATE_0);
        return list(queryWrapper);
    }

    @Override
    public Page<Trade> findPayChannelVOPage(TradeVO tradeVO, int pageNum, int pageSize) {
        Page<Trade> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();

        if (!EmptyUtil.isNullOrEmpty(tradeVO.getTradeOrderNo())) {
            queryWrapper.lambda().eq(Trade::getTradeOrderNo,tradeVO.getTradeOrderNo());
        }
        if (!EmptyUtil.isNullOrEmpty(tradeVO.getProductOrderNo())) {
            queryWrapper.lambda().likeRight(Trade::getProductOrderNo,tradeVO.getProductOrderNo());
        }
        if (!EmptyUtil.isNullOrEmpty(tradeVO.getTradeState())) {
            queryWrapper.lambda().eq(Trade::getTradeState,tradeVO.getTradeState());
        }
        queryWrapper.lambda().orderByAsc(Trade::getCreateTime);
        return page(page, queryWrapper);
    }
}
