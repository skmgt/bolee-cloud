package com.itheima.bolee.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.dict.mapper.DataDictMapper;
import com.itheima.bolee.dict.pojo.DataDict;
import com.itheima.bolee.dict.service.IDataDictService;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.dict.DataDictCacheConstant;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import com.itheima.bolee.framework.commons.enums.dict.DataDictEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description：数据字典表 服务实现类
 */
@Slf4j
@Service
public class DataDictServiceImpl extends ServiceImpl<DataDictMapper, DataDict> implements IDataDictService {

    /***
     * @description 构建多条件查询条件
     * @param dataDictVO 查询条件
     * @return 查询条件
     */
    private QueryWrapper queryWrapper(DataDictVO dataDictVO){
        QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
        //多条件查询
        if (!EmptyUtil.isNullOrEmpty(dataDictVO.getDiscription())){
            queryWrapper.lambda().like(DataDict::getDiscription,dataDictVO.getDiscription());
        }
        if (!EmptyUtil.isNullOrEmpty(dataDictVO.getParentKey())){
            queryWrapper.lambda().like(DataDict::getParentKey,dataDictVO.getParentKey());
        }
        if (!EmptyUtil.isNullOrEmpty(dataDictVO.getDataKey())){
            queryWrapper.lambda().like(DataDict::getDataKey,dataDictVO.getDataKey());
        }
        if (!EmptyUtil.isNullOrEmpty(dataDictVO.getDataValue())){
            queryWrapper.lambda().like(DataDict::getDataValue,dataDictVO.getDataValue());
        }
        if (!EmptyUtil.isNullOrEmpty(dataDictVO.getDataState())){
            queryWrapper.lambda().eq(DataDict::getDataState,dataDictVO.getDataState());
        }
        queryWrapper.lambda().orderByDesc(DataDict::getCreateTime).orderByAsc(DataDict::getDataKey);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = DataDictCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#dataDictVO.hashCode()")
    public Page<DataDictVO> findDataDictVOPage(DataDictVO dataDictVO, int pageNum, int pageSize) {
        try {
            Page<DataDict> page = new Page<>(pageNum, pageSize);
            QueryWrapper<DataDict> queryWrapper = this.queryWrapper(dataDictVO);
            return BeanConv.toPage(page(page, queryWrapper),DataDictVO.class);
        }catch (Exception e){
            log.error("查询数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.PAGE_FAIL);
        }

    }

    @Override
    public Boolean checkByDataKey(String dataKey) {
        try {
            QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataDict::getDataKey,dataKey);
            return !EmptyUtil.isNullOrEmpty(getOne(queryWrapper));
        }catch (Exception e){
            log.error("检查数字字典重复性异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.CHECK_FALL);
        }
    }

    @Override
    @Caching(
            evict={@CacheEvict(value = DataDictCacheConstant.PAGE,allEntries = true),
                    @CacheEvict(value = DataDictCacheConstant.PARENT_KEY,key = "#dataDictVO.parentKey")},
            put={@CachePut(value = DataDictCacheConstant.DATA_KEY,key = "#dataDictVO.dataKey")})
    @Transactional
    public DataDictVO saveDataDict(DataDictVO dataDictVO) {
        try {
            DataDict dataDict =BeanConv.toBean(dataDictVO,DataDict.class);
            boolean flag = save(dataDict);
            if (flag){
                return BeanConv.toBean(dataDict,DataDictVO.class);
            }else {
                log.error("数据字典保存异常!");
                throw new ProjectException(DataDictEnum.SAVE_FAIL);
            }
        }catch (Exception e){
            log.error("数据字典保存异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.SAVE_FAIL);
        }
    }

    @Override
    @Caching(
        evict={@CacheEvict(value = DataDictCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = DataDictCacheConstant.PARENT_KEY,key = "#dataDictVO.parentKey"),
            @CacheEvict(value = DataDictCacheConstant.DATA_KEY,key = "#dataDictVO.dataKey")})
    @Transactional
    public DataDictVO updateDataDict(DataDictVO dataDictVO) {
        try {
            DataDict dataDict = BeanConv.toBean(dataDictVO, DataDict.class);
            DataDict dataDictTemp = getById(dataDictVO.getId());
            dataDict.setCreateBy(dataDictTemp.getCreateBy());
            dataDict.setCreateTime(dataDictTemp.getCreateTime());
            boolean flag = updateById(dataDict);
            if (flag){
                return BeanConv.toBean(dataDict, DataDictVO.class);
            }else {
                log.error("修改数据字典列表异常!");
                throw new ProjectException(DataDictEnum.UPDATE_FAIL);
            }
        }catch (Exception e){
            log.error("修改数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.UPDATE_FAIL);
        }
    }

    @Override
    public Set<String> findParentKeyAll() {
        try {
            QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataDict::getDataState, SuperConstant.DATA_STATE_0);
            List<DataDict> list = list(queryWrapper);
            return EmptyUtil.isNullOrEmpty(list)?null:list.stream().map(DataDict::getParentKey).collect(Collectors.toSet());
        }catch (Exception e){
            log.error("查询数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.FIND_PARENTKEY_ALL);
        }
    }

    @Override
    @Cacheable(value = DataDictCacheConstant.PARENT_KEY,key = "#parentKey")
    public List<DataDictVO> findDataDictVOByParentKey(String parentKey) {
        try {
            QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataDict::getParentKey,parentKey)
                    .eq(DataDict::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBeanList(list(queryWrapper),DataDictVO.class);
        }catch (Exception e){
            log.error("查询数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.FIND_DATADICTVO_PARENTKEY);
        }

    }

    @Override
    public Set<String> findDataKeyAll() {
        try {
            QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataDict::getDataState, SuperConstant.DATA_STATE_0);
            List<DataDict> list = list(queryWrapper);
            return EmptyUtil.isNullOrEmpty(list)?null:list.stream().map(DataDict::getDataKey).collect(Collectors.toSet());
        }catch (Exception e){
            log.error("查询数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.FIND_DATAKEY_ALL);
        }
    }

    @Override
    @Cacheable(value = DataDictCacheConstant.DATA_KEY,key = "#dataKey")
    public DataDictVO findDataDictVOByDataKey(String dataKey) {
        try {
            QueryWrapper<DataDict> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DataDict::getDataKey,dataKey)
                    .eq(DataDict::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper),DataDictVO.class);
        }catch (Exception e){
            log.error("查询数据字典列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DataDictEnum.FIND_DATADICTVO_DATAKEY);
        }
    }

}
