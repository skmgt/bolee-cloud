package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.pojo.BrowsingHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.BrowsingHistoryVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：浏览记录服务类
 */
public interface IBrowsingHistoryService extends IService<BrowsingHistory> {

    /**
     * @Description 多条件查询浏览记录分页
     * @param browsingHistoryVO 浏览记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<BrowsingHistoryVO>
     */
    Page<BrowsingHistoryVO> findPage(BrowsingHistoryVO browsingHistoryVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询浏览记录
    * @param browsingHistoryId 合同信息ID
    * @return BrowsingHistoryVO
    */
    BrowsingHistoryVO findById(String browsingHistoryId);

    /**
     * @Description 浏览记录新增
     * @param browsingHistoryVO 浏览记录查询条件
     * @return BrowsingHistoryVO
     */
    BrowsingHistoryVO save(BrowsingHistoryVO browsingHistoryVO);

    /**
     * @Description 浏览记录修改
     * @param browsingHistoryVO 浏览记录对象
     * @return BrowsingHistoryVO
     */
    Boolean update(BrowsingHistoryVO browsingHistoryVO);

    /**
     * @Description 浏览记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询浏览记录列表
     * @param browsingHistoryVO 查询条件
     * @return: List<BrowsingHistoryVO>
     */
    List<BrowsingHistoryVO> findList(BrowsingHistoryVO browsingHistoryVO);


    /**
     * 找到我近半年的历史记录
     * @return
     */
    List<BrowsingHistoryVO> findMyHistory(UserVO userVO);
}
