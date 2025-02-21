package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WorryFreeRiskItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WorryFreeRiskItemVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：服务类
 */
public interface IWorryFreeRiskItemService extends IService<WorryFreeRiskItem> {

    /**
     * @Description 多条件查询分页
     * @param worryFreeRiskItemVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WorryFreeRiskItemVO>
     */
    Page<WorryFreeRiskItemVO> findPage(WorryFreeRiskItemVO worryFreeRiskItemVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询
    * @param worryFreeRiskItemId 合同信息ID
    * @return WorryFreeRiskItemVO
    */
    WorryFreeRiskItemVO findById(String worryFreeRiskItemId);

    /**
     * @Description 新增
     * @param worryFreeRiskItemVO 查询条件
     * @return WorryFreeRiskItemVO
     */
    WorryFreeRiskItemVO save(WorryFreeRiskItemVO worryFreeRiskItemVO);

    /**
     * @Description 修改
     * @param worryFreeRiskItemVO 对象
     * @return WorryFreeRiskItemVO
     */
    Boolean update(WorryFreeRiskItemVO worryFreeRiskItemVO);

    /**
     * @Description 删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询列表
     * @param worryFreeRiskItemVO 查询条件
     * @return: List<WorryFreeRiskItemVO>
     */
    List<WorryFreeRiskItemVO> findList(WorryFreeRiskItemVO worryFreeRiskItemVO);

    /**
     * 根据当前登录人获取对应的风险项
     * @param id
     * @return
     */
    List<WorryFreeRiskItemVO> findMyRiskItem(Long id);

    void cleanCustomerHistry(String id);
}
