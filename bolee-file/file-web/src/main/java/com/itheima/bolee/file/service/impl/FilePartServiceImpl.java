package com.itheima.bolee.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.file.mapper.FilePartMapper;
import com.itheima.bolee.file.pojo.FilePart;
import com.itheima.bolee.file.service.IFilePartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description：服务实现类
 */
@Service
public class FilePartServiceImpl extends ServiceImpl<FilePartMapper, FilePart> implements IFilePartService {

    @Override
    public Page<FilePart> findFilePartPage(FilePartVO filePartVo, int pageNum, int pageSize) {
        //构建分页对象
        Page<FilePart> page = new Page<>(pageNum,pageSize);
        //构建查询条件
        QueryWrapper<FilePart> queryWrapper = new QueryWrapper<>();
        //构建多条件查询，代码生成后自己可自行调整
        //唯一上传id查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getUploadId())) {
            queryWrapper.lambda().eq(FilePart::getUploadId,filePartVo.getUploadId());
        }
        //当前片数查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getPartNumber())) {
            queryWrapper.lambda().eq(FilePart::getPartNumber,filePartVo.getPartNumber());
        }
        //分片上传结果(json)查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getUploadResult())) {
            queryWrapper.lambda().eq(FilePart::getUploadResult,filePartVo.getUploadResult());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getDataState())) {
            queryWrapper.lambda().eq(FilePart::getDataState,filePartVo.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(FilePart::getCreateTime);
        //执行分页查询
        return page(page, queryWrapper);
    }

    @Override
    public FilePart createFilePart(FilePartVO filePartVo) {
        //转换FilePartVO为FilePart
        FilePart filePart = BeanConv.toBean(filePartVo, FilePart.class);
        boolean flag = save(filePart);
        if (flag){
        return filePart;
        }
        return null;
    }

    @Override
    public Boolean updateFilePart(FilePartVO filePartVo) {
        //转换FilePartVO为FilePart
        FilePart filePart = BeanConv.toBean(filePartVo, FilePart.class);
        return updateById(filePart);
    }

    @Override
    public Boolean deleteFilePart(String[] checkedIds) {
        //转换数组为集合
        List<String> ids = Arrays.asList(checkedIds);
        List<Long> idsLong = new ArrayList<>();
        ids.forEach(n->{
            idsLong.add(Long.valueOf(n));
        });
        return removeByIds(idsLong);
    }

    @Override
    public List<FilePart> findFilePartList(FilePartVO filePartVo) {
        //构建查询条件
        QueryWrapper<FilePart> queryWrapper = new QueryWrapper<>();
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getId())) {
            queryWrapper.lambda().eq(FilePart::getId,filePartVo.getId());
        }
        //唯一上传id查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getUploadId())) {
            queryWrapper.lambda().eq(FilePart::getUploadId,filePartVo.getUploadId());
        }
        //当前片数查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getPartNumber())) {
            queryWrapper.lambda().eq(FilePart::getPartNumber,filePartVo.getPartNumber());
        }
        //当前片数查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getMd5())) {
            queryWrapper.lambda().eq(FilePart::getMd5,filePartVo.getMd5());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(filePartVo.getDataState())) {
            queryWrapper.lambda().eq(FilePart::getDataState,filePartVo.getDataState());
        }
        return list(queryWrapper);
    }

    @Override
    public Boolean deleteFilePartByUpLoadId(String upLoadId) {
        UpdateWrapper<FilePart> updateWrapperp = new UpdateWrapper<>();
        updateWrapperp.lambda().eq(FilePart::getUploadId,upLoadId);
        return remove(updateWrapperp);
    }
}
