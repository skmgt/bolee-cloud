package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.CategoryCoefficent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CategoryCoefficentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * @Description：分类系数项服务类
 */
public interface ICategoryCoefficentService extends IService<CategoryCoefficent> {

    /**
     * @Description 多条件查询分类系数项分页
     * @param categoryCoefficentVO 分类系数项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CategoryCoefficentVO>
     */
    Page<CategoryCoefficentVO> findPage(CategoryCoefficentVO categoryCoefficentVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询分类系数项
    * @param categoryCoefficentId 合同信息ID
    * @return CategoryCoefficentVO
    */
    CategoryCoefficentVO findById(String categoryCoefficentId);

    /**
     * @Description 分类系数项新增
     * @param categoryCoefficentVO 分类系数项查询条件
     * @return CategoryCoefficentVO
     */
    CategoryCoefficentVO save(CategoryCoefficentVO categoryCoefficentVO);

    /**
     * @Description 分类系数项修改
     * @param categoryCoefficentVO 分类系数项对象
     * @return CategoryCoefficentVO
     */
    Boolean update(CategoryCoefficentVO categoryCoefficentVO);

    /**
     * @Description 分类系数项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询分类系数项列表
     * @param categoryCoefficentVO 查询条件
     * @return: List<CategoryCoefficentVO>
     */
    List<CategoryCoefficentVO> findList(CategoryCoefficentVO categoryCoefficentVO);

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
    List<CategoryCoefficentVO> findListInCategoryNo(Set<String> categoryNoSet);

    /***
     * @description 删除分类下分类系数项
     * @param categoryNos
     * @return
     */
    Boolean deleteByCategoryNos(List<String> categoryNos);
}
