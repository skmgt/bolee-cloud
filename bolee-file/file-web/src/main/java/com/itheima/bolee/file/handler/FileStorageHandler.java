package com.itheima.bolee.file.handler;

import com.itheima.bolee.file.pojo.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName FileStorageHandler.java
 * @Description 文件存储处理接口类
 */
public interface FileStorageHandler {

    /**
     * @Description 文件上传
     * @param suffix 文件后缀
     * @param filename 文件名称-可以指定存储空间下的目录，例如：a/b/c/filename
     * @param bucketName 存储空间的名称
     * @param autoCatalog 是否自动生成文件存储目录,如果在filename指定了目录，此值设置为false
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String uploadFile(String suffix, String filename,String bucketName,boolean autoCatalog, InputStream inputStream);

    /***
     * @description 分片上传-初始化分片请求
     * @param suffix 文件后缀
     * @param filename 文件名称-可以指定存储空间下的目录，例如：a/b/c/filename
     * @param bucketName 存储空间的名称
     * @param autoCatalog 是否自动生成文件存储目录,如果在filename指定了目录，此值设置为false
     * @return uploadId 文件上传id
     */
    File initiateMultipartUpload(String suffix, String filename, String bucketName, boolean autoCatalog);

    /***
     * @description 分片上传-上传每个分片文件
     * @param upLoadId 文件上传id
     * @param filename 文件名称-可以指定存储空间下的目录，例如：a/b/c/filename
     * @param partNumber 当前分片
     * @param partSize 分片数
     * @param bucketName 存储空间的名称
     * @param inputStream 当前分片文件流
     * @return PartETag json字符串
     */
    String uploadPart(String upLoadId,String filename,int partNumber,long partSize,String bucketName,InputStream inputStream);


    /***
     * @description 分片上传-合并所有上传文件
     *
     * @param upLoadId 文件上传id
     * @param partETags json字符串
     * @param filename 文件名称-可以指定存储空间下的目录，例如：a/b/c/filename
     * @param bucketName 存储空间的名称
     * @return 合并结果
     */
    String completeMultipartUpload(String upLoadId,List<String> partETags,String filename,String bucketName);

    /**
     * @Description  下载文件
     * @param bucketName 存储空间名称
     * @param pathUrl 全路径
     * @return
     */
    InputStream downloadFile(String bucketName,String pathUrl) throws IOException;

    /**
     * @Description 文件删除
     * @param bucketName 存储空间名称
     * @param pathUrl 全路径
     * @throws Exception
     */
    void delete(String bucketName,String pathUrl);

    /**
     * @Description 批量文件删除
     * @param bucketName 存储空间名称
     * @param pathUrls 全路径集合
     * @throws Exception
     */
    void deleteBatch(String bucketName,List<String> pathUrls);

    /**
     * @Description 获取文件文本内容
     * @param bucketName 存储空间名称
     * @param pathUrl 全路径
     * @return
     * @throws IOException
     */
    String getFileContent(String bucketName,String pathUrl) throws IOException;

}
