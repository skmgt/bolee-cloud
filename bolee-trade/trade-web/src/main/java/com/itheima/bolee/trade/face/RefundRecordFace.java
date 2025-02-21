package com.itheima.bolee.trade.face;

import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;

import java.util.List;

/**
 * @ClassName TradeFace.java
 * @Description 交易Face接口
 */
public interface RefundRecordFace {


    /***
     * @description 按退款状态查询退款记录
     * @param refundStatus
     * @return
     */
    List<RefundRecordVO> findRefundRecordByRefundStatus(String refundStatus);
}
