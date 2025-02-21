package com.itheima.bolee.insurance.service;

import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.pojo.CustomerRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：客户关系表服务类
 */
public interface ICustomerRelationService extends IService<CustomerRelation> {

    /**
     * @Description 多条件查询客户关系表分页
     * @param customerRelationVO 客户关系表查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<CustomerRelationVO>
     */
    Page<CustomerRelationVO> findPage(CustomerRelationVO customerRelationVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询客户关系表
    * @param customerRelationId 用户id
    * @return CustomerRelationVO
    */
    CustomerRelationVO findById(String customerRelationId);

    /**
     * @Description 多条件查询客户关系表
     * @param customerRelationIds 用户ids
     * @return CustomerRelationVO
     */
    List<CustomerRelationVO> findInId(List<String> customerRelationIds);

    /**
     * @Description 客户关系表新增
     * @param customerRelationVO 客户关系表查询条件
     * @return CustomerRelationVO
     */
    CustomerRelationVO save(CustomerRelationVO customerRelationVO);

    /**
     * @Description 客户关系表修改
     * @param customerRelationVO 客户关系表对象
     * @return CustomerRelationVO
     */
    Boolean update(CustomerRelationVO customerRelationVO);

    /**
     * @Description 客户关系表删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询客户关系表列表
     * @param customerRelationVO 查询条件
     * @return: List<CustomerRelationVO>
     */
    List<CustomerRelationVO> findList(CustomerRelationVO customerRelationVO);


    /**
     * @description 多条件查询客户关系表列表
     * @param customerRelationVO 查询条件
     * @return: List<CustomerRelationVO>
     */
    List<CustomerRelationVO> myRelation(CustomerRelationVO customerRelationVO);

    /**
     * 获取当前用户的对象
     * @param userId
     * @return
     */
    CustomerRelationVO findOne(Long userId);

    /**
     * 根据逗号截取关系字段
     * @param fields
     * @return
     */
    String[] getRelations(String fields);

    /**
     * 获取所有家庭成员列表
     * @return
     */
    List<CustomerRelationVO> findHomeList(UserVO userVO);

}
