package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.DeptCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.PostCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.RoleCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.UserCacheConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.basic.TreeItemVO;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import com.itheima.bolee.framework.commons.enums.security.DeptEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.security.mapper.DeptMapper;
import com.itheima.bolee.security.pojo.Dept;
import com.itheima.bolee.security.service.IDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description：部门表服务实现类
 */
@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Autowired
    DeptMapper deptMapper;

    /***
     * @description 多条件查询
     * @param queryWrapper 条件对象
     * @param deptVO 查询条件
     * @return
     */
    private QueryWrapper<Dept> queryWrapper(QueryWrapper<Dept> queryWrapper, DeptVO deptVO){
        //父部门编号查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getParentDeptNo())) {
            queryWrapper.lambda().likeRight(Dept::getParentDeptNo, NoProcessing.processString(deptVO.getParentDeptNo()));
        }
        //部门编号
        if (!EmptyUtil.isNullOrEmpty(deptVO.getDeptNo())) {
            queryWrapper.lambda().likeRight(Dept::getDeptNo,deptVO.getDeptNo());
        }
        //部门名称查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getDeptName())) {
            queryWrapper.lambda().likeRight(Dept::getDeptName,deptVO.getDeptName());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getSortNo())) {
            queryWrapper.lambda().eq(Dept::getSortNo,deptVO.getSortNo());
        }
        //创建者:username查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getCreateBy())) {
            queryWrapper.lambda().eq(Dept::getCreateBy,deptVO.getCreateBy());
        }
        //更新者:username查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Dept::getUpdateBy,deptVO.getUpdateBy());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(deptVO.getDataState())) {
            queryWrapper.lambda().eq(Dept::getDataState,deptVO.getDataState());
        }
        //按sottNo降序
        queryWrapper.lambda().orderByAsc(Dept::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = DeptCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#deptVO.hashCode()")
    public Page<DeptVO> findDeptPage(DeptVO deptVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Dept> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,deptVO);
            //执行分页查询
            return BeanConv.toPage(page(page, queryWrapper),DeptVO.class);
        }catch (Exception e){
            log.error("部门表PAGE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.PAGE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =DeptCacheConstant.BASIC,key = "#result.id")})
    public DeptVO createDept(DeptVO deptVO) {
        try {
            //转换DeptVO为Dept
            Dept dept = BeanConv.toBean(deptVO, Dept.class);
            dept.setDeptNo(this.createDeptNo(dept.getParentDeptNo()));
            boolean flag = save(dept);
            if (flag){
                return BeanConv.toBean(dept,DeptVO.class);
            }
            return null;
        } catch (Exception e) {
            log.error("保存部门表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.SAVE_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true),
            @CacheEvict(value = PostCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = PostCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LIST,allEntries = true),
            @CacheEvict(value =DeptCacheConstant.BASIC,key = "#deptVO.id")})
    public Boolean updateDept(DeptVO deptVO) {
        try {
            //转换DeptVO为Dept
            Dept dept = BeanConv.toBean(deptVO, Dept.class);
            return updateById(dept);
        } catch (Exception e) {
            log.error("修改部门表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = DeptCacheConstant.LIST,key ="#deptVO.hashCode()")
    public List<DeptVO> findDeptList(DeptVO deptVO) {
        try {
            //构建查询条件
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,deptVO);
            return BeanConv.toBeanList(list(queryWrapper),DeptVO.class);
        } catch (Exception e) {
            log.error("查询部门表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.LIST_FAIL);
        }
    }


    @Override
    @Cacheable(value = DeptCacheConstant.TREE,key ="#parentDeptNo+'-'+#checkedDeptNos")
    public TreeVO deptTreeVO(String parentDeptNo, String[] checkedDeptNos) {
        try {
            //根节点查询树形结构
            if (EmptyUtil.isNullOrEmpty(parentDeptNo)){
                parentDeptNo = SuperConstant.ROOT_PARENT_ID;
            }
            List<Dept> deptList = Lists.newLinkedList();
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
            //指定节点查询树形结构
            queryWrapper.lambda().eq(Dept::getDataState, SuperConstant.DATA_STATE_0)
                    .likeRight(Dept::getParentDeptNo,NoProcessing.processString(parentDeptNo))
                    .orderByAsc(Dept::getSortNo);
            deptList.addAll(list(queryWrapper));
            if (EmptyUtil.isNullOrEmpty(deptList)){
                throw new RuntimeException("部门信息为定义！");
            }
            List<TreeItemVO> treeItemVOList  = new ArrayList<>();
            List<String> expandedIds = new ArrayList<>();
            //递归构建树形结构
            List<String> checkedDeptNoList = Lists.newArrayList();
            if (!EmptyUtil.isNullOrEmpty(checkedDeptNos)){
                checkedDeptNoList = Arrays.asList(checkedDeptNos);
            }
            Dept rootDept =  deptList.stream()
                    .filter(d -> SuperConstant.ROOT_PARENT_ID.equals(d.getParentDeptNo()))
                    .collect(Collectors.toList()).get(0);
            recursionTreeItem(treeItemVOList,rootDept,deptList,checkedDeptNoList,expandedIds);
            return TreeVO.builder()
                    .items(treeItemVOList)
                    .checkedIds(checkedDeptNoList)
                    .expandedIds(expandedIds)
                    .build();
        } catch (Exception e) {
            log.error("查询部门表TREE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.TREE_FAIL);
        }
    }

    private void recursionTreeItem(List<TreeItemVO> treeItemVOList, Dept DeptRoot, List<Dept> deptList,
                                   List<String> checkedDeptNos, List<String> expandedIds) {
        TreeItemVO treeItem = TreeItemVO.builder()
            .id(DeptRoot.getDeptNo())
            .label(DeptRoot.getDeptName())
            .build();
        //判断是否选择
        if (!EmptyUtil.isNullOrEmpty(checkedDeptNos)&&checkedDeptNos.contains(DeptRoot.getDeptNo())){
            treeItem.setIsChecked(true);
        }else {
            treeItem.setIsChecked(false);
        }
        //是否默认展开:如果当前的部门为第二层或者第三层则展开
        if(NoProcessing.processString(DeptRoot.getDeptNo()).length()/3==3){
            expandedIds.add(DeptRoot.getDeptNo());
        }
        //获得当前部门下子部门
        List<Dept> childrenDept = deptList.stream()
            .filter(n -> n.getParentDeptNo().equals(DeptRoot.getDeptNo()))
            .collect(Collectors.toList());
        if (!EmptyUtil.isNullOrEmpty(childrenDept)){
            List<TreeItemVO> listChildren  = Lists.newArrayList();
            childrenDept.forEach(n->{
                this.recursionTreeItem(listChildren,n,deptList,checkedDeptNos,expandedIds);});
                treeItem.setChildren(listChildren);
        }
        treeItemVOList.add(treeItem);
    }

    @Override
    @Cacheable(value = DeptCacheConstant.TREE,key ="#deptNos.hashCode()")
    public List<DeptVO> findDeptInDeptNos(Set<String> deptNos) {
        try {
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(Dept::getDeptNo,deptNos)
                    .eq(Dept::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBeanList(list(queryWrapper),DeptVO.class);
        } catch (Exception e) {
            log.error("查询部门表TREE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.TREE_FAIL);
        }
    }

    @Override
    @Cacheable(value = DeptCacheConstant.LIST,key ="#userId")
    public List<DeptVO> findDeptVOListByUserId(Long userId) {
        try {
            return deptMapper.findDeptVOListByUserId(userId);
        } catch (Exception e) {
            log.error("查询部门表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = DeptCacheConstant.LIST,key ="#roleIds.hashCode()")
    public List<DeptVO> findDeptVOListInRoleId(List<Long> roleIds) {
        return deptMapper.findDeptVOListInRoleId(roleIds);
    }

    @Override
    public String createDeptNo(String parentDeptNo) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Dept::getParentDeptNo,parentDeptNo);
        List<Dept> deptList = deptMapper.selectList(queryWrapper);
        //无下属节点则创建下属节点
        if (EmptyUtil.isNullOrEmpty(deptList)){
            return NoProcessing.createNo(parentDeptNo,false);
            //有下属节点则累加下属节点
        }else {
            Long deptNo = deptList.stream()
                .map(dept -> { return Long.valueOf(dept.getDeptNo());})
                .max(Comparator.comparing(i -> i)).get();
            return NoProcessing.createNo(String.valueOf(deptNo),true);
        }
    }
}
