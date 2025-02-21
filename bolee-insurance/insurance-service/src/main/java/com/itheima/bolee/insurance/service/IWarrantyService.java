package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.pojo.Warranty;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description：合同信息服务类
 */
public interface IWarrantyService extends IService<Warranty> {

    /**
     * @Description 多条件查询合同信息分页
     * @param warrantyVO 合同信息查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyVO>
     */
    Page<WarrantyVO> findPage(WarrantyVO warrantyVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询合同信息
    * @param warrantyId 合同信息ID
    * @return WarrantyVO
    */
    WarrantyVO findById(String warrantyId);

    /**
     * @Description 合同信息新增
     * @param warrantyVO 合同信息查询条件
     * @return WarrantyVO
     */
    WarrantyVO save(WarrantyVO warrantyVO);

    /**
     * @Description 合同信息修改
     * @param warrantyVO 合同信息对象
     * @return WarrantyVO
     */
    Boolean update(WarrantyVO warrantyVO);

    /**
     * @Description 合同信息删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询合同信息列表
     * @param warrantyVO 查询条件
     * @return: List<WarrantyVO>
     */
    List<WarrantyVO> findList(WarrantyVO warrantyVO);

    /**
     * 查询我的保险数量
     * @return
     */
    SelfWarrantyVO findMyWarrantyNums(UserVO userVO) ;

    /**
     * 获取家庭人身保障
      * @return
     */
    List<HomePeopleVO> findHomeSafeInfo(UserVO userVO);

    /***
     * @description 保险投保
     * @param doInsureVo 保险产品VO对象
     * @return  保险合同
     */
    WarrantyVO doInsure(DoInsureVo doInsureVo);

    /***
     * @description 我的保单
     * @param userVO 投保人
     * @return  保险合同
     */
    List<WarrantyVO> myWarrantyVOs(UserVO userVO,CustomerRelationVO customerRelationVO);

    /**
     * 我的保单数量
     * @param userVO
     * @return
     */
    MyWarrantyInfoVO myWarrantyNums(UserVO userVO);

    /***
     * @description 核保补发
     * @return
     */
    void sendCheckInfo();

    /***
     * @description 保批补发
     * @return
     */
    void sendApproveInfo();

    /***
     * @description 查询合同及合同为支付订单
     * @param warrantyId
     * @return
     */
    WarrantyVO findWarranty(String warrantyId);

    /**
     * description 根据合同编号查询合同信息
     * @param warrantyNo
     * @return
     */
    WarrantyVO findByWarrantyNo(String warrantyNo);

    /***
     * @description 清理合同保单
     * @param warrantyNo
     * @return
     */
    Boolean cleanWarranty(String warrantyNo);

    /**
     * 计划任务：日投保额度明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    SaleReportDTO doInsureDetailDay(String reportTime);

    /**
     * 计划任务：统计日投保分类明细
     * @param reportTime 统计时间
     * @return 是否执行成功
     */
    List<InsureCategoryDTO> doInsureCategory(String reportTime);
}
