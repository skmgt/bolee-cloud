package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.dto.SickVO;
import com.itheima.bolee.insurance.pojo.SickSearchRecord;
import com.itheima.bolee.insurance.mapper.SickSearchRecordMapper;
import com.itheima.bolee.insurance.service.ISickSearchRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.SickSearchRecordCacheConstant;
import com.itheima.bolee.insurance.dto.SickSearchRecordVO;
import com.itheima.bolee.insurance.enums.SickSearchRecordEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @Description：疾病搜索记录服务实现类
 */
@Slf4j
@Service
public class SickSearchRecordServiceImpl extends ServiceImpl<SickSearchRecordMapper, SickSearchRecord> implements ISickSearchRecordService {

    @Autowired
    SickSearchRecordMapper sickSearchRecordMapper;

    /***
    * @description 疾病搜索记录多条件组合
    * @param sickSearchRecordVO 疾病搜索记录
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<SickSearchRecord> queryWrapper(SickSearchRecordVO sickSearchRecordVO){
        QueryWrapper<SickSearchRecord> queryWrapper = new QueryWrapper<>();
        //搜索内容查询
        if (!EmptyUtil.isNullOrEmpty(sickSearchRecordVO.getContent())) {
            queryWrapper.lambda().eq(SickSearchRecord::getContent,sickSearchRecordVO.getContent());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(sickSearchRecordVO.getDataState())) {
            queryWrapper.lambda().eq(SickSearchRecord::getDataState,sickSearchRecordVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SickSearchRecord::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SickSearchRecordCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#sickSearchRecordVO.hashCode()")
    public Page<SickSearchRecordVO> findPage(SickSearchRecordVO sickSearchRecordVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<SickSearchRecord> SickSearchRecordPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<SickSearchRecord> queryWrapper = queryWrapper(sickSearchRecordVO);
            //执行分页查询
            Page<SickSearchRecordVO> sickSearchRecordVOPage = BeanConv.toPage(
                page(SickSearchRecordPage, queryWrapper), SickSearchRecordVO.class);
            //返回结果
            return sickSearchRecordVOPage;
        }catch (Exception e){
            log.error("疾病搜索记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SickSearchRecordCacheConstant.BASIC,key ="#sickSearchRecordId")
    public SickSearchRecordVO findById(String sickSearchRecordId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(sickSearchRecordId),SickSearchRecordVO.class);
        }catch (Exception e){
            log.error("疾病搜索记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickSearchRecordCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickSearchRecordCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =SickSearchRecordCacheConstant.BASIC,key = "#result.id")})
    public SickSearchRecordVO save(SickSearchRecordVO sickSearchRecordVO) {
        try {
            //转换SickSearchRecordVO为SickSearchRecord
            SickSearchRecord sickSearchRecord = BeanConv.toBean(sickSearchRecordVO, SickSearchRecord.class);
            boolean flag = save(sickSearchRecord);
            if (!flag){
                throw new RuntimeException("保存疾病搜索记录失败");
            }
            //转换返回对象SickSearchRecordVO
            SickSearchRecordVO sickSearchRecordVOHandler = BeanConv.toBean(sickSearchRecord, SickSearchRecordVO.class);
            return sickSearchRecordVOHandler;
        }catch (Exception e){
            log.error("保存疾病搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickSearchRecordCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickSearchRecordCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SickSearchRecordCacheConstant.BASIC,key = "#sickSearchRecordVO.id")})
    public Boolean update(SickSearchRecordVO sickSearchRecordVO) {
        try {
            //转换SickSearchRecordVO为SickSearchRecord
            SickSearchRecord sickSearchRecord = BeanConv.toBean(sickSearchRecordVO, SickSearchRecord.class);
            boolean flag = updateById(sickSearchRecord);
            if (!flag){
                throw new RuntimeException("修改疾病搜索记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改疾病搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickSearchRecordCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickSearchRecordCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SickSearchRecordCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除疾病搜索记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除疾病搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = SickSearchRecordCacheConstant.LIST,key ="#sickSearchRecordVO.hashCode()")
    public List<SickSearchRecordVO> findList(SickSearchRecordVO sickSearchRecordVO) {
        try {
            //构建查询条件
            QueryWrapper<SickSearchRecord> queryWrapper = queryWrapper(sickSearchRecordVO);
            //执行列表查询
            List<SickSearchRecordVO> sickSearchRecordVOs = BeanConv.toBeanList(list(queryWrapper),SickSearchRecordVO.class);
            return sickSearchRecordVOs;
        }catch (Exception e){
            log.error("疾病搜索记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.LIST_FAIL);
        }
    }

    /**
     * 批量插入搜索记录
     * @param searchRecordVOList
     * @return
     */
    @Override
    public Boolean saveRecord(List<SickSearchRecordVO> searchRecordVOList) {
        try {
            List<SickSearchRecord> searchRecords = BeanConv.toBeanList(searchRecordVOList, SickSearchRecord.class);
            saveBatch(searchRecords);
            return true;
        }catch (Exception e){
            log.error("疾病搜索记录列表保存异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.SAVE_FAIL);
        }
    }

    /**
     * 疾病热搜榜
     * 默认查询30天内的疾病
     * @return
     */
    @Override
    public List<SickVO> hotSickList() {
        try{
            //开始查询这个月内搜索次数最多的疾病
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now().plusMonths(-1L);
            LocalDateTime maxTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);
            LocalDateTime minTime = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
            String begin = dtf.format(minTime);
            String end = dtf.format(maxTime);
            List<SickVO> res = sickSearchRecordMapper.findSearchList(begin,end);
            return res;
        }catch (Exception e){
            log.error("热搜榜搜索异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.SAVE_FAIL);
        }
    }

}
