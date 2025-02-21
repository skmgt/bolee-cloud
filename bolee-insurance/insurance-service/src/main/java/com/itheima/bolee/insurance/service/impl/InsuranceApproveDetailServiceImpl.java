package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.InsuranceApproveDetail;
import com.itheima.bolee.insurance.mapper.InsuranceApproveDetailMapper;
import com.itheima.bolee.insurance.service.IInsuranceApproveDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.InsuranceApproveDetailCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceApproveDetailVO;
import com.itheima.bolee.insurance.enums.InsuranceApproveDetailEnum;
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
 * @Description：保批信息服务实现类
 */
@Slf4j
@Service
public class InsuranceApproveDetailServiceImpl extends ServiceImpl<InsuranceApproveDetailMapper, InsuranceApproveDetail> implements IInsuranceApproveDetailService {


    /***
    * @description 保批信息多条件组合
    * @param insuranceApproveDetailVO 保批信息
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceApproveDetail> queryWrapper(InsuranceApproveDetailVO insuranceApproveDetailVO){
        QueryWrapper<InsuranceApproveDetail> queryWrapper = new QueryWrapper<>();
        //报批id查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getApproveId())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getApproveId,insuranceApproveDetailVO.getApproveId());
        }
        //投保人：0  被保人：1查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getType())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getType,insuranceApproveDetailVO.getType());
        }
        //修改字段键查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getChangeFieldKey())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getChangeFieldKey,insuranceApproveDetailVO.getChangeFieldKey());
        }
        //修改字段名查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getTitle())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getTitle,insuranceApproveDetailVO.getTitle());
        }
        //修改前的内容查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getLabel())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getLabel,insuranceApproveDetailVO.getLabel());
        }
        //修改后的内容查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getModifyCont())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getModifyCont,insuranceApproveDetailVO.getModifyCont());
        }
        //原因查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getModifyReason())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getModifyReason,insuranceApproveDetailVO.getModifyReason());
        }
        //修改时间查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getApproveTime())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getApproveTime,insuranceApproveDetailVO.getApproveTime());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getSortNo())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getSortNo,insuranceApproveDetailVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveDetailVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceApproveDetail::getDataState,insuranceApproveDetailVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceApproveDetail::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceApproveDetailCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceApproveDetailVO.hashCode()")
    public Page<InsuranceApproveDetailVO> findPage(InsuranceApproveDetailVO insuranceApproveDetailVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceApproveDetail> InsuranceApproveDetailPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceApproveDetail> queryWrapper = queryWrapper(insuranceApproveDetailVO);
            //执行分页查询
            Page<InsuranceApproveDetailVO> insuranceApproveDetailVOPage = BeanConv.toPage(
                page(InsuranceApproveDetailPage, queryWrapper), InsuranceApproveDetailVO.class);
            //返回结果
            return insuranceApproveDetailVOPage;
        }catch (Exception e){
            log.error("保批信息分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceApproveDetailCacheConstant.BASIC,key ="#insuranceApproveDetailId")
    public InsuranceApproveDetailVO findById(String insuranceApproveDetailId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceApproveDetailId),InsuranceApproveDetailVO.class);
        }catch (Exception e){
            log.error("保批信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveDetailCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveDetailCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =InsuranceApproveDetailCacheConstant.BASIC,key = "#result.id")})
    public InsuranceApproveDetailVO save(InsuranceApproveDetailVO insuranceApproveDetailVO) {
        try {
            //转换InsuranceApproveDetailVO为InsuranceApproveDetail
            InsuranceApproveDetail insuranceApproveDetail = BeanConv.toBean(insuranceApproveDetailVO, InsuranceApproveDetail.class);
            boolean flag = save(insuranceApproveDetail);
            if (!flag){
                throw new RuntimeException("保存保批信息失败");
            }
            //转换返回对象InsuranceApproveDetailVO
            InsuranceApproveDetailVO insuranceApproveDetailVOHandler = BeanConv.toBean(insuranceApproveDetail, InsuranceApproveDetailVO.class);
            return insuranceApproveDetailVOHandler;
        }catch (Exception e){
            log.error("保存保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveDetailCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveDetailCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceApproveDetailCacheConstant.BASIC,key = "#insuranceApproveDetailVO.id")})
    public Boolean update(InsuranceApproveDetailVO insuranceApproveDetailVO) {
        try {
            //转换InsuranceApproveDetailVO为InsuranceApproveDetail
            InsuranceApproveDetail insuranceApproveDetail = BeanConv.toBean(insuranceApproveDetailVO, InsuranceApproveDetail.class);
            boolean flag = updateById(insuranceApproveDetail);
            if (!flag){
                throw new RuntimeException("修改保批信息失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveDetailCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveDetailCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceApproveDetailCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保批信息失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceApproveDetailCacheConstant.LIST,key ="#insuranceApproveDetailVO.hashCode()")
    public List<InsuranceApproveDetailVO> findList(InsuranceApproveDetailVO insuranceApproveDetailVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceApproveDetail> queryWrapper = queryWrapper(insuranceApproveDetailVO);
            //执行列表查询
            List<InsuranceApproveDetailVO> insuranceApproveDetailVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceApproveDetailVO.class);
            return insuranceApproveDetailVOs;
        }catch (Exception e){
            log.error("保批信息列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveDetailEnum.LIST_FAIL);
        }
    }
}
