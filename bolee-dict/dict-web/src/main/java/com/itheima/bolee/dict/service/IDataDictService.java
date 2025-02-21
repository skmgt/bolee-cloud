package com.itheima.bolee.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.dict.pojo.DataDict;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;

import java.util.List;
import java.util.Set;

/**
 * @Description：数据字典表 服务类
 */
public interface IDataDictService extends IService<DataDict> {

    /***
     * @description 数据字典列表数据
     *
     * @param dataDictVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<DataDictVO>
     */
    Page<DataDictVO> findDataDictVOPage(DataDictVO dataDictVO, int pageNum, int pageSize);

    /**
     * @Description 检测key是否已经存在
     * @return
     */
    Boolean checkByDataKey(String dataKey);

    /**
     * @Description 保存字典数据
     * @return
     */
    DataDictVO saveDataDict(DataDictVO dataDictVO) ;

    /**
     * @Description 修改字典数据
     * @return
     */
    DataDictVO updateDataDict(DataDictVO dataDictVO);

    /**
     * @Description 根据dataKey获取value
     * @return DataDictVO
     */
    DataDictVO findDataDictVOByDataKey(String dataKey);

    /**
     * @Description  获得所有不重复的ParentKey的set集合
     * @return Set<String>
     */
    Set<String> findParentKeyAll();

    /**
     * @Description 获取父key下的数据
     * @return List<DataDictVO>
     */
    List<DataDictVO> findDataDictVOByParentKey(String parentKey);

    /**
     * @Description  获得所有不重复的DataKey的set集合
     * @return Set<String>
     */
    Set<String> findDataKeyAll();
}
