package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.insurance.pojo.WorryFreeRiskItem;
import com.itheima.bolee.insurance.mapper.WorryFreeRiskItemMapper;
import com.itheima.bolee.insurance.service.IWorryFreeRiskItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WorryFreeRiskItemCacheConstant;
import com.itheima.bolee.insurance.dto.WorryFreeRiskItemVO;
import com.itheima.bolee.insurance.enums.WorryFreeRiskItemEnum;
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
 * @Description：服务实现类
 */
@Slf4j
@Service
public class WorryFreeRiskItemServiceImpl extends ServiceImpl<WorryFreeRiskItemMapper, WorryFreeRiskItem> implements IWorryFreeRiskItemService {


    /***
    * @description 多条件组合
    * @param worryFreeRiskItemVO 
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WorryFreeRiskItem> queryWrapper(WorryFreeRiskItemVO worryFreeRiskItemVO){
        QueryWrapper<WorryFreeRiskItem> queryWrapper = new QueryWrapper<>();
        //风险类型（意外：accident 医疗：medical 重疾：serious 身故：die 用户标签：person_title）查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeRiskItemVO.getType())) {
            queryWrapper.lambda().eq(WorryFreeRiskItem::getType,worryFreeRiskItemVO.getType());
        }
        //风险项名称列表 json查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeRiskItemVO.getNames())) {
            queryWrapper.lambda().eq(WorryFreeRiskItem::getNames,worryFreeRiskItemVO.getNames());
        }
        //用户id查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeRiskItemVO.getCustomerId())) {
            queryWrapper.lambda().eq(WorryFreeRiskItem::getCustomerId,worryFreeRiskItemVO.getCustomerId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeRiskItemVO.getSortNo())) {
            queryWrapper.lambda().eq(WorryFreeRiskItem::getSortNo,worryFreeRiskItemVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeRiskItemVO.getDataState())) {
            queryWrapper.lambda().eq(WorryFreeRiskItem::getDataState,worryFreeRiskItemVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WorryFreeRiskItem::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WorryFreeRiskItemCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#worryFreeRiskItemVO.hashCode()")
    public Page<WorryFreeRiskItemVO> findPage(WorryFreeRiskItemVO worryFreeRiskItemVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WorryFreeRiskItem> WorryFreeRiskItemPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WorryFreeRiskItem> queryWrapper = queryWrapper(worryFreeRiskItemVO);
            //执行分页查询
            Page<WorryFreeRiskItemVO> worryFreeRiskItemVOPage = BeanConv.toPage(
                page(WorryFreeRiskItemPage, queryWrapper), WorryFreeRiskItemVO.class);
            //返回结果
            return worryFreeRiskItemVOPage;
        }catch (Exception e){
            log.error("分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeRiskItemCacheConstant.BASIC,key ="#worryFreeRiskItemId")
    public WorryFreeRiskItemVO findById(String worryFreeRiskItemId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(worryFreeRiskItemId),WorryFreeRiskItemVO.class);
        }catch (Exception e){
            log.error("单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeRiskItemCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeRiskItemCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WorryFreeRiskItemCacheConstant.BASIC,key = "#result.id")})
    public WorryFreeRiskItemVO save(WorryFreeRiskItemVO worryFreeRiskItemVO) {
        try {
            //转换WorryFreeRiskItemVO为WorryFreeRiskItem
            WorryFreeRiskItem worryFreeRiskItem = BeanConv.toBean(worryFreeRiskItemVO, WorryFreeRiskItem.class);
            boolean flag = save(worryFreeRiskItem);
            if (!flag){
                throw new RuntimeException("保存失败");
            }
            //转换返回对象WorryFreeRiskItemVO
            WorryFreeRiskItemVO worryFreeRiskItemVOHandler = BeanConv.toBean(worryFreeRiskItem, WorryFreeRiskItemVO.class);
            return worryFreeRiskItemVOHandler;
        }catch (Exception e){
            log.error("保存异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeRiskItemCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeRiskItemCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeRiskItemCacheConstant.BASIC,key = "#worryFreeRiskItemVO.id")})
    public Boolean update(WorryFreeRiskItemVO worryFreeRiskItemVO) {
        try {
            //转换WorryFreeRiskItemVO为WorryFreeRiskItem
            WorryFreeRiskItem worryFreeRiskItem = BeanConv.toBean(worryFreeRiskItemVO, WorryFreeRiskItem.class);
            boolean flag = updateById(worryFreeRiskItem);
            if (!flag){
                throw new RuntimeException("修改失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeRiskItemCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeRiskItemCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeRiskItemCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeRiskItemCacheConstant.LIST,key ="#worryFreeRiskItemVO.hashCode()")
    public List<WorryFreeRiskItemVO> findList(WorryFreeRiskItemVO worryFreeRiskItemVO) {
        try {
            //构建查询条件
            QueryWrapper<WorryFreeRiskItem> queryWrapper = queryWrapper(worryFreeRiskItemVO);
            //执行列表查询
            List<WorryFreeRiskItemVO> worryFreeRiskItemVOs = BeanConv.toBeanList(list(queryWrapper),WorryFreeRiskItemVO.class);
            return worryFreeRiskItemVOs;
        }catch (Exception e){
            log.error("列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.LIST_FAIL);
        }
    }

    /**
     * 获取我的风险项
     * @param id
     * @return
     */
    @Override
    public List<WorryFreeRiskItemVO> findMyRiskItem(Long id) {
        try{
            QueryWrapper<WorryFreeRiskItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WorryFreeRiskItem::getCustomerId,id);
            queryWrapper.lambda().eq(WorryFreeRiskItem::getDataState, SuperConstant.DATA_STATE_0);
            List<WorryFreeRiskItemVO> res = BeanConv.toBeanList(list(queryWrapper),WorryFreeRiskItemVO.class);
            return res;
        }catch (Exception e){
            log.error("列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.LIST_FAIL);
        }
    }

    @Override
    public void cleanCustomerHistry(String id) {
        try{
            QueryWrapper<WorryFreeRiskItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WorryFreeRiskItem::getCustomerId,id);
            remove(queryWrapper);
        }catch (Exception e){
            log.error("列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeRiskItemEnum.LIST_FAIL);
        }
    }
}
