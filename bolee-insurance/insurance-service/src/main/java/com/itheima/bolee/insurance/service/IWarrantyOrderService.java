package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerInsuranceDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerSexDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisInsuranceTypeDTO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.insurance.dto.SelfWarrantyVO;
import com.itheima.bolee.insurance.pojo.WarrantyOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WarrantyOrderVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：合同扣款计划服务类
 */
public interface IWarrantyOrderService extends IService<WarrantyOrder>{

    /**
     * @Description 多条件查询合同扣款计划分页
     * @param warrantyOrderVO 合同扣款计划查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WarrantyOrderVO>
     */
    Page<WarrantyOrderVO> findPage(WarrantyOrderVO warrantyOrderVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询合同扣款计划
    * @param warrantyOrderId 合同信息ID
    * @return WarrantyOrderVO
    */
    WarrantyOrderVO findById(String warrantyOrderId);

    /**
     * @Description 查询合同扣款计划
     * @param warrantyOrderId 合同信息ID
     * @return WarrantyOrderVO
     */
    WarrantyOrderVO findByIdAndOrderState(String warrantyOrderId,String OrderState);

    /**
     * @Description 合同扣款计划新增
     * @param warrantyOrderVO 合同扣款计划查询条件
     * @return WarrantyOrderVO
     */
    WarrantyOrderVO save(WarrantyOrderVO warrantyOrderVO);

    /**
     * @Description 合同扣款计划修改
     * @param warrantyOrderVO 合同扣款计划对象
     * @return WarrantyOrderVO
     */
    Boolean update(WarrantyOrderVO warrantyOrderVO);

    /**
     * @Description 合同扣款计划删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询合同扣款计划列表
     * @param warrantyOrderVO 查询条件
     * @return: List<WarrantyOrderVO>
     */
    List<WarrantyOrderVO> findList(WarrantyOrderVO warrantyOrderVO);

    /***
     * @description 一次性支付
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return 支付会话签名
     */
    TradeVO doPayment(String warrantyOrderId, String tradingChannel);


    /***
     * @description 关闭支付
     * @param tradeOrderNo 交易订单号
     * @param tradingChannel 支付渠道
     * @return 支付会话签名
     */
    TradeVO doClose(String tradeOrderNo, String tradingChannel);

    /***
     * @description 独立签约
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return 签约唤醒签名
     */
    String signContract(String warrantyOrderId, String tradingChannel);

    /***
     * @description 签约同步
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @param agreementNo 支付签约号
     * @return
     */
    Boolean signContractSync(String warrantyOrderId, String tradingChannel, String agreementNo);

    /***
     * @description 关闭签约
     * @param warrantyOrderId 合同订单Id
     * @param tradingChannel 支付渠道
     * @return
     */
    Boolean doCloseSignContract(String warrantyOrderId, String tradingChannel);

    /***
     * @description 计划任务:周期代扣
     * @return
     */
    Boolean periodicPay();

    /**
     * 统计分析: 统计保险数量及金额数量
     * @return
     */
    AnalysisCustomerInsuranceDTO analysisInsurance();

    /**
     * 统计分析：投保人性别统计
     * @return
     */
    AnalysisCustomerSexDTO analysisWarrantySex();

    /**
     * 统计分析：统计投保人投保保单类型和对应类型的保单数量
     * @return
     */
    List<AnalysisInsuranceTypeDTO> analysisCustomerInsuranceType();

    /**
     * description 同步合同支付结果
     * @param orderNo 合同订单编号
     * @param tradeState 交易状态
     * @return
     */
    Boolean syncPayment(String orderNo,String tradeState);

}
