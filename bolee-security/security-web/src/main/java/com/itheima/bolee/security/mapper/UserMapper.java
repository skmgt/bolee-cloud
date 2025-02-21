package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.security.pojo.User;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Description：用户表Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select({"SELECT",
            "u.id ,",
            "u.user_name,",
            "u.open_id,",
            "u.password,",
            "u.user_type,",
            "u.avatar,",
            "u.nick_name,",
            "u.email,",
            "u.real_name,",
            "u.mobile,",
            "u.sex,",
            "u.data_state,",
            "u.create_by,",
            "u.create_time,",
            "u.update_by,",
            "u.update_time,",
            "u.remark,",
            "dpu.post_no",
            "FROM",
            "tab_dept_post_user dpu",
            "LEFT JOIN tab_user u ON dpu.user_id = u.id",
            "WHERE dpu.post_no = #{postNo} AND u.data_state='"+ SuperConstant.DATA_STATE_0+",",
            })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="open_id", property="openId", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_type", property="userType", jdbcType=JdbcType.VARCHAR),
            @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="real_name", property="realName", jdbcType=JdbcType.VARCHAR),
            @Result(column="mobile", property="mobile", jdbcType=JdbcType.VARCHAR),
            @Result(column="sex", property="sex", jdbcType=JdbcType.CHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="post_no", property="postNo", jdbcType=JdbcType.VARCHAR)
    })
    List<UserVO> findUserVOListByDeptNo(@Param("postNo") String postNo);

    @Select({"SELECT",
            "u.id ,",
            "u.user_name,",
            "u.open_id,",
            "u.password,",
            "u.user_type,",
            "u.avatar,",
            "u.nick_name,",
            "u.email,",
            "u.real_name,",
            "u.mobile,",
            "u.sex,",
            "u.data_state,",
            "u.create_by,",
            "u.create_time,",
            "u.update_by,",
            "u.update_time,",
            "u.remark,",
            "ur.role_id ",
            "FROM ",
            "tab_user_role ur ",
            "LEFT JOIN tab_user u ON ur.user_id = u.id ",
            "WHERE ur.role_id = #{roleId} AND u.data_state='"+ SuperConstant.DATA_STATE_0+",",
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR),
            @Result(column="open_id", property="openId", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_type", property="userType", jdbcType=JdbcType.VARCHAR),
            @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="real_name", property="realName", jdbcType=JdbcType.VARCHAR),
            @Result(column="mobile", property="mobile", jdbcType=JdbcType.VARCHAR),
            @Result(column="sex", property="sex", jdbcType=JdbcType.CHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.VARCHAR)
    })
    List<UserVO> findUserVOListByRoleId(@Param("roleId")Long roleId);
}
