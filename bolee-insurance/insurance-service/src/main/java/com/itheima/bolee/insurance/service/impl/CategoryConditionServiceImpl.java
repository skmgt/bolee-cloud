package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.constant.CategoryConditionCacheConstant;
import com.itheima.bolee.insurance.dto.CategoryConditionVO;
import com.itheima.bolee.insurance.dto.ConditionVO;
import com.itheima.bolee.insurance.enums.CategoryConditionEnum;
import com.itheima.bolee.insurance.mapper.CategoryConditionMapper;
import com.itheima.bolee.insurance.pojo.CategoryCondition;
import com.itheima.bolee.insurance.service.ICategoryConditionService;
import com.itheima.bolee.insurance.service.IConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * @Description：分类筛选项服务实现类
 */
@Slf4j
@Service
public class CategoryConditionServiceImpl extends ServiceImpl<CategoryConditionMapper, CategoryCondition> implements ICategoryConditionService {

    @Autowired
    private IConditionService conditionService;


    /***
    * @description 分类筛选项多条件组合
    * @param categoryConditionVO 分类筛选项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<CategoryCondition> queryWrapper(CategoryConditionVO categoryConditionVO){
        QueryWrapper<CategoryCondition> queryWrapper = new QueryWrapper<>();
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(categoryConditionVO.getCategoryNo())) {
            queryWrapper.lambda().eq(CategoryCondition::getCategoryNo,categoryConditionVO.getCategoryNo());
        }
        //条件key查询
        if (!EmptyUtil.isNullOrEmpty(categoryConditionVO.getConditionKey())) {
            queryWrapper.lambda().eq(CategoryCondition::getConditionKey,categoryConditionVO.getConditionKey());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(categoryConditionVO.getSortNo())) {
            queryWrapper.lambda().eq(CategoryCondition::getSortNo,categoryConditionVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(categoryConditionVO.getRemake())) {
            queryWrapper.lambda().eq(CategoryCondition::getRemake,categoryConditionVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(categoryConditionVO.getDataState())) {
            queryWrapper.lambda().eq(CategoryCondition::getDataState,categoryConditionVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CategoryCondition::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CategoryConditionCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#categoryConditionVO.hashCode()")
    public Page<CategoryConditionVO> findPage(CategoryConditionVO categoryConditionVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CategoryCondition> CategoryConditionPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<CategoryCondition> queryWrapper = queryWrapper(categoryConditionVO);
            //执行分页查询
            Page<CategoryConditionVO> categoryConditionVOPage = BeanConv.toPage(
                page(CategoryConditionPage, queryWrapper), CategoryConditionVO.class);
            //返回结果
            return categoryConditionVOPage;
        }catch (Exception e){
            log.error("分类筛选项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategoryConditionCacheConstant.BASIC,key ="#categoryConditionId")
    public CategoryConditionVO findById(String categoryConditionId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(categoryConditionId),CategoryConditionVO.class);
        }catch (Exception e){
            log.error("分类筛选项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryConditionCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CategoryConditionCacheConstant.BASIC,key = "#result.id")})
    public CategoryConditionVO save(CategoryConditionVO categoryConditionVO) {
        try {
            //转换CategoryConditionVO为CategoryCondition
            CategoryCondition categoryCondition = BeanConv.toBean(categoryConditionVO, CategoryCondition.class);
            boolean flag = save(categoryCondition);
            if (!flag){
                throw new RuntimeException("保存分类筛选项失败");
            }
            //转换返回对象CategoryConditionVO
            CategoryConditionVO categoryConditionVOHandler = BeanConv.toBean(categoryCondition, CategoryConditionVO.class);
            return categoryConditionVOHandler;
        }catch (Exception e){
            log.error("保存分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryConditionCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryConditionCacheConstant.BASIC,key = "#categoryConditionVO.id")})
    public Boolean update(CategoryConditionVO categoryConditionVO) {
        try {
            //转换CategoryConditionVO为CategoryCondition
            CategoryCondition categoryCondition = BeanConv.toBean(categoryConditionVO, CategoryCondition.class);
            boolean flag = updateById(categoryCondition);
            if (!flag){
                throw new RuntimeException("修改分类筛选项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryConditionCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryConditionCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除分类筛选项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategoryConditionCacheConstant.LIST,key ="#categoryConditionVO.hashCode()")
    public List<CategoryConditionVO> findList(CategoryConditionVO categoryConditionVO) {
        try {
            //构建查询条件
            QueryWrapper<CategoryCondition> queryWrapper = queryWrapper(categoryConditionVO);
            List<CategoryCondition> categoryConditions = list(queryWrapper);
            if (EmptyUtil.isNullOrEmpty(categoryConditions)){
                throw new RuntimeException("分类未定义筛选项目");
            }
            List<CategoryConditionVO> categoryConditionVOs = BeanConv.toBeanList(categoryConditions,CategoryConditionVO.class);
            List<String> conditionKeys = categoryConditions.stream().map(CategoryCondition::getConditionKey).collect(Collectors.toList());
            List<ConditionVO> conditionVOs = conditionService.findInConditionKeys(conditionKeys);
            categoryConditionVOs.forEach(n->{
                conditionVOs.forEach(k->{
                    if (n.getConditionKey().equals(k.getConditionKey())){
                        n.setConditionVal(k.getConditionVal());
                    }
                });
            });
            return categoryConditionVOs;
        }catch (Exception e){
            log.error("分类筛选项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.LIST_FAIL);
        }
    }

    /**
     * 根据分类查询出对应的筛选项
     * @param categoryNo 分类编号
     * @return
     */
    @Override
    public List<CategoryConditionVO> findConditionByType(String categoryNo) {
        try {
            //拿到所有的分类筛选项
            CategoryConditionVO categoryConditionVO = CategoryConditionVO.builder().dataState(SuperConstant.DATA_STATE_0).build();
            if(StrUtil.isNotEmpty(categoryNo)){
                categoryConditionVO.setCategoryNo(categoryNo);
            }
            //构建查询条件
            QueryWrapper<CategoryCondition> queryWrapper = queryWrapper(categoryConditionVO);
            //执行列表查询
            List<CategoryConditionVO> categoryConditionVOs = BeanConv.toBeanList(list(queryWrapper),CategoryConditionVO.class);
            if (!EmptyUtil.isNullOrEmpty(categoryConditionVOs)){
                //所有的筛选项的key
                List<String> conditionKeys = categoryConditionVOs.stream().map(CategoryConditionVO::getConditionKey).collect(Collectors.toList());
                List<ConditionVO> inConditionKeys = conditionService.findInConditionKeys(conditionKeys);
                categoryConditionVOs.forEach(n->{
                    inConditionKeys.forEach(k->{
                        if (n.getConditionKey().equals(k.getConditionKey()))
                            n.setConditionVal(k.getConditionVal());
                    });
                });
            }
            return categoryConditionVOs;
        }catch (Exception e){
            log.error("分类筛选项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNo(String categoryNo) {
        try {
            UpdateWrapper<CategoryCondition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(CategoryCondition::getCategoryNo,categoryNo);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.DEL_FAIL);
        }

    }

    @Override
    public List<CategoryConditionVO> findListInCategoryNo(Set<String> categoryNoSet) {
        try {
            QueryWrapper<CategoryCondition> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(CategoryCondition::getCategoryNo,categoryNoSet);
            return BeanConv.toBeanList(list(queryWrapper),CategoryConditionVO.class);
        }catch (Exception e){
            log.error("查询分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean deleteByCategoryNos(List<String> categoryNos) {
        try {
            UpdateWrapper<CategoryCondition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(CategoryCondition::getCategoryNo,categoryNos);
            return remove(updateWrapper);
        }catch (Exception e){
            log.error("删除分类筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryConditionEnum.DEL_FAIL);
        }
    }


}
