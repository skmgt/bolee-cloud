package com.itheima.bolee.file.adapter.impl;

import com.itheima.bolee.file.adapter.FileStorageAdapter;
import com.itheima.bolee.file.handler.FileStorageHandler;
import com.itheima.bolee.framework.commons.constant.file.FileConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.RegisterBeanHandler;
import com.itheima.bolee.file.pojo.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FileStorageAdapterImpl.java
 * @Description 文件存储适配处理
 */
@Component
public class FileStorageAdapterImpl implements FileStorageAdapter {

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    private static Map<String,String> fileStorageHandlers =new HashMap<>();

    static {
        fileStorageHandlers.put(FileConstant.ALIYUN_OSS,"ossFileStorageHandler");
        fileStorageHandlers.put(FileConstant.QINIU_KODO,"kodoFileStorageHandler");
    }

    @Override
    public String uploadFile(FileVO fileVO, InputStream inputStream) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(fileVO.getStoreFlag())?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(fileVO.getStoreFlag());
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.uploadFile(fileVO.getSuffix(), fileVO.getFileName(), fileVO.getBucketName(), fileVO.getAutoCatalog(), inputStream);
    }

    @Override
    public File initiateMultipartUpload(FileVO fileVO) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(fileVO.getStoreFlag())?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(fileVO.getStoreFlag());
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.initiateMultipartUpload(fileVO.getSuffix(),fileVO.getFileName(),fileVO.getBucketName(),fileVO.getAutoCatalog());
    }

    @Override
    public String uploadPart(FilePartVO filePartVo, InputStream inputStream) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(filePartVo.getStoreFlag())?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(filePartVo.getStoreFlag());
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.uploadPart(filePartVo.getUploadId(),filePartVo.getFileName(),filePartVo.getPartNumber(),filePartVo.getPartSize(),filePartVo.getBucketName(),inputStream);
    }

    @Override
    public String completeMultipartUpload(FileVO fileVO) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(fileVO.getStoreFlag())?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(fileVO.getStoreFlag());
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.completeMultipartUpload(fileVO.getUploadId(),fileVO.getPartETags(),fileVO.getFileName(),fileVO.getBucketName());
    }

    @Override
    public InputStream downloadFile(String storeFlag,String bucketName,String pathUrl) throws IOException {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(storeFlag)?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(storeFlag);
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.downloadFile(bucketName,pathUrl);
    }

    @Override
    public void delete(String storeFlag,String bucketName,String pathUrl) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(storeFlag)?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(storeFlag);
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        fileStorageHandler.delete(bucketName,pathUrl);
    }

    @Override
    public void deleteBatch(String storeFlag,String bucketName,List<String> pathUrls) {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(storeFlag)?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(storeFlag);
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        fileStorageHandler.deleteBatch(bucketName,pathUrls);
    }

    @Override
    public String getFileContent(String storeFlag,String bucketName,String pathUrl) throws IOException {
        String fileStorageHandlerString = EmptyUtil.isNullOrEmpty(storeFlag)?
                fileStorageHandlers.get(FileConstant.ALIYUN_OSS):fileStorageHandlers.get(storeFlag);
        FileStorageHandler fileStorageHandler = registerBeanHandler.getBean(fileStorageHandlerString, FileStorageHandler.class);
        return fileStorageHandler.getFileContent(bucketName,pathUrl);
    }
}
