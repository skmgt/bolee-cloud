package com.itheima.bolee.insurance.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.dto.HomePeopleVO;
import com.itheima.bolee.insurance.dto.SelfWarrantyVO;
import com.itheima.bolee.insurance.service.ICustomerRelationService;
import com.itheima.bolee.insurance.service.IRuleService;
import com.itheima.bolee.insurance.service.IWarrantyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WarrantyController.java
 * @Description 家庭保障
 */
@Slf4j
@Api(tags = "我的家庭")
@RestController
@RequestMapping("mine-home")
public class MineHomeController {

    @Autowired
    private IWarrantyService warrantyService;

    @Autowired
    private IRuleService ruleService;

    @Autowired
    private ICustomerRelationService customerRelationService;

    /**
     * 获取我的保单
     */
    @PostMapping("home-all-warranty-order")
    @ApiOperation(value = "家庭全部保单数量", notes = "家庭全部保单数量")
    public ResponseResult<SelfWarrantyVO> myRelationsFields() {
        return ResponseResultBuild.successBuild(warrantyService.findMyWarrantyNums(SubjectContent.getUserVO()));
    }

    /**
     * 家庭人身保障
     */
    @PostMapping("home-people-safe")
    @ApiOperation(value = "家庭人身保障", notes = "家庭人身保障")
    public ResponseResult<List<HomePeopleVO>> homePeopleSafe() {
        return ResponseResultBuild.successBuild(warrantyService.findHomeSafeInfo(SubjectContent.getUserVO()));
    }

    /**
     * 添加人物关系
     */
    @PostMapping("customer-relation")
    @ApiOperation(value = "添加人物关系", notes = "添加人物关系")
    @ApiImplicitParam(name = "customerRelationVO", value = "用户关系", required = false, dataType = "CustomerRelationVO")
    public ResponseResult<CustomerRelationVO> addCustomerRelation(@Valid @RequestBody CustomerRelationVO customerRelationVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseResultBuild.validateErrorBuild(null);
        }else {
            return ResponseResultBuild.successBuild(customerRelationService.save(customerRelationVO));
        }
    }

    /**
     * 修改人物关系
     */
    @PostMapping("update-customer-relation")
    @ApiOperation(value = "修改人物关系", notes = "修改人物关系")
    @ApiImplicitParam(name = "customerRelationVO", value = "用户关系", required = false, dataType = "CustomerRelationVO")
    public ResponseResult<CustomerRelationVO> updateHomePeopleSafe(@Valid @RequestBody CustomerRelationVO customerRelationVO,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 返回包含错误信息的响应
            return ResponseResultBuild.validateErrorBuild(null);
        }else {
            customerRelationService.update(customerRelationVO);
            return ResponseResultBuild.successBuild(customerRelationVO);
        }
    }

    /**
     * 添加人物关系
     */
    @PostMapping("delete-customer-relation")
    @ApiOperation(value = "删除人物关系", notes = "删除人物关系")
    @ApiImplicitParam(name = "customerRelationVO", value = "用户关系", required = false, dataType = "CustomerRelationVO")
    @ApiOperationSupport(includeParameters = {"customerRelationVO.checkedIds"})
    public ResponseResult<Boolean> deleteHomePeopleSafe(@RequestBody CustomerRelationVO customerRelationVO) {
        return ResponseResultBuild.successBuild(customerRelationService.delete(customerRelationVO.getCheckedIds()));
    }
}
