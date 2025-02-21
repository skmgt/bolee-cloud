package com.itheima.bolee.trade.face;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;

import java.util.List;

/**
 * @ClassName PayChannelFace.java
 * @Description 渠道Face接口
 */
public interface PayChannelFace {

    /**
     * @Description 渠道列表
     * @param smsChannelVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<PayChannelVO>
     */
    Page<PayChannelVO> findPayChannelVOPage(PayChannelVO smsChannelVO,
                                            int pageNum,
                                            int pageSize)throws ProjectException;

    /**
     * @Description 创建渠道
     * @param smsChannelVO 对象信息
     * @return PayChannelVO
     */
    PayChannelVO createPayChannel(PayChannelVO smsChannelVO)throws ProjectException;

    /**
     * @Description 修改渠道
     * @param smsChannelVO 对象信息
     * @return Boolean
     */
    Boolean updatePayChannel(PayChannelVO smsChannelVO)throws ProjectException;

    /**
     * @Description 删除渠道
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean deletePayChannel(String[] checkedIds)throws ProjectException;

    /**
     * @Description 查询唯一有效支付渠道
     * @param channelLabel 渠道标识
     * @return Boolean
     */
    PayChannelVO findPayChannelVlid(String channelLabel);
}
