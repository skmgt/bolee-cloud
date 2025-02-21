package com.itheima.bolee.file.hystrix;

import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileHystrix.java
 * @Description FileFeign的Hystrix
 */
@Component
public class FileBusinessHystrix implements FileBusinessFeign {


    @Override
    public List<FileVO> bindFile(FileVO fileVO) {
        return null;
    }

    @Override
    public List<FileVO> bindBatchFile(ArrayList<FileVO> fileVOs) {
        return null;
    }

    @Override
    public Boolean replaceBindFile(FileVO fileVO) {
        return null;
    }

    @Override
    public Boolean replaceBindBatchFile(ArrayList<FileVO> fileVOs) {
        return null;
    }

    @Override
    public List<FileVO> findInBusinessIds(List<Long> businessIds) {
        return null;
    }

    @Override
    public Boolean deleteByBusinessIds(ArrayList<Long> businessIds) {
        return null;
    }

    @Override
    public Boolean clearFile() {
        return null;
    }

    @Override
    public Boolean clearFileById(Long id) {
        return null;
    }
}
