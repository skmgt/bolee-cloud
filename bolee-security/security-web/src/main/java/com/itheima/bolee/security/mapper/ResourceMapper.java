package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.ResourceVO;
import com.itheima.bolee.security.pojo.Resource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Description：权限表Mapper接口
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    @Select({"<script>",
            "SELECT",
            "r.id,",
            "r.resource_no,",
            "r.parent_resource_no,",
            "r.resource_name,",
            "r.resource_type,",
            "r.request_path,",
            "r.label,",
            "r.data_state,",
            "r.sort_no,",
            "r.icon,",
            "r.create_by,",
            "r.create_time,",
            "r.update_by,",
            "r.update_time,",
            "r.remark,",
            "rr.role_id ",
            "FROM ",
            "tab_role_resource rr ",
            "LEFT JOIN tab_resource r ON rr.resource_no = r.resource_no ",
            "WHERE r.data_state = '"+SuperConstant.DATA_STATE_0+"' ",
            "AND rr.role_id IN (" ,
            "<foreach collection='roleIds' separator=',' item='roleId'>",
            "#{roleId}",
            "</foreach> ",
            ")</script>"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="resource_no", property="resourceNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="parent_resource_no", property="parentResourceNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="resource_name", property="resourceName", jdbcType=JdbcType.VARCHAR),
            @Result(column="resource_type", property="resourceType", jdbcType=JdbcType.CHAR),
            @Result(column="request_path", property="requestPath", jdbcType=JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.INTEGER),
            @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.VARCHAR)
    })
    List<ResourceVO> findResourceVOListInRoleId(@Param("roleIds") List<Long> roleIds);

    @Select({"SELECT",
            "r.id,",
            "r.resource_no,",
            "r.parent_resource_no,",
            "r.resource_name,",
            "r.resource_type,",
            "r.request_path,",
            "r.label,",
            "r.data_state,",
            "r.sort_no,",
            "r.icon,",
            "r.create_by,",
            "r.create_time,",
            "r.update_by,",
            "r.update_time,",
            "r.remark ",
            "FROM ",
            "tab_role_resource rr ",
            "LEFT JOIN tab_user_role ur ON ur.role_id = rr.role_id ",
            "LEFT JOIN tab_resource r ON rr.resource_no = r.resource_no ",
            "WHERE r.data_state = '"+SuperConstant.DATA_STATE_0+"' ",
            "AND ur.user_id = #{userId}"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="resource_no", property="resourceNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="parent_resource_no", property="parentResourceNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="resource_name", property="resourceName", jdbcType=JdbcType.VARCHAR),
            @Result(column="resource_type", property="resourceType", jdbcType=JdbcType.CHAR),
            @Result(column="request_path", property="requestPath", jdbcType=JdbcType.VARCHAR),
            @Result(column="label", property="label", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.INTEGER),
            @Result(column="icon", property="icon", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<ResourceVO> findResourceVOListByUserId(@Param("userId") Long userId);

}
