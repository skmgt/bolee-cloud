package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.Safeguard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.SafeguardVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Set;

/**
 * @Description：保障项服务类
 */
public interface ISafeguardService extends IService<Safeguard> {

    /**
     * @Description 多条件查询保障项分页
     * @param safeguardVO 保障项查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SafeguardVO>
     */
    Page<SafeguardVO> findPage(SafeguardVO safeguardVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询保障项
    * @param safeguardId 合同信息ID
    * @return SafeguardVO
    */
    SafeguardVO findById(String safeguardId);

    /**
     * @Description 保障项新增
     * @param safeguardVO 保障项查询条件
     * @return SafeguardVO
     */
    SafeguardVO save(SafeguardVO safeguardVO);

    /**
     * @Description 保障项修改
     * @param safeguardVO 保障项对象
     * @return SafeguardVO
     */
    Boolean update(SafeguardVO safeguardVO);

    /**
     * @Description 保障项删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询保障项列表
     * @param safeguardVO 查询条件
     * @return: List<SafeguardVO>
     */
    List<SafeguardVO> findList(SafeguardVO safeguardVO);

    /**
     * 根据保障项key找到所有可在首页展示的保障项
     * @param safeguardKeyList
     * @return
     */
    List<SafeguardVO> findShowPageItemByKey(List<String> safeguardKeyList);

    /***
     * @description 按safeguardKey查询SafeguardVO
     * @param safeguardKey 保障项key
     * @return SafeguardVO
     */
    SafeguardVO findBySafeguardKey(String safeguardKey);

}
