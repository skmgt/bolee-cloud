package com.itheima.bolee.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.trade.PayChannelVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.trade.mapper.PayChannelMapper;
import com.itheima.bolee.trade.pojo.PayChannel;
import com.itheima.bolee.trade.service.IPayChannelService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description： 服务实现类
 */
@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements IPayChannelService {

    @Override
    public Page<PayChannel> findPayChannelVOPage(PayChannelVO payChannelVO, int pageNum, int pageSize) {
        Page<PayChannel> page = new Page<>(pageNum,pageSize);
        QueryWrapper<PayChannel> queryWrapper = new QueryWrapper<>();

        if (!EmptyUtil.isNullOrEmpty(payChannelVO.getId())) {
            queryWrapper.lambda().like(PayChannel::getId,payChannelVO.getId());
        }
        if (!EmptyUtil.isNullOrEmpty(payChannelVO.getChannelLabel())) {
            queryWrapper.lambda().eq(PayChannel::getChannelLabel,payChannelVO.getChannelLabel());
        }
        if (!EmptyUtil.isNullOrEmpty(payChannelVO.getChannelName())) {
            queryWrapper.lambda().likeRight(PayChannel::getChannelName,payChannelVO.getChannelName());
        }
        if (!EmptyUtil.isNullOrEmpty(payChannelVO.getDataState())) {
            queryWrapper.lambda().eq(PayChannel::getDataState,payChannelVO.getDataState());
        }
        queryWrapper.lambda().orderByAsc(PayChannel::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public PayChannel createPayChannel(PayChannelVO payChannelVO) {
        PayChannel payChannel = BeanConv.toBean(payChannelVO, PayChannel.class);
        payChannel.setOtherConfig(JSONObject.toJSONString(payChannelVO.getOtherConfigs()));
        boolean flag = save(payChannel);
        if (flag){
            return payChannel;
        }
        return null;
    }

    @Override
    public Boolean updatePayChannel(PayChannelVO payChannelVO) {
        PayChannel payChannel = BeanConv.toBean(payChannelVO, PayChannel.class);
        payChannel.setOtherConfig(JSONObject.toJSONString(payChannelVO.getOtherConfigs()));
        return updateById(payChannel);
    }

    @Override
    public Boolean deletePayChannel(String[] checkedIds) {
        List<String> ids = Arrays.asList(checkedIds);
        return removeByIds(ids);
    }

    @Override
    public PayChannel findPayChannelVlid(String channelLabel) {
        QueryWrapper<PayChannel> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PayChannel::getChannelLabel,channelLabel)
                .eq(PayChannel::getDataState, SuperConstant.DATA_STATE_0);
        return getOne(queryWrapper);
    }
}
