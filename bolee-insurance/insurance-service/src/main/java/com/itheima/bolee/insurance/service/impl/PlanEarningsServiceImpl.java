package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.bolee.insurance.pojo.PlanEarnings;
import com.itheima.bolee.insurance.mapper.PlanEarningsMapper;
import com.itheima.bolee.insurance.service.IPlanEarningsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.PlanEarningsCacheConstant;
import com.itheima.bolee.insurance.dto.PlanEarningsVO;
import com.itheima.bolee.insurance.enums.PlanEarningsEnum;
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
 * @Description：方案给付服务实现类
 */
@Slf4j
@Service
public class PlanEarningsServiceImpl extends ServiceImpl<PlanEarningsMapper, PlanEarnings> implements IPlanEarningsService {


    /***
    * @description 方案给付多条件组合
    * @param planEarningsVO 方案给付
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<PlanEarnings> queryWrapper(PlanEarningsVO planEarningsVO){
        QueryWrapper<PlanEarnings> queryWrapper = new QueryWrapper<>();
        //保险方案id查询
        if (!EmptyUtil.isNullOrEmpty(planEarningsVO.getPalnId())) {
            queryWrapper.lambda().eq(PlanEarnings::getPalnId,planEarningsVO.getPalnId());
        }
        //给付类型:0终身领取 1固定领取查询
        if (!EmptyUtil.isNullOrEmpty(planEarningsVO.getEarningsType())) {
            queryWrapper.lambda().eq(PlanEarnings::getEarningsType,planEarningsVO.getEarningsType());
        }
        //周期数查询
        if (!EmptyUtil.isNullOrEmpty(planEarningsVO.getPeriods())) {
            queryWrapper.lambda().eq(PlanEarnings::getPeriods,planEarningsVO.getPeriods());
        }
        //领取计划查询
        if (!EmptyUtil.isNullOrEmpty(planEarningsVO.getEarningsJson())) {
            queryWrapper.lambda().eq(PlanEarnings::getEarningsJson,planEarningsVO.getEarningsJson());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(planEarningsVO.getDataState())) {
            queryWrapper.lambda().eq(PlanEarnings::getDataState,planEarningsVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(PlanEarnings::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = PlanEarningsCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#planEarningsVO.hashCode()")
    public Page<PlanEarningsVO> findPage(PlanEarningsVO planEarningsVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<PlanEarnings> PlanEarningsPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<PlanEarnings> queryWrapper = queryWrapper(planEarningsVO);
            //执行分页查询
            Page<PlanEarningsVO> planEarningsVOPage = BeanConv.toPage(
                page(PlanEarningsPage, queryWrapper), PlanEarningsVO.class);
            //返回结果
            return planEarningsVOPage;
        }catch (Exception e){
            log.error("方案给付分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = PlanEarningsCacheConstant.BASIC,key ="#planEarningsId")
    public PlanEarningsVO findById(String planEarningsId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(planEarningsId),PlanEarningsVO.class);
        }catch (Exception e){
            log.error("方案给付单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanEarningsCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanEarningsCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =PlanEarningsCacheConstant.BASIC,key = "#result.id")})
    public PlanEarningsVO save(PlanEarningsVO planEarningsVO) {
        try {
            //转换PlanEarningsVO为PlanEarnings
            PlanEarnings planEarnings = BeanConv.toBean(planEarningsVO, PlanEarnings.class);
            boolean flag = save(planEarnings);
            if (!flag){
                throw new RuntimeException("保存方案给付失败");
            }
            //转换返回对象PlanEarningsVO
            PlanEarningsVO planEarningsVOHandler = BeanConv.toBean(planEarnings, PlanEarningsVO.class);
            return planEarningsVOHandler;
        }catch (Exception e){
            log.error("保存方案给付异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanEarningsCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanEarningsCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = PlanEarningsCacheConstant.BASIC,key = "#planEarningsVO.id")})
    public Boolean update(PlanEarningsVO planEarningsVO) {
        try {
            //转换PlanEarningsVO为PlanEarnings
            PlanEarnings planEarnings = BeanConv.toBean(planEarningsVO, PlanEarnings.class);
            boolean flag = updateById(planEarnings);
            if (!flag){
                throw new RuntimeException("修改方案给付失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改方案给付异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PlanEarningsCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = PlanEarningsCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = PlanEarningsCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除方案给付失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除方案给付异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = PlanEarningsCacheConstant.LIST,key ="#planEarningsVO.hashCode()")
    public List<PlanEarningsVO> findList(PlanEarningsVO planEarningsVO) {
        try {
            //构建查询条件
            QueryWrapper<PlanEarnings> queryWrapper = queryWrapper(planEarningsVO);
            //执行列表查询
            List<PlanEarningsVO> planEarningsVOs = BeanConv.toBeanList(list(queryWrapper),PlanEarningsVO.class);
            return planEarningsVOs;
        }catch (Exception e){
            log.error("方案给付列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = PlanEarningsCacheConstant.BASIC,key ="#planId")
    public PlanEarningsVO findByPlanId(Long planId) {
        try {
            //执行查询
            QueryWrapper<PlanEarnings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PlanEarnings::getPalnId,planId);
            return BeanConv.toBean(getOne(queryWrapper),PlanEarningsVO.class);
        }catch (Exception e){
            log.error("方案给付单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    public boolean deleteInPlanIds(List<Long> planIds) {
        try {
            //执行查询
            UpdateWrapper<PlanEarnings> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(PlanEarnings::getPalnId,planIds);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("方案给付单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.DEL_FAIL);
        }
    }

    @Override
    public boolean deleteInPlanId(List<Long> planIds) {
        try {
            //执行查询
            UpdateWrapper<PlanEarnings> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(PlanEarnings::getPalnId,planIds);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("方案给付单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.DEL_FAIL);
        }
    }

    @Override
    public List<PlanEarningsVO> findInPlanId(Set<Long> planIds) {
        try {
            //执行查询
            QueryWrapper<PlanEarnings> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(PlanEarnings::getPalnId,planIds);
            return BeanConv.toBeanList(list(queryWrapper),PlanEarningsVO.class);
        }catch (Exception e){
            log.error("方案给付单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlanEarningsEnum.LIST_FAIL);
        }
    }
}
