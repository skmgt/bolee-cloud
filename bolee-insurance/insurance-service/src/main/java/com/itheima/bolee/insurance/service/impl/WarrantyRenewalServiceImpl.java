package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.WarrantyRenewal;
import com.itheima.bolee.insurance.mapper.WarrantyRenewalMapper;
import com.itheima.bolee.insurance.service.IWarrantyRenewalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WarrantyRenewalCacheConstant;
import com.itheima.bolee.insurance.dto.WarrantyRenewalVO;
import com.itheima.bolee.insurance.enums.WarrantyRenewalEnum;
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
 * @Description：合同续期服务实现类
 */
@Slf4j
@Service
public class WarrantyRenewalServiceImpl extends ServiceImpl<WarrantyRenewalMapper, WarrantyRenewal> implements IWarrantyRenewalService {


    /***
    * @description 合同续期多条件组合
    * @param warrantyRenewalVO 合同续期
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WarrantyRenewal> queryWrapper(WarrantyRenewalVO warrantyRenewalVO){
        QueryWrapper<WarrantyRenewal> queryWrapper = new QueryWrapper<>();
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getWarrantyNo,warrantyRenewalVO.getWarrantyNo());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getSortNo())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getSortNo,warrantyRenewalVO.getSortNo());
        }
        //保障起始时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getSafeguardStartTime())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getSafeguardStartTime,warrantyRenewalVO.getSafeguardStartTime());
        }
        //保障截止时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getSafeguardEndTime())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getSafeguardEndTime,warrantyRenewalVO.getSafeguardEndTime());
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getCompanyNo())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getCompanyNo,warrantyRenewalVO.getCompanyNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyRenewalVO.getDataState())) {
            queryWrapper.lambda().eq(WarrantyRenewal::getDataState,warrantyRenewalVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WarrantyRenewal::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyRenewalCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#warrantyRenewalVO.hashCode()")
    public Page<WarrantyRenewalVO> findPage(WarrantyRenewalVO warrantyRenewalVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WarrantyRenewal> WarrantyRenewalPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WarrantyRenewal> queryWrapper = queryWrapper(warrantyRenewalVO);
            //执行分页查询
            Page<WarrantyRenewalVO> warrantyRenewalVOPage = BeanConv.toPage(
                page(WarrantyRenewalPage, queryWrapper), WarrantyRenewalVO.class);
            //返回结果
            return warrantyRenewalVOPage;
        }catch (Exception e){
            log.error("合同续期分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyRenewalCacheConstant.BASIC,key ="#warrantyRenewalId")
    public WarrantyRenewalVO findById(String warrantyRenewalId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(warrantyRenewalId),WarrantyRenewalVO.class);
        }catch (Exception e){
            log.error("合同续期单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyRenewalCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyRenewalCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WarrantyRenewalCacheConstant.BASIC,key = "#result.id")})
    public WarrantyRenewalVO save(WarrantyRenewalVO warrantyRenewalVO) {
        try {
            //转换WarrantyRenewalVO为WarrantyRenewal
            WarrantyRenewal warrantyRenewal = BeanConv.toBean(warrantyRenewalVO, WarrantyRenewal.class);
            boolean flag = save(warrantyRenewal);
            if (!flag){
                throw new RuntimeException("保存合同续期失败");
            }
            //转换返回对象WarrantyRenewalVO
            WarrantyRenewalVO warrantyRenewalVOHandler = BeanConv.toBean(warrantyRenewal, WarrantyRenewalVO.class);
            return warrantyRenewalVOHandler;
        }catch (Exception e){
            log.error("保存合同续期异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyRenewalCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyRenewalCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyRenewalCacheConstant.BASIC,key = "#warrantyRenewalVO.id")})
    public Boolean update(WarrantyRenewalVO warrantyRenewalVO) {
        try {
            //转换WarrantyRenewalVO为WarrantyRenewal
            WarrantyRenewal warrantyRenewal = BeanConv.toBean(warrantyRenewalVO, WarrantyRenewal.class);
            boolean flag = updateById(warrantyRenewal);
            if (!flag){
                throw new RuntimeException("修改合同续期失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改合同续期异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyRenewalCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyRenewalCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyRenewalCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除合同续期失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除合同续期异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyRenewalCacheConstant.LIST,key ="#warrantyRenewalVO.hashCode()")
    public List<WarrantyRenewalVO> findList(WarrantyRenewalVO warrantyRenewalVO) {
        try {
            //构建查询条件
            QueryWrapper<WarrantyRenewal> queryWrapper = queryWrapper(warrantyRenewalVO);
            //执行列表查询
            List<WarrantyRenewalVO> warrantyRenewalVOs = BeanConv.toBeanList(list(queryWrapper),WarrantyRenewalVO.class);
            return warrantyRenewalVOs;
        }catch (Exception e){
            log.error("合同续期列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyRenewalEnum.LIST_FAIL);
        }
    }
}
