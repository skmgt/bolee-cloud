package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.constant.InsuranceCacheConstant;
import com.itheima.bolee.insurance.constant.InsuranceCoefficentCacheConstant;
import com.itheima.bolee.insurance.dto.CoefficentVO;
import com.itheima.bolee.insurance.dto.InsuranceCoefficentVO;
import com.itheima.bolee.insurance.enums.InsuranceCoefficentEnum;
import com.itheima.bolee.insurance.mapper.InsuranceCoefficentMapper;
import com.itheima.bolee.insurance.pojo.InsuranceCoefficent;
import com.itheima.bolee.insurance.service.ICoefficentService;
import com.itheima.bolee.insurance.service.IInsuranceCoefficentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：保险系数项服务实现类
 */
@Slf4j
@Service
public class InsuranceCoefficentServiceImpl extends ServiceImpl<InsuranceCoefficentMapper, InsuranceCoefficent> implements IInsuranceCoefficentService {

    @Autowired
    private ICoefficentService coefficentService;

    /***
    * @description 保险系数项多条件组合
    * @param insuranceCoefficentVO 保险系数项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceCoefficent> queryWrapper(InsuranceCoefficentVO insuranceCoefficentVO){
        QueryWrapper<InsuranceCoefficent> queryWrapper = new QueryWrapper<>();
        //保险商品id查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getInsuranceId,insuranceCoefficentVO.getInsuranceId());
        }
        //保险商品ids批量查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getInstanceIds())) {
            queryWrapper.lambda().in(InsuranceCoefficent::getInsuranceId,insuranceCoefficentVO.getInstanceIds());
        }
        //系数维度key查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getCoefficentKey())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getCoefficentKey,insuranceCoefficentVO.getCoefficentKey());
        }
        //系数维度value查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getCoefficentValue())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getCoefficentValue,insuranceCoefficentVO.getCoefficentValue());
        }
        //系数维度数值查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getScore())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getScore,insuranceCoefficentVO.getScore());
        }
        //是否默认查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getIsDefault())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getIsDefault,insuranceCoefficentVO.getIsDefault());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getSortNo())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getSortNo,insuranceCoefficentVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceCoefficentVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceCoefficent::getDataState,insuranceCoefficentVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByAsc(InsuranceCoefficent::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceCoefficentCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceCoefficentVO.hashCode()")
    public Page<InsuranceCoefficentVO> findPage(InsuranceCoefficentVO insuranceCoefficentVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceCoefficent> InsuranceCoefficentPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceCoefficent> queryWrapper = queryWrapper(insuranceCoefficentVO);
            //执行分页查询
            Page<InsuranceCoefficentVO> insuranceCoefficentVOPage = BeanConv.toPage(
                page(InsuranceCoefficentPage, queryWrapper), InsuranceCoefficentVO.class);
            //返回结果
            return insuranceCoefficentVOPage;
        }catch (Exception e){
            log.error("保险系数项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceCoefficentCacheConstant.BASIC,key ="#insuranceCoefficentId")
    public InsuranceCoefficentVO findById(String insuranceCoefficentId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceCoefficentId),InsuranceCoefficentVO.class);
        }catch (Exception e){
            log.error("保险系数项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCoefficentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.PAGE,allEntries = true)})
    public Boolean save(List<InsuranceCoefficentVO> insuranceCoefficentVOs) {
        try {
            //转换InsuranceCoefficentVO为InsuranceCoefficent
            List<InsuranceCoefficent> insuranceCoefficents = BeanConv.toBeanList(insuranceCoefficentVOs, InsuranceCoefficent.class);
            boolean flag = saveBatch(insuranceCoefficents);
            if (!flag){
                throw new RuntimeException("保存保险系数项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("保存保险系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCoefficentCacheConstant.LIST,allEntries = true)})
    public Boolean update(List<InsuranceCoefficentVO> insuranceCoefficentVOs) {
        try {
            //删除所有的保险系数
            Boolean flagRemove = deleteByInsuranceId(insuranceCoefficentVOs.get(0).getInsuranceId());
            if(!flagRemove){
                throw new RuntimeException("保险系数项删除失败");
            }
            //转换InsuranceCoefficentVO为InsuranceCoefficent
            List<InsuranceCoefficent> insuranceCoefficents = BeanConv.toBeanList(insuranceCoefficentVOs, InsuranceCoefficent.class);
            boolean flag = saveBatch(insuranceCoefficents);
            if (!flag){
                throw new RuntimeException("修改保险系数项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改保险系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.UPDATE_FAIL);
        }
    }

    /**
     *
     * @param insuranceId
     * @return
     */
    private Boolean deleteByInsuranceId(Long insuranceId) {
        try {
            UpdateWrapper<InsuranceCoefficent> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(InsuranceCoefficent::getInsuranceId,insuranceId);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除保险系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.DEL_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCoefficentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCoefficentCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险系数项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceCoefficentCacheConstant.LIST,key ="#insuranceCoefficentVO.hashCode()")
    public List<InsuranceCoefficentVO> findList(InsuranceCoefficentVO insuranceCoefficentVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceCoefficent> queryWrapper = queryWrapper(insuranceCoefficentVO);
            //执行列表查询
            List<InsuranceCoefficentVO> insuranceCoefficentVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceCoefficentVO.class);
            return insuranceCoefficentVOs;
        }catch (Exception e){
            log.error("保险系数项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceCoefficentCacheConstant.LIST,key ="#insuranceCoefficentIds.hashCode()")
    public List<InsuranceCoefficentVO> findListByIdsAndInsuranceId(List<Long> insuranceCoefficentIds,Long insuranceId ) {
        try {
            //执行列表查询
            LambdaQueryWrapper<InsuranceCoefficent> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(InsuranceCoefficent::getId,insuranceCoefficentIds).eq(InsuranceCoefficent::getInsuranceId,insuranceId);
            return BeanConv.toBeanList(list(queryWrapper),InsuranceCoefficentVO.class);
        }catch (Exception e){
            log.error("保险系数项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceCoefficentEnum.LIST_FAIL);
        }
    }

}
