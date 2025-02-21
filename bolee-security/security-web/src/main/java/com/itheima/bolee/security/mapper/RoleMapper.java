package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.security.pojo.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Description：角色表Mapper接口
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select({"<script>",
            "SELECT",
            "r.id,",
            "r.role_name,",
            "r.label,",
            "r.sort_no,",
            "r.data_state,",
            "r.create_by,",
            "r.create_time,",
            "r.update_by,",
            "r.update_time,",
            "ur.user_id,",
            "r.remark ",
            "FROM ",
            "tab_user_role ur ",
            "LEFT JOIN tab_role r ON ur.role_id = r.id ",
            "WHERE r.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND ur.user_id IN (" ,
            "<foreach collection='userIds' separator=',' item='userId'>",
            "#{userId}",
            "</foreach> ",
            ")</script>"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="role_name", property="roleName", jdbcType=JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<RoleVO> findRoleVOListInUserId(@Param("userIds")List<Long> userIds);

    @Select({"SELECT",
            "r.id,",
            "r.role_name,",
            "r.label,",
            "r.sort_no,",
            "r.data_state,",
            "r.create_by,",
            "r.create_time,",
            "r.update_by,",
            "r.update_time,",
            "r.remark ",
            "FROM ",
            "tab_role_resource rr ",
            "LEFT JOIN tab_role r ON rr.role_id = r.id ",
            "WHERE r.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND rr.resource_no = #{resourceNo}"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="role_name", property="roleName", jdbcType=JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<RoleVO> findRoleVOListByResourceNo(String resourceNo);

    @Select({"SELECT",
            "r.id,",
            "r.role_name,",
            "r.label,",
            "r.sort_no,",
            "r.data_state,",
            "r.create_by,",
            "r.create_time,",
            "r.update_by,",
            "r.update_time,",
            "r.remark,",
            "r.data_scope ",
            "FROM ",
            "tab_user_role ur ",
            "LEFT JOIN tab_role r ON ur.role_id = r.id ",
            "WHERE r.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND ur.user_id = #{userId}"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="role_name", property="roleName", jdbcType=JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_scope", property="dataScope", jdbcType=JdbcType.VARCHAR)
    })
    List<RoleVO> findRoleVOListByUserId(@Param("userId") Long userId);
}
