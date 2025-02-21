package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.Risk;
import com.itheima.bolee.insurance.mapper.RiskMapper;
import com.itheima.bolee.insurance.service.IRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.RiskCacheConstant;
import com.itheima.bolee.insurance.dto.RiskVO;
import com.itheima.bolee.insurance.enums.RiskEnum;
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
 * @Description：风险表服务实现类
 */
@Slf4j
@Service
public class RiskServiceImpl extends ServiceImpl<RiskMapper, Risk> implements IRiskService {


    /***
    * @description 风险表多条件组合
    * @param riskVO 风险表
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Risk> queryWrapper(RiskVO riskVO){
        QueryWrapper<Risk> queryWrapper = new QueryWrapper<>();
        //风险项类型key查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getRiskTypeKey())) {
            queryWrapper.lambda().eq(Risk::getRiskTypeKey,riskVO.getRiskTypeKey());
        }
        //风险项类型name查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getRiskTypeName())) {
            queryWrapper.lambda().eq(Risk::getRiskTypeName,riskVO.getRiskTypeName());
        }
        //风险项key查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getRiskKey())) {
            queryWrapper.lambda().eq(Risk::getRiskKey,riskVO.getRiskKey());
        }
        //风险项名称查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getRiskName())) {
            queryWrapper.lambda().eq(Risk::getRiskName,riskVO.getRiskName());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getSortNo())) {
            queryWrapper.lambda().eq(Risk::getSortNo,riskVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(riskVO.getDataState())) {
            queryWrapper.lambda().eq(Risk::getDataState,riskVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Risk::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = RiskCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#riskVO.hashCode()")
    public Page<RiskVO> findPage(RiskVO riskVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Risk> RiskPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Risk> queryWrapper = queryWrapper(riskVO);
            //执行分页查询
            Page<RiskVO> riskVOPage = BeanConv.toPage(
                page(RiskPage, queryWrapper), RiskVO.class);
            //返回结果
            return riskVOPage;
        }catch (Exception e){
            log.error("风险表分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = RiskCacheConstant.BASIC,key ="#riskId")
    public RiskVO findById(String riskId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(riskId),RiskVO.class);
        }catch (Exception e){
            log.error("风险表单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =RiskCacheConstant.BASIC,key = "#result.id")})
    public RiskVO save(RiskVO riskVO) {
        try {
            //转换RiskVO为Risk
            Risk risk = BeanConv.toBean(riskVO, Risk.class);
            boolean flag = save(risk);
            if (!flag){
                throw new RuntimeException("保存风险表失败");
            }
            //转换返回对象RiskVO
            RiskVO riskVOHandler = BeanConv.toBean(risk, RiskVO.class);
            return riskVOHandler;
        }catch (Exception e){
            log.error("保存风险表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = RiskCacheConstant.BASIC,key = "#riskVO.id")})
    public Boolean update(RiskVO riskVO) {
        try {
            //转换RiskVO为Risk
            Risk risk = BeanConv.toBean(riskVO, Risk.class);
            boolean flag = updateById(risk);
            if (!flag){
                throw new RuntimeException("修改风险表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改风险表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RiskCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = RiskCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = RiskCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除风险表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除风险表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = RiskCacheConstant.LIST,key ="#riskVO.hashCode()")
    public List<RiskVO> findList(RiskVO riskVO) {
        try {
            //构建查询条件
            QueryWrapper<Risk> queryWrapper = queryWrapper(riskVO);
            //执行列表查询
            List<RiskVO> riskVOs = BeanConv.toBeanList(list(queryWrapper),RiskVO.class);
            return riskVOs;
        }catch (Exception e){
            log.error("风险表列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RiskEnum.LIST_FAIL);
        }
    }
}
