package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.dto.InsuranceTypeInfoVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.pojo.SearchRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.SearchRecordVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：搜索记录服务类
 */
public interface ISearchRecordService extends IService<SearchRecord> {

    /**
     * @Description 多条件查询搜索记录分页
     * @param searchRecordVO 搜索记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SearchRecordVO>
     */
    Page<SearchRecordVO> findPage(SearchRecordVO searchRecordVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询搜索记录
    * @param searchRecordId 合同信息ID
    * @return SearchRecordVO
    */
    SearchRecordVO findById(String searchRecordId);

    /**
     * @Description 搜索记录新增
     * @param searchRecordVO 搜索记录查询条件
     * @return SearchRecordVO
     */
    SearchRecordVO save(SearchRecordVO searchRecordVO);

    /**
     * @Description 搜索记录修改
     * @param searchRecordVO 搜索记录对象
     * @return SearchRecordVO
     */
    Boolean update(SearchRecordVO searchRecordVO);

    /**
     * @Description 搜索记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询搜索记录列表
     * @param searchRecordVO 查询条件
     * @return: List<SearchRecordVO>
     */
    List<SearchRecordVO> findList(SearchRecordVO searchRecordVO);

    /**
     * 查询热搜榜排名前10的数据
     * @param limit
     * @return
     */
    List<SearchRecordVO> tindTopRecord(int limit);

    /**
     * 清空历史记录
     * @return
     */
    Boolean cleanSearchRecord();

    /**
     * 搜索我的历史保险
     * @param userVO
     * @return
     */
    List<InsuranceVO> findMyHistory(UserVO userVO);
}
