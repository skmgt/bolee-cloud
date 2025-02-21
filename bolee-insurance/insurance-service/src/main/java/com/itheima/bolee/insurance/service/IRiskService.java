package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.Risk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.RiskVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：风险表服务类
 */
public interface IRiskService extends IService<Risk> {

    /**
     * @Description 多条件查询风险表分页
     * @param riskVO 风险表查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<RiskVO>
     */
    Page<RiskVO> findPage(RiskVO riskVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询风险表
    * @param riskId 合同信息ID
    * @return RiskVO
    */
    RiskVO findById(String riskId);

    /**
     * @Description 风险表新增
     * @param riskVO 风险表查询条件
     * @return RiskVO
     */
    RiskVO save(RiskVO riskVO);

    /**
     * @Description 风险表修改
     * @param riskVO 风险表对象
     * @return RiskVO
     */
    Boolean update(RiskVO riskVO);

    /**
     * @Description 风险表删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询风险表列表
     * @param riskVO 查询条件
     * @return: List<RiskVO>
     */
    List<RiskVO> findList(RiskVO riskVO);
}
