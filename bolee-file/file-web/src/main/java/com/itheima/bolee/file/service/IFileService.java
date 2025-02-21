package com.itheima.bolee.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.dto.file.UploadMultipartFile;
import com.itheima.bolee.file.pojo.File;
import com.itheima.bolee.framework.commons.dto.file.FileVO;

import java.util.List;
import java.util.Set;

/**
 * @Description：附件 服务类
 */
public interface IFileService extends IService<File> {

    /**
     * @Description 按业务ID查询附件
     * @param  businessId 附件对象业务Id
     * @return
     */
    List<FileVO> findFileVoByBusinessId(Long businessId) ;

    /**
     * @Description 附件列表
     * @param fileVO 查询条件
     * @return
     */
    Page<FileVO> findFileVOPage(FileVO fileVO, int pageNum, int pageSize);

    /***
     * @description 定时清理文件
     * @return
     */
    List<FileVO> needClearFile();

    /***
     * @description 延迟队列清理文件
     * @return
     */
    FileVO needClearFileById(String id);

    /**
     * @Description 业务绑定单个附件
     * @param  fileVO 附件对象
     * @return
     */
    FileVO bindFile(FileVO fileVO);

    /**
     * @Description 相同业务绑定多个附件
     * @param  fileVOs 相同业务的多个附件对象
     * @return
     */
    List<FileVO> bindBatchFile(List<FileVO> fileVOs);

    /**
     * @Description 移除业务原图片，并绑定新的图片到业务上
     * @param  fileVO 附件对象
     * @return
     */
    Boolean replaceBindFile(FileVO fileVO);

    /**
     * @Description 移除业务原图片，并批量绑定新的图片到业务上
     * @param  fileVOs 附件对象
     * @return
     */
    Boolean replaceBindBatchFile(List<FileVO> fileVOs);

    /**
     * @description 按业务ID查询附件
     * @param  businessIds 业务ids
     * @return java.util.List<com.itheima.travel.req.FileVO>
     */
    List<FileVO> findInBusinessIds(List<Long> businessIds);

    /**
     * @Description 删除业务相关附件
     * @param businessIds 附件信息ids
     * @return
     */
    Boolean deleteInBusinessIds(List<Long> businessIds);

    /**
     * @Description 删除业务相关附件
     * @param ids 附件信息ids
     * @return
     */
    Boolean deleteInIds(List<Long> ids);

    /**
     * @Description 定时清理文件
     * @return Boolean
     */
    Boolean clearFile();

    /**
     * @Description 定时清理文件
     * @return Boolean
     */
    Boolean clearFileById(String fileId);

    /**
     * @Description 查询所有业务对应附件
     * @return Set<Long>
     */
    Set<Long> findBusinessIdAll();

    /***
     * @description 文件简单上传
     *
     * @param uploadMultipartFile
     * @param fileVO
     * @return FileVO
     */
    FileVO upLoad(UploadMultipartFile uploadMultipartFile, FileVO fileVO);

    /***
     * @description 初始化分片上传
     *
     * @param fileVO
     * @return FileVO
     */
    FileVO initiateMultipartUpload(FileVO fileVO);

    /***
     * @description 分片每个分片
     * @param uploadMultipartFile
     * @param filePartVo
     * @return String
     */
    String uploadPart(UploadMultipartFile uploadMultipartFile, FilePartVO filePartVo);

    /***
     * @description 合并所有分片
     * @param fileVO
     * @return String
     */
    String completeMultipartUpload(FileVO fileVO);

    /***
     * @description 文件下载
     * @param fileId
     * @return FileVO
     */
    FileVO downLoad(Long fileId);
}
