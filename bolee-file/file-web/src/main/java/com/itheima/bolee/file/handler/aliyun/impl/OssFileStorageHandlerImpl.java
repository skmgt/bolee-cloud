package com.itheima.bolee.file.handler.aliyun.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.OSSConstants;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.itheima.bolee.framework.commons.enums.file.FileEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.file.handler.AbsFileStorageHandler;
import com.itheima.bolee.file.handler.FileStorageHandler;
import com.itheima.bolee.file.handler.aliyun.properties.OssAliyunConfigProperties;
import com.itheima.bolee.file.pojo.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName OssFileStorageHandlerImpl.java
 * @Description 阿里云文件上传
 */
@Slf4j
@Service("ossFileStorageHandler")
@EnableConfigurationProperties(OssAliyunConfigProperties.class)
public class OssFileStorageHandlerImpl extends AbsFileStorageHandler implements FileStorageHandler {

    @Autowired
    OSS ossClient;

    @Autowired
    OssAliyunConfigProperties ossAliyunConfigProperties;
    /***
     * @description 文件元数据处理
     * @param prefix 文件后缀
     * @return
     */
    public ObjectMetadata fileMetaHandler(String prefix){
        //元数据对象
        ObjectMetadata objectMeta = new ObjectMetadata();
        //文件字符集
        objectMeta.setContentEncoding("UTF-8");
        //文件类型匹配
        objectMeta.setContentType(metaMimeTypeMap.get(prefix.toLowerCase()));
        return objectMeta;
    }

    @Override
    public String uploadFile(String suffix, String filename, String bucketName,boolean autoCatalog, InputStream inputStream) {
        // 是否自动生成存储路径并设置文件路径和名称（Key）
        String key = autoCatalog?builderOssPath(filename):filename;
        log.info("OSS文件上传开始：{}" ,key);
        try {
            //上传文件元数据处理
            ObjectMetadata objectMeta = fileMetaHandler(suffix);
            //文件上传请求对象
            PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream,objectMeta);
            //上传限流
            if (ossAliyunConfigProperties.getIslimitSpeed()){
                request.setTrafficLimit(ossAliyunConfigProperties.getUplimitSpeed());
            }
            //文件上传
            PutObjectResult result = ossClient.putObject(request);
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        } catch (OSSException oe) {
            log.error("OSS文件上传错误：{}", oe);
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        } catch (ClientException ce) {
            log.error("OSS文件上传客户端错误：{}",ce);
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        }
        return key;
    }

    @Override
    public File initiateMultipartUpload(String suffix, String filename, String bucketName, boolean autoCatalog) {
        // 是否自动生成存储路径并设置文件路径和名称（Key）
        String key = filename;
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        // 如果需要在初始化分片时设置请求头，请参考以下示例代码。
        ObjectMetadata metadata = fileMetaHandler(suffix);
        metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // 指定该Object的网页缓存行为。
        metadata.setCacheControl("no-cache");
        // 指定该Object被下载时的名称。
        metadata.setContentDisposition("inline;filename="+key);
        // 指定该Object的内容编码格式。
        metadata.setContentEncoding(OSSConstants.DEFAULT_CHARSET_NAME);
         //指定请求
        request.setObjectMetadata(metadata);
        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 设置权限(公开读)
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        return File.builder().bucketName(bucketName).pathUrl(key).fileName(key).uploadId(upresult.getUploadId()).build();
    }

    @Override
    public String uploadPart(String upLoadId, String filename, int partNumber, long partSize, String bucketName, InputStream inputStream) {
        //封装分片上传请求
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setUploadId(upLoadId);
        //part大小 1-10000
        uploadPartRequest.setPartNumber(partNumber);
        uploadPartRequest.setPartSize(partSize);
        //文件上传的bucketName
        uploadPartRequest.setBucketName(bucketName);
        //分片文件
        uploadPartRequest.setInputStream(inputStream);
        uploadPartRequest.setKey(filename);
        // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        log.info("{}文件第 {} 片上传成功,上传结果:{}", upLoadId, uploadPartRequest.getPartNumber(),JSON.toJSON(uploadPartResult));
        return JSONObject.toJSONString(uploadPartResult.getPartETag());

    }

    @Override
    public String completeMultipartUpload(String upLoadId, List<String> partETags, String filename, String bucketName) {
        StopWatch st = new StopWatch();
        st.start();
        //转换jsonarray为list
        List<PartETag> partETagList =Lists.newArrayList();
        partETags.forEach(n->{
            partETagList.add(JSONObject.parseObject(n,PartETag.class));
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, filename, upLoadId, partETagList);
        log.info("{}文件上传完成,开始合并,partList:{}", upLoadId, partETags);
        // 完成分片上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        st.stop();
        log.info("{}文件上传完成,上传结果:{},耗时:{}", upLoadId, JSON.toJSON(completeMultipartUploadResult), st.getTotalTimeMillis());
        return completeMultipartUploadResult.getETag();
    }


    @Override
    public InputStream downloadFile(String bucketName,String pathUrl) throws IOException {
        GetObjectRequest request = new GetObjectRequest(bucketName, pathUrl);
        //下载传限流
        if (ossAliyunConfigProperties.getIslimitSpeed()){
            request.setTrafficLimit(ossAliyunConfigProperties.getDownlimitSpeed());
        }
        //ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        InputStream inputStream =  ossClient.getObject(request).getObjectContent();
        return inputStream;
    }

    @Override
    public void delete(String bucketName,String pathUrl) {
        // 删除Objects
        ossClient.deleteObject(bucketName,pathUrl);
    }

    @Override
    public void deleteBatch(String bucketName,List<String> pathUrls) {
        // 删除Objects
        ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(pathUrls));
    }

    @Override
    public String getFileContent(String bucketName,String pathUrl) throws IOException {
        InputStream inputStream = downloadFile(bucketName,pathUrl);
        return new String(ByteStreams.toByteArray(inputStream));
    }
}
