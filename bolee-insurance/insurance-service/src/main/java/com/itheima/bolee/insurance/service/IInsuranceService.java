package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.pojo.Insurance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description：保险产品服务类
 */
public interface IInsuranceService extends IService<Insurance> {

    /**
     * @param insuranceVO 保险产品查询条件
     * @param pageNum     页码
     * @param pageSize    每页条数
     * @return Page<InsuranceVO>
     * @Description 多条件查询保险产品分页
     */
    Page<InsuranceVO> findPage(InsuranceVO insuranceVO, int pageNum, int pageSize);

    /**
     * @param insuranceId 保险产品信息ID
     * @return InsuranceVO
     * @Description 多条件查询保险产品
     */
    InsuranceVO findById(String insuranceId);

    /**
     * @param insuranceVO 保险产品查询条件
     * @return InsuranceVO
     * @Description 保险产品新增
     */
    InsuranceVO save(InsuranceVO insuranceVO);

    /**
     * @param insuranceVO 保险产品对象
     * @return InsuranceVO
     * @Description 保险产品修改
     */
    Boolean update(InsuranceVO insuranceVO);

    /**
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     * @Description 保险产品删除
     */
    Boolean delete(String[] checkedIds);

    /**
     * @param insuranceVO 查询条件
     * @description 多条件查询保险产品列表
     * @return: List<InsuranceVO>
     */
    List<InsuranceVO> findList(InsuranceVO insuranceVO);

    /***
     * @description 保费计算
     * @param doInsureVo
     * @return 投入型：最终本期投保金额，如果为不可购买则返回：不符合条件，理财型：最终收益
     */
    String doPremium(DoInsureVo doInsureVo);

    /***
     * @description 收益计算
     * @param doInsureVo 投保试算对象
     * @return  理财型：最终收益，返回0则表示不符合条件
     */
    EarningVO doEarnings(DoInsureVo doInsureVo);

}
