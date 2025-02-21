package com.itheima.bolee.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.sms.SmsCacheConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSendRecordEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.adapter.SmsSendAdapter;
import com.itheima.bolee.sms.mapper.SmsSendRecordMapper;
import com.itheima.bolee.sms.service.ISmsSendRecordService;
import com.itheima.bolee.sms.pojo.SmsSendRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description：发送记录表 服务实现类
 */
@Slf4j
@Service
public class SmsSendRecordServiceImpl extends ServiceImpl<SmsSendRecordMapper, SmsSendRecord> implements ISmsSendRecordService {

    @Autowired
    SmsSendAdapter smsSendAdapter;

    @Override
    @Cacheable(value = SmsCacheConstant.PAGE_SEND_RECORD,key ="#pageNum+'-'+#pageSize+'-'+#smsSendRecordVO.hashCode()")
    public Page<SmsSendRecordVO> findSmsSendRecordVOPage(SmsSendRecordVO smsSendRecordVO, int pageNum, int pageSize) {
        try {
            Page<SmsSendRecord> page = new Page<>(pageNum,pageSize);
            QueryWrapper<SmsSendRecord> queryWrapper = new QueryWrapper<>();

            if (!EmptyUtil.isNullOrEmpty(smsSendRecordVO.getMobile())) {
                queryWrapper.lambda().eq(SmsSendRecord::getMobile,smsSendRecordVO.getMobile());
            }
            if (!EmptyUtil.isNullOrEmpty(smsSendRecordVO.getChannelLabel())) {
                queryWrapper.lambda().eq(SmsSendRecord::getChannelLabel,smsSendRecordVO.getChannelLabel());
            }
            if (!EmptyUtil.isNullOrEmpty(smsSendRecordVO.getAcceptStatus())) {
                queryWrapper.lambda().eq(SmsSendRecord::getAcceptStatus,smsSendRecordVO.getAcceptStatus());
            }
            if (!EmptyUtil.isNullOrEmpty(smsSendRecordVO.getSendStatus())) {
                queryWrapper.lambda().eq(SmsSendRecord::getSendStatus,smsSendRecordVO.getSendStatus());
            }
            if (!EmptyUtil.isNullOrEmpty(smsSendRecordVO.getDataState())) {
                queryWrapper.lambda().eq(SmsSendRecord::getDataState,smsSendRecordVO.getDataState());
            }
            queryWrapper.lambda().orderByDesc(SmsSendRecord::getCreateTime);
            return BeanConv.toPage(page(page, queryWrapper),SmsSendRecordVO.class);
        } catch (Exception e) {
            log.error("查询发送记录列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.PAGE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SEND_RECORD,allEntries = true)},
            put={@CachePut(value =SmsCacheConstant.PREFIX_SEND_RECORD,key = "#result.id")})
    public SmsSendRecordVO createSmsSendRecord(SmsSendRecordVO smsSendRecordVO) {
        try {
            SmsSendRecord smsSendRecord = BeanConv.toBean(smsSendRecordVO, SmsSendRecord.class);
            boolean flag = save(smsSendRecord);
            if (flag){
                return BeanConv.toBean(smsSendRecord, SmsSendRecordVO.class);
            }
        } catch (Exception e) {
            log.error("保存发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.CREATE_FAIL);
        }
        return null;
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SEND_RECORD,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_SEND_RECORD,allEntries = true)})
    public Boolean updateSmsSendRecord(SmsSendRecordVO smsSendRecordVO) {
        try {
            SmsSendRecord smsSendRecord = BeanConv.toBean(smsSendRecordVO, SmsSendRecord.class);
            return updateById(smsSendRecord);
        } catch (Exception e) {
            log.error("保存发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SEND_RECORD,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_SEND_RECORD,allEntries = true)})
    public Boolean deleteSmsSendRecord(String[] checkedIds) {
        try {
            List<String> ids = Arrays.asList(checkedIds);
            List<Long> idsLong = new ArrayList<>();
            ids.forEach(n->{
                idsLong.add(Long.valueOf(n));
            });
            return removeByIds(idsLong);
        } catch (Exception e) {
            log.error("删除发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.DELETE_FAIL);
        }

    }

    @Override
    @Cacheable(value = SmsCacheConstant.PREFIX_SEND_RECORD,key = "#smsSendRecordId")
    public SmsSendRecordVO findSmsSendRecordBySmsSendRecordId(Long smsSendRecordId)throws ProjectException {
        try {
            SmsSendRecord smsSendRecord = getById(smsSendRecordId);
            if (!EmptyUtil.isNullOrEmpty(smsSendRecord)){
                return BeanConv.toBean(smsSendRecord,SmsSendRecordVO.class);
            }
            return null;
        } catch (Exception e) {
            log.error("查找发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.SELECT_FAIL);
        }
    }

    @Override
    @CacheEvict(value = SmsCacheConstant.PREFIX_SEND_RECORD,key = "#smsSendRecordVO.id")
    @Transactional
    public Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO) {
        try {
            return smsSendAdapter.retrySendSms(BeanConv.toBean(smsSendRecordVO,SmsSendRecord.class));
        } catch (Exception e) {
            log.error("查找发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.SEND_RECORD_FAIL);
        }
    }

    @Override
    public List<SmsSendRecordVO> callBackSmsSendRecords() {
        try {
            QueryWrapper<SmsSendRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsSendRecord::getAcceptStatus, SmsConstant.STATUS_ACCEPT_0)
                    .eq(SmsSendRecord::getSendStatus, SmsConstant.STATUS_SEND_2)
                    .last("limit 0 , 20");;
            return BeanConv.toBeanList(list(queryWrapper),SmsSendRecordVO.class);
        } catch (Exception e) {
            log.error("查找发送记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSendRecordEnum.SEND_RECORD_FAIL);
        }

    }
}
