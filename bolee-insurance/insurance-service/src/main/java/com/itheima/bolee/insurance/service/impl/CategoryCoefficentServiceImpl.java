package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.bolee.insurance.enums.CategoryConditionEnum;
import com.itheima.bolee.insurance.pojo.CategoryCoefficent;
import com.itheima.bolee.insurance.mapper.CategoryCoefficentMapper;
import com.itheima.bolee.insurance.pojo.CategoryCondition;
import com.itheima.bolee.insurance.service.ICategoryCoefficentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.CategoryCoefficentCacheConstant;
import com.itheima.bolee.insurance.dto.CategoryCoefficentVO;
import com.itheima.bolee.insurance.enums.CategoryCoefficentEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：分类系数项服务实现类
 */
@Slf4j
@Service
public class CategoryCoefficentServiceImpl extends ServiceImpl<CategoryCoefficentMapper, CategoryCoefficent> implements ICategoryCoefficentService {


    /***
    * @description 分类系数项多条件组合
    * @param categoryCoefficentVO 分类系数项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<CategoryCoefficent> queryWrapper(CategoryCoefficentVO categoryCoefficentVO){
        QueryWrapper<CategoryCoefficent> queryWrapper = new QueryWrapper<>();
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(categoryCoefficentVO.getCategoryNo())) {
            queryWrapper.lambda().eq(CategoryCoefficent::getCategoryNo,categoryCoefficentVO.getCategoryNo());
        }
        //系数key查询
        if (!EmptyUtil.isNullOrEmpty(categoryCoefficentVO.getCoefficentKey())) {
            queryWrapper.lambda().eq(CategoryCoefficent::getCoefficentKey,categoryCoefficentVO.getCoefficentKey());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(categoryCoefficentVO.getSortNo())) {
            queryWrapper.lambda().eq(CategoryCoefficent::getSortNo,categoryCoefficentVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(categoryCoefficentVO.getRemake())) {
            queryWrapper.lambda().eq(CategoryCoefficent::getRemake,categoryCoefficentVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(categoryCoefficentVO.getDataState())) {
            queryWrapper.lambda().eq(CategoryCoefficent::getDataState,categoryCoefficentVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CategoryCoefficent::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CategoryCoefficentCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#categoryCoefficentVO.hashCode()")
    public Page<CategoryCoefficentVO> findPage(CategoryCoefficentVO categoryCoefficentVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CategoryCoefficent> CategoryCoefficentPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<CategoryCoefficent> queryWrapper = queryWrapper(categoryCoefficentVO);
            //执行分页查询
            Page<CategoryCoefficentVO> categoryCoefficentVOPage = BeanConv.toPage(
                page(CategoryCoefficentPage, queryWrapper), CategoryCoefficentVO.class);
            //返回结果
            return categoryCoefficentVOPage;
        }catch (Exception e){
            log.error("分类系数项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategoryCoefficentCacheConstant.BASIC,key ="#categoryCoefficentId")
    public CategoryCoefficentVO findById(String categoryCoefficentId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(categoryCoefficentId),CategoryCoefficentVO.class);
        }catch (Exception e){
            log.error("分类系数项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCoefficentCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CategoryCoefficentCacheConstant.BASIC,key = "#result.id")})
    public CategoryCoefficentVO save(CategoryCoefficentVO categoryCoefficentVO) {
        try {
            //转换CategoryCoefficentVO为CategoryCoefficent
            CategoryCoefficent categoryCoefficent = BeanConv.toBean(categoryCoefficentVO, CategoryCoefficent.class);
            boolean flag = save(categoryCoefficent);
            if (!flag){
                throw new RuntimeException("保存分类系数项失败");
            }
            //转换返回对象CategoryCoefficentVO
            CategoryCoefficentVO categoryCoefficentVOHandler = BeanConv.toBean(categoryCoefficent, CategoryCoefficentVO.class);
            return categoryCoefficentVOHandler;
        }catch (Exception e){
            log.error("保存分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCoefficentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryCoefficentCacheConstant.BASIC,key = "#categoryCoefficentVO.id")})
    public Boolean update(CategoryCoefficentVO categoryCoefficentVO) {
        try {
            //转换CategoryCoefficentVO为CategoryCoefficent
            CategoryCoefficent categoryCoefficent = BeanConv.toBean(categoryCoefficentVO, CategoryCoefficent.class);
            boolean flag = updateById(categoryCoefficent);
            if (!flag){
                throw new RuntimeException("修改分类系数项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCoefficentCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCoefficentCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryCoefficentCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除分类系数项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategoryCoefficentCacheConstant.LIST,key ="#categoryCoefficentVO.hashCode()")
    public List<CategoryCoefficentVO> findList(CategoryCoefficentVO categoryCoefficentVO) {
        try {
            //构建查询条件
            QueryWrapper<CategoryCoefficent> queryWrapper = queryWrapper(categoryCoefficentVO);
            //执行列表查询
            List<CategoryCoefficentVO> categoryCoefficentVOs = BeanConv.toBeanList(list(queryWrapper),CategoryCoefficentVO.class);
            return categoryCoefficentVOs;
        }catch (Exception e){
            log.error("分类系数项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNo(String categoryNo) {
        try {
            UpdateWrapper<CategoryCoefficent> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(CategoryCoefficent::getCategoryNo,categoryNo);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.DEL_FAIL);
        }
    }

    @Override
    public List<CategoryCoefficentVO> findListInCategoryNo(Set<String> categoryNoSet) {
        try {
            QueryWrapper<CategoryCoefficent> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(CategoryCoefficent::getCategoryNo,categoryNoSet);
            return BeanConv.toBeanList(list(queryWrapper),CategoryCoefficentVO.class);
        }catch (Exception e){
            log.error("删除分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNos(List<String> categoryNos) {
        try {
            UpdateWrapper<CategoryCoefficent> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(CategoryCoefficent::getCategoryNo,categoryNos);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类系数项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryCoefficentEnum.DEL_FAIL);
        }
    }
}
