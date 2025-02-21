package com.itheima.bolee.file.fegin;

import com.itheima.bolee.file.service.IFileService;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("file-feign")
@Api(tags = "附件feign-controller")
@Slf4j
public class FileBusinessFeignController {

    @Autowired
    IFileService fileService;

    /**
     * @Description 业务绑定单个附件
     * @param  fileVO 附件对象
     * @return
     */
    @PostMapping(value = "bind-file")
    @ApiOperation(value = "业务绑定单文件",notes = "业务绑定单文件")
    @ApiImplicitParam(name = "fileVO",value = "附件对象",required = true,dataType = "FileVO")
    public FileVO bindFile(@RequestBody FileVO fileVO){
        return fileService.bindFile(fileVO);
    }

    /**
     * @Description 相同业务绑定多个附件
     * @param  fileVOs 相同业务的多个附件对象
     * @return
     */
    @PostMapping(value = "bind-batch-file")
    @ApiOperation(value = "业务绑定多文件",notes = "业务绑定多文件")
    @ApiImplicitParam(name = "fileVOs",value = "附件对象",required = true,dataType = "FileVO")
    public List<FileVO> bindBatchFile(@RequestBody ArrayList<FileVO> fileVOs){
        return fileService.bindBatchFile(fileVOs);
    }

    /**
     * @Description 移除业务原图片，并绑定新的图片到业务上
     * @param  fileVO 附件对象
     * @return
     */
    @PostMapping(value = "replace-bind-file")
    @ApiOperation(value = "移除业务原图片，并绑定新的图片到业务上",notes = "移除业务原图片，并绑定新的图片到业务上")
    @ApiImplicitParam(name = "fileVO",value = "附件对象",required = true,dataType = "FileVO")
    public Boolean replaceBindFile(@RequestBody FileVO fileVO){
        return fileService.replaceBindFile(fileVO);
    }

    /**
     * @Description 批量移除业务原图片，并批量绑定新的图片到业务上
     * @param  fileVOs 附件对象
     * @return
     */
    @PostMapping(value = "replace-bind-batch-file")
    @ApiOperation(value = "移除业务原图片，并绑定新的图片到业务上",notes = "移除业务原图片，并绑定新的图片到业务上")
    @ApiImplicitParam(name = "fileVOs",value = "附件对象",required = true,dataType = "FileVO")
    public Boolean replaceBindBatchFile(@RequestBody ArrayList<FileVO> fileVOs){
        return fileService.replaceBindBatchFile(fileVOs);
    }

    /**
     * @description 按业务ID查询附件
     * @param  businessIds 业务ids
     * @return java.util.List<com.itheima.travel.req.FileVO>
     */
    @PostMapping("find-in-business-ids")
    @ApiOperation(value = "查询业务对应附件",notes = "查询业务对应附件")
    @ApiImplicitParam(name = "fileVO",value = "附件对象",required = true,dataType = "FileVO")
    public List<FileVO> findInBusinessIds(@RequestBody ArrayList<Long> businessIds)  {
        return fileService.findInBusinessIds(businessIds);
    }

    /**
     * @Description 删除业务相关附件
     * @param businessIds 附件信息ids
     * @return
     */
    @DeleteMapping("delete-by-business-ids")
    @ApiOperation(value = "删除业务对应附件",notes = "删除业务对应附件")
    @ApiImplicitParam(name = "fileVO",value = "附件对象",required = true,dataType = "FileVO")
    public Boolean deleteByBusinessIds(@RequestBody ArrayList<Long> businessIds) {
        return fileService.deleteInBusinessIds(businessIds);
    }


    /**
     * @Description 定时清理文件
     * @return Boolean
     */
    @DeleteMapping("clear-file")
    @ApiOperation(value = "删除业务对应附件",notes = "删除业务对应附件")
    public Boolean clearFile(){
        return fileService.clearFile();
    }

    /**
     * @Description 延迟清理文件
     * @return Boolean
     */
    @DeleteMapping("clear-file-id/{id}")
    @ApiOperation(value = "删除业务对应附件",notes = "删除业务对应附件")
    @ApiImplicitParam(name = "id",value = "业务id",required = true,dataType = "String")
    public Boolean clearFileById(@PathVariable("id")String id){
        return fileService.clearFileById(id);
    }

}

