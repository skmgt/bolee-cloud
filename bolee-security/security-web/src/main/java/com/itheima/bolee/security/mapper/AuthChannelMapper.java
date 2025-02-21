package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.security.pojo.AuthChannel;
import com.itheima.bolee.security.pojo.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description：授权三方表Mapper接口
 */
@Mapper
public interface AuthChannelMapper extends BaseMapper<AuthChannel> {

}
