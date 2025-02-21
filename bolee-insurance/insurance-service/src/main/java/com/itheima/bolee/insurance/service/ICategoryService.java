package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.insurance.dto.CategoryConditionVO;
import com.itheima.bolee.insurance.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CategoryVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：保险分类服务类
 */
public interface ICategoryService extends IService<Category> {

    /**
     * @Description 多条件查询保险分类分页
     * @param categoryVO 保险分类查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CategoryVO>
     */
    Page<CategoryVO> findPage(CategoryVO categoryVO, int pageNum, int pageSize);


    /**
     * @Description 保险分类新增
     * @param categoryVO 保险分类查询条件
     * @return CategoryVO
     */
    CategoryVO save(CategoryVO categoryVO);

    /**
     * @Description 保险分类修改
     * @param categoryVO 保险分类对象
     * @return CategoryVO
     */
    Boolean update(CategoryVO categoryVO);

    /**
     * @Description 保险分类删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保险分类列表
     * @param categoryVO 查询条件
     * @return: List<CategoryVO>
     */
    List<CategoryVO> findList(CategoryVO categoryVO);


    /**
     * @description 分类树形
     * @param parentCategoryNo 根节点
     * @param categoryType 保障类型：0推荐分类  1产品分类
     * @param checkedCategoryNos 选择节点
     * @return: TreeVO
     */
    TreeVO categoryTreeVO(String parentCategoryNo, String categoryType,String[] checkedCategoryNos);

    /**
     * @Description 创建分类编号
     * @param parentCategoryNo 创建分类编号
     * @return
     */
    String createCategoryNo(String parentCategoryNo);

    /**
     * 获取险种榜分类和人种榜分类
     * @param type
     */
    List<CategoryVO> categoryCheckRule(String type);
}
