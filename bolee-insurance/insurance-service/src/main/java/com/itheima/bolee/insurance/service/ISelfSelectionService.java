package com.itheima.bolee.insurance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.dto.InsuranceTypeInfoVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.dto.SelfSelectionVO;
import com.itheima.bolee.insurance.pojo.SelfSelection;

import java.util.List;

/**
 * @Description：自选保险服务类
 */
public interface ISelfSelectionService extends IService<SelfSelection> {

    /**
     * @param selfSelectionVO 自选保险查询条件
     * @param pageNum         页码
     * @param pageSize        每页条数
     * @return Page<SelfSelectionVO>
     * @Description 多条件查询自选保险分页
     */
    Page<SelfSelectionVO> findPage(SelfSelectionVO selfSelectionVO, int pageNum, int pageSize);

    /**
     * @param selfSelectionId 合同信息ID
     * @return SelfSelectionVO
     * @Description 多条件查询自选保险
     */
    SelfSelectionVO findById(String selfSelectionId);

    /**
     * @param selfSelectionVO 自选保险查询条件
     * @return SelfSelectionVO
     * @Description 自选保险新增
     */
    SelfSelectionVO save(SelfSelectionVO selfSelectionVO);

    /**
     * @param selfSelectionVO 自选保险对象
     * @return SelfSelectionVO
     * @Description 自选保险修改
     */
    Boolean update(SelfSelectionVO selfSelectionVO);

    /**
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     * @Description 自选保险删除
     */
    Boolean delete(String[] checkedIds);

    /**
     * @param selfSelectionVO 查询条件
     * @description 多条件查询自选保险列表
     * @return: List<SelfSelectionVO>
     */
    List<SelfSelectionVO> findList(SelfSelectionVO selfSelectionVO);

    /**
     * 查询我的自选列表方法
     */
    List<InsuranceTypeInfoVO> findMySelection(UserVO userVO);

}
