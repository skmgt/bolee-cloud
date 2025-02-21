package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.service.IInsuranceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：保险产品响应接口
 */
@Slf4j
@Api(tags = "保险产品")
@RestController
@RequestMapping("insurance")
public class InsuranceController {

    @Autowired
    IInsuranceService insuranceService;

    /***
     * @description 多条件查询保险产品分页
     * @param insuranceVO 保险产品VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<InsuranceVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "保险产品分页",notes = "保险产品分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "insuranceVO",value = "保险产品VO对象",required = true,dataType = "InsuranceVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"insuranceVO.categoryNo","insurance.checkRule",
        "insuranceVO.publishNumber","insuranceVO.categoryNo","insuranceVO.recommendCategoryNo",
        "insuranceVO.insuranceName","insuranceVO.optimization","insuranceVO.relieved",
        "insuranceVO.showIndex","insuranceVO.labelsJson","insuranceVO.remakeJson","insuranceVO.timeStart",
        "insuranceVO.timeStartUnit","insuranceVO.timeEnd","insuranceVO.timeEndUnit","insuranceVO.relation",
        "insuranceVO.sortNo","insuranceVO.multiple","insuranceVO.continuousInsuranceAge","insuranceVO.companyNo",
        "insuranceVO.insuranceState","insuranceVO.grace","insuranceVO.graceUnit","insuranceVO.revival",
        "insuranceVO.revivalUnit","insuranceVO.comment","insuranceVO.remake"})
    public ResponseResult<Page<InsuranceVO>> findInsuranceVOPage(
                                    @RequestBody InsuranceVO insuranceVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<InsuranceVO> insuranceVOPage = insuranceService.findPage(insuranceVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(insuranceVOPage);
    }

    /**
     * @Description 保存保险产品
     * @param insuranceVO 保险产品VO对象
     * @return InsuranceVO
     */
    @PutMapping
    @ApiOperation(value = "保存Insurance",notes = "添加Insurance")
    @ApiImplicitParam(name = "insuranceVO",value = "保险产品VO对象",required = true,dataType = "InsuranceVO")
    @ApiOperationSupport(includeParameters = {"insuranceVO.publishNumber","insuranceVO.categoryNo",
        "insuranceVO.recommendCategoryNo","insuranceVO.insuranceName","insuranceVO.goldSelection",
        "insuranceVO.carefree","insuranceVO.showIndex","insuranceVO.labelsJson","insuranceVO.remakeJson",
        "insuranceVO.timeStart","insuranceVO.timeStartUnit","insuranceVO.timeEnd",
        "insuranceVO.timeEndUnit","insuranceVO.relation","insuranceVO.sortNo","insuranceVO.multiple",
        "insuranceVO.continuousInsuranceAge","insuranceVO.checkRule","insuranceVO.companyNo",
        "insuranceVO.insuranceState", "insuranceVO.grace","insuranceVO.graceUnit","insuranceVO.revival",
        "insuranceVO.revivalUnit", "insuranceVO.comment","insuranceVO.remake","insuranceVO.hesitation",
        "insuranceVO.waits", "insuranceVO.operatingRate","insuranceVO.individualAgentRate",
        "insuranceVO.platformAgentRate"})
    public ResponseResult<InsuranceVO> createInsurance(@RequestBody InsuranceVO insuranceVO) {
        InsuranceVO insuranceVOResult = insuranceService.save(insuranceVO);
        return ResponseResultBuild.successBuild(insuranceVOResult);
    }

    /**
     * @Description 修改保险产品
     * @param insuranceVO 保险产品VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改保险产品",notes = "修改保险产品")
    @ApiImplicitParam(name = "insuranceVO",value = "保险产品VO对象",required = true,dataType = "InsuranceVO")
    @ApiOperationSupport(includeParameters = {"insuranceVO.id","insuranceVO.publishNumber","insuranceVO.categoryNo",
        "insuranceVO.recommendCategoryNo","insuranceVO.insuranceName","insuranceVO.goldSelection",
        "insuranceVO.carefree","insuranceVO.showIndex","insuranceVO.labelsJson","insuranceVO.remakeJson",
        "insuranceVO.timeStart","insuranceVO.timeStartUnit","insuranceVO.timeEnd",
        "insuranceVO.timeEndUnit","insuranceVO.relation","insuranceVO.sortNo","insuranceVO.multiple",
        "insuranceVO.continuousInsuranceAge","insuranceVO.checkRule","insuranceVO.companyNo","insuranceVO.insuranceState",
        "insuranceVO.grace","insuranceVO.graceUnit","insuranceVO.revival","insuranceVO.revivalUnit",
        "insuranceVO.comment","insuranceVO.remake","insuranceVO.hesitation","insuranceVO.waits",
        "insuranceVO.operatingRate","insuranceVO.individualAgentRate","insuranceVO.platformAgentRate"})
    public ResponseResult<Boolean> updateInsurance(@RequestBody InsuranceVO insuranceVO) {
        Boolean flag = insuranceService.update(insuranceVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询保险产品列表
     * @param insuranceVO 保险产品VO对象
     * @return List<InsuranceVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询保险产品列表",notes = "多条件查询保险产品列表")
    @ApiImplicitParam(name = "insuranceVO",value = "保险产品VO对象",required = true,dataType = "InsuranceVO")
    @ApiOperationSupport(includeParameters = {"insuranceVO.publishNumber","insuranceVO.categoryNo",
            "insuranceVO.recommendCategoryNo","insuranceVO.insuranceName","insuranceVO.goldSelection",
            "insuranceVO.carefree","insuranceVO.showIndex","insuranceVO.labelsJson","insuranceVO.remakeJson",
            "insuranceVO.timeStart","insuranceVO.timeStartUnit","insuranceVO.timeEnd",
            "insuranceVO.timeEndUnit","insuranceVO.relation","insuranceVO.sortNo","insuranceVO.multiple",
            "insuranceVO.continuousInsuranceAge","insuranceVO.checkRule","insuranceVO.companyNo","insuranceVO.insuranceState",
            "insuranceVO.grace","insuranceVO.graceUnit","insuranceVO.revival","insuranceVO.revivalUnit",
            "insuranceVO.comment","insuranceVO.remake","insuranceVO.hesitation","insuranceVO.waits",
            "insuranceVO.operatingRate","insuranceVO.individualAgentRate","insuranceVO.platformAgentRate"})
    public ResponseResult<List<InsuranceVO>> insuranceList(@RequestBody InsuranceVO insuranceVO) {
        List<InsuranceVO> insuranceVOList = insuranceService.findList(insuranceVO);
        return ResponseResultBuild.successBuild(insuranceVOList);
    }

    /***
     * @description 保险产品详情
     * @param insuranceId 保险id
     * @return
     */
    @PostMapping("insurance/{insuranceId}")
    @ApiOperation(value = "保险产品详情",notes = "保险产品详情")
    @ApiImplicitParam(name = "insuranceId",value = "保险id",required = true,dataType = "String")
    public ResponseResult<InsuranceVO> findInsuranceDetails(@PathVariable("insuranceId")String insuranceId){
        InsuranceVO insuranceVO = insuranceService.findById(insuranceId);
        return ResponseResultBuild.successBuild(insuranceVO);
    }

}
