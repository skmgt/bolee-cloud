package com.itheima.bolee.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.trade.pojo.PayChannel;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;

import java.util.List;

/**
 * @Description： 支付通道服务类
 */
public interface IPayChannelService extends IService<PayChannel> {

    /**
     * @Description 支付通道列表
     * @param payChannelVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<PayChannel>
     */
    Page<PayChannel> findPayChannelVOPage(PayChannelVO payChannelVO, int pageNum, int pageSize);

    /**
     * @Description 创建支付通道
     * @param payChannelVO 对象信息
     * @return PayChannel
     */
    PayChannel createPayChannel(PayChannelVO payChannelVO);

    /**
     * @Description 修改支付通道
     * @param payChannelVO 对象信息
     * @return Boolean
     */
    Boolean updatePayChannel(PayChannelVO payChannelVO);

    /**
     * @Description 删除支付通道
     * @param checkedIds 选择的支付通道ID
     * @return Boolean
     */
    Boolean deletePayChannel(String[] checkedIds);

    /**
     * @Description 查找有效渠道标识
     * @param channelLabel 支付通道标识
     * @return Boolean
     */
    PayChannel findPayChannelVlid(String channelLabel);
}
