package com.itheima.bolee.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.trade.pojo.RefundRecord;

import java.util.List;

/**
 * @Description： 退款记录表服务类
 */
public interface IRefundRecordService extends IService<RefundRecord> {

    /***
     * @description 查询当前订单是否有退款中的记录
     *
     * @param productOrderNo
     * @return
     */
    RefundRecord findRefundRecordByProductOrderNoAndSending(Long productOrderNo);

    /***
     * @description 按RefundNo查询退款单
     *
     * @param refundNo
     * @return
     */
    RefundRecord findRefundRecordByRefundNo(String refundNo);

    /***
     * @description 订单对应的退款单
     *
     * @param productOrderNos
     * @return
     */
    List<RefundRecordVO> findRefundRecordInProductOrderNo(List<Long> productOrderNos);
}
