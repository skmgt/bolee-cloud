package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.security.pojo.Post;
import com.itheima.bolee.framework.commons.dto.security.PostVO;

import java.util.List;

/**
 * @Description：岗位表服务类
 */
public interface IPostService extends IService<Post> {

    /**
     * @Description 多条件查询岗位表分页列表
     * @param postVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<PostVO>
     */
    Page<PostVO> findPostPage(PostVO postVO, int pageNum, int pageSize);

    /**
     * @Description 创建岗位表
     * @param postVO 对象信息
     * @return PostVO
     */
    PostVO createPost(PostVO postVO);

    /**
     * @Description 修改岗位表
     * @param postVO 对象信息
     * @return Boolean
     */
    Boolean updatePost(PostVO postVO);

    /**
     * @description 多条件查询岗位表列表
     * @param postVO 查询条件
     * @return: List<PostVO>
     */
    List<PostVO> findPostList(PostVO postVO);

    /**
     * @description 人员对应职位
     * @param userId 查询条件
     * @return: List<PostVO>
     */
    List<PostVO> findPostVOListByUserId(Long userId);

    /**
     * @Description 创建编号
     * @param deptNo 部门编号
     * @return
     */
    String createPostNo(String deptNo);
}
