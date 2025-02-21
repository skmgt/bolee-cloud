package com.itheima.bolee.insurance.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.service.ICustomerCardService;
import com.itheima.bolee.insurance.service.ICustomerInfoService;
import com.itheima.bolee.insurance.service.ICustomerRelationService;
import com.itheima.bolee.insurance.service.IWarrantyService;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.wenxin.WenXinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName MineController.java
 * @Description 我的
 */
@Slf4j
@Api(tags = "我的tab页")
@RestController
@RequestMapping("mined")
public class MineController {

    @Autowired
    private CustomerFeign customerFeign;

    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private ICustomerCardService customerCardService;

    @Autowired
    private ICustomerRelationService customerRelationService;

    @Autowired
    private WenXinService wenXinService;

    /**
     * 01、注册页面：调用security-interface中的CustomerFeign接口
     */
    @PostMapping("register")
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParam(name = "customerVO", value = "客户对象VO", dataType = "CustomerVO")
    public ResponseResult<UserVO> register(@RequestBody CustomerVO customerVO) {
        UserVO userVO = customerFeign.registerUser(customerVO);
        userVO.setPassword("");
        return ResponseResultBuild.successBuild(userVO);
    }

    //03、重置密码：必须登录才能重置密码，必须是完成手机号码补充内容
    @PostMapping("reset-password")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public ResponseResult<Boolean> resetPassword() {
        UserVO userVO = SubjectContent.getUserVO();
        return ResponseResultBuild.successBuild(customerInfoService.resetPassword(userVO));
    }

    /**
     * 07、我的关系-->tab_customer_relation
     */
    @PostMapping("my-relation")
    @ApiOperation(value = "根据当前登录人信息获取关系网", notes = "根据当前登录人信息获取关系网")
    public ResponseResult<List<CustomerRelationVO>> myRelation() {
        UserVO userVO = SubjectContent.getUserVO();
        CustomerRelationVO relationVO = CustomerRelationVO.builder()
            .customerId(userVO.getId())
            .dataState(SuperConstant.DATA_STATE_0)
            .build();
        List<CustomerRelationVO> res = customerRelationService.myRelation(relationVO);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 根据关系字段获取对应的亲属
     *
     * @param fields
     */
    @PostMapping("my-relation-field/{relationgFields}")
    @ApiOperation(value = "根据关系字段获取当前登录人信息获取关系网", notes = "根据关系字段获取当前登录人信息获取关系网")
    @ApiImplicitParam(name = "relationgFields", value = "关系字段列表(用逗号分隔)", required = true, dataType = "String")
    public ResponseResult<List<CustomerRelationVO>> myRelationsFields(@PathVariable("relationgFields") String fields) {
        CustomerRelationVO relationVO = CustomerRelationVO.builder().
                dataState(SuperConstant.DATA_STATE_0).
                customerId(SubjectContent.getUserVO().getId()).
                relations(customerRelationService.getRelations(fields)).
                build();
        List<CustomerRelationVO> res = customerRelationService.myRelation(relationVO);
        return ResponseResultBuild.successBuild(res);
    }



    /**
     * 根据关系字段获取对应的亲属
     */
    @PostMapping("ocr-access")
    @ApiOperation(value = "获取百度云token", notes = "获取百度云token")
    public ResponseResult<BaiduCloudTokenVO> myRelationsFields() {
        BaiduCloudTokenVO ocrToken = customerInfoService.getOcrToken(SubjectContent.getUserVO());
        return ResponseResultBuild.successBuild(ocrToken);
    }

    @Autowired
    IWarrantyService warrantyService;

    /***
     * @description 我的保单合同列表
     * @return 保单列表
     */
    @PostMapping("my-warrantys-nums")
    @ApiOperation(value = "我的保单数量", notes = "我的保单数量")
    public ResponseResult<MyWarrantyInfoVO> myWarrantyNums(){
        MyWarrantyInfoVO res = warrantyService.myWarrantyNums(SubjectContent.getUserVO());
        return ResponseResultBuild.successBuild(res);
    }

    /***
     * @description 我的保单合同列表
     * @return 保单列表
     */
    @PostMapping("my-warrantys")
    @ApiOperation(value = "我的保单列表", notes = "我的保单列表")
    public ResponseResult<List<WarrantyVO>> myWarrantyVOs(@RequestBody(required = false) CustomerRelationVO customerRelationVO){
        List<WarrantyVO> warrantyVOs = warrantyService.myWarrantyVOs(SubjectContent.getUserVO(),customerRelationVO);
        return ResponseResultBuild.successBuild(warrantyVOs);
    }

    /**
     * @Description 保单详情
     * @param warrantyId 保险合同ID
     * @return WarrantyVO
     */
    @PostMapping("detail/{warrantyId}")
    @ApiOperation(value = "保单详情",notes = "保单详情")
    @ApiImplicitParam(paramType = "path",name = "warrantyId",value = "合同ID",dataType = "String")
    public ResponseResult<WarrantyVO> createWarranty(@PathVariable("warrantyId") String warrantyId) {
        WarrantyVO warrantyVOResult = warrantyService.findById(warrantyId);
        return ResponseResultBuild.successBuild(warrantyVOResult);
    }

    /**
     * 根据关系字段获取对应的亲属
     */
    @PostMapping("find-risk-solution")
    @ApiOperation(value = "获取我的风险和解决方案", notes = "向文心一言请求风险和解决方案")
    public ResponseResult<String> findRiskAndSolution() {
        String question = wenXinService.askQuestion(null);
        return ResponseResultBuild.successBuild(question);
    }

    /**
     * 银行卡列表
     */
    @PostMapping("bank-card-list")
    @ApiOperation(value = "我的银行卡列表", notes = "我的银行卡列表")
    public ResponseResult<List<CustomerCardVO>> bankCardList() {
        CustomerCardVO customerCardVO = CustomerCardVO.builder().
            dataState(SuperConstant.DATA_STATE_0).
            customerId(String.valueOf(SubjectContent.getUserVO().getId())).
            build();
        List<CustomerCardVO> res = customerCardService.bankCardList(customerCardVO);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 添加银行卡
     */
    @PostMapping("bank-card")
    @ApiOperation(value = "添加银行卡", notes = "添加银行卡")
    public ResponseResult<CustomerCardVO> saveBankCard(@RequestBody CustomerCardVO customerCardVO) {
        CustomerCardVO res = customerCardService.saveBankCard(customerCardVO);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 人脸识别认证
     */
    @PostMapping("face-check")
    @ApiOperation(value = "人脸识别认证", notes = "人脸识别认证")
    public ResponseResult<Boolean> faceCheck() {
        Boolean res = customerInfoService.faceCheck();
        return ResponseResultBuild.successBuild(res);
    }


    /**
     * 身份证号存储
     */
    @PostMapping("id-card")
    @ApiOperation(value = "身份证号存储", notes = "身份证号存储")
    public ResponseResult<CustomerInfoVO> idCard(@RequestBody CustomerInfoVO customerInfo) {
        CustomerInfoVO res = customerInfoService.idCard(customerInfo);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 身份证号存储
     */
    @PostMapping("id-card-info")
    @ApiOperation(value = "身份证信息", notes = "身份证信息")
    public ResponseResult<CustomerInfoVO> idCardInfo() {
        CustomerInfoVO res = customerInfoService.idCardInfo();
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 修改默认使用银行卡
     */
    @PostMapping("default-bank-card/{customerCardId}")
    @ApiOperation(value = "修改默认使用银行卡", notes = "修改默认使用银行卡")
    public ResponseResult<CustomerCardVO> defaultBankCard(@PathVariable("customerCardId") String customerCardId) {
        CustomerCardVO res = customerCardService.defaultBankCard(customerCardId);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 解绑银行卡
     */
    @PostMapping("remove-bank-card")
    @ApiOperation(value = "解绑银行卡", notes = "解绑银行卡")
    public ResponseResult<CustomerCardVO> removeBankCarrd(@RequestBody CustomerCardVO customerCardVO) {
        CustomerCardVO res = customerCardService.removeBankCard(customerCardVO);
        return ResponseResultBuild.successBuild(res);
    }
}
