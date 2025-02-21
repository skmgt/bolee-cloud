package com.itheima.bolee.trade.face;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;

import java.util.List;

/**
 * @ClassName TradeFace.java
 * @Description 交易Face接口
 */
public interface TradeFace {


    /***
     * @description 按交易状态查询最近10分钟内交易单
     * @param tradeState
     * @return
     */
    List<TradeVO> findTradeByTradeState(String tradeState);

    /***
     * @description 交易记录分页查询
     *
     * @param tradeVO
     * @param pageNum
     * @param pageSize
     * @return Page<TradeVO>
     */
    Page<TradeVO> findTradeVOPage(TradeVO tradeVO, int pageNum, int pageSize);
}
