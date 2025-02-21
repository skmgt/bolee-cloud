package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.RiskAnalysis;
import com.itheima.bolee.insurance.mapper.RiskAnalysisMapper;
import com.itheima.bolee.insurance.service.IRiskAnalysisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.RiskAnalysisCacheConstant;
import com.itheima.bolee.insurance.dto.RiskAnalysisVO;
import com.itheima.bolee.insurance.enums.RiskAnalysisEnum;
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
 * @Description：风险类目服务实现类
 */
@Slf4j
@Service
public class RiskAnalysisServiceImpl extends ServiceImpl<RiskAnalysisMapper, RiskAnalysis> implements IRiskAnalysisService {


    /***
    * @description 风险类目多条件组合
    * @param riskAnalysisVO 风险类目
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<RiskAnalysis> queryWrapper(RiskAnalysisVO riskAnalysisVO){
        QueryWrapper<RiskAnalysis> queryWrapper = new QueryWrapper<>();
        //保险产品id查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getInsuranceId())) {
            queryWrapper.lambda().eq(RiskAnalysis::getInsuranceId,riskAnalysisVO.getInsuranceId());
        }
        //风险项key查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getAssessmentKey())) {
            queryWrapper.lambda().eq(RiskAnalysis::getAssessmentKey,riskAnalysisVO.getAssessmentKey());
        }
        //风险项项名称查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getAssessmentKeyName())) {
            queryWrapper.lambda().eq(RiskAnalysis::getAssessmentKeyName,riskAnalysisVO.getAssessmentKeyName());
        }
        //风险项项值查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getAssessmentVal())) {
            queryWrapper.lambda().eq(RiskAnalysis::getAssessmentVal,riskAnalysisVO.getAssessmentVal());
        }
        //风险项类型: 0医疗风险 1重疾风险 2身故风险 3意外风险查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getAssessmentType())) {
            queryWrapper.lambda().eq(RiskAnalysis::getAssessmentType,riskAnalysisVO.getAssessmentType());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getSortNo())) {
            queryWrapper.lambda().eq(RiskAnalysis::getSortNo,riskAnalysisVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getRemake())) {
            queryWrapper.lambda().eq(RiskAnalysis::getRemake,riskAnalysisVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(riskAnalysisVO.getDataState())) {
            queryWrapper.lambda().eq(RiskAnalysis::getDataState,riskAnalysisVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(RiskAnalysis::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = RiskAnalysisCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#riskAnalysisVO.hashCode()")
    public Page<RiskAnalysisVO> findPage(RiskAnalysisVO riskAnalysisVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<RiskAnalysis> RiskAnalysisPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<RiskAnalysis> queryWrapper = queryWrapper(riskAnalysisVO);
            //执行分页查询
            Page<RiskAnalysisVO> riskAnalysisVOPage = BeanConv.toPage(
                page(RiskAnalysisPage, queryWrapper), RiskAnalysisVO.class);
            //返回结果
            return riskAnalysisVOPage;
        }catch (Exception e){
            log.error("风险类目分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = RiskAnalysisCacheConstant.BASIC,key ="#riskAnalysisId")
    public RiskAnalysisVO findById(String riskAnalysisId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(riskAnalysisId),RiskAnalysisVO.class);
        }catch (Exception e){
            log.error("风险类目单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskAnalysisCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskAnalysisCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =RiskAnalysisCacheConstant.BASIC,key = "#result.id")})
    public RiskAnalysisVO save(RiskAnalysisVO riskAnalysisVO) {
        try {
            //转换RiskAnalysisVO为RiskAnalysis
            RiskAnalysis riskAnalysis = BeanConv.toBean(riskAnalysisVO, RiskAnalysis.class);
            boolean flag = save(riskAnalysis);
            if (!flag){
                throw new RuntimeException("保存风险类目失败");
            }
            //转换返回对象RiskAnalysisVO
            RiskAnalysisVO riskAnalysisVOHandler = BeanConv.toBean(riskAnalysis, RiskAnalysisVO.class);
            return riskAnalysisVOHandler;
        }catch (Exception e){
            log.error("保存风险类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskAnalysisCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskAnalysisCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = RiskAnalysisCacheConstant.BASIC,key = "#riskAnalysisVO.id")})
    public Boolean update(RiskAnalysisVO riskAnalysisVO) {
        try {
            //转换RiskAnalysisVO为RiskAnalysis
            RiskAnalysis riskAnalysis = BeanConv.toBean(riskAnalysisVO, RiskAnalysis.class);
            boolean flag = updateById(riskAnalysis);
            if (!flag){
                throw new RuntimeException("修改风险类目失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改风险类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskAnalysisCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskAnalysisCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = RiskAnalysisCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除风险类目失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除风险类目异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = RiskAnalysisCacheConstant.LIST,key ="#riskAnalysisVO.hashCode()")
    public List<RiskAnalysisVO> findList(RiskAnalysisVO riskAnalysisVO) {
        try {
            //构建查询条件
            QueryWrapper<RiskAnalysis> queryWrapper = queryWrapper(riskAnalysisVO);
            //执行列表查询
            List<RiskAnalysisVO> riskAnalysisVOs = BeanConv.toBeanList(list(queryWrapper),RiskAnalysisVO.class);
            return riskAnalysisVOs;
        }catch (Exception e){
            log.error("风险类目列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskAnalysisEnum.LIST_FAIL);
        }
    }
}
