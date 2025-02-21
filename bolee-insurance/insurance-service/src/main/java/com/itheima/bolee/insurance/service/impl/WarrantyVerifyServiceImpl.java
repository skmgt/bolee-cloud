package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.WarrantyVerify;
import com.itheima.bolee.insurance.mapper.WarrantyVerifyMapper;
import com.itheima.bolee.insurance.service.IWarrantyVerifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WarrantyVerifyCacheConstant;
import com.itheima.bolee.insurance.dto.WarrantyVerifyVO;
import com.itheima.bolee.insurance.enums.WarrantyVerifyEnum;
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
 * @Description：合同核保服务实现类
 */
@Slf4j
@Service
public class WarrantyVerifyServiceImpl extends ServiceImpl<WarrantyVerifyMapper, WarrantyVerify> implements IWarrantyVerifyService {


    /***
    * @description 合同核保多条件组合
    * @param warrantyVerifyVO 合同核保
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WarrantyVerify> queryWrapper(WarrantyVerifyVO warrantyVerifyVO){
        QueryWrapper<WarrantyVerify> queryWrapper = new QueryWrapper<>();
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(WarrantyVerify::getWarrantyNo,warrantyVerifyVO.getWarrantyNo());
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getCompanyNo())) {
            queryWrapper.lambda().eq(WarrantyVerify::getCompanyNo,warrantyVerifyVO.getCompanyNo());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getSortNo())) {
            queryWrapper.lambda().eq(WarrantyVerify::getSortNo,warrantyVerifyVO.getSortNo());
        }
        //核保结果查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getVerifyCode())) {
            queryWrapper.lambda().eq(WarrantyVerify::getVerifyCode,warrantyVerifyVO.getVerifyCode());
        }
        //核保信息查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getVerifyMsg())) {
            queryWrapper.lambda().eq(WarrantyVerify::getVerifyMsg,warrantyVerifyVO.getVerifyMsg());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVerifyVO.getDataState())) {
            queryWrapper.lambda().eq(WarrantyVerify::getDataState,warrantyVerifyVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WarrantyVerify::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyVerifyCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#warrantyVerifyVO.hashCode()")
    public Page<WarrantyVerifyVO> findPage(WarrantyVerifyVO warrantyVerifyVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WarrantyVerify> WarrantyVerifyPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WarrantyVerify> queryWrapper = queryWrapper(warrantyVerifyVO);
            //执行分页查询
            Page<WarrantyVerifyVO> warrantyVerifyVOPage = BeanConv.toPage(
                page(WarrantyVerifyPage, queryWrapper), WarrantyVerifyVO.class);
            //返回结果
            return warrantyVerifyVOPage;
        }catch (Exception e){
            log.error("合同核保分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyVerifyCacheConstant.BASIC,key ="#warrantyVerifyId")
    public WarrantyVerifyVO findById(String warrantyVerifyId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(warrantyVerifyId),WarrantyVerifyVO.class);
        }catch (Exception e){
            log.error("合同核保单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyVerifyCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyVerifyCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WarrantyVerifyCacheConstant.BASIC,key = "#result.id")})
    public WarrantyVerifyVO save(WarrantyVerifyVO warrantyVerifyVO) {
        try {
            //转换WarrantyVerifyVO为WarrantyVerify
            WarrantyVerify warrantyVerify = BeanConv.toBean(warrantyVerifyVO, WarrantyVerify.class);
            boolean flag = save(warrantyVerify);
            if (!flag){
                throw new RuntimeException("保存合同核保失败");
            }
            //转换返回对象WarrantyVerifyVO
            WarrantyVerifyVO warrantyVerifyVOHandler = BeanConv.toBean(warrantyVerify, WarrantyVerifyVO.class);
            return warrantyVerifyVOHandler;
        }catch (Exception e){
            log.error("保存合同核保异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyVerifyCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyVerifyCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyVerifyCacheConstant.BASIC,key = "#warrantyVerifyVO.id")})
    public Boolean update(WarrantyVerifyVO warrantyVerifyVO) {
        try {
            //转换WarrantyVerifyVO为WarrantyVerify
            WarrantyVerify warrantyVerify = BeanConv.toBean(warrantyVerifyVO, WarrantyVerify.class);
            boolean flag = updateById(warrantyVerify);
            if (!flag){
                throw new RuntimeException("修改合同核保失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改合同核保异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyVerifyCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyVerifyCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyVerifyCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除合同核保失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除合同核保异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyVerifyCacheConstant.LIST,key ="#warrantyVerifyVO.hashCode()")
    public List<WarrantyVerifyVO> findList(WarrantyVerifyVO warrantyVerifyVO) {
        try {
            //构建查询条件
            QueryWrapper<WarrantyVerify> queryWrapper = queryWrapper(warrantyVerifyVO);
            //执行列表查询
            List<WarrantyVerifyVO> warrantyVerifyVOs = BeanConv.toBeanList(list(queryWrapper),WarrantyVerifyVO.class);
            return warrantyVerifyVOs;
        }catch (Exception e){
            log.error("合同核保列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyVerifyEnum.LIST_FAIL);
        }
    }
}
