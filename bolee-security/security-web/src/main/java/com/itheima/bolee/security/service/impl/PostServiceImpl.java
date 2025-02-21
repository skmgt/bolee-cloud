package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.PostCacheConstant;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import com.itheima.bolee.framework.commons.dto.security.PostVO;
import com.itheima.bolee.framework.commons.enums.security.PostEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.security.mapper.PostMapper;
import com.itheima.bolee.security.pojo.Dept;
import com.itheima.bolee.security.pojo.Post;
import com.itheima.bolee.security.service.IDeptService;
import com.itheima.bolee.security.service.IPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description：岗位表服务实现类
 */
@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    PostMapper postMapper;

    @Autowired
    IDeptService deptService;

    /***
     * @description 多条件查询
     * @param queryWrapper
     * @param postVO
     * @return
     */
    private QueryWrapper<Post> queryWrapper(QueryWrapper<Post> queryWrapper, PostVO postVO){
        //部门编号
        if (!EmptyUtil.isNullOrEmpty(postVO.getDeptNo())) {
            queryWrapper.lambda().likeRight(Post::getDeptNo, NoProcessing.processString(postVO.getDeptNo()));
        }
        //岗位编码：父部门编号+01【2位】查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getPostNo())) {
            queryWrapper.lambda().eq(Post::getPostNo,postVO.getPostNo());
        }
        //岗位名称查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getPostName())) {
            queryWrapper.lambda().likeRight(Post::getPostName,postVO.getPostName());
        }
        //显示顺序查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getSortNo())) {
            queryWrapper.lambda().eq(Post::getSortNo,postVO.getSortNo());
        }
        //创建者:username查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getCreateBy())) {
            queryWrapper.lambda().eq(Post::getCreateBy,postVO.getCreateBy());
        }
        //更新者:username查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Post::getUpdateBy,postVO.getUpdateBy());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getRemark())) {
            queryWrapper.lambda().eq(Post::getRemark,postVO.getRemark());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(postVO.getDataState())) {
            queryWrapper.lambda().eq(Post::getDataState,postVO.getDataState());
        }
        //按sortNo降序
        queryWrapper.lambda().orderByAsc(Post::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = PostCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#postVO.hashCode()")
    public Page<PostVO> findPostPage(PostVO postVO, int pageNum, int pageSize) {
        try {
            //查询职位
            //构建分页对象
            Page<Post> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,postVO);
            //执行分页查询
            Page<PostVO> pageVo = BeanConv.toPage(page(page, queryWrapper),PostVO.class);
            if (!EmptyUtil.isNullOrEmpty(pageVo.getRecords())){
                //对应部门
                Set<String> deptNos = pageVo.getRecords().stream().map(PostVO::getDeptNo).collect(Collectors.toSet());
                List<DeptVO> deptVOList = deptService.findDeptInDeptNos(deptNos);
                pageVo.getRecords().forEach(n->{
                    //装配部门
                    deptVOList.forEach(d->{
                        if (n.getDeptNo().equals(d.getDeptNo())){
                            n.setDeptVO(BeanConv.toBean(d,DeptVO.class));
                        }
                    });

                });
            }
            return pageVo;
        }catch (Exception e){
            log.error("岗位表PAGE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PostCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = PostCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =PostCacheConstant.BASIC,key = "#result.id")})
    public PostVO createPost(PostVO postVO) {
        try {
            //转换PostVO为Post
            Post post = BeanConv.toBean(postVO, Post.class);
            post.setPostNo(this.createPostNo(post.getDeptNo()));
            boolean flag = save(post);
            //装配部门
            if (flag){
                PostVO postVOResult = BeanConv.toBean(post, PostVO.class);
                QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(Dept::getDataState,SuperConstant.DATA_STATE_0)
                        .eq(Dept::getParentDeptNo,postVO.getDeptNo());
                Dept dept = deptService.getOne(queryWrapper);
                if (!EmptyUtil.isNullOrEmpty(dept)){
                    postVOResult.setDeptVO(BeanConv.toBean(dept,DeptVO.class));
                }
                return postVOResult;
            }
            return null;
        } catch (Exception e) {
            log.error("保存岗位表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = PostCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = PostCacheConstant.LIST,allEntries = true),
            @CacheEvict(value =PostCacheConstant.BASIC,key = "#postVO.id")})
    public Boolean updatePost(PostVO postVO) {
        try {
            //转换PostVO为Post
            Post post = BeanConv.toBean(postVO, Post.class);
            return updateById(post);
        } catch (Exception e) {
            log.error("修改岗位表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.UPDATE_FAIL);
        }

    }

    @Override
    @Cacheable(value = PostCacheConstant.LIST,key ="#postVO.hashCode()")
    public List<PostVO> findPostList(PostVO postVO) {
        try {
            //构建查询条件
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            this.queryWrapper(queryWrapper,postVO);
            List<PostVO> records = BeanConv.toBeanList(list(queryWrapper),PostVO.class);
            if (!EmptyUtil.isNullOrEmpty(records)){
                //对应部门
                Set<String> deptNos = records.stream().map(PostVO::getDeptNo).collect(Collectors.toSet());
                List<DeptVO> deptVOList = deptService.findDeptInDeptNos(deptNos);
                records.forEach(n->{
                    //装配部门
                    deptVOList.forEach(d->{
                        if (n.getDeptNo().equals(d.getDeptNo())){
                            n.setDeptVO(BeanConv.toBean(d,DeptVO.class));
                        }
                    });
                });
            }
            return records;
        } catch (Exception e) {
            log.error("查询岗位表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = PostCacheConstant.LIST,key ="#userId")
    public List<PostVO> findPostVOListByUserId(Long userId) {
        try {
            return postMapper.findPostVOListByUserId(userId);
        } catch (Exception e) {
            log.error("查询用户岗位异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.LIST_FAIL);
        }
    }

    @Override
    public String createPostNo(String deptNo) {
        try {
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Post::getDeptNo,deptNo);
            List<Post> postList = list(queryWrapper);
            //无下属节点则创建下属节点
            if (EmptyUtil.isNullOrEmpty(postList)){
                return NoProcessing.createNo(deptNo,false);
                //有下属节点则累加下属节点
            }else {
                Long postNo = postList.stream()
                        .map(post -> { return Long.valueOf(post.getPostNo());})
                        .max(Comparator.comparing(i -> i)).get();
                return NoProcessing.createNo(String.valueOf(postNo),true);
            }
        } catch (Exception e) {
            log.error("创建岗位编号异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PostEnum.CREATE_POST_NO_FAIL);
        }
    }
}
