package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.category.CategoryConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeItemVO;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.insurance.constant.CategoryCacheConstant;
import com.itheima.bolee.insurance.dto.CategoryCoefficentVO;
import com.itheima.bolee.insurance.dto.CategoryConditionVO;
import com.itheima.bolee.insurance.dto.CategorySafeguardVO;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.itheima.bolee.insurance.enums.CategoryEnum;
import com.itheima.bolee.insurance.mapper.CategoryMapper;
import com.itheima.bolee.insurance.pojo.Category;
import com.itheima.bolee.insurance.pojo.CategoryCoefficent;
import com.itheima.bolee.insurance.pojo.CategoryCondition;
import com.itheima.bolee.insurance.pojo.CategorySafeguard;
import com.itheima.bolee.insurance.service.ICategoryCoefficentService;
import com.itheima.bolee.insurance.service.ICategoryConditionService;
import com.itheima.bolee.insurance.service.ICategorySafeguardService;
import com.itheima.bolee.insurance.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description：保险分类服务实现类
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    ICategoryConditionService categoryConditionService;

    @Autowired
    ICategoryCoefficentService categoryCoefficentService;

    @Autowired
    ICategorySafeguardService categorySafeguardService;

    /***
    * @description 保险分类多条件组合
    * @param categoryVO 保险分类
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Category> queryWrapper(CategoryVO categoryVO){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        //父分类编号查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getParentCategoryNo())) {
            queryWrapper.lambda().eq(Category::getParentCategoryNo,categoryVO.getParentCategoryNo());
        }
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getCategoryNo())) {
            queryWrapper.lambda().eq(Category::getCategoryNo,categoryVO.getCategoryNo());
        }
        //分类名称查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getCategoryName())) {
            queryWrapper.lambda().eq(Category::getCategoryName,categoryVO.getCategoryName());
        }
        //图标查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getIcon())) {
            queryWrapper.lambda().eq(Category::getIcon,categoryVO.getIcon());
        }
        //是否叶子节点（0是 否1）查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getLeafNode())) {
            queryWrapper.lambda().eq(Category::getLeafNode,categoryVO.getLeafNode());
        }
        //是否显示在首页（0是 否1）查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getShowIndex())) {
            queryWrapper.lambda().eq(Category::getShowIndex,categoryVO.getShowIndex());
        }
        //校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getCheckRule())) {
            queryWrapper.lambda().eq(Category::getCheckRule,categoryVO.getCheckRule());
        }
        //分类类型：0推荐分类  1产品分类
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getCategoryType())) {
            queryWrapper.lambda().eq(Category::getCategoryType,categoryVO.getCategoryType());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getSortNo())) {
            queryWrapper.lambda().eq(Category::getSortNo,categoryVO.getSortNo());
        }
        //分类补充说明查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getRemake())) {
            queryWrapper.lambda().eq(Category::getRemake,categoryVO.getRemake());
        }
        //校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getCheckRule())) {
            queryWrapper.lambda().eq(Category::getCheckRule,categoryVO.getCheckRule());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(categoryVO.getDataState())) {
            queryWrapper.lambda().eq(Category::getDataState,categoryVO.getDataState());
        }
        //按排序字段升序
        queryWrapper.lambda().orderByAsc(Category::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CategoryCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#categoryVO.hashCode()")
    public Page<CategoryVO> findPage(CategoryVO categoryVO, int pageNum, int pageSize) {
        try {
            //构建分页对象

            //构建查询条件

            //执行分页查询

            //组合子属性

                //构建分类编号Set

                //查询分类筛选项

                //查询分类保障项

                //查询分类系数项

                //匹配属性

            //返回结果
            return null;
        }catch (Exception e){
            log.error("保险分类分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CategoryCacheConstant.BASIC,key = "#result.id")})
    public CategoryVO save(CategoryVO categoryVO) {
        try {
            //转换CategoryVO为Category
            Category category = BeanConv.toBean(categoryVO, Category.class);
            category.setCategoryNo(this.createCategoryNo(category.getParentCategoryNo()));
            boolean flag = save(category);
            if (!flag){
                throw new RuntimeException("保存保险分类失败");
            }
            //转换返回对象CategoryVO
            return BeanConv.toBean(category, CategoryVO.class);
        }catch (Exception e){
            log.error("保存保险分类异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryCacheConstant.BASIC,key = "#categoryVO.id")})
    public Boolean update(CategoryVO categoryVO) {
        try {
            //转换CategoryVO为Category
            Category category = BeanConv.toBean(categoryVO, Category.class);
            boolean flag = updateById(category);
            if (!flag){
                throw new RuntimeException("修改保险分类失败");
            }
            //保存分类筛选项
            if(CollectionUtil.isNotEmpty(categoryVO.getCategoryConditionVOs())){
                categoryVO.getCategoryConditionVOs().forEach(n->{n.setCategoryNo(category.getCategoryNo()); });
                categoryConditionService.deleteByCategoryNo(category.getCategoryNo());
                categoryConditionService.saveBatch(BeanConv.toBeanList(categoryVO.getCategoryConditionVOs(),CategoryCondition.class));
            }
            //保存分类系数项
            if(CollectionUtil.isNotEmpty(categoryVO.getCategoryCoefficentVOs())){
                categoryVO.getCategoryCoefficentVOs().forEach(n->{ n.setCategoryNo(category.getCategoryNo());});
                categoryCoefficentService.deleteByCategoryNo(category.getCategoryNo());
                categoryCoefficentService.saveBatch(BeanConv.toBeanList(categoryVO.getCategoryCoefficentVOs(), CategoryCoefficent.class));
            }
            //保存分类保障项
            if(CollectionUtil.isNotEmpty(categoryVO.getCategorySafeguardVOs())){
                categoryVO.getCategorySafeguardVOs().forEach(n->{ n.setCategoryNo(category.getCategoryNo());});
                categorySafeguardService.deleteByCategoryNo(category.getCategoryNo());
                categorySafeguardService.saveBatch(BeanConv.toBeanList(categoryVO.getCategorySafeguardVOs(), CategorySafeguard.class));
            }
            return flag;
        }catch (Exception e){
            log.error("修改保险分类异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CategoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CategoryCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CategoryCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds).stream().map(Long::new).collect(Collectors.toList());
            List<Category> categories = listByIds(idsLong);
            //删除关联的分类保障项、分类筛选项、分类系数项信息
            if (EmptyUtil.isNullOrEmpty(categories)){
                List<String> categoryNos = categories.stream().map(Category::getCategoryNo).collect(Collectors.toList());
                categoryConditionService.deleteByCategoryNos(categoryNos);
                categoryCoefficentService.deleteByCategoryNos(categoryNos);
                categorySafeguardService.deleteByCategoryNos(categoryNos);
            }
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险分类失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险分类异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CategoryCacheConstant.LIST,key ="#categoryVO.hashCode()")
    public List<CategoryVO> findList(CategoryVO categoryVO) {
        try {
            //构建查询条件
            QueryWrapper<Category> queryWrapper = queryWrapper(categoryVO);
            List<Category> categorys = list(queryWrapper);
            List<Category> categorysHandler = Lists.newArrayList();
            //显示指定节点层
            if (!EmptyUtil.isNullOrEmpty(categoryVO.getNodeFloors())){
                categorys.forEach(n->{
                    if (categoryVO.getNodeFloors().contains(Long.valueOf(NoProcessing.processString(n.getCategoryNo()).length()/3-1))){
                        categorysHandler.add(n);
                    }
                });
            }else {
                categorysHandler.addAll(categorys);
            }
            //执行列表查询
            List<CategoryVO> categoryVOs = BeanConv.toBeanList(categorysHandler,CategoryVO.class);
            if (!EmptyUtil.isNullOrEmpty(categoryVOs)){
                Set<String> categoryNoSet = categoryVOs.stream().map(CategoryVO::getCategoryNo).collect(Collectors.toSet());
                //查询分类筛选项
                List<CategoryConditionVO> categoryConditionVOs = categoryConditionService.findListInCategoryNo(categoryNoSet);
                //查询分类保障项
                List<CategorySafeguardVO> categorySafeguardVOs = categorySafeguardService.findListInCategoryNo(categoryNoSet);
                //查询分类系数项
                List<CategoryCoefficentVO> categoryCoefficentVOs = categoryCoefficentService.findListInCategoryNo(categoryNoSet);
                //组合表单
                categoryVOs.forEach(n->{
                    List<CategoryConditionVO> categoryConditionVOsHandler = Lists.newArrayList();
                    List<CategorySafeguardVO> categorySafeguardVOsHandler = Lists.newArrayList();
                    List<CategoryCoefficentVO> categoryCoefficentVOsHandler = Lists.newArrayList();
                    categoryConditionVOs.forEach(y->{
                        if (n.getCategoryNo().equals(y.getCategoryNo())) {
                            categoryConditionVOsHandler.add(y);
                        }
                    });
                    categorySafeguardVOs.forEach(z->{
                        if (n.getCategoryNo().equals(z.getCategoryNo())) {
                            categorySafeguardVOsHandler.add(z);
                        }
                    });
                    categoryCoefficentVOs.forEach(e->{
                        if (n.getCategoryNo().equals(e.getCategoryNo())) {
                            categoryCoefficentVOsHandler.add(e);
                        }
                    });
                    n.setCategoryConditionVOs(categoryConditionVOsHandler);
                    n.setCategorySafeguardVOs(categorySafeguardVOsHandler);
                    n.setCategoryCoefficentVOs(categoryCoefficentVOsHandler);
                });
            }
            return categoryVOs;
        }catch (Exception e){
            log.error("保险分类列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.LIST_FAIL);
        }
    }



    @Override
    @Cacheable(value = CategoryCacheConstant.TREE,key ="#parentCategoryNo+'-'+#categoryType+'-'+#checkedCategoryNos")
    public TreeVO categoryTreeVO(String parentCategoryNo, String categoryType,String[] checkedCategoryNos) {
        try {
            //起始节点确定

            //起始节点子节点

            //类型不为空，添加到条件中

            //构建展开节点

            //构建选中节点

            //构建List<TreeItemVO>对象

                //构建当前节点

                //当前节点是否选择

                //当前节点展开处理

            //数据进行分组（key-->parentId:val-->List<TreeItemVO>）

            //遍历回填子节点

            //构建treeItemVOResult

            //返回TreeVO
            return null;
        } catch (Exception e) {
            log.error("查询分类表TREE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CategoryEnum.TREE_FAIL);
        }
    }

    @Override
    public String createCategoryNo(String parentCategoryNo) {
        CategoryVO categoryVO = CategoryVO.builder()
            .parentCategoryNo(parentCategoryNo)
            .build();
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Category::getParentCategoryNo,parentCategoryNo);
        List<Category> categoryList = list(queryWrapper);
        //无下属节点则创建下属节点
        if (EmptyUtil.isNullOrEmpty(categoryList)){
            return NoProcessing.createNo(parentCategoryNo,false);
        //有下属节点则累加下属节点
        }else {
            Long categoryNo = categoryList.stream()
                .map(category -> { return Long.valueOf(category.getCategoryNo());})
                .max(Comparator.comparing(i -> i)).get();
            return NoProcessing.createNo(String.valueOf(categoryNo),true);
        }
    }

    /**
     * 人种榜险种榜分类
      * @param type
     */
    @Override
    public List<CategoryVO> categoryCheckRule(String type) {
        //判断是否是险种榜
        if(InsureConstant.checkIsInsureType(type)){
            List<CategoryVO> res = new ArrayList<>();
            //获取所有的险种榜
            List<String> allCheckRule = InsureConstant.getAllCheckRule();
            allCheckRule.forEach(e->{
                CategoryVO categoryVO = CategoryVO.builder().categoryNo(e).categoryName(InsureConstant.getRuleNameById(e)).build();
                res.add(categoryVO);
            });
            return res;
        }else{
            //说明是人种榜
            List<String> categoryNames = Arrays.asList("成年人", "少儿", "老人");
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Category::getCategoryName,categoryNames);
            queryWrapper.eq(Category::getDataState,SuperConstant.DATA_STATE_0);
            List<CategoryVO> res = BeanConv.toBeanList(list(queryWrapper), CategoryVO.class);
            return res;
        }
    }
}
