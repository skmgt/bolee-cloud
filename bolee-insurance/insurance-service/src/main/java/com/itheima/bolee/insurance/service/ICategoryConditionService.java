package com.itheima.bolee.insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.insurance.dto.CategoryConditionVO;
import com.itheima.bolee.insurance.pojo.CategoryCondition;

import java.util.List;
import java.util.Set;

/**
 * @Description：分类筛选项服务类
 */
public interface ICategoryConditionService extends IService<CategoryCondition> {

    /**
     * @param categoryConditionVO 分类筛选项查询条件
     * @param pageNum             页码
     * @param pageSize            每页条数
     * @return Page<CategoryConditionVO>
     * @Description 多条件查询分类筛选项分页
     */
    Page<CategoryConditionVO> findPage(CategoryConditionVO categoryConditionVO, int pageNum, int pageSize);

    /**
     * @param categoryConditionId 合同信息ID
     * @return CategoryConditionVO
     * @Description 多条件查询分类筛选项
     */
    CategoryConditionVO findById(String categoryConditionId);

    /**
     * @param categoryConditionVO 分类筛选项查询条件
     * @return CategoryConditionVO
     * @Description 分类筛选项新增
     */
    CategoryConditionVO save(CategoryConditionVO categoryConditionVO);

    /**
     * @param categoryConditionVO 分类筛选项对象
     * @return CategoryConditionVO
     * @Description 分类筛选项修改
     */
    Boolean update(CategoryConditionVO categoryConditionVO);

    /**
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     * @Description 分类筛选项删除
     */
    Boolean delete(String[] checkedIds);

    /**
     * @param categoryConditionVO 查询条件
     * @description 多条件查询分类筛选项列表
     * @return: List<CategoryConditionVO>
     */
    List<CategoryConditionVO> findList(CategoryConditionVO categoryConditionVO);

    /**
     * 根据分类查询出分类对应的筛选项
     *
     * @param type
     * @return
     */
    List<CategoryConditionVO> findConditionByType(String type);

    /***
     * @description 删除分类下分类筛选项
     * @param categoryNo
     * @return
     */
    Boolean deleteByCategoryNo(String categoryNo);

    /***
     * @description 分类集合下分类筛选项
     * @param categoryNoSet
     * @return
     */
    List<CategoryConditionVO> findListInCategoryNo(Set<String> categoryNoSet);

    /***
     * @description 删除分类下分类筛选项
     * @param categoryNos
     * @return
     */
    Boolean deleteByCategoryNos(List<String> categoryNos);
}
