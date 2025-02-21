package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.security.pojo.Dept;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Description：部门表Mapper接口
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    @Select({"SELECT",
            "d.id,",
            "d.dept_no,",
            "d.dept_name,",
            "d.sort_no,",
            "d.data_state,",
            "d.create_by,",
            "d.create_time,",
            "d.update_by,",
            "d.update_time,",
            "FROM ",
            "tab_dept_post_user dpu ",
            "LEFT JOIN tab_dept d ON dpu.dept_no = d.dept_no ",
            "WHERE d.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND dpu.user_id = #{userId}"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="dept_no", property="deptNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name", property="deptName", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<DeptVO> findDeptVOListByUserId(@Param("userId") Long userId);

    @Select({"<script>" ,
            "SELECT",
            "d.id,",
            "d.dept_no,",
            "d.dept_name,",
            "d.sort_no,",
            "d.data_state,",
            "d.create_by,",
            "d.create_time,",
            "d.update_by,",
            "d.update_time,",
            "rd.role_id ",
            "FROM ",
            "tab_role_dept rd ",
            "LEFT JOIN tab_dept d ON rd.dept_no = d.dept_no ",
            "WHERE d.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND rd.role_id in (",
            "<foreach collection='roleIds' separator=',' item='roleId'>",
            "#{roleId}",
            "</foreach>",
            ")</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="dept_no", property="deptNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="dept_name", property="deptName", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT)
    })
    List<DeptVO> findDeptVOListInRoleId(@Param("roleIds") List<Long> roleIds);
}
