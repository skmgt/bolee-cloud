package com.itheima.bolee.trade.face.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.constant.trade.TradeCacheConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;
import com.itheima.bolee.framework.commons.enums.trade.PayChannelEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.trade.face.PayChannelFace;
import com.itheima.bolee.trade.pojo.PayChannel;
import com.itheima.bolee.trade.service.IPayChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName PayChannelFaceImpl.java
 * @Description 支付渠道Face接口接口实现
 */
@Slf4j
@Component
public class PayChannelFaceImpl implements PayChannelFace {

    @Autowired
    IPayChannelService payChannelService;

    @Override
    @Cacheable(value = TradeCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#payChannelVO.hashCode()")
    public Page<PayChannelVO> findPayChannelVOPage(PayChannelVO payChannelVO,
                                                   int pageNum,
                                                   int pageSize) throws ProjectException{
        try {
            Page<PayChannel> page = payChannelService.findPayChannelVOPage(payChannelVO, pageNum, pageSize);
            Page<PayChannelVO> pageVo = BeanConv.toPage(page,PayChannelVO.class);
            //结果集转换
            List<PayChannelVO> payChannelVOList = pageVo.getRecords();
            if (!EmptyUtil.isNullOrEmpty(payChannelVOList)){
                payChannelVOList.forEach(n->{
                    if (!EmptyUtil.isNullOrEmpty(n.getOtherConfig())){
                        List<OtherConfigVO> list = JSONArray.parseArray(n.getOtherConfig(),OtherConfigVO.class);
                        n.setOtherConfigs(list);
                    }
                });
            }
            pageVo.setRecords(payChannelVOList);
            return pageVo;
        } catch (Exception e) {
            log.error("查询支付通道列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PayChannelEnum.PAGE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = TradeCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = TradeCacheConstant.PAY_CHANNEL_VLID,allEntries = true)})
    public PayChannelVO createPayChannel(PayChannelVO payChannelVO) throws ProjectException{
        try {
            return BeanConv.toBean( payChannelService.createPayChannel(payChannelVO), PayChannelVO.class);
        } catch (Exception e) {
            log.error("保存支付通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PayChannelEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = TradeCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = TradeCacheConstant.PAY_CHANNEL_VLID,allEntries = true)})
    public Boolean updatePayChannel(PayChannelVO payChannelVO) throws ProjectException{
        try {
            return payChannelService.updatePayChannel(payChannelVO);
        } catch (Exception e) {
            log.error("保存支付通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PayChannelEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = TradeCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = TradeCacheConstant.PAY_CHANNEL_VLID,allEntries = true)})
    public Boolean deletePayChannel(String[] checkedIds) throws ProjectException{
        try {
            return payChannelService.deletePayChannel(checkedIds);
        } catch (Exception e) {
            log.error("删除支付通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PayChannelEnum.DELETE_FAIL);
        }
    }

    @Override
    @Cacheable(value = TradeCacheConstant.PAY_CHANNEL_VLID,key = "#channelLabel")
    public PayChannelVO findPayChannelVlid(String channelLabel) {
        PayChannelVO payChannelVO = BeanConv.toBean(payChannelService.findPayChannelVlid(channelLabel), PayChannelVO.class);
        if (!EmptyUtil.isNullOrEmpty(payChannelVO)&&!EmptyUtil.isNullOrEmpty(payChannelVO.getOtherConfig())){
            List<OtherConfigVO> list = JSONArray.parseArray(payChannelVO.getOtherConfig(),OtherConfigVO.class);
            payChannelVO.setOtherConfigs(list);
        }
        return payChannelVO;
    }

}
