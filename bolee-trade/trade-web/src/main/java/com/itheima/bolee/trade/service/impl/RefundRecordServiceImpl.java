package com.itheima.bolee.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.trade.mapper.RefundRecordMapper;
import com.itheima.bolee.trade.pojo.RefundRecord;
import com.itheima.bolee.trade.service.IRefundRecordService;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description： 退款记录服务实现类
 */
@Service
public class RefundRecordServiceImpl extends ServiceImpl<RefundRecordMapper, RefundRecord> implements IRefundRecordService {

    @Override
    public RefundRecord findRefundRecordByProductOrderNoAndSending(Long productOrderNo) {
        QueryWrapper<RefundRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RefundRecord::getProductOrderNo, productOrderNo)
                .eq(RefundRecord::getRefundStatus, TradeConstant.REFUND_STATUS_SENDING);
        return getOne(queryWrapper);
    }

    @Override
    public RefundRecord findRefundRecordByRefundNo(String refundNo) {
        QueryWrapper<RefundRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RefundRecord::getRefundNo, refundNo);
        return getOne(queryWrapper);
    }

    @Override
    public List<RefundRecordVO> findRefundRecordInProductOrderNo(List<Long> productOrderNos) {
        QueryWrapper<RefundRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(RefundRecord::getProductOrderNo, productOrderNos);
        return BeanConv.toBeanList(list(queryWrapper),RefundRecordVO.class);
    }
}
