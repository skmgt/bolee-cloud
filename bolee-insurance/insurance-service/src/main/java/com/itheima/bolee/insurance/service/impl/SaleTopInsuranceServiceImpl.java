package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.SaleTopInsurance;
import com.itheima.bolee.insurance.mapper.SaleTopInsuranceMapper;
import com.itheima.bolee.insurance.service.ISaleTopInsuranceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.SaleTopInsuranceCacheConstant;
import com.itheima.bolee.insurance.dto.SaleTopInsuranceVO;
import com.itheima.bolee.insurance.enums.SaleTopInsuranceEnum;
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
 * @Description：服务实现类
 */
@Slf4j
@Service
public class SaleTopInsuranceServiceImpl extends ServiceImpl<SaleTopInsuranceMapper, SaleTopInsurance> implements ISaleTopInsuranceService {


    /***
    * @description 多条件组合
    * @param saleTopInsuranceVO 
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<SaleTopInsurance> queryWrapper(SaleTopInsuranceVO saleTopInsuranceVO){
        QueryWrapper<SaleTopInsurance> queryWrapper = new QueryWrapper<>();
        //保险类型查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getCheckRule())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getCheckRule,saleTopInsuranceVO.getCheckRule());
        }
        //保险id查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getInsuranceId())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getInsuranceId,saleTopInsuranceVO.getInsuranceId());
        }
        //保险名称查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getInsuranceName())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getInsuranceName,saleTopInsuranceVO.getInsuranceName());
        }
        //保险详情查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getInsuranceDetail())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getInsuranceDetail,saleTopInsuranceVO.getInsuranceDetail());
        }
        //产品点评查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getComment())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getComment,saleTopInsuranceVO.getComment());
        }
        //产品描述查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getRemake())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getRemake,saleTopInsuranceVO.getRemake());
        }
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getCategoryNo())) {
            String categoryStr = String.valueOf(saleTopInsuranceVO.getCategoryNo()).substring(0, 9);
            queryWrapper.lambda().likeRight(SaleTopInsurance::getCategoryNo,Long.valueOf(categoryStr));
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getCompanyNo())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getCompanyNo,saleTopInsuranceVO.getCompanyNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(saleTopInsuranceVO.getDataState())) {
            queryWrapper.lambda().eq(SaleTopInsurance::getDataState,saleTopInsuranceVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SaleTopInsurance::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SaleTopInsuranceCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#saleTopInsuranceVO.hashCode()")
    public Page<SaleTopInsuranceVO> findPage(SaleTopInsuranceVO saleTopInsuranceVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<SaleTopInsurance> SaleTopInsurancePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<SaleTopInsurance> queryWrapper = queryWrapper(saleTopInsuranceVO);
            //执行分页查询
            Page<SaleTopInsuranceVO> saleTopInsuranceVOPage = BeanConv.toPage(
                page(SaleTopInsurancePage, queryWrapper), SaleTopInsuranceVO.class);
            //返回结果
            return saleTopInsuranceVOPage;
        }catch (Exception e){
            log.error("分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SaleTopInsuranceCacheConstant.BASIC,key ="#saleTopInsuranceId")
    public SaleTopInsuranceVO findById(String saleTopInsuranceId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(saleTopInsuranceId),SaleTopInsuranceVO.class);
        }catch (Exception e){
            log.error("单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SaleTopInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SaleTopInsuranceCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =SaleTopInsuranceCacheConstant.BASIC,key = "#result.id")})
    public SaleTopInsuranceVO save(SaleTopInsuranceVO saleTopInsuranceVO) {
        try {
            //转换SaleTopInsuranceVO为SaleTopInsurance
            SaleTopInsurance saleTopInsurance = BeanConv.toBean(saleTopInsuranceVO, SaleTopInsurance.class);
            boolean flag = save(saleTopInsurance);
            if (!flag){
                throw new RuntimeException("保存失败");
            }
            //转换返回对象SaleTopInsuranceVO
            SaleTopInsuranceVO saleTopInsuranceVOHandler = BeanConv.toBean(saleTopInsurance, SaleTopInsuranceVO.class);
            return saleTopInsuranceVOHandler;
        }catch (Exception e){
            log.error("保存异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SaleTopInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SaleTopInsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SaleTopInsuranceCacheConstant.BASIC,key = "#saleTopInsuranceVO.id")})
    public Boolean update(SaleTopInsuranceVO saleTopInsuranceVO) {
        try {
            //转换SaleTopInsuranceVO为SaleTopInsurance
            SaleTopInsurance saleTopInsurance = BeanConv.toBean(saleTopInsuranceVO, SaleTopInsurance.class);
            boolean flag = updateById(saleTopInsurance);
            if (!flag){
                throw new RuntimeException("修改失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SaleTopInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SaleTopInsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SaleTopInsuranceCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = SaleTopInsuranceCacheConstant.LIST,key ="#saleTopInsuranceVO.hashCode()")
    public List<SaleTopInsuranceVO> findList(SaleTopInsuranceVO saleTopInsuranceVO) {
        try {
            //构建查询条件
            QueryWrapper<SaleTopInsurance> queryWrapper = queryWrapper(saleTopInsuranceVO);
            //执行列表查询
            List<SaleTopInsuranceVO> saleTopInsuranceVOs = BeanConv.toBeanList(list(queryWrapper),SaleTopInsuranceVO.class);
            return saleTopInsuranceVOs;
        }catch (Exception e){
            log.error("列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SaleTopInsuranceEnum.LIST_FAIL);
        }
    }
}