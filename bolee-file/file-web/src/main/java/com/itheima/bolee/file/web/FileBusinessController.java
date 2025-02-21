package com.itheima.bolee.file.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.file.service.IFileService;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileController.java
 * @Description 附件展示维护controller
 */
@RestController
@RequestMapping("file")
@Api(tags = "附件controller")
@Slf4j
public class FileBusinessController {

    @Autowired
    IFileService fileService;

    /***
     * @description 附件分页列表
     * @param fileVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<FileVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询附件分页",notes = "查询附件分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fileVO",value = "附件查询对象",required = false,dataType = "FileVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters ={"fileVO.businessType","","fileVO.pathUrl",
            "fileVO.dataState","fileVO.status"} )
    public ResponseResult<Page<FileVO>> findFileVOPage(
        @RequestBody FileVO fileVO,
        @PathVariable("pageNum") int pageNum,
        @PathVariable("pageSize") int pageSize) {
        //查询附件分页信息
        Page<FileVO> fileVOPage = fileService.findFileVOPage(fileVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(fileVOPage);
    }

    /**
     * @Description 移除业务原图片，并批量绑定新的图片到业务上
     * @param  fileVOs 附件对象
     * @return
     */
    @PutMapping(value = "replace-bind-batch-file")
    @ApiOperation(value = "移除业务原图片，并绑定新的图片到业务上",notes = "移除业务原图片，并绑定新的图片到业务上")
    @ApiImplicitParam(name = "fileVOs",value = "附件对象",required = true,dataType = "FileVO")
    public ResponseResult<Boolean> replaceBindBatchFile(@RequestBody List<FileVO> fileVOs){
        Boolean flag = fileService.replaceBindBatchFile(fileVOs);
        return ResponseResultBuild.successBuild(flag);
    }
}

