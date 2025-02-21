package com.itheima.bolee.file.utils;

import com.itheima.bolee.file.handler.aliyun.properties.OssAliyunConfigProperties;
import com.itheima.bolee.file.handler.qiniu.properties.QiniuProperties;
import com.itheima.bolee.framework.commons.constant.file.FileConstant;
import com.itheima.bolee.framework.commons.enums.file.FileEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     获得资源文件完整路径地址上下文对象
 *      如果添加了新的对象存储资源，需要在此类中对 initMap 方法添加新的路径前缀
 * </p>
 *
 * @Description:
 */
@Component
public class FileUrlContext {

    @Autowired
    private OssAliyunConfigProperties ossAliyunConfigProperties;

    @Autowired
    private QiniuProperties qiniuProperties;

    private static Map<String,String> fileStoreUrlHandler =new HashMap<>();

    @PostConstruct
    public void initMap() {
        // 初始化各个对象存储的文件访问路径地址
        String ossPrefixUrl = "https://" + ossAliyunConfigProperties.getBucketName() + "." +
                ossAliyunConfigProperties.getEndpoint() + "/";
        String kodoPrefixUrl = "http://" + qiniuProperties.getKodo().getEndpoint()  + "/";

        fileStoreUrlHandler.put(FileConstant.ALIYUN_OSS,ossPrefixUrl);
        fileStoreUrlHandler.put(FileConstant.QINIU_KODO,kodoPrefixUrl);
    }

    /**
     * 获得资源文件的访问地址
     * @param storeFlag String 对象存储标识
     * @param pathUrl String 相对路径
     * @return String 资源文件对应的完整路径地址
     */
    public  String getFileUrl(String storeFlag, String pathUrl) {
        String prefix = fileStoreUrlHandler.get(storeFlag);
        if (StringUtils.isEmpty(prefix)) {
            throw new ProjectException(FileEnum.FILE_PREFIX_NOT_FOUND);
        }
        return prefix + pathUrl;
    }

}
