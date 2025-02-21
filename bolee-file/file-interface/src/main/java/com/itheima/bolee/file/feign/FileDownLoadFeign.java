package com.itheima.bolee.file.feign;

import com.itheima.bolee.file.hystrix.FileBusinessHystrix;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName FileFeign.java
 * @Description 文件下载feign接口
 */
@FeignClient(value = "file-web",fallback = FileBusinessHystrix.class)
public interface FileDownLoadFeign {

    /***
     * @description 文件下载-服务远程调用-base64方式返回
     * @param fileId 上传对象
     * @return: com.itheima.travel.req.FileVo
     */
    @PostMapping("file-feign/down-load/{fileId}")
    FileVO downLoad(@PathVariable("fileId") Long fileId);

}
