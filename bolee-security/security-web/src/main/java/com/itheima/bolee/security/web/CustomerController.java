package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.CustomerEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.security.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：客户前端控制器
 */
@Slf4j
@Api(tags = "客户管理")
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    /***
     * @description 多条件查询客户分页列表
     * @param customerVO 客户Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CustomerVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "客户分页",notes = "客户分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "customerVO",value = "客户Vo对象",required = true,dataType = "CustomerVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<CustomerVO>> findCustomerVOPage(
                                    @RequestBody CustomerVO customerVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<CustomerVO> customerVOPage = customerService.findCustomerPage(customerVO, pageNum, pageSize);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,customerVOPage);
    }

    /**
     * @Description 修改客户
     * @param customerVO 客户Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "客户修改",notes = "客户修改")
    @ApiImplicitParam(name = "customerVO",value = "客户Vo对象",required = true,dataType = "CustomerVO")
    public ResponseResult<Boolean> updateCustomer(@RequestBody CustomerVO customerVO) {
        Boolean flag = customerService.updateCustomer(customerVO);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询客户列表
     * @param customerVO 客户Vo对象
     * @return List<CustomerVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "客户列表",notes = "客户列表")
    @ApiImplicitParam(name = "customerVO",value = "客户Vo对象",required = true,dataType = "CustomerVO")
    public ResponseResult<List<CustomerVO>> customerList(@RequestBody CustomerVO customerVO) {
        List<CustomerVO> customerVOList = customerService.findCustomerList(customerVO);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,customerVOList);
    }

    @PostMapping("current-customer")
    @ApiOperation(value = "当前客户",notes = "当前客户")
    ResponseResult<CustomerVO> findCurrentCustomer()  {
        CustomerVO customerVO = BeanConv.toBean(SubjectContent.getUserVO(),CustomerVO.class);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,customerVO);
    }

    /**
     * @Description 重置密码
     * @param customerId 客户Vo对象
     * @return Boolean 是否修改成功
     */
    @PostMapping("reset-passwords/{customerId}")
    @ApiOperation(value = "密码重置",notes = "密码重置")
    @ApiImplicitParam(paramType = "path",name = "customerId",value = "用戶Id",required = true,dataType = "String")
    public ResponseResult<Boolean> resetPasswords(@PathVariable("customerId") String customerId) {
        Boolean flag = customerService.resetPasswords(customerId);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,flag);
    }

    /**
     * @Description 登录验证码
     * @param mobile 手机号码
     * @return
     */
    @PostMapping("loginCode/{mobile}")
    @ApiOperation(value = "登录验证码",notes = "登录验证码")
    @ApiImplicitParam(name = "mobile",value = "手机号",required = true,dataType = "String")
    ResponseResult<Boolean> loginCode(@PathVariable("mobile")String mobile) {
        Boolean flag = customerService.sendLoginCode(mobile);
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,flag);
    }

    /**
     * @Description 注册
     * @param customerVO 客户信息
     * @return
     */
    @ApiOperation(value = "注册客户",notes = "注册客户")
    @ApiImplicitParam(name = "customerVO",value = "客户Vo对象",required = true,dataType = "CustomerVO")
    @PostMapping("register-user")
    ResponseResult<UserVO> registerUser(@RequestBody CustomerVO customerVO){
        return ResponseResultBuild.build(CustomerEnum.SUCCEED,BeanConv.toBean(customerService.createCustomer(customerVO),UserVO.class));
    }

}
