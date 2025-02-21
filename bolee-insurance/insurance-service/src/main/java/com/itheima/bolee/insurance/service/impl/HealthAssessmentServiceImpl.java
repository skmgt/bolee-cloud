package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.HealthAssessment;
import com.itheima.bolee.insurance.mapper.HealthAssessmentMapper;
import com.itheima.bolee.insurance.service.IHealthAssessmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.HealthAssessmentCacheConstant;
import com.itheima.bolee.insurance.dto.HealthAssessmentVO;
import com.itheima.bolee.insurance.enums.HealthAssessmentEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：评估类目服务实现类
 */
@Slf4j
@Service
public class HealthAssessmentServiceImpl extends ServiceImpl<HealthAssessmentMapper, HealthAssessment> implements IHealthAssessmentService {


    /***
    * @description 评估类目多条件组合
    * @param healthAssessmentVO 评估类目
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<HealthAssessment> queryWrapper(HealthAssessmentVO healthAssessmentVO){
        QueryWrapper<HealthAssessment> queryWrapper = new QueryWrapper<>();
        //保险产品id查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getInsuranceId())) {
            queryWrapper.lambda().eq(HealthAssessment::getInsuranceId,healthAssessmentVO.getInsuranceId());
        }
        //评估类目key查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getAssessmentKey())) {
            queryWrapper.lambda().eq(HealthAssessment::getAssessmentKey,healthAssessmentVO.getAssessmentKey());
        }
        //评估类目名称查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getAssessmentKeyName())) {
            queryWrapper.lambda().eq(HealthAssessment::getAssessmentKeyName,healthAssessmentVO.getAssessmentKeyName());
        }
        //评估类目值查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getAssessmentVal())) {
            queryWrapper.lambda().eq(HealthAssessment::getAssessmentVal,healthAssessmentVO.getAssessmentVal());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getSortNo())) {
            queryWrapper.lambda().eq(HealthAssessment::getSortNo,healthAssessmentVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getRemake())) {
            queryWrapper.lambda().eq(HealthAssessment::getRemake,healthAssessmentVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(healthAssessmentVO.getDataState())) {
            queryWrapper.lambda().eq(HealthAssessment::getDataState,healthAssessmentVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(HealthAssessment::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = HealthAssessmentCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#healthAssessmentVO.hashCode()")
    public Page<HealthAssessmentVO> findPage(HealthAssessmentVO healthAssessmentVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<HealthAssessment> HealthAssessmentPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<HealthAssessment> queryWrapper = queryWrapper(healthAssessmentVO);
            //执行分页查询
            Page<HealthAssessmentVO> healthAssessmentVOPage = BeanConv.toPage(
                page(HealthAssessmentPage, queryWrapper), HealthAssessmentVO.class);
            //返回结果
            return healthAssessmentVOPage;
        }catch (Exception e){
            log.error("评估类目分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = HealthAssessmentCacheConstant.BASIC,key ="#healthAssessmentId")
    public HealthAssessmentVO findById(String healthAssessmentId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(healthAssessmentId),HealthAssessmentVO.class);
        }catch (Exception e){
            log.error("评估类目单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = HealthAssessmentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = HealthAssessmentCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =HealthAssessmentCacheConstant.BASIC,key = "#result.id")})
    public HealthAssessmentVO save(HealthAssessmentVO healthAssessmentVO) {
        try {
            //转换HealthAssessmentVO为HealthAssessment
            HealthAssessment healthAssessment = BeanConv.toBean(healthAssessmentVO, HealthAssessment.class);
            boolean flag = save(healthAssessment);
            if (!flag){
                throw new RuntimeException("保存评估类目失败");
            }
            //转换返回对象HealthAssessmentVO
            HealthAssessmentVO healthAssessmentVOHandler = BeanConv.toBean(healthAssessment, HealthAssessmentVO.class);
            return healthAssessmentVOHandler;
        }catch (Exception e){
            log.error("保存评估类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = HealthAssessmentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = HealthAssessmentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = HealthAssessmentCacheConstant.BASIC,key = "#healthAssessmentVO.id")})
    public Boolean update(HealthAssessmentVO healthAssessmentVO) {
        try {
            //转换HealthAssessmentVO为HealthAssessment
            HealthAssessment healthAssessment = BeanConv.toBean(healthAssessmentVO, HealthAssessment.class);
            boolean flag = updateById(healthAssessment);
            if (!flag){
                throw new RuntimeException("修改评估类目失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改评估类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = HealthAssessmentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = HealthAssessmentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = HealthAssessmentCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除评估类目失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除评估类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = HealthAssessmentCacheConstant.LIST,key ="#healthAssessmentVO.hashCode()")
    public List<HealthAssessmentVO> findList(HealthAssessmentVO healthAssessmentVO) {
        try {
            //构建查询条件
            QueryWrapper<HealthAssessment> queryWrapper = queryWrapper(healthAssessmentVO);
            //执行列表查询
            List<HealthAssessmentVO> healthAssessmentVOs = BeanConv.toBeanList(list(queryWrapper),HealthAssessmentVO.class);
            return healthAssessmentVOs;
        }catch (Exception e){
            log.error("评估类目列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(HealthAssessmentEnum.LIST_FAIL);
        }
    }
}
