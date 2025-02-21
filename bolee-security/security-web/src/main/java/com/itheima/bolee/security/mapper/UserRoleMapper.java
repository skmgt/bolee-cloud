package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.security.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description：用户角色关联表Mapper接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
