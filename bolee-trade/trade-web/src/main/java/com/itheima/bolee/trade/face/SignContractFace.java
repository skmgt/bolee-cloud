package com.itheima.bolee.trade.face;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.trade.SignContractVO;
import com.itheima.bolee.trade.pojo.SignContract;

import java.util.List;

/**
 * @Description： 签约合同服务类
 */
public interface SignContractFace {

    /**
     * @Description 签约合同分页
     * @param payChannelVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<SignContract>
     */
    Page<SignContractVO> findSignContractPage(SignContractVO payChannelVO, int pageNum, int pageSize);

    /**
     * @Description 创建签约合同
     * @param payChannelVO 对象信息
     * @return SignContract
     */
    SignContractVO createSignContract(SignContractVO payChannelVO);

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
    List<SignContractVO> findSignContractList(SignContractVO signContractVO);

}
