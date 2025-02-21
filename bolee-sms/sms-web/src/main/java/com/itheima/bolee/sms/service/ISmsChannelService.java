package com.itheima.bolee.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.sms.pojo.SmsChannel;

import java.util.List;
import java.util.Set;

/**
 * @Description： 短信通道服务类
 */
public interface ISmsChannelService extends IService<SmsChannel> {

    /***
     * @description 查询通道配置
     * @param channelLabel 通道标识
     * @return 通道
     */
    SmsChannelVO findChannelByChannelLabel(String channelLabel);

    /***
     * @description 查询通道配置
     * @param channelLabels 通道标识
     * @return 通道
     */
    List<SmsChannelVO> findChannelInChannelLabel(Set<String> channelLabels);

    /**
     * @Description 通道列表
     * @param smsChannelVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<SmsChannel>
     */
    Page<SmsChannelVO> findSmsChannelVOPage(SmsChannelVO smsChannelVO, int pageNum, int pageSize);

    /**
     * @Description 创建通道
     * @param smsChannelVO 对象信息
     * @return SmsChannel
     */
    SmsChannelVO createSmsChannel(SmsChannelVO smsChannelVO);

    /**
     * @Description 修改通道
     * @param smsChannelVO 对象信息
     * @return Boolean
     */
    SmsChannelVO updateSmsChannel(SmsChannelVO smsChannelVO);

    /**
     * @Description 删除通道
     * @param checkedIds 选择的通道ID
     * @return Boolean
     */
    Boolean deleteSmsChannel(String[] checkedIds);

    /***
     * @description 查询通道下拉框
     * @return: List<SmsChannel>
     */
    List<SmsChannelVO> findSmsChannelVOList();

    /**
     * @Description 按渠道标识查找渠道
     * @param channelLabel
     * @return SmsChannelVO
     */
    SmsChannelVO findSmsChannelByChannelLabel(String channelLabel);
}
