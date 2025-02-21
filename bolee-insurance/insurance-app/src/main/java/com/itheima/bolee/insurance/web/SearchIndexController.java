package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.dto.SearchRecordVO;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.IInsuranceTopService;
import com.itheima.bolee.insurance.service.ISearchRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SearchIndexController.java
 * @Description 搜索提示框首页
 */
@Slf4j
@Api(tags = "搜索提示框首页")
@RestController
@RequestMapping("search-index")
public class SearchIndexController {

    @Autowired
    private ISearchRecordService searchRecordService;

    @Autowired
    private IInsuranceTopService insuranceTopService;

    @Autowired
    private IInsuranceService insuranceService;


    /**
     * 搜索历史-->tab_search_record-->SubjectContent[当前登录人]
     * 只查询前5条记录
     */
    @PostMapping("find-search-record")
    @ApiOperation(value = "搜索历史", notes = "搜索历史")
    public ResponseResult<List<SearchRecordVO>> findSearchRecord() {
        SearchRecordVO searchVO = SearchRecordVO.builder().
                createBy(SubjectContent.getUserVO().getId()).
                dataState(SuperConstant.DATA_STATE_0).
                build();
        List<SearchRecordVO> records = searchRecordService.findPage(searchVO, 0, 6).getRecords();
        return ResponseResultBuild.successBuild(records);
    }

    /**
     * 搜索历史-->tab_search_record-->SubjectContent[当前登录人]
     * 只查询前5条记录
     */
    @PostMapping("clean-search-record")
    @ApiOperation(value = "清空历史记录", notes = "清空历史记录")
    public ResponseResult<Boolean> cleanSearchRecord() {
        return ResponseResultBuild.successBuild(searchRecordService.cleanSearchRecord());
    }

    /**
     * 热搜榜-->tab_search_record
     */
    @PostMapping("/find-hot-search-record")
    @ApiOperation(value = "热搜榜", notes = "热搜榜")
    public ResponseResult<List<SearchRecordVO>> findHotSearchRecord() {
        List<SearchRecordVO> records = searchRecordService.tindTopRecord(10);
        return ResponseResultBuild.successBuild(records);
    }

    /**
     * 人气保险-->tab_insurance_top
     */
    @PostMapping("find-popular-top-insurance")
    @ApiOperation(value = "人气保险", notes = "人气保险")
    public ResponseResult<List<InsuranceVO>> findPopularTopInsurance() {
        //默认条数
        Integer defaultNum = 3;
        //30天内有效数据
        Integer dateLimit = 30;
        //获取人气保险
        List<InsuranceVO> topInsurance = insuranceTopService.findTopInsurance(defaultNum, dateLimit);
        return ResponseResultBuild.successBuild(topInsurance);
    }

    /***
     * @description 搜索保险
     * @param insuranceVO 保险名称
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CategoryCoefficentVO>
     */
    @PostMapping("find-search-insure/{pageNum}/{pageSize}")
    @ApiOperation(value = "搜索保险", notes = "搜索保险")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "insuranceVO", value = "保险搜索条件", dataType = "InsuranceVO"),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", dataType = "Integer")
    })
    public ResponseResult<Page<InsuranceVO>> findSearchInsure(
            @RequestBody InsuranceVO insuranceVO,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) {
        Page<InsuranceVO> insurePageVO = insuranceService.findPage(insuranceVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(insurePageVO);
    }
}
