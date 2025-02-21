package com.itheima.bolee.file.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.file.service.IFileService;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.dto.file.UploadMultipartFile;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName FileUpLoadController.java
 * @Description 文件上传接口
 */
@RestController
@RequestMapping("file")
@Api(tags = "附件controller")
@Slf4j
public class FileUpLoadController {

    @Autowired
    IFileService fileService;

    /***
     * @description 文件上传-简单上传-前端直接调用
     * @param file 上传对象
     * @return: com.itheima.travel.req.FileVO
     */
    @PostMapping(value = "up-load")
    @ApiOperation(value = "文件上传-简单上传",notes = "文件上传-简单上传")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "form", name = "file", value = "文件对象", required = true, dataTypeClass = MultipartFile.class)
    })
    @ApiOperationSupport(includeParameters = {"fileVO.businessType","fileVO.bucketName","fileVO.storeFlag","fileVO.autoCatalog"})
    public ResponseResult<FileVO> upLoad(
            @RequestParam("file") MultipartFile file,
            FileVO fileVO) throws IOException {
        fileVO.setCompanyNo(SubjectContent.getCompanyNo());
        //构建文件上传对象
        UploadMultipartFile uploadMultipartFile = UploadMultipartFile
            .builder()
            .originalFilename(file.getOriginalFilename())
            .fileByte(IOUtils.toByteArray(file.getInputStream()))
            .build();
        //执行文件上传
        FileVO fileVOResult = fileService.upLoad(uploadMultipartFile, fileVO);
        return ResponseResultBuild.successBuild(fileVOResult);

    }

    @PostMapping(value = "initiate-multipart-up-load")
    @ApiOperation(value = "文件分片上传-初始化",notes = "文件分片上传-初始化")
    @ApiImplicitParam(name = "fileVO",value = "文件对象",required = true,dataType = "FileVO")
    public ResponseResult<FileVO> initiateMultipartUpload(
            @RequestBody FileVO fileVO){
        fileVO.setCompanyNo(SubjectContent.getCompanyNo());
        //初始化上传Id
        FileVO fileVOResult = fileService.initiateMultipartUpload(fileVO);
        return ResponseResultBuild.successBuild(fileVOResult);
    }

    @PostMapping(value = "up-load-part")
    @ApiOperation(value = "文件分片上传-上传分片",notes = "文件分片上传-上传分片")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "form", name = "file", value = "文件对象", required = true, dataTypeClass = MultipartFile.class)
    })
    public ResponseResult<String> uploadPart(
            @RequestParam("file") MultipartFile file,
            FilePartVO filePartVO)throws IOException {
        filePartVO.setCompanyNo(SubjectContent.getCompanyNo());
        //构建文件上次对象
        UploadMultipartFile uploadMultipartFile = UploadMultipartFile
            .builder()
            .originalFilename(file.getOriginalFilename())
            .fileByte(IOUtils.toByteArray(file.getInputStream()))
            .build();
        //上传分片返回partETagJson
        String partETagJson = fileService.uploadPart(uploadMultipartFile,filePartVO);
        return ResponseResultBuild.successBuild(partETagJson);
    }

    @PostMapping(value = "complete-multipart-up-load")
    @ApiOperation(value = "文件分片上传-合并分片",notes = "文件分片上传-合并分片")
    @ApiImplicitParam(name = "fileVO",value = "文件对象",required = true,dataType = "FileVO")
    public ResponseResult<String> completeMultipartUpload(
            @RequestBody FileVO fileVO)throws IOException {
        //问上传分片返回partETagJson
        String eTagJson = fileService.completeMultipartUpload(fileVO);
        return ResponseResultBuild.successBuild(eTagJson);
    }

}
