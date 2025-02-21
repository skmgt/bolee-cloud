package com.itheima.bolee.insurance.web;


import com.alibaba.nacos.common.utils.MD5Utils;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;
import com.itheima.bolee.insurance.service.DataExportService;
import com.itheima.bolee.insurance.service.IRuleService;
import com.itheima.bolee.insurance.service.ISickSearchRecordService;
import com.itheima.bolee.insurance.service.ISickService;
import com.itheima.bolee.insurance.service.impl.SickServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SeekAdviceController.java
 * @Description 健康咨询
 */
@Slf4j
@Api(tags = "健康咨询")
@RestController
@RequestMapping("seek-advice")
public class SeekAdviceController {

    @Autowired
    private ISickService sickService;

    @Autowired
    private ISickSearchRecordService sickSearchRecordService;

    /**
     * @description 医疗重疾类保险 健康咨询
     */
    @PostMapping("sick-type")
    @ApiOperation(value = "疾病类型",notes = "疾病类型列表")
    public ResponseResult<List<DataDictVO>> findSickType() {
        return ResponseResultBuild.successBuild(sickService.findSickType());
    }

    /**
     * @description 疾病添加
     */
    @ApiOperation(value = "添加疾病",notes = "添加疾病")
    @PostMapping("sick-add")
    public ResponseResult<SickVO> saveSick(@RequestBody SickVO sickVO) {
        return ResponseResultBuild.successBuild(sickService.save(sickVO));
    }

    /**
     * @description 疾病搜索
     */
    @ApiOperation(value = "疾病搜索",notes = "疾病搜索")
    @PostMapping("sick-search")
    public ResponseResult<List<SickVO>> searchSick(@RequestBody SickVO sickVO) {
        return ResponseResultBuild.successBuild(sickService.searchSick(sickVO.getSickKeyName(), SubjectContent.getUserVO()));
    }

    /**
     * @description 热搜榜
     * 默认查询30天内搜索次数最多的疾病
     */
    @ApiOperation(value = "疾病热搜榜",notes = "30天内被搜索次数最多的疾病")
    @PostMapping("hot-sick-list")
    public ResponseResult<List<SickVO>> hotSickList() {
        return ResponseResultBuild.successBuild(sickSearchRecordService.hotSickList());
    }
    /**
     * @description 常见疾病
     * 默认查询30天内搜索次数最多的疾病
     */
    @ApiOperation(value = "常见疾病",notes = "常见疾病")
    @PostMapping("common-diseases")
    public ResponseResult<List<SickVO>> commonDiseases() {
        return ResponseResultBuild.successBuild(sickService.commonDiseases());
    }

    /**
     * @description 医疗重疾类保险 健康咨询
     */
    @PostMapping("seek-advice-from-health")
    @ApiOperation(value = "健康咨询",notes = "医疗重疾类保险:健康咨询")
    @ApiImplicitParam(name = "seekAdviceVO",value = "健康咨询传入对象",required = true,dataType = "SeekAdviceVO")
    public ResponseResult<QuestionTemplateVO> seekAdviceHealth(@RequestBody SeekAdviceVO seekAdviceVO) {

        return ResponseResultBuild.successBuild(sickService.seekAdviceHealth(seekAdviceVO));
    }

    /**
     * @description 提交问卷选项
     */
    @PostMapping("submit-question-choose")
    @ApiOperation(value = "提交问卷选项",notes = "用户点击确认无误后,提交用户的选项")
    public ResponseResult<AdviceHealthDTO> submitQuestionChoose(@RequestBody AdviceHealthDTO healthTemplate) {
        return ResponseResultBuild.successBuild(sickService.submitQuestionChoose(healthTemplate));
    }

    //=====================疾病数据导入=====================

    @Autowired
    private DataExportService dataExportService;

    /**
     * @description 提交问卷选项
     */
    @PostMapping("data-input")
    public ResponseResult dataInport() {
        return ResponseResultBuild.successBuild(dataExportService.dataInport());
    }


    /**
     * @description 数据修改
     */
    @PostMapping("data-update")
    public ResponseResult dataUpdate() {
        return ResponseResultBuild.successBuild(dataExportService.dataUpdate());
    }
}