package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.PostVO;
import com.itheima.bolee.framework.commons.enums.security.DeptEnum;
import com.itheima.bolee.framework.commons.enums.security.PostEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.IPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：岗位前端控制器
 */
@Slf4j
@Api(tags = "岗位管理")
@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    IPostService postService;

    /***
     * @description 多条件查询岗位分页列表
     * @param postVO 岗位Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<PostVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "岗位分页",notes = "岗位分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "postVO",value = "岗位Vo对象",required = true,dataType = "PostVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<PostVO>> findPostVOPage(
                                    @RequestBody PostVO postVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<PostVO> postVOPage = postService.findPostPage(postVO, pageNum, pageSize);
        return ResponseResultBuild.build(PostEnum.SUCCEED,postVOPage);
    }

    /**
     * @Description 保存岗位
     * @param postVO 岗位Vo对象
     * @return PostVO
     */
    @PutMapping
    @ApiOperation(value = "岗位添加",notes = "岗位添加")
    @ApiImplicitParam(name = "postVO",value = "岗位Vo对象",required = true,dataType = "PostVO")
    public ResponseResult<PostVO> createPost(@RequestBody PostVO postVO) {
        PostVO postVOResult = postService.createPost(postVO);
        return ResponseResultBuild.build(PostEnum.SUCCEED,postVOResult);
    }

    /**
     * @Description 修改岗位
     * @param postVO 岗位Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "岗位修改",notes = "岗位修改")
    @ApiImplicitParam(name = "postVO",value = "岗位Vo对象",required = true,dataType = "PostVO")
    public ResponseResult<Boolean> updatePost(@RequestBody PostVO postVO) {
        Boolean flag = postService.updatePost(postVO);
        return ResponseResultBuild.build(PostEnum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询岗位列表
     * @param postVO 岗位Vo对象
     * @return List<PostVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "岗位列表",notes = "岗位列表")
    @ApiImplicitParam(name = "postVO",value = "岗位Vo对象",required = true,dataType = "PostVO")
    public ResponseResult<List<PostVO>> postList(@RequestBody PostVO postVO) {
        List<PostVO> postVOList = postService.findPostList(postVO);
        return ResponseResultBuild.build(PostEnum.SUCCEED,postVOList);
    }

}
