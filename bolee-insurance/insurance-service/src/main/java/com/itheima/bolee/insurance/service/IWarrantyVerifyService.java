package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WarrantyVerify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WarrantyVerifyVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：合同核保服务类
 */
public interface IWarrantyVerifyService extends IService<WarrantyVerify> {

    /**
     * @Description 多条件查询合同核保分页
     * @param warrantyVerifyVO 合同核保查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyVerifyVO>
     */
    Page<WarrantyVerifyVO> findPage(WarrantyVerifyVO warrantyVerifyVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询合同核保
    * @param warrantyVerifyId 合同信息ID
    * @return WarrantyVerifyVO
    */
    WarrantyVerifyVO findById(String warrantyVerifyId);

    /**
     * @Description 合同核保新增
     * @param warrantyVerifyVO 合同核保查询条件
     * @return WarrantyVerifyVO
     */
    WarrantyVerifyVO save(WarrantyVerifyVO warrantyVerifyVO);

    /**
     * @Description 合同核保修改
     * @param warrantyVerifyVO 合同核保对象
     * @return WarrantyVerifyVO
     */
    Boolean update(WarrantyVerifyVO warrantyVerifyVO);

    /**
     * @Description 合同核保删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询合同核保列表
     * @param warrantyVerifyVO 查询条件
     * @return: List<WarrantyVerifyVO>
     */
    List<WarrantyVerifyVO> findList(WarrantyVerifyVO warrantyVerifyVO);
}
