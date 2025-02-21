package com.itheima.bolee.file.handler.qiniu.impl;

import com.aliyun.oss.ClientException;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.itheima.bolee.file.handler.qiniu.properties.QiniuProperties;
import com.itheima.bolee.framework.commons.enums.file.FileEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.file.handler.AbsFileStorageHandler;
import com.itheima.bolee.file.handler.FileStorageHandler;
import com.itheima.bolee.file.pojo.File;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @ClassName OssFileStorageHandlerImpl.java
 * @Description 七牛云文件上传
 */
@Slf4j
@Service("kodoFileStorageHandler")
public class KodoFileStorageHandlerImpl extends AbsFileStorageHandler implements FileStorageHandler {

    @Autowired
    private Auth qiniuAuth;

    @Autowired
    private Configuration qiniuConfiguration;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    QiniuProperties qiniuConfigProperties;

    @Override
    public String uploadFile(String suffix, String filename, String bucketName, boolean autoCatalog, InputStream inputStream) {
        if (EmptyUtil.isNullOrEmpty(bucketName)){
            bucketName=qiniuConfigProperties.getKodo().getBucketName();
        }
        String pathUrl = null;
        // 是否自动生成存储路径并设置文件路径和名称（Key）
        String key = autoCatalog?builderOssPath(filename):filename;
        log.info("七牛Kodo文件上传开始：{}", key);
        try {
            String upToken = qiniuAuth.uploadToken(bucketName);
            String mimeType = metaMimeTypeMap.get(suffix);
            UploadManager uploadManager = new UploadManager(qiniuConfiguration);
            Response response = uploadManager.put(inputStream, key, upToken, null, mimeType);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            if (!(StringUtils.isEmpty(putRet.key))) {
                log.info("七牛Kodo文件上传成功：{}", putRet.key);
                pathUrl = putRet.key;
            }
        } catch (QiniuException oe) {
            log.error("七牛Kodo文件上传错误：{}", oe);
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        } catch (ClientException ce) {
            log.error("七牛Kodo文件上传客户端错误：{}", ce);
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        }
        return pathUrl;
    }

    @Override
    public File initiateMultipartUpload(String suffix, String filename, String bucketName, boolean autoCatalog) {
        return null;
    }

    @Override
    public String uploadPart(String uploadId, String filename, int partNumber, long partSize, String bucketName, InputStream inputStream) {
        return null;
    }

    @Override
    public String completeMultipartUpload(String uploadId, List<String> partETags, String filename, String bucketName) {
        return null;
    }

    /*
     * 七牛获得文件的inputStream
     *   1.官方文档只有获得文件的URL路径地址的接口，没有直接获得Inputstream
     *   2.URL地址需要获得bucket所属于域名
     *   3.通过域名来获得文件的URL
     *   4.通过URL转为InputStream
     * */
    @Override
    public InputStream downloadFile(String bucketName, String pathUrl) throws IOException {

        InputStream inputStream = null;
        try {
            // 默认获得指定Bucket第一个域名
            String[] domainList = bucketManager.domainList(bucketName);
            String domain = domainList[0];
            // 获得文件的路径并转为URL对象
            DownloadUrl downloadUrl = new DownloadUrl(domain, false, pathUrl);
            String buildURL = downloadUrl.buildURL();
            URL url = new URL(buildURL);
            // URL转为InputStream
            inputStream = url.openStream();

        } catch (IOException e) {
            log.error("七牛Kodo获得文件输入流失败：{}", e);
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        }

        return inputStream;
    }

    @Override
    public void delete(String bucketName, String pathUrl) {
        try {
            bucketManager.delete(bucketName, pathUrl);
        } catch (QiniuException e) {
            //如果遇到异常，说明删除失败
            log.error("七牛Kodo获得文件输入流失败：{}", e);
            throw new ProjectException(FileEnum.DELETE_FAIL);
        }

    }

    @Override
    public void deleteBatch(String bucketName, List<String> pathUrls) {

        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucketName, pathUrls.toArray(new String[0]));
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);

            for (int i = 0; i < pathUrls.size(); i++) {
                BatchStatus status = batchStatusList[i];
                String key = pathUrls.get(i);
                if (status.code != 200) {
                    log.error(status.data.error);
                    log.error("七牛Kodo批量删除文件失败：key 为 {}", key);
                    throw new ProjectException(FileEnum.DELETE_FAIL);
                }
            }
        } catch (QiniuException e) {
            //如果遇到异常，说明删除失败
            log.error("七牛Kodo批量删除文件失败：{}", e);
            throw new ProjectException(FileEnum.DELETE_FAIL);
        }
    }

    @Override
    public String getFileContent(String bucketName, String pathUrl) throws IOException {
        InputStream inputStream = downloadFile(bucketName, pathUrl);
        return new String(ByteStreams.toByteArray(inputStream));
    }
}
