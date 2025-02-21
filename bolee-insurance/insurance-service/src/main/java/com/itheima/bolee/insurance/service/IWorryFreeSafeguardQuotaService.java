package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WorryFreeSafeguardQuota;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WorryFreeSafeguardQuotaVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：省心配流程保障配额记录服务类
 */
public interface IWorryFreeSafeguardQuotaService extends IService<WorryFreeSafeguardQuota> {

    /**
     * @Description 多条件查询省心配流程保障配额记录分页
     * @param worryFreeSafeguardQuotaVO 省心配流程保障配额记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WorryFreeSafeguardQuotaVO>
     */
    Page<WorryFreeSafeguardQuotaVO> findPage(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询省心配流程保障配额记录
    * @param worryFreeSafeguardQuotaId 合同信息ID
    * @return WorryFreeSafeguardQuotaVO
    */
    WorryFreeSafeguardQuotaVO findById(String worryFreeSafeguardQuotaId);

    /**
     * @Description 省心配流程保障配额记录新增
     * @param worryFreeSafeguardQuotaVO 省心配流程保障配额记录查询条件
     * @return WorryFreeSafeguardQuotaVO
     */
    WorryFreeSafeguardQuotaVO save(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO);

    /**
     * @Description 省心配流程保障配额记录修改
     * @param worryFreeSafeguardQuotaVO 省心配流程保障配额记录对象
     * @return WorryFreeSafeguardQuotaVO
     */
    Boolean update(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO);

    /**
     * @Description 省心配流程保障配额记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询省心配流程保障配额记录列表
     * @param worryFreeSafeguardQuotaVO 查询条件
     * @return: List<WorryFreeSafeguardQuotaVO>
     */
    List<WorryFreeSafeguardQuotaVO> findList(WorryFreeSafeguardQuotaVO worryFreeSafeguardQuotaVO);

    /**
     * 获取保险配额
     * @param customerId 用户信息ID
     * @return
     */
    WorryFreeSafeguardQuotaVO findSaferQuota(Long customerId);

    void cleanCustomerHistry(String id);
}
