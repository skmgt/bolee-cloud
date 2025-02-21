package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import com.itheima.bolee.security.pojo.Dept;

import java.util.List;
import java.util.Set;

/**
 * @Description：部门表服务类
 */
public interface IDeptService extends IService<Dept> {

    /**
     * @Description 多条件查询部门表分页列表
     * @param deptVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<DeptVO>
     */
    Page<DeptVO> findDeptPage(DeptVO deptVO, int pageNum, int pageSize);

    /**
     * @Description 创建部门表
     * @param deptVO 对象信息
     * @return DeptVO
     */
    DeptVO createDept(DeptVO deptVO);

    /**
     * @Description 修改部门表
     * @param deptVO 对象信息
     * @return Boolean
     */
    Boolean updateDept(DeptVO deptVO);

    /**
     * @description 多条件查询部门表列表
     * @param deptVO 查询条件
     * @return: List<DeptVO>
     */
    List<DeptVO> findDeptList(DeptVO deptVO);

    /**
     * @description 组织部门树形
     * @param parentDeptNo 根节点
     * @param checkedDeptNos 选择节点
     * @return: TreeVO
     */
    TreeVO deptTreeVO(String parentDeptNo, String[] checkedDeptNos);

    /**
     * @description 批量查詢部門
     * @param deptNos 查询条件
     * @return: TreeVO
     */
    List<DeptVO> findDeptInDeptNos(Set<String> deptNos);

    /**
     * @description 员工对应部门
     * @param userId 员工
     * @return: List<Dept>
     */
    List<DeptVO> findDeptVOListByUserId(Long userId);

    /**
     * @Description 创建编号
     * @param parentDeptNo 父部门编号
     * @return
     */
    String createDeptNo(String parentDeptNo);

    /***
     * @description 角色对应部门
     * @param roleIds
     * @return
     */
    List<DeptVO> findDeptVOListInRoleId(List<Long> roleIds);
}
