package com.itheima.bolee.insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.InsuranceTopVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.pojo.InsuranceTop;

import java.util.List;

/**
 * @Description：人气保险服务类
 */
public interface IInsuranceTopService extends IService<InsuranceTop> {

    /**
     * @param insuranceTopVO 人气保险查询条件
     * @param pageNum        页码
     * @param pageSize       每页条数
     * @return Page<InsuranceTopVO>
     * @Description 多条件查询人气保险分页
     */
    Page<InsuranceTopVO> findPage(InsuranceTopVO insuranceTopVO, int pageNum, int pageSize);

    /**
     * @param insuranceTopId 合同信息ID
     * @return InsuranceTopVO
     * @Description 多条件查询人气保险
     */
    InsuranceTopVO findById(String insuranceTopId);

    /**
     * @param insuranceTopVO 人气保险查询条件
     * @return InsuranceTopVO
     * @Description 人气保险新增
     */
    InsuranceTopVO save(InsuranceTopVO insuranceTopVO);

    /**
     * @param insuranceTopVO 人气保险对象
     * @return InsuranceTopVO
     * @Description 人气保险修改
     */
    Boolean update(InsuranceTopVO insuranceTopVO);

    /**
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     * @Description 人气保险删除
     */
    Boolean delete(String[] checkedIds);

    /**
     * @param insuranceTopVO 查询条件
     * @description 多条件查询人气保险列表
     * @return: List<InsuranceTopVO>
     */
    List<InsuranceTopVO> findList(InsuranceTopVO insuranceTopVO);

    /**
     * 查询排名前10的榜单列表
     *
     * @return
     */
    List<InsuranceVO> findTopInsurance(Integer num, Integer dateLimit);
}
