package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.dto.WorryFreeCustomerInfoVO;
import com.itheima.bolee.insurance.pojo.WorryFreeInsuranceMatch;
import com.itheima.bolee.insurance.mapper.WorryFreeInsuranceMatchMapper;
import com.itheima.bolee.insurance.service.IWorryFreeInsuranceMatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WorryFreeInsuranceMatchCacheConstant;
import com.itheima.bolee.insurance.dto.WorryFreeInsuranceMatchVO;
import com.itheima.bolee.insurance.enums.WorryFreeInsuranceMatchEnum;
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
 * @Description：省心配保险推荐记录服务实现类
 */
@Slf4j
@Service
public class WorryFreeInsuranceMatchServiceImpl extends ServiceImpl<WorryFreeInsuranceMatchMapper, WorryFreeInsuranceMatch> implements IWorryFreeInsuranceMatchService {


    /***
    * @description 省心配保险推荐记录多条件组合
    * @param worryFreeInsuranceMatchVO 省心配保险推荐记录
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WorryFreeInsuranceMatch> queryWrapper(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO){
        QueryWrapper<WorryFreeInsuranceMatch> queryWrapper = new QueryWrapper<>();
        //风险类型（意外：accident 医疗：medical 重疾：serious 身故：die 用户标签：person_title）查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getType())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getType,worryFreeInsuranceMatchVO.getType());
        }
        //保险json查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getInsuranceJson())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getInsuranceJson,worryFreeInsuranceMatchVO.getInsuranceJson());
        }
        //保险方案查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getInsurancePlan())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getInsurancePlan,worryFreeInsuranceMatchVO.getInsurancePlan());
        }
        //分配的保额查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getAmount())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getAmount,worryFreeInsuranceMatchVO.getAmount());
        }
        //解决方案查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getSolution())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getSolution,worryFreeInsuranceMatchVO.getSolution());
        }
        //方案建议查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getPlanSuggest())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getPlanSuggest,worryFreeInsuranceMatchVO.getPlanSuggest());
        }
        //用户id查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getCustomerId())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getCustomerId,worryFreeInsuranceMatchVO.getCustomerId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getSortNo())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getSortNo,worryFreeInsuranceMatchVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeInsuranceMatchVO.getDataState())) {
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getDataState,worryFreeInsuranceMatchVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WorryFreeInsuranceMatch::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WorryFreeInsuranceMatchCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#worryFreeInsuranceMatchVO.hashCode()")
    public Page<WorryFreeInsuranceMatchVO> findPage(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WorryFreeInsuranceMatch> WorryFreeInsuranceMatchPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WorryFreeInsuranceMatch> queryWrapper = queryWrapper(worryFreeInsuranceMatchVO);
            //执行分页查询
            Page<WorryFreeInsuranceMatchVO> worryFreeInsuranceMatchVOPage = BeanConv.toPage(
                page(WorryFreeInsuranceMatchPage, queryWrapper), WorryFreeInsuranceMatchVO.class);
            //返回结果
            return worryFreeInsuranceMatchVOPage;
        }catch (Exception e){
            log.error("省心配保险推荐记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeInsuranceMatchCacheConstant.BASIC,key ="#worryFreeInsuranceMatchId")
    public WorryFreeInsuranceMatchVO findById(String worryFreeInsuranceMatchId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(worryFreeInsuranceMatchId),WorryFreeInsuranceMatchVO.class);
        }catch (Exception e){
            log.error("省心配保险推荐记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WorryFreeInsuranceMatchCacheConstant.BASIC,key = "#result.id")})
    public WorryFreeInsuranceMatchVO save(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO) {
        try {
            //转换WorryFreeInsuranceMatchVO为WorryFreeInsuranceMatch
            WorryFreeInsuranceMatch worryFreeInsuranceMatch = BeanConv.toBean(worryFreeInsuranceMatchVO, WorryFreeInsuranceMatch.class);
            boolean flag = save(worryFreeInsuranceMatch);
            if (!flag){
                throw new RuntimeException("保存省心配保险推荐记录失败");
            }
            //转换返回对象WorryFreeInsuranceMatchVO
            WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVOHandler = BeanConv.toBean(worryFreeInsuranceMatch, WorryFreeInsuranceMatchVO.class);
            return worryFreeInsuranceMatchVOHandler;
        }catch (Exception e){
            log.error("保存省心配保险推荐记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.BASIC,key = "#worryFreeInsuranceMatchVO.id")})
    public Boolean update(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO) {
        try {
            //转换WorryFreeInsuranceMatchVO为WorryFreeInsuranceMatch
            WorryFreeInsuranceMatch worryFreeInsuranceMatch = BeanConv.toBean(worryFreeInsuranceMatchVO, WorryFreeInsuranceMatch.class);
            boolean flag = updateById(worryFreeInsuranceMatch);
            if (!flag){
                throw new RuntimeException("修改省心配保险推荐记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改省心配保险推荐记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeInsuranceMatchCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除省心配保险推荐记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除省心配保险推荐记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeInsuranceMatchCacheConstant.LIST,key ="#worryFreeInsuranceMatchVO.hashCode()")
    public List<WorryFreeInsuranceMatchVO> findList(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO) {
        try {
            //构建查询条件
            QueryWrapper<WorryFreeInsuranceMatch> queryWrapper = queryWrapper(worryFreeInsuranceMatchVO);
            //执行列表查询
            List<WorryFreeInsuranceMatchVO> worryFreeInsuranceMatchVOs = BeanConv.toBeanList(list(queryWrapper),WorryFreeInsuranceMatchVO.class);
            return worryFreeInsuranceMatchVOs;
        }catch (Exception e){
            log.error("省心配保险推荐记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.LIST_FAIL);
        }
    }

    /**
     * 构建查询条件
     * @param id
     * @return
     */
    @Override
    public List<WorryFreeInsuranceMatchVO> productList(Long id) {
        try{
            QueryWrapper<WorryFreeInsuranceMatch> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getCustomerId,id);
            List<WorryFreeInsuranceMatch> list = list(queryWrapper);
            return BeanConv.toBeanList(list,WorryFreeInsuranceMatchVO.class);
        }catch (Exception e){
            log.error("省心配保险推荐记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.LIST_FAIL);
        }
    }

    /**
     *
     * @param id
     */
    @Override
    public void cleanCustomerHistry(String id) {
        try{
            QueryWrapper<WorryFreeInsuranceMatch> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WorryFreeInsuranceMatch::getCustomerId,id);
            remove(queryWrapper);
        }catch (Exception e){
            log.error("省心配保险推荐记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeInsuranceMatchEnum.LIST_FAIL);
        }
    }
}
