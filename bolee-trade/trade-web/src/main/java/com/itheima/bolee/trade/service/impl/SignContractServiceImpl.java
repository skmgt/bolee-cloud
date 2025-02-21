package com.itheima.bolee.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.trade.SignContractConstant;
import com.itheima.bolee.framework.commons.dto.trade.SignContractVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.mapper.SignContractMapper;
import com.itheima.bolee.trade.pojo.SignContract;
import com.itheima.bolee.trade.service.ISignContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Description： 签约合成服务实现类
 */
@Service
public class SignContractServiceImpl extends ServiceImpl<SignContractMapper, SignContract> implements ISignContractService {

    /***
     * @description 签约合成多条件组合
     * @param signContract 签约合成
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<SignContract> queryWrapper(SignContractVO signContract){
        QueryWrapper<SignContract> queryWrapper = new QueryWrapper<>();
        //合同编号:关联业务
        if (!EmptyUtil.isNullOrEmpty(signContract.getContractNo())) {
            queryWrapper.lambda().eq(SignContract::getContractNo,signContract.getContractNo());
        }
        //商户签约号:关联支付
        if (!EmptyUtil.isNullOrEmpty(signContract.getExternalAgreementNo())) {
            queryWrapper.lambda().eq(SignContract::getExternalAgreementNo,signContract.getExternalAgreementNo());
        }
        //理赔项值查询
        if (!EmptyUtil.isNullOrEmpty(signContract.getSignState())) {
            queryWrapper.lambda().eq(SignContract::getSignState,signContract.getSignState());
        }
        //支付渠道【支付宝、微信】
        if (!EmptyUtil.isNullOrEmpty(signContract.getTradeChannel())) {
            queryWrapper.lambda().eq(SignContract::getTradeChannel,signContract.getTradeChannel());
        }
        //周期类型
        if (!EmptyUtil.isNullOrEmpty(signContract.getRulePeriodType())) {
            queryWrapper.lambda().eq(SignContract::getRulePeriodType,signContract.getRulePeriodType());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SignContract::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Page<SignContract> findSignContractPage(SignContractVO signContractVO, int pageNum, int pageSize) {
        Page<SignContract> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SignContract> queryWrapper =queryWrapper(signContractVO);
        return page(page, queryWrapper);
    }

    @Override
    public SignContract createSignContract(SignContractVO signContractVO) {
        SignContract signContract = BeanConv.toBean(signContractVO, SignContract.class);
        boolean flag = save(signContract);
        if (flag){
            return signContract;
        }
        return null;
    }

    @Override
    public Boolean updateSignContract(SignContractVO signContractVO) {
        SignContract signContract = BeanConv.toBean(signContractVO, SignContract.class);
        return updateById(signContract);
    }

    @Override
    public Boolean deleteSignContract(String[] checkedIds) {
        List<String> ids = Arrays.asList(checkedIds);
        return removeByIds(ids);
    }

    @Override
    public List<SignContract> findSignContractList(SignContractVO signContractVO) {
        QueryWrapper<SignContract> queryWrapper =queryWrapper(signContractVO);
        return list(queryWrapper);
    }

    @Override
    public SignContract findSignContractByContract_no(String contractNo) {
        QueryWrapper<SignContract> queryWrapper= new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(SignContract::getContractNo,contractNo);
        queryWrapper.lambda().and(QueryWrapper->{
            QueryWrapper
                .eq(SignContract::getSignState,SignContractConstant.SIGNSTATE_NORMAL)
                .or()
                .eq(SignContract::getSignState,SignContractConstant.SIGNSTATE_TEMP);
        });
        return getOne(queryWrapper);
    }

    @Override
    public Boolean h5CloseSignContract(String warrantyNo, String tradingChannel) {
        UpdateWrapper<SignContract> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
            .set(SignContract::getSignState,SignContractConstant.SIGNSTATE_STOP)
            .eq(SignContract::getContractNo,warrantyNo)
            .eq(SignContract::getTradeChannel,tradingChannel);
        return update(updateWrapper);
    }

    @Override
    @Transactional
    public Boolean signContractSync(String warrantyNo, String tradingChannel, String agreementNo) {
        UpdateWrapper<SignContract> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
            .set(SignContract::getSignState,SignContractConstant.SIGNSTATE_NORMAL)
            .set(SignContract::getAgreementNo,agreementNo)
            .eq(SignContract::getContractNo,warrantyNo)
            .eq(SignContract::getTradeChannel,tradingChannel);
        return update(updateWrapper);
    }

}
