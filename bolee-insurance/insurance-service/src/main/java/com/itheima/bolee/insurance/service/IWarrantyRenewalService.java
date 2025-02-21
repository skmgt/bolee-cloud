package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WarrantyRenewal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WarrantyRenewalVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：合同续期服务类
 */
public interface IWarrantyRenewalService extends IService<WarrantyRenewal> {

    /**
     * @Description 多条件查询合同续期分页
     * @param warrantyRenewalVO 合同续期查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyRenewalVO>
     */
    Page<WarrantyRenewalVO> findPage(WarrantyRenewalVO warrantyRenewalVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询合同续期
    * @param warrantyRenewalId 合同信息ID
    * @return WarrantyRenewalVO
    */
    WarrantyRenewalVO findById(String warrantyRenewalId);

    /**
     * @Description 合同续期新增
     * @param warrantyRenewalVO 合同续期查询条件
     * @return WarrantyRenewalVO
     */
    WarrantyRenewalVO save(WarrantyRenewalVO warrantyRenewalVO);

    /**
     * @Description 合同续期修改
     * @param warrantyRenewalVO 合同续期对象
     * @return WarrantyRenewalVO
     */
    Boolean update(WarrantyRenewalVO warrantyRenewalVO);

    /**
     * @Description 合同续期删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询合同续期列表
     * @param warrantyRenewalVO 查询条件
     * @return: List<WarrantyRenewalVO>
     */
    List<WarrantyRenewalVO> findList(WarrantyRenewalVO warrantyRenewalVO);
}
