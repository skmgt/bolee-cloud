package com.itheima.bolee.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.trade.pojo.Trade;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description：交易订单表Mapper接口
 */
@Mapper
public interface TradeMapper extends BaseMapper<Trade> {

}
