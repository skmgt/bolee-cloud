package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.dto.SearchRecordVO;
import com.itheima.bolee.insurance.dto.SickVO;
import com.itheima.bolee.insurance.pojo.SickSearchRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.SickSearchRecordVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：疾病搜索记录服务类
 */
public interface ISickSearchRecordService extends IService<SickSearchRecord> {

    /**
     * @Description 多条件查询疾病搜索记录分页
     * @param sickSearchRecordVO 疾病搜索记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SickSearchRecordVO>
     */
    Page<SickSearchRecordVO> findPage(SickSearchRecordVO sickSearchRecordVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询疾病搜索记录
    * @param sickSearchRecordId 合同信息ID
    * @return SickSearchRecordVO
    */
    SickSearchRecordVO findById(String sickSearchRecordId);

    /**
     * @Description 疾病搜索记录新增
     * @param sickSearchRecordVO 疾病搜索记录查询条件
     * @return SickSearchRecordVO
     */
    SickSearchRecordVO save(SickSearchRecordVO sickSearchRecordVO);

    /**
     * @Description 疾病搜索记录修改
     * @param sickSearchRecordVO 疾病搜索记录对象
     * @return SickSearchRecordVO
     */
    Boolean update(SickSearchRecordVO sickSearchRecordVO);

    /**
     * @Description 疾病搜索记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询疾病搜索记录列表
     * @param sickSearchRecordVO 查询条件
     * @return: List<SickSearchRecordVO>
     */
    List<SickSearchRecordVO> findList(SickSearchRecordVO sickSearchRecordVO);

    /**
     * 批量插入搜索记录
     * @param searchRecordVOList
     */
    Boolean saveRecord(List<SickSearchRecordVO> searchRecordVOList);

    /**
     * 疾病热搜榜
     * @return
     */
    List<SickVO> hotSickList();

}
