package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.WarrantyInsured;
import com.itheima.bolee.insurance.mapper.WarrantyInsuredMapper;
import com.itheima.bolee.insurance.service.IWarrantyInsuredService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WarrantyInsuredCacheConstant;
import com.itheima.bolee.insurance.dto.WarrantyInsuredVO;
import com.itheima.bolee.insurance.enums.WarrantyInsuredEnum;
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
 * @Description：合同被保人服务实现类
 */
@Slf4j
@Service
public class WarrantyInsuredServiceImpl extends ServiceImpl<WarrantyInsuredMapper, WarrantyInsured> implements IWarrantyInsuredService {


    /***
    * @description 合同被保人多条件组合
    * @param warrantyInsuredVO 合同被保人
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WarrantyInsured> queryWrapper(WarrantyInsuredVO warrantyInsuredVO){
        QueryWrapper<WarrantyInsured> queryWrapper = new QueryWrapper<>();
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyInsuredVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(WarrantyInsured::getWarrantyNo,warrantyInsuredVO.getWarrantyNo());
        }
        //被投保人姓名查询
        if (!EmptyUtil.isNullOrEmpty(warrantyInsuredVO.getInsuredName())) {
            queryWrapper.lambda().eq(WarrantyInsured::getInsuredName,warrantyInsuredVO.getInsuredName());
        }
        //被投保人身份证号码查询
        if (!EmptyUtil.isNullOrEmpty(warrantyInsuredVO.getInsuredIdentityCard())) {
            queryWrapper.lambda().eq(WarrantyInsured::getInsuredIdentityCard,warrantyInsuredVO.getInsuredIdentityCard());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyInsuredVO.getDataState())) {
            queryWrapper.lambda().eq(WarrantyInsured::getDataState,warrantyInsuredVO.getDataState());
        }
        //批量查询保险合同
        if (!EmptyUtil.isNullOrEmpty(warrantyInsuredVO.getWarrantyNos())){
            queryWrapper.lambda().in(WarrantyInsured::getWarrantyNo,warrantyInsuredVO.getWarrantyNos());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WarrantyInsured::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyInsuredCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#warrantyInsuredVO.hashCode()")
    public Page<WarrantyInsuredVO> findPage(WarrantyInsuredVO warrantyInsuredVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WarrantyInsured> WarrantyInsuredPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WarrantyInsured> queryWrapper = queryWrapper(warrantyInsuredVO);
            //执行分页查询
            Page<WarrantyInsuredVO> warrantyInsuredVOPage = BeanConv.toPage(
                page(WarrantyInsuredPage, queryWrapper), WarrantyInsuredVO.class);
            //返回结果
            return warrantyInsuredVOPage;
        }catch (Exception e){
            log.error("合同被保人分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyInsuredCacheConstant.BASIC,key ="#warrantyInsuredId")
    public WarrantyInsuredVO findById(String warrantyInsuredId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(warrantyInsuredId),WarrantyInsuredVO.class);
        }catch (Exception e){
            log.error("合同被保人单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyInsuredCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyInsuredCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WarrantyInsuredCacheConstant.BASIC,key = "#result.id")})
    public WarrantyInsuredVO save(WarrantyInsuredVO warrantyInsuredVO) {
        try {
            //转换WarrantyInsuredVO为WarrantyInsured
            WarrantyInsured warrantyInsured = BeanConv.toBean(warrantyInsuredVO, WarrantyInsured.class);
            boolean flag = save(warrantyInsured);
            if (!flag){
                throw new RuntimeException("保存合同被保人失败");
            }
            //转换返回对象WarrantyInsuredVO
            WarrantyInsuredVO warrantyInsuredVOHandler = BeanConv.toBean(warrantyInsured, WarrantyInsuredVO.class);
            return warrantyInsuredVOHandler;
        }catch (Exception e){
            log.error("保存合同被保人异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyInsuredCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyInsuredCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyInsuredCacheConstant.BASIC,key = "#warrantyInsuredVO.id")})
    public Boolean update(WarrantyInsuredVO warrantyInsuredVO) {
        try {
            //转换WarrantyInsuredVO为WarrantyInsured
            WarrantyInsured warrantyInsured = BeanConv.toBean(warrantyInsuredVO, WarrantyInsured.class);
            boolean flag = updateById(warrantyInsured);
            if (!flag){
                throw new RuntimeException("修改合同被保人失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改合同被保人异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyInsuredCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyInsuredCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyInsuredCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除合同被保人失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除合同被保人异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyInsuredCacheConstant.LIST,key ="#warrantyInsuredVO.hashCode()")
    public List<WarrantyInsuredVO> findList(WarrantyInsuredVO warrantyInsuredVO) {
        try {
            //构建查询条件
            QueryWrapper<WarrantyInsured> queryWrapper = queryWrapper(warrantyInsuredVO);
            //执行列表查询
            List<WarrantyInsuredVO> warrantyInsuredVOs = BeanConv.toBeanList(list(queryWrapper),WarrantyInsuredVO.class);
            return warrantyInsuredVOs;
        }catch (Exception e){
            log.error("合同被保人列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyInsuredEnum.LIST_FAIL);
        }
    }
}
