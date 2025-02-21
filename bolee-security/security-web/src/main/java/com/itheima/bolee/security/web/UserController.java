package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.UserEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.security.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：用户前端控制器
 */
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    IUserService userService;

    /***
     * @description 多条件查询用户分页列表
     * @param userVO 用户Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<UserVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "用户分页",notes = "用户分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userVO",value = "用户Vo对象",required = true,dataType = "UserVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<UserVO>> findUserVOPage(
                                    @RequestBody UserVO userVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<UserVO> userVOPage = userService.findUserPage(userVO, pageNum, pageSize);
        return ResponseResultBuild.build(UserEnum.SUCCEED,userVOPage);
    }

    /**
     * @Description 保存用户
     * @param userVO 用户Vo对象
     * @return UserVO
     */
    @PutMapping
    @ApiOperation(value = "用户添加",notes = "用户添加")
    @ApiImplicitParam(name = "userVO",value = "用户Vo对象",required = true,dataType = "UserVO")
    public ResponseResult<UserVO> createUser(@RequestBody UserVO userVO) {
        UserVO userVOResult = userService.createUser(userVO);
        return ResponseResultBuild.build(UserEnum.SUCCEED,userVOResult);
    }

    /**
     * @Description 修改用户
     * @param userVO 用户Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "用户修改",notes = "用户修改")
    @ApiImplicitParam(name = "userVO",value = "用户Vo对象",required = true,dataType = "UserVO")
    public ResponseResult<Boolean> updateUser(@RequestBody UserVO userVO) {
        Boolean flag = userService.updateUser(userVO);
        return ResponseResultBuild.build(UserEnum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询用户列表
     * @param userVO 用户Vo对象
     * @return List<UserVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "用户列表",notes = "用户列表")
    @ApiImplicitParam(name = "userVO",value = "用户Vo对象",required = true,dataType = "UserVO")
    public ResponseResult<List<UserVO>> userList(@RequestBody UserVO userVO) {
        List<UserVO> userVOList = userService.findUserList(userVO);
        return ResponseResultBuild.build(UserEnum.SUCCEED,userVOList);
    }

    @PostMapping("current-user")
    @ApiOperation(value = "当前用户",notes = "当前用户")
    ResponseResult<UserVO> findCurrentUser()  {
        UserVO userVO = SubjectContent.getUserVO();
        return ResponseResultBuild.build(UserEnum.SUCCEED,userVO);
    }

    /**
     * @Description 重置密码
     * @param userId 用户Vo对象
     * @return Boolean 是否修改成功
     */
    @PostMapping("reset-passwords/{userId}")
    @ApiOperation(value = "密码重置",notes = "密码重置")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用戶Id",required = true,dataType = "String")
    public ResponseResult<Boolean> resetPasswords(@PathVariable("userId") String userId) {
        Boolean flag = userService.resetPasswords(userId);
        return ResponseResultBuild.build(UserEnum.SUCCEED,flag);
    }

}
