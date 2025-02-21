package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.safeguard.SafeguardConstant;
import com.itheima.bolee.insurance.dto.SafeguardVO;
import com.itheima.bolee.insurance.pojo.PlanSafeguard;
import com.itheima.bolee.insurance.mapper.PlanSafeguardMapper;
import com.itheima.bolee.insurance.service.IPlanSafeguardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.insurance.service.ISafeguardService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.PlanSafeguardCacheConstant;
import com.itheima.bolee.insurance.dto.PlanSafeguardVO;
import com.itheima.bolee.insurance.enums.PlanSafeguardEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：方案保障项服务实现类
 */
@Slf4j
@Service
public class PlanSafeguardServiceImpl extends ServiceImpl<PlanSafeguardMapper, PlanSafeguard> implements IPlanSafeguardService {

    @Autowired
    private ISafeguardService safeguardService;

    /***
    * @description 方案保障项多条件组合
    * @param planSafeguardVO 方案保障项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<PlanSafeguard> queryWrapper(PlanSafeguardVO planSafeguardVO){
        QueryWrapper<PlanSafeguard> queryWrapper = new QueryWrapper<>();
        //保险方案ID查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getPlanId())) {
            queryWrapper.lambda().eq(PlanSafeguard::getPlanId,planSafeguardVO.getPlanId());
        }
        //条款维度key查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getSafeguardKey())) {
            queryWrapper.lambda().eq(PlanSafeguard::getSafeguardKey,planSafeguardVO.getSafeguardKey());
        }
        //条款维度value查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getSafeguardValue())) {
            queryWrapper.lambda().eq(PlanSafeguard::getSafeguardValue,planSafeguardVO.getSafeguardValue());
        }
        //显示位置：0 列表页 1 详情页查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getPosition())) {
            queryWrapper.lambda().eq(PlanSafeguard::getPosition,planSafeguardVO.getPosition());
        }
        //保障内容补充说明查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getRemake())) {
            queryWrapper.lambda().eq(PlanSafeguard::getRemake,planSafeguardVO.getRemake());
        }
        //保障类型（0保障规则 1 保障信息）查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getSafeguardType())) {
            queryWrapper.lambda().eq(PlanSafeguard::getSafeguardType,planSafeguardVO.getSafeguardType());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getSortNo())) {
            queryWrapper.lambda().eq(PlanSafeguard::getSortNo,planSafeguardVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(planSafeguardVO.getDataState())) {
            queryWrapper.lambda().eq(PlanSafeguard::getDataState,planSafeguardVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(PlanSafeguard::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = PlanSafeguardCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#planSafeguardVO.hashCode()")
    public Page<PlanSafeguardVO> findPage(PlanSafeguardVO planSafeguardVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<PlanSafeguard> PlanSafeguardPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<PlanSafeguard> queryWrapper = queryWrapper(planSafeguardVO);
            //执行分页查询
            Page<PlanSafeguardVO> planSafeguardVOPage = BeanConv.toPage(
                page(PlanSafeguardPage, queryWrapper), PlanSafeguardVO.class);
            //返回结果
            return planSafeguardVOPage;
        }catch (Exception e){
            log.error("方案保障项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = PlanSafeguardCacheConstant.BASIC,key ="#planSafeguardId")
    public PlanSafeguardVO findById(String planSafeguardId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(planSafeguardId),PlanSafeguardVO.class);
        }catch (Exception e){
            log.error("方案保障项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanSafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanSafeguardCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =PlanSafeguardCacheConstant.BASIC,key = "#result.id")})
    public PlanSafeguardVO save(PlanSafeguardVO planSafeguardVO) {
        try {
            //转换PlanSafeguardVO为PlanSafeguard
            PlanSafeguard planSafeguard = BeanConv.toBean(planSafeguardVO, PlanSafeguard.class);
            boolean flag = save(planSafeguard);
            if (!flag){
                throw new RuntimeException("保存方案保障项失败");
            }
            //转换返回对象PlanSafeguardVO
            PlanSafeguardVO planSafeguardVOHandler = BeanConv.toBean(planSafeguard, PlanSafeguardVO.class);
            return planSafeguardVOHandler;
        }catch (Exception e){
            log.error("保存方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanSafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanSafeguardCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = PlanSafeguardCacheConstant.BASIC,key = "#planSafeguardVO.id")})
    public Boolean update(PlanSafeguardVO planSafeguardVO) {
        try {
            //转换PlanSafeguardVO为PlanSafeguard
            PlanSafeguard planSafeguard = BeanConv.toBean(planSafeguardVO, PlanSafeguard.class);
            boolean flag = updateById(planSafeguard);
            if (!flag){
                throw new RuntimeException("修改方案保障项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanSafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanSafeguardCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = PlanSafeguardCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除方案保障项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = PlanSafeguardCacheConstant.LIST,key ="#planSafeguardVO.hashCode()")
    public List<PlanSafeguardVO> findList(PlanSafeguardVO planSafeguardVO) {
        try {
            //构建查询条件
            QueryWrapper<PlanSafeguard> queryWrapper = queryWrapper(planSafeguardVO);
            //执行列表查询
            List<PlanSafeguardVO> planSafeguardVOs = BeanConv.toBeanList(list(queryWrapper),PlanSafeguardVO.class);
            return planSafeguardVOs;
        }catch (Exception e){
            log.error("方案保障项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.LIST_FAIL);
        }
    }

    @Override
    public boolean deleteInPlanIds(List<Long> planIds) {
        try {
            UpdateWrapper<PlanSafeguard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(PlanSafeguard::getPlanId,planIds);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.DEL_FAIL);
        }
    }

    @Override
    public boolean deleteInPlanId(List<Long> planIds) {
        try {
            UpdateWrapper<PlanSafeguard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(PlanSafeguard::getPlanId,planIds);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.DEL_FAIL);
        }
    }

    @Override
    public List<PlanSafeguardVO> findInPlanId(Set<Long> planIds) {
        try {
            QueryWrapper<PlanSafeguard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(PlanSafeguard::getPlanId,planIds);
            return BeanConv.toBeanList(list(queryWrapper),PlanSafeguardVO.class);
        }catch (Exception e){
            log.error("查询方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.LIST_FAIL);
        }
    }

    @Override
    public List<PlanSafeguardVO> findByPlanId(Long planId) {
        try {
            QueryWrapper<PlanSafeguard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PlanSafeguard::getPlanId,planId);
            return BeanConv.toBeanList(list(queryWrapper),PlanSafeguardVO.class);
        }catch (Exception e){
            log.error("查询方案保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanSafeguardEnum.LIST_FAIL);
        }
    }
}
