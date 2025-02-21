package com.itheima.bolee.insurance.service;

import com.itheima.bolee.dto.ResponseDTO;
import com.itheima.bolee.framework.commons.dto.external.TripartiteInsureDTO;
import com.itheima.bolee.insurance.dto.WarrantyVO;

/**
 * 三方接口提供的Service
 */
public interface TripartiteInsureService {

    /***
     * @description 三方投保接口
     *
     * @param warrantyVO
     * @return
     */
    ResponseDTO insure(WarrantyVO warrantyVO);
}
