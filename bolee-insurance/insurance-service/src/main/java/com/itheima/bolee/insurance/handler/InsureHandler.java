package com.itheima.bolee.insurance.handler;

import com.itheima.bolee.insurance.dto.DoInsureVo;
import com.itheima.bolee.insurance.dto.EarningVO;
import com.itheima.bolee.insurance.dto.WarrantyOrderVO;
import com.itheima.bolee.insurance.dto.WarrantyVO;

import java.util.List;

/**
 * @ClassName InsureHandler.java
 * @Description 投保、创建订单、保费计算、理财收益
 */
public interface InsureHandler {

    /***
     * @description 投保操作
     * @param doInsureVo 投保处理
     * @return  保险合同
     */
    WarrantyVO doInsure(DoInsureVo doInsureVo);

    /***
     * @description 创建投保合同订单
     * @param warrantyVO
     * @return
     */
    List<WarrantyOrderVO> createWarrantyOrderVO(WarrantyVO warrantyVO);

    /***
     * @description 保费计算
     * @param doInsureVo 投保请求对象
     * @return  保费
     */
    String doPremium(DoInsureVo doInsureVo);

    /***
     * @description 理财收益
     * @param doInsureVo 投保请求对象
     * @return  收益
     */
    EarningVO doEarnings(DoInsureVo doInsureVo);


}
