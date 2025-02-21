package com.itheima.bolee.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.trade.SignContractVO;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.SignContractVO;
import com.itheima.bolee.trade.pojo.SignContract;
import com.itheima.bolee.trade.pojo.RefundRecord;
import com.itheima.bolee.trade.pojo.SignContract;

import java.util.List;

/**
 * @Description： 签约合同服务类
 */
public interface ISignContractService extends IService<SignContract> {

    /**
     * @Description 签约合同分页
     * @param payChannelVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<SignContract>
     */
    Page<SignContract> findSignContractPage(SignContractVO payChannelVO, int pageNum, int pageSize);

    /**
     * @Description 创建签约合同
     * @param payChannelVO 对象信息
     * @return SignContract
     */
    SignContract createSignContract(SignContractVO payChannelVO);

    /**
     * @Description 修改签约合同
     * @param payChannelVO 对象信息
     * @return Boolean
     */
    Boolean updateSignContract(SignContractVO payChannelVO);

    /**
     * @Description 删除签约合同
     * @param checkedIds 选择的签约合同ID
     * @return Boolean
     */
    Boolean deleteSignContract(String[] checkedIds);

    /***
     * @description 多条件查询
     * @param signContractVO
     * @return
     */
    List<SignContract> findSignContractList(SignContractVO signContractVO);

    /***
     * @description 查询当前业务对应的有效签约信息
     * @param contractNo
     * @return
     */
    SignContract findSignContractByContract_no(String contractNo);

    /***
     * @description 关闭h5签约
     * @param warrantyNo 合同号
     * @param tradingChannel 渠道
     * @return
     */
    Boolean h5CloseSignContract(String warrantyNo, String tradingChannel);

    /***
     * @description 签约同步
     * @param warrantyNo 合同号
     * @param tradingChannel 交易渠道
     * @param agreementNo 签约号
     * @return
     */
    Boolean signContractSync(String warrantyNo, String tradingChannel, String agreementNo);
}
