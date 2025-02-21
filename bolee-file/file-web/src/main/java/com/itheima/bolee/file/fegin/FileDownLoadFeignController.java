package com.itheima.bolee.file.fegin;

import com.itheima.bolee.file.service.IFileService;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FileUpLoadController.java
 * @Description 文件下载接口
 */
@RestController
@RequestMapping("file-feign")
@Api(tags = "附件controller")
@Slf4j
public class FileDownLoadFeignController {

    @Autowired
    IFileService fileService;

    /***
     * @description 文件下载-简单下载-图片base64Image方式展示
     * @param fileId 上传对象
     * @return: com.itheima.travel.req.FileVo
     */
    @PostMapping(value = "down-load/{fileId}")
    @ApiOperation(value = "文件下载",notes = "文件下载")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "fileId",value = "附件Id",dataType = "Long")
    })
    public FileVO downLoad(@PathVariable("fileId") Long fileId){
        FileVO fileVO = fileService.downLoad(fileId);
        return fileVO;
    }

}
