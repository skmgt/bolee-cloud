package com.itheima.bolee.trade.face.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.dto.trade.SignContractVO;
import com.itheima.bolee.framework.commons.enums.trade.SignContractEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.face.SignContractFace;
import com.itheima.bolee.trade.mapper.SignContractMapper;
import com.itheima.bolee.trade.pojo.SignContract;
import com.itheima.bolee.trade.service.ISignContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description： 签约合成服务实现类
 */
@Component
public class SignContractFaceImpl implements SignContractFace {

    @Autowired
    ISignContractService signContractService;

    @Override
    public Page<SignContractVO> findSignContractPage(SignContractVO signContractVO, int pageNum, int pageSize) {
        try {
            Page<SignContract> signContractPage = signContractService
                .findSignContractPage(signContractVO, pageNum, pageSize);
            return BeanConv.toPage(signContractPage,SignContractVO.class);
        }catch (Exception e){
            throw new ProjectException(SignContractEnum.PAGE_FAIL);
        }

    }

    @Override
    public SignContractVO createSignContract(SignContractVO signContractVO) {
        try {
            return BeanConv.toBean(signContractService.createSignContract(signContractVO),SignContractVO.class);
        }catch (Exception e){
            throw new ProjectException(SignContractEnum.SAVE_FAIL);
        }
    }

    @Override
    public Boolean updateSignContract(SignContractVO signContractVO) {
        try {
            return signContractService.updateSignContract(signContractVO);
        }catch (Exception e){
            throw new ProjectException(SignContractEnum.UPDATE_FAIL);
        }
    }

    @Override
    public Boolean deleteSignContract(String[] checkedIds) {
        try {
            return signContractService.deleteSignContract(checkedIds);
        }catch (Exception e){
            throw new ProjectException(SignContractEnum.DELETE_FAIL);
        }
    }

    @Override
    public List<SignContractVO> findSignContractList(SignContractVO signContractVO) {
        try {
            return BeanConv.toBeanList(signContractService
                .findSignContractList(signContractVO),SignContractVO.class);
        }catch (Exception e){
            throw new ProjectException(SignContractEnum.LIST_FAIL);
        }
    }

}
