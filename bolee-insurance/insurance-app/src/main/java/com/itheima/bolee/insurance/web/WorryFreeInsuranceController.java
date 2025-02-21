package com.itheima.bolee.insurance.web;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.WorryFreeCustomerInfoVO;
import com.itheima.bolee.insurance.dto.WorryFreeInsuranceMatchVO;
import com.itheima.bolee.insurance.dto.WorryFreeRiskItemVO;
import com.itheima.bolee.insurance.dto.WorryFreeSafeguardQuotaVO;
import com.itheima.bolee.insurance.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WorryFreeInsuranceController
 *
 * @author: wgl
 * @describe: 省心配controller
 * @date: 2022/12/28 10:10
 */
@Slf4j
@Api(tags = "省心配")
@RestController
@RequestMapping("worry-free-insurance")
public class WorryFreeInsuranceController {

    @Autowired
    private IWorryFreeInsuranceService worryFreeInsuranceService;

    @Autowired
    private IRuleService ruleService;

    @Autowired
    private IWorryFreeRiskItemService worryFreeRiskItemService;

    @Autowired
    private IWorryFreeFlowNodeService worryFreeFlowNodeService;

    @Autowired
    private IWorryFreeSafeguardQuotaService worryFreeSafeguardQuotaService;

    @Autowired
    private IWorryFreeInsuranceMatchService worryFreeInsuranceMatchService;
    /**
     * 省心配首页
     * @return
     */
    @PostMapping("/index")
    @ApiOperation(value = "省心配首页", notes = "省心配首页")
    public String index() {
        return "worryfree";
    }


    /**
     * 根据当前登录人从redis中获取对应的风险项
     * @return
     */
    @PostMapping("/flow-node/{customerInfoId}")
    @ApiOperation(value = "流程节点", notes = "流程节点")
    @ApiImplicitParam(paramType = "path",name = "customerInfoId",value = "用户信息ID",example = "1",dataType = "String")
    public ResponseResult<List<String>> flowNode(@PathVariable("customerInfoId") String customerInfoId) {
        //获取流程节点数据
        List<String> res = worryFreeFlowNodeService.flowNode(Long.valueOf(customerInfoId));
        return ResponseResultBuild.successBuild(res);
    }


    /**
     * 根据当前登录人从redis中获取对应的风险项
     * @return
     */
    @PostMapping("/safeguard-assess/{customerInfoId}")
    @ApiOperation(value = "保障评估", notes = "保障评估")
    @ApiImplicitParam(paramType = "path",name = "customerInfoId",value = "用户信息ID",example = "1",dataType = "String")
    public ResponseResult<WorryFreeSafeguardQuotaVO> safeguardAssess(@PathVariable("customerInfoId") String customerInfoId) {
        WorryFreeSafeguardQuotaVO res = worryFreeSafeguardQuotaService.findSaferQuota(Long.valueOf(customerInfoId));
        return ResponseResultBuild.successBuild(res);
    }


    /**
     * 根据当前登录人从redis中获取对应的风险项
     * @return
     */
    @PostMapping("/risk-analysis/{customerInfoId}")
    @ApiOperation(value = "风险分析", notes = "获取风险分析结果")
    @ApiImplicitParam(paramType = "path",name = "customerInfoId",value = "用户信息ID",example = "1",dataType = "String")
    public ResponseResult<List<WorryFreeRiskItemVO>> findMyRiskItem(@PathVariable("customerInfoId") String customerInfoId) {
        List<WorryFreeRiskItemVO> res = worryFreeRiskItemService.findMyRiskItem(Long.valueOf(customerInfoId));
        return ResponseResultBuild.successBuild(res);
    }


    /**
     * 开始结算接口
     * @return
     */
    @PostMapping("/do-calculate")
    @ApiOperation(value = "省心配核心：开始计算", notes = "省心配核心：开始计算")
    public ResponseResult<WorryFreeCustomerInfoVO> worryFree(@RequestBody WorryFreeCustomerInfoVO worryFreeCustomerInfoVO) {
        ruleService.buildBaseParams(worryFreeCustomerInfoVO);
        WorryFreeCustomerInfoVO res = ruleService.worryFreePairing(worryFreeCustomerInfoVO);
        return ResponseResultBuild.successBuild(res);
    }

    /**
     * 获取推荐产品列表
     * @return
     */
    @PostMapping("/product-list/{customerInfoId}")
    @ApiOperation(value = "省心配核心：获取推荐产品列表", notes = "省心配核心：获取推荐产品列表")
    @ApiImplicitParam(paramType = "path",name = "customerInfoId",value = "用户信息ID",example = "1",dataType = "String")
    public ResponseResult<List<WorryFreeInsuranceMatchVO>> productList(@PathVariable("customerInfoId") String customerInfoId) {
        List<WorryFreeInsuranceMatchVO> res = worryFreeInsuranceMatchService.productList(Long.valueOf(customerInfoId));
        return ResponseResultBuild.successBuild(res);
    }
}
