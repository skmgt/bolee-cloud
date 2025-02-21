package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.insurance.pojo.WorryFreeSafeguardQuota;
import com.itheima.bolee.insurance.mapper.WorryFreeSafeguardQuotaMapper;
import com.itheima.bolee.insurance.service.IWorryFreeSafeguardQuotaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WorryFreeSafeguardQuotaCacheConstant;
import com.itheima.bolee.insurance.dto.WorryFreeSafeguardQuotaVO;
import com.itheima.bolee.insurance.enums.WorryFreeSafeguardQuotaEnum;
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
 * @Description：省心配流程保障配额记录服务实现类
 */
@Slf4j
@Service
public class WorryFreeSafeguardQuotaServiceImpl extends ServiceImpl<WorryFreeSafeguardQuotaMapper, WorryFreeSafeguardQuota> implements IWorryFreeSafeguardQuotaService {


    /***
     * @description 省心配流程保障配额记录多条件组合
     * @param worryFreeSafeguardQuotaVO 省心配流程保障配额记录
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<WorryFreeSafeguardQuota> queryWrapper(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO){
        QueryWrapper<WorryFreeSafeguardQuota> queryWrapper = new QueryWrapper<>();
        //保障项列表 json查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getSafeguards())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getSafeguards,worryFreeSafeguardQuotaVO.getSafeguards());
        }
        //医疗配额查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getMedicalAmount())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getMedicalAmount,worryFreeSafeguardQuotaVO.getMedicalAmount());
        }
        //意外配额查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getAccidentAmount())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getAccidentAmount,worryFreeSafeguardQuotaVO.getAccidentAmount());
        }
        //医疗配额查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getDieAmount())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getDieAmount,worryFreeSafeguardQuotaVO.getDieAmount());
        }
        //重疾配额查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getSeriousAmount())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getSeriousAmount,worryFreeSafeguardQuotaVO.getSeriousAmount());
        }
        //用户id查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getCustomerId())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getCustomerId,worryFreeSafeguardQuotaVO.getCustomerId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getSortNo())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getSortNo,worryFreeSafeguardQuotaVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeSafeguardQuotaVO.getDataState())) {
            queryWrapper.lambda().eq(WorryFreeSafeguardQuota::getDataState,worryFreeSafeguardQuotaVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WorryFreeSafeguardQuota::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WorryFreeSafeguardQuotaCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#worryFreeSafeguardQuotaVO.hashCode()")
    public Page<WorryFreeSafeguardQuotaVO> findPage(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WorryFreeSafeguardQuota> WorryFreeSafeguardQuotaPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WorryFreeSafeguardQuota> queryWrapper = queryWrapper(worryFreeSafeguardQuotaVO);
            //执行分页查询
            Page<WorryFreeSafeguardQuotaVO> worryFreeSafeguardQuotaVOPage = BeanConv.toPage(
                    page(WorryFreeSafeguardQuotaPage, queryWrapper), WorryFreeSafeguardQuotaVO.class);
            //返回结果
            return worryFreeSafeguardQuotaVOPage;
        }catch (Exception e){
            log.error("省心配流程保障配额记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeSafeguardQuotaCacheConstant.BASIC,key ="#worryFreeSafeguardQuotaId")
    public WorryFreeSafeguardQuotaVO findById(String worryFreeSafeguardQuotaId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(worryFreeSafeguardQuotaId),WorryFreeSafeguardQuotaVO.class);
        }catch (Exception e){
            log.error("省心配流程保障配额记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =WorryFreeSafeguardQuotaCacheConstant.BASIC,key = "#result.id")})
    public WorryFreeSafeguardQuotaVO save(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO) {
        try {
            //转换WorryFreeSafeguardQuotaVO为WorryFreeSafeguardQuota
            WorryFreeSafeguardQuota worryFreeSafeguardQuota = BeanConv.toBean(worryFreeSafeguardQuotaVO, WorryFreeSafeguardQuota.class);
            boolean flag = save(worryFreeSafeguardQuota);
            if (!flag){
                throw new RuntimeException("保存省心配流程保障配额记录失败");
            }
            //转换返回对象WorryFreeSafeguardQuotaVO
            WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVOHandler = BeanConv.toBean(worryFreeSafeguardQuota, WorryFreeSafeguardQuotaVO.class);
            return worryFreeSafeguardQuotaVOHandler;
        }catch (Exception e){
            log.error("保存省心配流程保障配额记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.BASIC,key = "#worryFreeSafeguardQuotaVO.id")})
    public Boolean update(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO) {
        try {
            //转换WorryFreeSafeguardQuotaVO为WorryFreeSafeguardQuota
            WorryFreeSafeguardQuota worryFreeSafeguardQuota = BeanConv.toBean(worryFreeSafeguardQuotaVO, WorryFreeSafeguardQuota.class);
            boolean flag = updateById(worryFreeSafeguardQuota);
            if (!flag){
                throw new RuntimeException("修改省心配流程保障配额记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改省心配流程保障配额记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = WorryFreeSafeguardQuotaCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除省心配流程保障配额记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除省心配流程保障配额记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeSafeguardQuotaCacheConstant.LIST,key ="#worryFreeSafeguardQuotaVO.hashCode()")
    public List<WorryFreeSafeguardQuotaVO> findList(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO) {
        try {
            //构建查询条件
            QueryWrapper<WorryFreeSafeguardQuota> queryWrapper = queryWrapper(worryFreeSafeguardQuotaVO);
            //执行列表查询
            List<WorryFreeSafeguardQuotaVO> worryFreeSafeguardQuotaVOs = BeanConv.toBeanList(list(queryWrapper),WorryFreeSafeguardQuotaVO.class);
            return worryFreeSafeguardQuotaVOs;
        }catch (Exception e){
            log.error("省心配流程保障配额记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.LIST_FAIL);
        }
    }

    /**
     * 获取保险配额
     * @param customerId
     * @return
     */
    @Override
    public WorryFreeSafeguardQuotaVO findSaferQuota(Long customerId) {
        try {
            WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO = WorryFreeSafeguardQuotaVO.builder()
                    .dataState(SuperConstant.DATA_STATE_0)
                    .customerId(customerId).build();
            //构建查询条件
            QueryWrapper<WorryFreeSafeguardQuota> queryWrapper = queryWrapper(worryFreeSafeguardQuotaVO);
            return BeanConv.toBean(getOne(queryWrapper), WorryFreeSafeguardQuotaVO.class);
        }catch (Exception e){
            log.error("省心配流程保障配额记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.LIST_FAIL);
        }
    }

    @Override
    public void cleanCustomerHistry(String id) {
        try {
            WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO = WorryFreeSafeguardQuotaVO.builder().customerId(Long.valueOf(id)).build();
            //构建查询条件
            QueryWrapper<WorryFreeSafeguardQuota> queryWrapper = queryWrapper(worryFreeSafeguardQuotaVO);
            remove(queryWrapper);
        }catch (Exception e){
            log.error("省心配流程保障配额记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeSafeguardQuotaEnum.LIST_FAIL);
        }
    }
}
