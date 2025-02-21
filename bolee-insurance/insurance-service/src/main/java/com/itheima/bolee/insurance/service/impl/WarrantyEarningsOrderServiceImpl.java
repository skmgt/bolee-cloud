package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.WarrantyEarningsOrder;
import com.itheima.bolee.insurance.mapper.WarrantyEarningsOrderMapper;
import com.itheima.bolee.insurance.service.IWarrantyEarningsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WarrantyEarningsOrderCacheConstant;
import com.itheima.bolee.insurance.dto.WarrantyEarningsOrderVO;
import com.itheima.bolee.insurance.enums.WarrantyEarningsOrderEnum;
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
 * @Description：给付计划订单服务实现类
 */
@Slf4j
@Service
public class WarrantyEarningsOrderServiceImpl extends ServiceImpl<WarrantyEarningsOrderMapper, WarrantyEarningsOrder> implements IWarrantyEarningsOrderService {


    /***
    * @description 给付计划订单多条件组合
    * @param warrantyEarningsOrderVO 给付计划订单
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WarrantyEarningsOrder> queryWrapper(WarrantyEarningsOrderVO warrantyEarningsOrderVO){
        QueryWrapper<WarrantyEarningsOrder> queryWrapper = new QueryWrapper<>();
        //订单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getOrderNo())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getOrderNo,warrantyEarningsOrderVO.getOrderNo());
        }
        //当期赔付查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getPremium())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getPremium,warrantyEarningsOrderVO.getPremium());
        }
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getWarrantyNo,warrantyEarningsOrderVO.getWarrantyNo());
        }
        //当前期数查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getCurrentPeriod())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getCurrentPeriod,warrantyEarningsOrderVO.getCurrentPeriod());
        }
        //计划时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getScheduleTime())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getScheduleTime,warrantyEarningsOrderVO.getScheduleTime());
        }
        //实际时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getActualTime())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getActualTime,warrantyEarningsOrderVO.getActualTime());
        }
        //状态（0未赔付 1已赔付 2 断保中止 3断保终止）查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getOrderState())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getOrderState,warrantyEarningsOrderVO.getOrderState());
        }
        //投保人姓名查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getApplicantName())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getApplicantName,warrantyEarningsOrderVO.getApplicantName());
        }
        //投保人身份证号码查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getApplicantIdentityCard())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getApplicantIdentityCard,warrantyEarningsOrderVO.getApplicantIdentityCard());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getSortNo())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getSortNo,warrantyEarningsOrderVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyEarningsOrderVO.getDataState())) {
            queryWrapper.lambda().eq(WarrantyEarningsOrder::getDataState,warrantyEarningsOrderVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WarrantyEarningsOrder::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyEarningsOrderCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#warrantyEarningsOrderVO.hashCode()")
    public Page<WarrantyEarningsOrderVO> findPage(WarrantyEarningsOrderVO warrantyEarningsOrderVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WarrantyEarningsOrder> WarrantyEarningsOrderPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WarrantyEarningsOrder> queryWrapper = queryWrapper(warrantyEarningsOrderVO);
            //执行分页查询
            Page<WarrantyEarningsOrderVO> warrantyEarningsOrderVOPage = BeanConv.toPage(
                page(WarrantyEarningsOrderPage, queryWrapper), WarrantyEarningsOrderVO.class);
            //返回结果
            return warrantyEarningsOrderVOPage;
        }catch (Exception e){
            log.error("给付计划订单分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyEarningsOrderCacheConstant.BASIC,key ="#warrantyEarningsOrderId")
    public WarrantyEarningsOrderVO findById(String warrantyEarningsOrderId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(warrantyEarningsOrderId),WarrantyEarningsOrderVO.class);
        }catch (Exception e){
            log.error("给付计划订单单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyEarningsOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyEarningsOrderCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WarrantyEarningsOrderCacheConstant.BASIC,key = "#result.id")})
    public WarrantyEarningsOrderVO save(WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        try {
            //转换WarrantyEarningsOrderVO为WarrantyEarningsOrder
            WarrantyEarningsOrder warrantyEarningsOrder = BeanConv.toBean(warrantyEarningsOrderVO, WarrantyEarningsOrder.class);
            boolean flag = save(warrantyEarningsOrder);
            if (!flag){
                throw new RuntimeException("保存给付计划订单失败");
            }
            //转换返回对象WarrantyEarningsOrderVO
            WarrantyEarningsOrderVO warrantyEarningsOrderVOHandler = BeanConv.toBean(warrantyEarningsOrder, WarrantyEarningsOrderVO.class);
            return warrantyEarningsOrderVOHandler;
        }catch (Exception e){
            log.error("保存给付计划订单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyEarningsOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyEarningsOrderCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyEarningsOrderCacheConstant.BASIC,key = "#warrantyEarningsOrderVO.id")})
    public Boolean update(WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        try {
            //转换WarrantyEarningsOrderVO为WarrantyEarningsOrder
            WarrantyEarningsOrder warrantyEarningsOrder = BeanConv.toBean(warrantyEarningsOrderVO, WarrantyEarningsOrder.class);
            boolean flag = updateById(warrantyEarningsOrder);
            if (!flag){
                throw new RuntimeException("修改给付计划订单失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改给付计划订单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyEarningsOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyEarningsOrderCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyEarningsOrderCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除给付计划订单失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除给付计划订单异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyEarningsOrderCacheConstant.LIST,key ="#warrantyEarningsOrderVO.hashCode()")
    public List<WarrantyEarningsOrderVO> findList(WarrantyEarningsOrderVO warrantyEarningsOrderVO) {
        try {
            //构建查询条件
            QueryWrapper<WarrantyEarningsOrder> queryWrapper = queryWrapper(warrantyEarningsOrderVO);
            //执行列表查询
            List<WarrantyEarningsOrderVO> warrantyEarningsOrderVOs = BeanConv.toBeanList(list(queryWrapper),WarrantyEarningsOrderVO.class);
            return warrantyEarningsOrderVOs;
        }catch (Exception e){
            log.error("给付计划订单列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEarningsOrderEnum.LIST_FAIL);
        }
    }
}
