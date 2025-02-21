package com.itheima.bolee.file.adapter;

import com.itheima.bolee.file.pojo.File;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName FileStorageAdapter.java
 * @Description 文件存储适配处理
 */
public interface FileStorageAdapter {

    /**
     * 文件上传
     * @param fileVO  {@link FileVO} 文件信息对象
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String uploadFile(FileVO fileVO, InputStream inputStream);

    /***
     * @description 分片上传-初始化分片请求
     * @param fileVO  {@link FileVO} 文件信息对象
     * @return uploadId 文件上传id
     */
    File initiateMultipartUpload(FileVO fileVO);

    /***
     * @description 分片上传-上传每个分片文件
     * @param filePartVo  {@link FilePartVO} 文件信息对象
     * @param inputStream 当前分片文件流
     * @return PartETag json字符串
     */
    String uploadPart(FilePartVO filePartVo, InputStream inputStream);


    /***
     * @description 分片上传-合并所有上传文件
     * @param fileVO  {@link File} 文件信息对象
     * @return 合并结果
     */
    String completeMultipartUpload(FileVO fileVO);

    /**
     * @Description  下载文件
     * @param storeFlag  存储源标识
     * @param bucketName  资源存储区域名称
     * @param pathUrl  资源文件路径地址（其中包含文件名称）
     * @return
     */
    InputStream downloadFile(String storeFlag,String bucketName,String pathUrl) throws IOException;

    /**
     * @Description 文件删除
     * @param pathUrl 全路径
     * @throws Exception
     */
    void delete(String storeFlag,String bucketName,String pathUrl);


    /**
     * @Description 批量文件删除
     * @param pathUrls 全路径集合
     * @throws Exception
     */
    void deleteBatch(String storeFlag,String bucketName,List<String> pathUrls);

    /**
     * @Description 获取文件文本内容
     * @param pathUrl 全路径
     * @return
     * @throws IOException
     */
    String getFileContent(String storeFlag,String bucketName,String pathUrl) throws IOException;
}
