package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.CategorySafeguard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CategorySafeguardVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * @Description：分类保障项服务类
 */
public interface ICategorySafeguardService extends IService<CategorySafeguard> {

    /**
     * @Description 多条件查询分类保障项分页
     * @param categorySafeguardVO 分类保障项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CategorySafeguardVO>
     */
    Page<CategorySafeguardVO> findPage(CategorySafeguardVO categorySafeguardVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询分类保障项
    * @param categorySafeguardId 合同信息ID
    * @return CategorySafeguardVO
    */
    CategorySafeguardVO findById(String categorySafeguardId);

    /**
     * @Description 分类保障项新增
     * @param categorySafeguardVO 分类保障项查询条件
     * @return CategorySafeguardVO
     */
    CategorySafeguardVO save(CategorySafeguardVO categorySafeguardVO);

    /**
     * @Description 分类保障项修改
     * @param categorySafeguardVO 分类保障项对象
     * @return CategorySafeguardVO
     */
    Boolean update(CategorySafeguardVO categorySafeguardVO);

    /**
     * @Description 分类保障项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询分类保障项列表
     * @param categorySafeguardVO 查询条件
     * @return: List<CategorySafeguardVO>
     */
    List<CategorySafeguardVO> findList(CategorySafeguardVO categorySafeguardVO);

    /***
     * @description 删除分类下分类系数项
     * @param categoryNo
     * @return
     */
    Boolean deleteByCategoryNo(String categoryNo);

    /***
     * @description 分类集合下分类系数项
     * @param categoryNoSet
     * @return
     */
    List<CategorySafeguardVO> findListInCategoryNo(Set<String> categoryNoSet);

    /***
     * @description 删除分类下分类系数项
     * @param categoryNos
     * @return
     */
    Boolean deleteByCategoryNos(List<String> categoryNos);
}
