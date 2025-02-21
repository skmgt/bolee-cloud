package com.itheima.bolee.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.trade.pojo.Trade;

import java.util.List;

/**
 * @Description：交易订单表 服务类
 */
public interface ITradeService extends IService<Trade> {

    /***
     * @description 按交易单号查询交易单
     * @param tradeOrderNo 交易单号
     * @return
     */
    Trade findTradByTradeOrderNo(Long tradeOrderNo);

    /***
     * @description 按订单单号查询交易单
     * @param productOrderNo 交易单号
     * @return
     */
    Trade findTradByProductOrderNo(Long productOrderNo);

    /***
     * @description 按交易状态查询最近10分钟交易单
     * @param tradeState
     * @return
     */
    List<Trade> findTradeByTradeState(String tradeState);

    /***
     * @description 分页查询
     *
     * @param tradeVO
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Trade> findPayChannelVOPage(TradeVO tradeVO, int pageNum, int pageSize);
}
