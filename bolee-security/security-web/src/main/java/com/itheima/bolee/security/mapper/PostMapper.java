package com.itheima.bolee.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.security.pojo.Post;
import com.itheima.bolee.framework.commons.dto.security.PostVO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @Description：岗位表Mapper接口
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select({"SELECT ",
            "p.id,",
            "p.dept_no,",
            "p.post_no,",
            "p.post_name,",
            "p.sort_no,",
            "p.data_state,",
            "p.create_by,",
            "p.create_time,",
            "p.update_by,",
            "p.update_time,",
            "p.remark,",
            "FROM ",
            "tab_dept_post_user dpu ",
            "LEFT JOIN tab_post p ON dpu.post_no = p.post_no ",
            "WHERE p.data_state = '"+ SuperConstant.DATA_STATE_0+"' ",
            "AND dpu.user_id = #{userId}"})
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="dept_no", property="deptNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="post_no", property="postNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="post_name", property="postName", jdbcType=JdbcType.VARCHAR),
            @Result(column="sort_no", property="sortNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="data_state", property="dataState", jdbcType=JdbcType.CHAR),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR)
    })
    List<PostVO> findPostVOListByUserId(@Param("userId") Long userId);
}
