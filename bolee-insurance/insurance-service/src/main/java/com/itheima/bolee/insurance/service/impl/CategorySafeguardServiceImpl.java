package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.bolee.insurance.enums.CategoryCoefficentEnum;
import com.itheima.bolee.insurance.pojo.CategoryCoefficent;
import com.itheima.bolee.insurance.pojo.CategorySafeguard;
import com.itheima.bolee.insurance.mapper.CategorySafeguardMapper;
import com.itheima.bolee.insurance.service.ICategorySafeguardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.CategorySafeguardCacheConstant;
import com.itheima.bolee.insurance.dto.CategorySafeguardVO;
import com.itheima.bolee.insurance.enums.CategorySafeguardEnum;
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
 * @Description：分类保障项服务实现类
 */
@Slf4j
@Service
public class CategorySafeguardServiceImpl extends ServiceImpl<CategorySafeguardMapper, CategorySafeguard> implements ICategorySafeguardService {


    /***
    * @description 分类保障项多条件组合
    * @param categorySafeguardVO 分类保障项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<CategorySafeguard> queryWrapper(CategorySafeguardVO categorySafeguardVO){
        QueryWrapper<CategorySafeguard> queryWrapper = new QueryWrapper<>();
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(categorySafeguardVO.getCategoryNo())) {
            queryWrapper.lambda().eq(CategorySafeguard::getCategoryNo,categorySafeguardVO.getCategoryNo());
        }
        //条例维度查询
        if (!EmptyUtil.isNullOrEmpty(categorySafeguardVO.getSafeguardKey())) {
            queryWrapper.lambda().eq(CategorySafeguard::getSafeguardKey,categorySafeguardVO.getSafeguardKey());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(categorySafeguardVO.getSortNo())) {
            queryWrapper.lambda().eq(CategorySafeguard::getSortNo,categorySafeguardVO.getSortNo());
        }
        //补充说明查询
        if (!EmptyUtil.isNullOrEmpty(categorySafeguardVO.getRemake())) {
            queryWrapper.lambda().eq(CategorySafeguard::getRemake,categorySafeguardVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(categorySafeguardVO.getDataState())) {
            queryWrapper.lambda().eq(CategorySafeguard::getDataState,categorySafeguardVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CategorySafeguard::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CategorySafeguardCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#categorySafeguardVO.hashCode()")
    public Page<CategorySafeguardVO> findPage(CategorySafeguardVO categorySafeguardVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CategorySafeguard> CategorySafeguardPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<CategorySafeguard> queryWrapper = queryWrapper(categorySafeguardVO);
            //执行分页查询
            Page<CategorySafeguardVO> categorySafeguardVOPage = BeanConv.toPage(
                page(CategorySafeguardPage, queryWrapper), CategorySafeguardVO.class);
            //返回结果
            return categorySafeguardVOPage;
        }catch (Exception e){
            log.error("分类保障项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategorySafeguardCacheConstant.BASIC,key ="#categorySafeguardId")
    public CategorySafeguardVO findById(String categorySafeguardId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(categorySafeguardId),CategorySafeguardVO.class);
        }catch (Exception e){
            log.error("分类保障项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategorySafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategorySafeguardCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CategorySafeguardCacheConstant.BASIC,key = "#result.id")})
    public CategorySafeguardVO save(CategorySafeguardVO categorySafeguardVO) {
        try {
            //转换CategorySafeguardVO为CategorySafeguard
            CategorySafeguard categorySafeguard = BeanConv.toBean(categorySafeguardVO, CategorySafeguard.class);
            boolean flag = save(categorySafeguard);
            if (!flag){
                throw new RuntimeException("保存分类保障项失败");
            }
            //转换返回对象CategorySafeguardVO
            CategorySafeguardVO categorySafeguardVOHandler = BeanConv.toBean(categorySafeguard, CategorySafeguardVO.class);
            return categorySafeguardVOHandler;
        }catch (Exception e){
            log.error("保存分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategorySafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategorySafeguardCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategorySafeguardCacheConstant.BASIC,key = "#categorySafeguardVO.id")})
    public Boolean update(CategorySafeguardVO categorySafeguardVO) {
        try {
            //转换CategorySafeguardVO为CategorySafeguard
            CategorySafeguard categorySafeguard = BeanConv.toBean(categorySafeguardVO, CategorySafeguard.class);
            boolean flag = updateById(categorySafeguard);
            if (!flag){
                throw new RuntimeException("修改分类保障项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategorySafeguardCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategorySafeguardCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategorySafeguardCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除分类保障项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategorySafeguardCacheConstant.LIST,key ="#categorySafeguardVO.hashCode()")
    public List<CategorySafeguardVO> findList(CategorySafeguardVO categorySafeguardVO) {
        try {
            //构建查询条件
            QueryWrapper<CategorySafeguard> queryWrapper = queryWrapper(categorySafeguardVO);
            //执行列表查询
            List<CategorySafeguardVO> categorySafeguardVOs = BeanConv.toBeanList(list(queryWrapper),CategorySafeguardVO.class);
            return categorySafeguardVOs;
        }catch (Exception e){
            log.error("分类保障项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNo(String categoryNo) {
        try {
            UpdateWrapper<CategorySafeguard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(CategorySafeguard::getCategoryNo,categoryNo);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.DEL_FAIL);
        }
    }

    @Override
    public List<CategorySafeguardVO> findListInCategoryNo(Set<String> categoryNoSet) {
        try {
            QueryWrapper<CategorySafeguard> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(CategorySafeguard::getCategoryNo,categoryNoSet);
            return BeanConv.toBeanList(list(queryWrapper),CategorySafeguardVO.class);
        }catch (Exception e){
            log.error("删除分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNos(List<String> categoryNos) {
        try {
            UpdateWrapper<CategorySafeguard> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(CategorySafeguard::getCategoryNo,categoryNos);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类保障项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategorySafeguardEnum.DEL_FAIL);
        }
    }
}
