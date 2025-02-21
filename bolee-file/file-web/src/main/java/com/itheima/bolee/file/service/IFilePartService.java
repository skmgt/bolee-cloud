package com.itheima.bolee.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.file.pojo.FilePart;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;

import java.util.List;

/**
 * @Description：服务类
 */
public interface IFilePartService extends IService<FilePart> {

    /**
     * @Description 多条件查询分页列表
     * @param filePartVo 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<FilePart>
     */
    Page<FilePart> findFilePartPage(FilePartVO filePartVo, int pageNum, int pageSize);

    /**
     * @Description 创建
     * @param filePartVo 对象信息
     * @return FilePart
     */
    FilePart createFilePart(FilePartVO filePartVo);

    /**
     * @Description 修改
     * @param filePartVo 对象信息
     * @return Boolean
     */
    Boolean updateFilePart(FilePartVO filePartVo);

    /**
     * @Description 删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean deleteFilePart(String[] checkedIds);

    /**
     * @description 多条件查询列表
     * @param filePartVo 查询条件
     * @return: List<FilePart>
     */
    List<FilePart> findFilePartList(FilePartVO filePartVo);

    /**
     * @description 按upLoadId删除记录
     * @param upLoadId 上传ID
     * @return: List<FilePart>
     */
    Boolean deleteFilePartByUpLoadId(String upLoadId);
}
