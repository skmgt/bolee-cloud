package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.HealthAssessmentVO;
import com.itheima.bolee.insurance.service.IHealthAssessmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：评估类目响应接口
 */
@Slf4j
@Api(tags = "评估类目")
@RestController
@RequestMapping("health-assessment")
public class HealthAssessmentController {

    @Autowired
    IHealthAssessmentService healthAssessmentService;

    /***
     * @description 多条件查询评估类目分页
     * @param healthAssessmentVO 评估类目VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<HealthAssessmentVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "评估类目分页",notes = "评估类目分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "healthAssessmentVO",value = "评估类目VO对象",required = true,dataType = "HealthAssessmentVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"healthAssessmentVO.insuranceId","healthAssessmentVO.assessmentKey","healthAssessmentVO.assessmentKeyName","healthAssessmentVO.assessmentVal","healthAssessmentVO.sortNo","healthAssessmentVO.remake"})
    public ResponseResult<Page<HealthAssessmentVO>> findHealthAssessmentVOPage(
                                    @RequestBody HealthAssessmentVO healthAssessmentVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<HealthAssessmentVO> healthAssessmentVOPage = healthAssessmentService.findPage(healthAssessmentVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(healthAssessmentVOPage);
    }

    /**
     * @Description 保存评估类目
     * @param healthAssessmentVO 评估类目VO对象
     * @return HealthAssessmentVO
     */
    @PutMapping
    @ApiOperation(value = "保存HealthAssessment",notes = "添加HealthAssessment")
    @ApiImplicitParam(name = "healthAssessmentVO",value = "评估类目VO对象",required = true,dataType = "HealthAssessmentVO")
    @ApiOperationSupport(includeParameters = {"healthAssessmentVO.dataState","healthAssessmentVO.insuranceId","healthAssessmentVO.assessmentKey","healthAssessmentVO.assessmentKeyName","healthAssessmentVO.assessmentVal","healthAssessmentVO.sortNo","healthAssessmentVO.remake"})
    public ResponseResult<HealthAssessmentVO> createHealthAssessment(@RequestBody HealthAssessmentVO healthAssessmentVO) {
        HealthAssessmentVO healthAssessmentVOResult = healthAssessmentService.save(healthAssessmentVO);
        return ResponseResultBuild.successBuild(healthAssessmentVOResult);
    }

    /**
     * @Description 修改评估类目
     * @param healthAssessmentVO 评估类目VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改评估类目",notes = "修改评估类目")
    @ApiImplicitParam(name = "healthAssessmentVO",value = "评估类目VO对象",required = true,dataType = "HealthAssessmentVO")
    @ApiOperationSupport(includeParameters = {"healthAssessmentVO.id","healthAssessmentVO.dataState","healthAssessmentVO.insuranceId","healthAssessmentVO.assessmentKey","healthAssessmentVO.assessmentKeyName","healthAssessmentVO.assessmentVal","healthAssessmentVO.sortNo","healthAssessmentVO.remake"})
    public ResponseResult<Boolean> updateHealthAssessment(@RequestBody HealthAssessmentVO healthAssessmentVO) {
        Boolean flag = healthAssessmentService.update(healthAssessmentVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除评估类目
     * @param healthAssessmentVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除评估类目",notes = "删除评估类目")
    @ApiImplicitParam(name = "healthAssessmentVO",value = "评估类目VO对象",required = true,dataType = "HealthAssessmentVO")
    @ApiOperationSupport(includeParameters = {"healthAssessmentVO.checkedIds"})
    public ResponseResult<Boolean> deleteHealthAssessment(@RequestBody HealthAssessmentVO healthAssessmentVO) {
        Boolean flag = healthAssessmentService.delete(healthAssessmentVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询评估类目列表
     * @param healthAssessmentVO 评估类目VO对象
     * @return List<HealthAssessmentVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询评估类目列表",notes = "多条件查询评估类目列表")
    @ApiImplicitParam(name = "healthAssessmentVO",value = "评估类目VO对象",required = true,dataType = "HealthAssessmentVO")
    @ApiOperationSupport(includeParameters = {"healthAssessmentVO.insuranceId","healthAssessmentVO.assessmentKey","healthAssessmentVO.assessmentKeyName","healthAssessmentVO.assessmentVal","healthAssessmentVO.sortNo","healthAssessmentVO.remake"})
    public ResponseResult<List<HealthAssessmentVO>> healthAssessmentList(@RequestBody HealthAssessmentVO healthAssessmentVO) {
        List<HealthAssessmentVO> healthAssessmentVOList = healthAssessmentService.findList(healthAssessmentVO);
        return ResponseResultBuild.successBuild(healthAssessmentVOList);
    }

}
