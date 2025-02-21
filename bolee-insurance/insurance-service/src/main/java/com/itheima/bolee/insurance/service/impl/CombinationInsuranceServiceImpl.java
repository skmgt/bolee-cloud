package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.itheima.bolee.insurance.dto.InsuranceCoefficentVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.pojo.CombinationInsurance;
import com.itheima.bolee.insurance.mapper.CombinationInsuranceMapper;
import com.itheima.bolee.insurance.pojo.Insurance;
import com.itheima.bolee.insurance.service.ICombinationInsuranceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.insurance.service.IInsuranceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.CombinationInsuranceCacheConstant;
import com.itheima.bolee.insurance.dto.CombinationInsuranceVO;
import com.itheima.bolee.insurance.enums.CombinationInsuranceEnum;
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
 * @Description：组合保险服务实现类
 */
@Slf4j
@Service
public class CombinationInsuranceServiceImpl extends ServiceImpl<CombinationInsuranceMapper, CombinationInsurance> implements ICombinationInsuranceService {

    @Autowired
    private IInsuranceService insuranceService;


    /***
    * @description 组合保险多条件组合
    * @param combinationInsuranceVO 组合保险
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<CombinationInsurance> queryWrapper(CombinationInsuranceVO combinationInsuranceVO){
        QueryWrapper<CombinationInsurance> queryWrapper = new QueryWrapper<>();
        //保险组合ID查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getCombinationId())) {
            queryWrapper.lambda().eq(CombinationInsurance::getCombinationId,combinationInsuranceVO.getCombinationId());
        }
        //保险ID查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getInsuranceId())) {
            queryWrapper.lambda().eq(CombinationInsurance::getInsuranceId,combinationInsuranceVO.getInsuranceId());
        }
        //建议查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getSuggest())) {
            queryWrapper.lambda().eq(CombinationInsurance::getSuggest,combinationInsuranceVO.getSuggest());
        }
        //配置思路查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getThinking())) {
            queryWrapper.lambda().eq(CombinationInsurance::getThinking,combinationInsuranceVO.getThinking());
        }
        //优先级查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getPriority())) {
            queryWrapper.lambda().eq(CombinationInsurance::getPriority,combinationInsuranceVO.getPriority());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(combinationInsuranceVO.getDataState())) {
            queryWrapper.lambda().eq(CombinationInsurance::getDataState,combinationInsuranceVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CombinationInsurance::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CombinationInsuranceCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#combinationInsuranceVO.hashCode()")
    public Page<CombinationInsuranceVO> findPage(CombinationInsuranceVO combinationInsuranceVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CombinationInsurance> CombinationInsurancePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<CombinationInsurance> queryWrapper = queryWrapper(combinationInsuranceVO);
            //执行分页查询
            Page<CombinationInsuranceVO> combinationInsuranceVOPage = BeanConv.toPage(
                page(CombinationInsurancePage, queryWrapper), CombinationInsuranceVO.class);
            //返回结果
            buildResult(combinationInsuranceVOPage.getRecords());
            return combinationInsuranceVOPage;
        }catch (Exception e){
            log.error("组合保险分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.PAGE_FAIL);
        }
    }

    /**
     * 构建保险方案结果集
     * @param combinationInsuranceVOs
     */
    private void buildResult(List<CombinationInsuranceVO> combinationInsuranceVOs) {
        if(CollectionUtil.isNotEmpty(combinationInsuranceVOs)){
            List<Long> instanceId = combinationInsuranceVOs.
                    stream().
                    map(CombinationInsuranceVO::getInsuranceId).
                    collect(Collectors.toList());
            InsuranceVO insuranceVO = InsuranceVO.builder().checkedIds(instanceId.toArray(String[]::new)).build();
            List<InsuranceVO> insuranceVOs = insuranceService.findList(insuranceVO);
            // 封装保险系数项到InsuranceVO的coefficents属性
            combinationInsuranceVOs.forEach(combinationHandler -> {
                //过滤出同一个保险id下的数据
                List<InsuranceVO> insuranceVOsResult = insuranceVOs.stream()
                        .filter(index -> index.getId().equals(combinationHandler.getInsuranceId()))
                        .collect(Collectors.toList());
                combinationHandler.setInsurances(insuranceVOsResult);
            });
        }
        //如果为空直接丢弃
    }

    @Override
    @Cacheable(value = CombinationInsuranceCacheConstant.BASIC,key ="#combinationInsuranceId")
    public CombinationInsuranceVO findById(String combinationInsuranceId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(combinationInsuranceId),CombinationInsuranceVO.class);
        }catch (Exception e){
            log.error("组合保险单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CombinationInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationInsuranceCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CombinationInsuranceCacheConstant.BASIC,key = "#result.id")})
    public CombinationInsuranceVO save(CombinationInsuranceVO combinationInsuranceVO) {
        try {
            //转换CombinationInsuranceVO为CombinationInsurance
            CombinationInsurance combinationInsurance = BeanConv.toBean(combinationInsuranceVO, CombinationInsurance.class);
            boolean flag = save(combinationInsurance);
            if (!flag){
                throw new RuntimeException("保存组合保险失败");
            }
            //转换返回对象CombinationInsuranceVO
            CombinationInsuranceVO combinationInsuranceVOHandler = BeanConv.toBean(combinationInsurance, CombinationInsuranceVO.class);
            return combinationInsuranceVOHandler;
        }catch (Exception e){
            log.error("保存组合保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CombinationInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationInsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CombinationInsuranceCacheConstant.BASIC,key = "#combinationInsuranceVO.id")})
    public Boolean update(CombinationInsuranceVO combinationInsuranceVO) {
        try {
            //转换CombinationInsuranceVO为CombinationInsurance
            CombinationInsurance combinationInsurance = BeanConv.toBean(combinationInsuranceVO, CombinationInsurance.class);
            boolean flag = updateById(combinationInsurance);
            if (!flag){
                throw new RuntimeException("修改组合保险失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改组合保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CombinationInsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationInsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CombinationInsuranceCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除组合保险失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除组合保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CombinationInsuranceCacheConstant.LIST,key ="#combinationInsuranceVO.hashCode()")
    public List<CombinationInsuranceVO> findList(CombinationInsuranceVO combinationInsuranceVO) {
        try {
            //构建查询条件
            QueryWrapper<CombinationInsurance> queryWrapper = queryWrapper(combinationInsuranceVO);
            //执行列表查询
            List<CombinationInsuranceVO> combinationInsuranceVOs = BeanConv.toBeanList(list(queryWrapper),CombinationInsuranceVO.class);
            return combinationInsuranceVOs;
        }catch (Exception e){
            log.error("组合保险列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationInsuranceEnum.LIST_FAIL);
        }
    }
}
