package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.security.pojo.DeptPostUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description：部门岗位用户关联表Mapper接口
 */
@Mapper
public interface DeptPostUserMapper extends BaseMapper<DeptPostUser> {

}
