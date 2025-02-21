package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.WorryFreeCustomerInfoVO;
import com.itheima.bolee.insurance.pojo.WorryFreeInsuranceMatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WorryFreeInsuranceMatchVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：省心配保险推荐记录服务类
 */
public interface IWorryFreeInsuranceMatchService extends IService<WorryFreeInsuranceMatch> {

    /**
     * @Description 多条件查询省心配保险推荐记录分页
     * @param worryFreeInsuranceMatchVO 省心配保险推荐记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WorryFreeInsuranceMatchVO>
     */
    Page<WorryFreeInsuranceMatchVO> findPage(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询省心配保险推荐记录
    * @param worryFreeInsuranceMatchId 合同信息ID
    * @return WorryFreeInsuranceMatchVO
    */
    WorryFreeInsuranceMatchVO findById(String worryFreeInsuranceMatchId);

    /**
     * @Description 省心配保险推荐记录新增
     * @param worryFreeInsuranceMatchVO 省心配保险推荐记录查询条件
     * @return WorryFreeInsuranceMatchVO
     */
    WorryFreeInsuranceMatchVO save(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO);

    /**
     * @Description 省心配保险推荐记录修改
     * @param worryFreeInsuranceMatchVO 省心配保险推荐记录对象
     * @return WorryFreeInsuranceMatchVO
     */
    Boolean update(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO);

    /**
     * @Description 省心配保险推荐记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询省心配保险推荐记录列表
     * @param worryFreeInsuranceMatchVO 查询条件
     * @return: List<WorryFreeInsuranceMatchVO>
     */
    List<WorryFreeInsuranceMatchVO> findList(WorryFreeInsuranceMatchVO worryFreeInsuranceMatchVO);

    /**
     * 根据当前登录用户获取对应的推荐产品列表
     * @param id
     * @return
     */
    List<WorryFreeInsuranceMatchVO> productList(Long id);

    void cleanCustomerHistry(String id);
}
