package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.Condition;
import com.itheima.bolee.insurance.mapper.ConditionMapper;
import com.itheima.bolee.insurance.service.IConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.ConditionCacheConstant;
import com.itheima.bolee.insurance.dto.ConditionVO;
import com.itheima.bolee.insurance.enums.ConditionEnum;
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
 * @Description：筛选项服务实现类
 */
@Slf4j
@Service
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements IConditionService {


    /***
    * @description 筛选项多条件组合
    * @param conditionVO 筛选项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Condition> queryWrapper(ConditionVO conditionVO){
        QueryWrapper<Condition> queryWrapper = new QueryWrapper<>();
        //条件键查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getConditionKey())) {
            queryWrapper.lambda().eq(Condition::getConditionKey,conditionVO.getConditionKey());
        }
        //条件键名查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getConditionKeyName())) {
            queryWrapper.lambda().eq(Condition::getConditionKeyName,conditionVO.getConditionKeyName());
        }
        //条件值查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getConditionVal())) {
            queryWrapper.lambda().eq(Condition::getConditionVal,conditionVO.getConditionVal());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getSortNo())) {
            queryWrapper.lambda().eq(Condition::getSortNo,conditionVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getRemake())) {
            queryWrapper.lambda().eq(Condition::getRemake,conditionVO.getRemake());
        }
        //ConditionKeys
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getConditionKeys())) {
            queryWrapper.lambda().in(Condition::getConditionKey,conditionVO.getConditionKeys());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(conditionVO.getDataState())) {
            queryWrapper.lambda().eq(Condition::getDataState,conditionVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Condition::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = ConditionCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#conditionVO.hashCode()")
    public Page<ConditionVO> findPage(ConditionVO conditionVO, int pageNum, int pageSize) {
        try {
            //构建分页对象

            //构建查询条件

            //执行分页查询

            //返回结果
            return null;
        }catch (Exception e){
            log.error("筛选项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = ConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ConditionCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =ConditionCacheConstant.BASIC,key = "#result.id")})
    public ConditionVO save(ConditionVO conditionVO) {
        try {
            //转换ConditionVO为Condition

            //转换返回对象ConditionVO

            return null;
        }catch (Exception e){
            log.error("保存筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = ConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ConditionCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = ConditionCacheConstant.BASIC,key = "#conditionVO.id")})
    public Boolean update(ConditionVO conditionVO) {
        try {
            //转换ConditionVO为Condition

            return null;
        }catch (Exception e){
            log.error("修改筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = ConditionCacheConstant.LIST,key ="#conditionVO.hashCode()")
    public List<ConditionVO> findList(ConditionVO conditionVO) {
        try {
            //构建查询条件

            //执行列表查询

            return null;
        }catch (Exception e){
            log.error("筛选项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = ConditionCacheConstant.LIST,key ="#conditionKeys.hashCode()")
    public List<ConditionVO> findInConditionKeys(List<String> conditionKeys) {
        try {
            //构建查询条件

            //执行列表查询

            return null;
        }catch (Exception e){
            log.error("筛选项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.LIST_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = ConditionCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = ConditionCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = ConditionCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            //构建IDS

            return null;
        }catch (Exception e){
            log.error("删除筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = ConditionCacheConstant.BASIC,key ="#conditionId")
    public ConditionVO findById(String conditionId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(conditionId),ConditionVO.class);
        }catch (Exception e){
            log.error("筛选项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ConditionEnum.FIND_ONE_FAIL);
        }
    }
}
