package com.itheima.bolee.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.sms.SmsCacheConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsBlacklistVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsBlacklistEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.mapper.SmsBlacklistMapper;
import com.itheima.bolee.sms.pojo.SmsBlacklist;
import com.itheima.bolee.sms.service.ISmsBlacklistService;
import lombok.extern.slf4j.Slf4j;
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
 * @Description：黑名单表 服务实现类
 */
@Slf4j
@Service
public class SmsBlacklistServiceImpl extends ServiceImpl<SmsBlacklistMapper, SmsBlacklist> implements ISmsBlacklistService {

    @Override
    @Cacheable(value = SmsCacheConstant.PAGE_BLACKLIST,key ="#pageNum+'-'+#pageSize+'-'+#smsBlacklistVO.hashCode()")
    public Page<SmsBlacklistVO> findSmsBlacklistVOPage(SmsBlacklistVO smsBlacklistVO, int pageNum, int pageSize) {
        try {
            Page<SmsBlacklist> page = new Page<>(pageNum,pageSize);
            QueryWrapper<SmsBlacklist> queryWrapper = new QueryWrapper<>();

            if (!EmptyUtil.isNullOrEmpty(smsBlacklistVO.getMobile())) {
                queryWrapper.lambda().eq(SmsBlacklist::getMobile,smsBlacklistVO.getMobile());
            }
            if (!EmptyUtil.isNullOrEmpty(smsBlacklistVO.getDataState())) {
                queryWrapper.lambda().eq(SmsBlacklist::getDataState,smsBlacklistVO.getDataState());
            }
            queryWrapper.lambda().orderByAsc(SmsBlacklist::getCreateTime);
            return BeanConv.toPage(page(page, queryWrapper),SmsBlacklistVO.class);
        } catch (Exception e) {
            log.error("查询黑名单列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsBlacklistEnum.PAGE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_BLACKLIST,allEntries = true)},
            put={@CachePut(value =SmsCacheConstant.PREFIX_BLACKLIST,key = "#result.id")})
    public SmsBlacklistVO createSmsBlacklist(SmsBlacklistVO smsBlacklistVO) {
        try {
            SmsBlacklist smsBlacklist = BeanConv.toBean(smsBlacklistVO, SmsBlacklist.class);
            boolean flag = save(smsBlacklist);
            if (flag){
                return BeanConv.toBean(smsBlacklist,SmsBlacklistVO.class);
            }
            return null;
        } catch (Exception e) {
            log.error("保存黑名单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsBlacklistEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_BLACKLIST,allEntries = true),
            @CacheEvict(value =SmsCacheConstant.PREFIX_BLACKLIST,key = "#smsBlacklistVO.id")})
    public Boolean updateSmsBlacklist(SmsBlacklistVO smsBlacklistVO) {
        try {
            SmsBlacklist smsBlacklist = BeanConv.toBean(smsBlacklistVO, SmsBlacklist.class);
            return updateById(smsBlacklist);
        } catch (Exception e) {
            log.error("保存黑名单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsBlacklistEnum.UPDATE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_BLACKLIST,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_BLACKLIST,allEntries = true)})
    public Boolean deleteSmsBlacklist(String[] checkedIds) {
        try {
            List<String> ids = Arrays.asList(checkedIds);
            List<Long> idsLong = new ArrayList<>();
            ids.forEach(n->{
                idsLong.add(Long.valueOf(n));
            });
            return removeByIds(idsLong);
        } catch (Exception e) {
            log.error("删除黑名单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsBlacklistEnum.DELETE_FAIL);
        }
    }
}
