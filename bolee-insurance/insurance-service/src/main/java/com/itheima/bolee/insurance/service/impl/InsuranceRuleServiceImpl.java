package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.InsuranceRule;
import com.itheima.bolee.insurance.mapper.InsuranceRuleMapper;
import com.itheima.bolee.insurance.service.IInsuranceRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.InsuranceRuleCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceRuleVO;
import com.itheima.bolee.insurance.enums.InsuranceRuleEnum;
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
 * @Description：保险规则服务实现类
 */
@Slf4j
@Service
public class InsuranceRuleServiceImpl extends ServiceImpl<InsuranceRuleMapper, InsuranceRule> implements IInsuranceRuleService {


    /***
    * @description 保险规则多条件组合
    * @param insuranceRuleVO 保险规则
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceRule> queryWrapper(InsuranceRuleVO insuranceRuleVO){
        QueryWrapper<InsuranceRule> queryWrapper = new QueryWrapper<>();
        //规则ID查询
        if (!EmptyUtil.isNullOrEmpty(insuranceRuleVO.getRulesId())) {
            queryWrapper.lambda().eq(InsuranceRule::getRulesId,insuranceRuleVO.getRulesId());
        }
        //条例键名称查询
        if (!EmptyUtil.isNullOrEmpty(insuranceRuleVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsuranceRule::getInsuranceId,insuranceRuleVO.getInsuranceId());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceRuleVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceRule::getDataState,insuranceRuleVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceRule::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceRuleCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceRuleVO.hashCode()")
    public Page<InsuranceRuleVO> findPage(InsuranceRuleVO insuranceRuleVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceRule> InsuranceRulePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceRule> queryWrapper = queryWrapper(insuranceRuleVO);
            //执行分页查询
            Page<InsuranceRuleVO> insuranceRuleVOPage = BeanConv.toPage(
                page(InsuranceRulePage, queryWrapper), InsuranceRuleVO.class);
            //返回结果
            return insuranceRuleVOPage;
        }catch (Exception e){
            log.error("保险规则分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceRuleCacheConstant.BASIC,key ="#insuranceRuleId")
    public InsuranceRuleVO findById(String insuranceRuleId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceRuleId),InsuranceRuleVO.class);
        }catch (Exception e){
            log.error("保险规则单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceRuleCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceRuleCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =InsuranceRuleCacheConstant.BASIC,key = "#result.id")})
    public InsuranceRuleVO save(InsuranceRuleVO insuranceRuleVO) {
        try {
            //转换InsuranceRuleVO为InsuranceRule
            InsuranceRule insuranceRule = BeanConv.toBean(insuranceRuleVO, InsuranceRule.class);
            boolean flag = save(insuranceRule);
            if (!flag){
                throw new RuntimeException("保存保险规则失败");
            }
            //转换返回对象InsuranceRuleVO
            InsuranceRuleVO insuranceRuleVOHandler = BeanConv.toBean(insuranceRule, InsuranceRuleVO.class);
            return insuranceRuleVOHandler;
        }catch (Exception e){
            log.error("保存保险规则异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceRuleCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceRuleCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceRuleCacheConstant.BASIC,key = "#insuranceRuleVO.id")})
    public Boolean update(InsuranceRuleVO insuranceRuleVO) {
        try {
            //转换InsuranceRuleVO为InsuranceRule
            InsuranceRule insuranceRule = BeanConv.toBean(insuranceRuleVO, InsuranceRule.class);
            boolean flag = updateById(insuranceRule);
            if (!flag){
                throw new RuntimeException("修改保险规则失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改保险规则异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceRuleCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceRuleCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceRuleCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险规则失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险规则异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceRuleCacheConstant.LIST,key ="#insuranceRuleVO.hashCode()")
    public List<InsuranceRuleVO> findList(InsuranceRuleVO insuranceRuleVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceRule> queryWrapper = queryWrapper(insuranceRuleVO);
            //执行列表查询
            List<InsuranceRuleVO> insuranceRuleVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceRuleVO.class);
            return insuranceRuleVOs;
        }catch (Exception e){
            log.error("保险规则列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceRuleEnum.LIST_FAIL);
        }
    }
}
