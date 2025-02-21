package com.itheima.bolee.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.sms.pojo.SmsBlacklist;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description：黑名单表Mapper接口
 */
@Mapper
public interface SmsBlacklistMapper extends BaseMapper<SmsBlacklist> {

}
