package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.Coefficent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CoefficentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：系数项服务类
 */
public interface ICoefficentService extends IService<Coefficent> {

    /**
     * @Description 多条件查询系数项分页
     * @param coefficentVO 系数项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CoefficentVO>
     */
    Page<CoefficentVO> findPage(CoefficentVO coefficentVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询系数项
    * @param coefficentId 合同信息ID
    * @return CoefficentVO
    */
    CoefficentVO findById(String coefficentId);

    /**
     * @Description 系数项新增
     * @param coefficentVO 系数项查询条件
     * @return CoefficentVO
     */
    CoefficentVO save(CoefficentVO coefficentVO);

    /**
     * @Description 系数项修改
     * @param coefficentVO 系数项对象
     * @return CoefficentVO
     */
    Boolean update(CoefficentVO coefficentVO);

    /**
     * @Description 系数项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询系数项列表
     * @param coefficentVO 查询条件
     * @return: List<CoefficentVO>
     */
    List<CoefficentVO> findList(CoefficentVO coefficentVO);

    /***
     * @description 系数项key查询CoefficentVO
     * @param coefficentKey 系数项key
     * @return CoefficentVO
     */
    CoefficentVO findByCoefficentKey(String coefficentKey);
}
