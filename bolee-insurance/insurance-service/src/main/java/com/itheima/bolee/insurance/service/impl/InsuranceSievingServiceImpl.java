package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.InsuranceSieving;
import com.itheima.bolee.insurance.mapper.InsuranceSievingMapper;
import com.itheima.bolee.insurance.service.IInsuranceSievingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.InsuranceSievingCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceSievingVO;
import com.itheima.bolee.insurance.enums.InsuranceSievingEnum;
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
 * @Description：初筛结果服务实现类
 */
@Slf4j
@Service
public class InsuranceSievingServiceImpl extends ServiceImpl<InsuranceSievingMapper, InsuranceSieving> implements IInsuranceSievingService {


    /***
    * @description 初筛结果多条件组合
    * @param insuranceSievingVO 初筛结果
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceSieving> queryWrapper(InsuranceSievingVO insuranceSievingVO){
        QueryWrapper<InsuranceSieving> queryWrapper = new QueryWrapper<>();
        //姓名查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getName())) {
            queryWrapper.lambda().eq(InsuranceSieving::getName,insuranceSievingVO.getName());
        }
        //疾病列表查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getSicksName())) {
            queryWrapper.lambda().eq(InsuranceSieving::getSicksName,insuranceSievingVO.getSicksName());
        }
        //疾病key列表查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getSicksKey())) {
            queryWrapper.lambda().eq(InsuranceSieving::getSicksKey,insuranceSievingVO.getSicksKey());
        }
        //初筛结果查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getResult())) {
            queryWrapper.lambda().eq(InsuranceSieving::getResult,insuranceSievingVO.getResult());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getSortNo())) {
            queryWrapper.lambda().eq(InsuranceSieving::getSortNo,insuranceSievingVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceSievingVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceSieving::getDataState,insuranceSievingVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceSieving::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceSievingCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceSievingVO.hashCode()")
    public Page<InsuranceSievingVO> findPage(InsuranceSievingVO insuranceSievingVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceSieving> InsuranceSievingPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceSieving> queryWrapper = queryWrapper(insuranceSievingVO);
            //执行分页查询
            Page<InsuranceSievingVO> insuranceSievingVOPage = BeanConv.toPage(
                page(InsuranceSievingPage, queryWrapper), InsuranceSievingVO.class);
            //返回结果
            return insuranceSievingVOPage;
        }catch (Exception e){
            log.error("初筛结果分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceSievingCacheConstant.BASIC,key ="#insuranceSievingId")
    public InsuranceSievingVO findById(String insuranceSievingId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceSievingId),InsuranceSievingVO.class);
        }catch (Exception e){
            log.error("初筛结果单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceSievingCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceSievingCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =InsuranceSievingCacheConstant.BASIC,key = "#result.id")})
    public InsuranceSievingVO save(InsuranceSievingVO insuranceSievingVO) {
        try {
            //转换InsuranceSievingVO为InsuranceSieving
            InsuranceSieving insuranceSieving = BeanConv.toBean(insuranceSievingVO, InsuranceSieving.class);
            boolean flag = save(insuranceSieving);
            if (!flag){
                throw new RuntimeException("保存初筛结果失败");
            }
            //转换返回对象InsuranceSievingVO
            InsuranceSievingVO insuranceSievingVOHandler = BeanConv.toBean(insuranceSieving, InsuranceSievingVO.class);
            return insuranceSievingVOHandler;
        }catch (Exception e){
            log.error("保存初筛结果异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceSievingCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceSievingCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceSievingCacheConstant.BASIC,key = "#insuranceSievingVO.id")})
    public Boolean update(InsuranceSievingVO insuranceSievingVO) {
        try {
            //转换InsuranceSievingVO为InsuranceSieving
            InsuranceSieving insuranceSieving = BeanConv.toBean(insuranceSievingVO, InsuranceSieving.class);
            boolean flag = updateById(insuranceSieving);
            if (!flag){
                throw new RuntimeException("修改初筛结果失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改初筛结果异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceSievingCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceSievingCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceSievingCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除初筛结果失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除初筛结果异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceSievingCacheConstant.LIST,key ="#insuranceSievingVO.hashCode()")
    public List<InsuranceSievingVO> findList(InsuranceSievingVO insuranceSievingVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceSieving> queryWrapper = queryWrapper(insuranceSievingVO);
            //执行列表查询
            List<InsuranceSievingVO> insuranceSievingVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceSievingVO.class);
            return insuranceSievingVOs;
        }catch (Exception e){
            log.error("初筛结果列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceSievingEnum.LIST_FAIL);
        }
    }
}
