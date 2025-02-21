package com.itheima.bolee.sms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsCacheConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsChannelVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsChannelEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.handler.baidu.config.BaiduSmsConfig;
import com.itheima.bolee.sms.handler.aliyun.config.AliyunSmsConfig;
import com.itheima.bolee.sms.handler.tencent.config.TencentSmsConfig;
import com.itheima.bolee.sms.mapper.SmsChannelMapper;
import com.itheima.bolee.sms.pojo.SmsChannel;
import com.itheima.bolee.sms.service.ISmsChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Description： 服务实现类
 */
@Slf4j
@Service
public class SmsChannelServiceImpl extends ServiceImpl<SmsChannelMapper, SmsChannel> implements ISmsChannelService {

    @Override
    @Cacheable(value = SmsCacheConstant.PAGE_CHANNEL,key ="#pageNum+'-'+#pageSize+'-'+#smsChannelVO.hashCode()")
    public Page<SmsChannelVO> findSmsChannelVOPage(SmsChannelVO smsChannelVO, int pageNum, int pageSize) {
        try {
            Page<SmsChannel> page = new Page<>(pageNum,pageSize);
            QueryWrapper<SmsChannel> queryWrapper = new QueryWrapper<>();

            if (!EmptyUtil.isNullOrEmpty(smsChannelVO.getChannelLabel())) {
                queryWrapper.lambda().eq(SmsChannel::getChannelLabel,smsChannelVO.getChannelLabel());
            }
            if (!EmptyUtil.isNullOrEmpty(smsChannelVO.getChannelName())) {
                queryWrapper.lambda().likeRight(SmsChannel::getChannelName,smsChannelVO.getChannelName());
            }
            if (!EmptyUtil.isNullOrEmpty(smsChannelVO.getDataState())) {
                queryWrapper.lambda().eq(SmsChannel::getDataState,smsChannelVO.getDataState());
            }
            queryWrapper.lambda().orderByAsc(SmsChannel::getCreateTime);
            Page<SmsChannelVO> pageVo = BeanConv.toPage(page(page, queryWrapper),SmsChannelVO.class);
            //结果集转换
            List<SmsChannelVO> smsChannelVOs =pageVo.getRecords();
            if (!EmptyUtil.isNullOrEmpty(smsChannelVOs)){
                smsChannelVOs.forEach(n->{
                    List <OtherConfigVO> list = JSONArray.parseArray(n.getOtherConfig(),OtherConfigVO.class);
                    n.setOtherConfigs(list);
                });
            }
            return pageVo;
        } catch (Exception e) {
            log.error("查询通道列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_CHANNEL,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.CHANNEL_LIST,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.CHANNEL_LABEL,key = "#smsChannelVO.channelLabel")},
        put={@CachePut(value =SmsCacheConstant.PREFIX_CHANNEL,key = "#result.channelLabel")})
    public SmsChannelVO createSmsChannel(SmsChannelVO smsChannelVO) {
        try {
            SmsChannel smsChannel = BeanConv.toBean(smsChannelVO, SmsChannel.class);
            smsChannel.setOtherConfig(JSONObject.toJSONString(smsChannelVO.getOtherConfigs()));
            boolean flag = save(smsChannel);
            return BeanConv.toBean( smsChannel, SmsChannelVO.class);
        } catch (Exception e) {
            log.error("保存通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_CHANNEL,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.CHANNEL_LIST,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.CHANNEL_LABEL,key = "#smsChannelVO.channelLabel"),
        @CacheEvict(value = SmsCacheConstant.PREFIX_CHANNEL,key = "#smsChannelVO.channelLabel")})
    public SmsChannelVO updateSmsChannel(SmsChannelVO smsChannelVO) {
        try {
            SmsChannel smsChannel = BeanConv.toBean(smsChannelVO, SmsChannel.class);
            smsChannel.setOtherConfig(JSONObject.toJSONString(smsChannelVO.getOtherConfigs()));
            updateById(smsChannel);
            return BeanConv.toBean(smsChannel,smsChannelVO.getClass());
        } catch (Exception e) {
            log.error("保存通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.UPDATE_FAIL);
        }

    }

    @Override
    public SmsChannelVO findChannelByChannelLabel(String channelLabel) {
        try {
            QueryWrapper<SmsChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(SmsChannel::getDataState, SuperConstant.DATA_STATE_0)
                    .eq(SmsChannel::getChannelLabel,channelLabel);
            return BeanConv.toBean(getOne(queryWrapper),SmsChannelVO.class);
        } catch (Exception e) {
            log.error("保存通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SmsCacheConstant.CHANNEL_LIST,key = "#channelLabels.hashCode()")
    public List<SmsChannelVO> findChannelInChannelLabel(Set<String> channelLabels) {
        try {
            QueryWrapper<SmsChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsChannel::getDataState, SuperConstant.DATA_STATE_0)
                    .in(SmsChannel::getChannelLabel,channelLabels);
            return BeanConv.toBeanList(list(queryWrapper),SmsChannelVO.class);
        } catch (Exception e) {
            log.error("保存通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_CHANNEL,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.CHANNEL_LIST,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.CHANNEL_LABEL,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_CHANNEL,allEntries = true)})
    public Boolean deleteSmsChannel(String[] checkedIds) {
        try {
            List<String> ids = Arrays.asList(checkedIds);
            for (String id : ids) {
                SmsChannel smsChannel = getById(id);
                removeById(id);
            }
            return true;
        } catch (Exception e) {
            log.error("删除通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.DELETE_FAIL);
        }

    }

    @Override
    @Cacheable(value =SmsCacheConstant.CHANNEL_LIST)
    public List<SmsChannelVO> findSmsChannelVOList() {
        try {
            QueryWrapper<SmsChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsChannel::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBeanList(list(queryWrapper),SmsChannelVO.class);
        } catch (Exception e) {
            log.error("查找所有通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.SELECT_FAIL);
        }

    }

    @Override
    @Cacheable(value =SmsCacheConstant.CHANNEL_LABEL,key = "#channelLabel")
    public SmsChannelVO findSmsChannelByChannelLabel(String channelLabel) {
        try {
            QueryWrapper<SmsChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsChannel::getDataState, SuperConstant.DATA_STATE_0)
                    .eq(SmsChannel::getChannelLabel,channelLabel);
            return BeanConv.toBean(getOne(queryWrapper),SmsChannelVO.class);
        } catch (Exception e) {
            log.error("查找通道异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsChannelEnum.SELECT_FAIL);
        }
    }

}
