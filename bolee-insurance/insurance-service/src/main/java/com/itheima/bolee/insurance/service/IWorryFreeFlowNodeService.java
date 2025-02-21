package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.WorryFreeFlowNode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.WorryFreeFlowNodeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：省心配流程节点记录服务类
 */
public interface IWorryFreeFlowNodeService extends IService<WorryFreeFlowNode> {

    /**
     * @Description 多条件查询省心配流程节点记录分页
     * @param worryFreeFlowNodeVO 省心配流程节点记录查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WorryFreeFlowNodeVO>
     */
    Page<WorryFreeFlowNodeVO> findPage(WorryFreeFlowNodeVO worryFreeFlowNodeVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询省心配流程节点记录
    * @param worryFreeFlowNodeId 合同信息ID
    * @return WorryFreeFlowNodeVO
    */
    WorryFreeFlowNodeVO findById(String worryFreeFlowNodeId);

    /**
     * @Description 省心配流程节点记录新增
     * @param worryFreeFlowNodeVO 省心配流程节点记录查询条件
     * @return WorryFreeFlowNodeVO
     */
    WorryFreeFlowNodeVO save(WorryFreeFlowNodeVO worryFreeFlowNodeVO);

    /**
     * @Description 省心配流程节点记录修改
     * @param worryFreeFlowNodeVO 省心配流程节点记录对象
     * @return WorryFreeFlowNodeVO
     */
    Boolean update(WorryFreeFlowNodeVO worryFreeFlowNodeVO);

    /**
     * @Description 省心配流程节点记录删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询省心配流程节点记录列表
     * @param worryFreeFlowNodeVO 查询条件
     * @return: List<WorryFreeFlowNodeVO>
     */
    List<WorryFreeFlowNodeVO> findList(WorryFreeFlowNodeVO worryFreeFlowNodeVO);

    /**
     * 根据登录用户id查询流程节点
     * @param id
     * @return
     */
    List<String> flowNode(Long id);

    void cleanCustomerHistry(String id);
}
