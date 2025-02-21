package com.itheima.bolee.file.feign;

import com.itheima.bolee.file.hystrix.FileBusinessHystrix;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileFeign.java
 * @Description 附件feign接口
 */
@FeignClient(value = "file-web",fallback = FileBusinessHystrix.class)
public interface FileBusinessFeign {

    /**
     * @Description 业务与文件绑定
     * @param  fileVO 附件对象
     * @return com.itheima.travel.req.FileVO
     */
    @PostMapping("file-feign/bind-file")
    List<FileVO> bindFile(@RequestBody FileVO fileVO);

    /**
     * @Description 业务绑定多个附件
     * @param  fileVOs 相同业务的多个附件对象
     * @return
     */
    @PostMapping("file-feign/bind-batch-file")
    List<FileVO> bindBatchFile(@RequestBody ArrayList<FileVO> fileVOs);

    /**
     * @Description 移除业务原附件，并绑定新的附件到业务上
     * @param  fileVO 附件对象
     * @return
     */
    @PostMapping(value = "file-feign/replace-bind-file")
    Boolean replaceBindFile(@RequestBody FileVO fileVO);

    /**
     * @Description 移除业务原附件，并批量绑定新的附件到业务上
     * @param  fileVOs 附件对象
     * @return
     */
    @PostMapping(value = "file-feign/replace-bind-batch-file")
    Boolean replaceBindBatchFile(@RequestBody ArrayList<FileVO> fileVOs);


    /**
     * @Description 按业务ID查询附件【对象传递参数方式】
     * @param  businessIds 业务IDS
     * @return List<FileVO>
     */
    @PostMapping("file-feign/find-in-business-ids")
    List<FileVO> findInBusinessIds(@RequestBody List<Long> businessIds);


    /**
     * @Description 删除业务相关附件
     * @param businessIds 业务IDS
     * @return Boolean
     */
    @DeleteMapping("file-feign/delete-by-business-ids")
    Boolean deleteByBusinessIds(@RequestBody ArrayList<Long> businessIds);

    /**
     * @Description 定时清理文件
     * @return
     */
    @DeleteMapping("file-feign/clear-file")
    Boolean clearFile();

    /**
     * @Description 延迟队列清理文件
     * @return
     */
    @DeleteMapping("file-feign/clear-file-id/{id}")
    Boolean clearFileById(@PathVariable("id") Long id);


}
