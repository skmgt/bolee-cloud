package com.itheima.bolee.insurance.web;

import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.service.IBrowsingHistoryService;
import com.itheima.bolee.insurance.service.ISearchRecordService;
import com.itheima.bolee.insurance.service.ISelfSelectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AppSelfSelectionController.java
 * @Description 自选
 */
@Slf4j
@Api(tags = "自选")
@RestController
@RequestMapping("app-self-selection")
public class AppSelfSelectionController {

    @Autowired
    private ISelfSelectionService selfSelectionService;

    @Autowired
    private IBrowsingHistoryService browsingHistoryService;

    @Autowired
    private ISearchRecordService searchRecordService;


    /***
     * @description 添加自选
     * @param selfSelectionVO
     * @return
     */
    @PostMapping("selection-insure")
    @ApiOperation(value = "添加自选保险",notes = "添加自选保险")
    public ResponseResult<SelfSelectionVO> selfSelectionInsuer(@RequestBody SelfSelectionVO selfSelectionVO){
        SelfSelectionVO res = selfSelectionService.save(selfSelectionVO);
        return ResponseResultBuild.successBuild(res);
    }

    /***
     * @description 删除自选
     * @param selfSelectionVO
     * @return
     */
    @DeleteMapping("selection-insure")
    @ApiOperation(value = "删除自选保险",notes = "删除自选保险")
    public ResponseResult<Boolean> delSelectionInsuer(@RequestBody SelfSelectionVO selfSelectionVO){
        Boolean flag = selfSelectionService.delete(selfSelectionVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 我的自选
     * @return
     */
    @PostMapping("find-self-selection-insure")
    @ApiOperation(value = "我的自选",notes = "我的自选")
    public ResponseResult<List<InsuranceTypeInfoVO>> findSelfSelectionInsure(){
        List<InsuranceTypeInfoVO> res = selfSelectionService.findMySelection(SubjectContent.getUserVO());
        return ResponseResultBuild.successBuild(res);
    }



    /***
     * @description 最近浏览
     * @return
     */
    @PostMapping("find-history")
    @ApiOperation(value = "最近的浏览",notes = "查询最近的浏览记录")
    public ResponseResult<List<InsuranceVO>> findMyHistory(){
        UserVO userVO = SubjectContent.getUserVO();
        List<InsuranceVO> res = searchRecordService.findMyHistory(userVO);
        return ResponseResultBuild.successBuild(res);
    }
}
