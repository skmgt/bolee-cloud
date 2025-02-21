package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.ResourceCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.RoleCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import com.itheima.bolee.framework.commons.constant.security.UserCacheConstant;
import com.itheima.bolee.framework.commons.dto.security.DeptVO;
import com.itheima.bolee.framework.commons.dto.security.ResourceVO;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.framework.commons.enums.security.RoleEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.mapper.DeptMapper;
import com.itheima.bolee.security.mapper.ResourceMapper;
import com.itheima.bolee.security.mapper.RoleMapper;
import com.itheima.bolee.security.pojo.Role;
import com.itheima.bolee.security.pojo.RoleDept;
import com.itheima.bolee.security.pojo.RoleResource;
import com.itheima.bolee.security.service.IRoleDeptService;
import com.itheima.bolee.security.service.IRoleResourceService;
import com.itheima.bolee.security.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description：角色表服务实现类
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    IRoleResourceService roleResourceService;

    @Autowired
    IRoleDeptService roleDeptService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    DeptMapper deptMapper;

    /***
     * @description 多条件查询
     * @param queryWrapper
     * @param roleVO
     * @return
     */
    private QueryWrapper<Role> queryWrapper(QueryWrapper<Role> queryWrapper, RoleVO roleVO){
        //角色名称查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getRoleName())) {
            queryWrapper.lambda().likeRight(Role::getRoleName,roleVO.getRoleName());
        }
        //权限标识查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getLabel())) {
            queryWrapper.lambda().likeRight(Role::getLabel,roleVO.getLabel());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getSortNo())) {
            queryWrapper.lambda().eq(Role::getSortNo,roleVO.getSortNo());
        }
        //创建者查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getCreateBy())) {
            queryWrapper.lambda().eq(Role::getCreateBy,roleVO.getCreateBy());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Role::getUpdateBy,roleVO.getUpdateBy());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getRemark())) {
            queryWrapper.lambda().eq(Role::getRemark,roleVO.getRemark());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(roleVO.getDataState())) {
            queryWrapper.lambda().eq(Role::getDataState,roleVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByAsc(Role::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = RoleCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#roleVO.hashCode()")
    public Page<RoleVO> findRolePage(RoleVO roleVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Role> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            //构建多条件查询，代码生成后自己可自行调整
            this.queryWrapper(queryWrapper,roleVO);
            //执行分页查询
            Page<RoleVO> pageVo = BeanConv.toPage(page(page, queryWrapper),RoleVO.class);
            if (!EmptyUtil.isNullOrEmpty(pageVo.getRecords())){
                List<Long> roleIds = pageVo.getRecords().stream().map(RoleVO::getId).collect(Collectors.toList());
                //查询对应资源
                List<ResourceVO> resourceList = resourceMapper.findResourceVOListInRoleId(roleIds);
                //查询对应数据权限
                List<DeptVO> deptVOList = deptMapper.findDeptVOListInRoleId(roleIds);
                pageVo.getRecords().forEach(n->{
                    //装配资源
                    Set<String> resourceNoSet = Sets.newHashSet();
                    resourceList.forEach(r->{
                        if (String.valueOf(n.getId()).equals(String.valueOf(r.getRoleId()))){
                            resourceNoSet.add(r.getResourceNo());
                        }
                    });
                    if (!EmptyUtil.isNullOrEmpty(resourceNoSet))
                        n.setCheckedResourceNos(resourceNoSet.toArray(new String[resourceNoSet.size()]));
                    //装配数据权限
                    Set<String> deptNoSet = Sets.newHashSet();
                    if (!EmptyUtil.isNullOrEmpty(deptVOList)){
                        deptVOList.forEach(d->{
                            if (String.valueOf(n.getId()).equals(String.valueOf(d.getRoleId()))){
                                deptNoSet.add(d.getDeptNo());
                            }
                        });
                        if (!EmptyUtil.isNullOrEmpty(deptNoSet)){
                            n.setCheckedDeptNos(deptNoSet.toArray(new String[deptNoSet.size()]));
                        }
                    }
                });
            }
            return pageVo;
        }catch (Exception e){
            log.error("角色表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = RoleCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =RoleCacheConstant.BASIC,key = "#result.id")})
    public RoleVO createRole(RoleVO roleVO) {
        try {
            //转换RoleVO为Role
            Role role = BeanConv.toBean(roleVO, Role.class);
            boolean flag = save(role);
            if (!flag){
                throw  new RuntimeException("保存角色失败");
            }
            //保存角色资源中间信息
            List<RoleResource> roleResourceList = Lists.newArrayList();
            Arrays.asList(roleVO.getCheckedResourceNos()).forEach(n->{
                RoleResource roleResource = RoleResource.builder()
                        .roleId(role.getId())
                        .resourceNo(n)
                        .build();
                roleResourceList.add(roleResource);
            });
            flag = roleResourceService.saveBatch(roleResourceList);
            if (!flag){
                throw new RuntimeException("保存角色资源信息出错");
            }
            //自定义权限：保存角色部门中间信息
            if (SecurityConstant.DATA_SCOPE_1.equals(roleVO.getDataScope())){
                //保存角色部门中间信息
                List<RoleDept> roleDeptList = Lists.newArrayList();
                Arrays.asList(roleVO.getCheckedDeptNos()).forEach(n->{
                    RoleDept roleDept = RoleDept.builder()
                            .roleId(role.getId())
                            .deptNo(n)
                            .dataState(SuperConstant.DATA_STATE_0)
                            .build();
                    roleDeptList.add(roleDept);
                });
                flag = roleDeptService.saveBatch(roleDeptList);
                if (!flag){
                    throw new RuntimeException("保存角色部门中间信息出错");
                }
            }
            if (flag){
                return BeanConv.toBean(role,RoleVO.class);
            }
            return null;
        } catch (Exception e) {
            log.error("保存角色表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.SAVE_FAIL);
        }
    }


    @Transactional
    @Caching(evict = {@CacheEvict(value = RoleCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = UserCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = UserCacheConstant.DATA_SECURITY,allEntries = true),
            @CacheEvict(value =RoleCacheConstant.BASIC,key = "#roleVO.id")})
    @Override
    public Boolean updateRole(RoleVO roleVO) {
        try {
            //转换RoleVO为Role
            Role role = BeanConv.toBean(roleVO, Role.class);
            Boolean flag = updateById(role);
            if (!flag){
                throw  new RuntimeException("修改角色失败");
            }
            //删除原有角色资源中间信息
            flag = roleResourceService.deleteRoleResourceByRoleId(role.getId());
            if (!flag){
                throw  new RuntimeException("删除原有角色资源中间信息失败");
            }
            //保存角色资源中间信息
            List<RoleResource> roleResourceList = Lists.newArrayList();
            Arrays.asList(roleVO.getCheckedResourceNos()).forEach(n->{
                RoleResource roleResource = RoleResource.builder()
                        .roleId(role.getId())
                        .resourceNo(n)
                        .build();
                roleResourceList.add(roleResource);
            });
            flag = roleResourceService.saveBatch(roleResourceList);
            if (!flag){
                throw  new RuntimeException("保存角色资源中间信息失败");
            }
            //删除原有角色数据权限:这里不需要判断返回结果，有可能之前就没有自定义数据权限
            roleDeptService.deleteRoleDeptByRoleId(role.getId());
            //保存先的数据权限
            if (SecurityConstant.DATA_SCOPE_1.equals(roleVO.getDataScope())){
                //保存角色部门中间信息
                List<RoleDept> roleDeptList = Lists.newArrayList();
                Arrays.asList(roleVO.getCheckedDeptNos()).forEach(n->{
                    RoleDept roleDept = RoleDept.builder()
                            .roleId(role.getId())
                            .deptNo(n)
                            .dataState(SuperConstant.DATA_STATE_0)
                            .build();
                    roleDeptList.add(roleDept);
                });
                flag = roleDeptService.saveBatch(roleDeptList);
                if (!flag){
                    throw new RuntimeException("保存角色部门中间信息出错");
                }
            }
            return flag;
        } catch (Exception e) {
            log.error("修改角色表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = RoleCacheConstant.LIST,key ="#roleVO.hashCode()")
    public List<RoleVO> findRoleList(RoleVO roleVO) {
        try {
            //构建查询条件
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,roleVO);
            List<RoleVO> records = BeanConv.toBeanList(list(queryWrapper),RoleVO.class);
            if (!EmptyUtil.isNullOrEmpty(records)){
                List<Long> roleIds = records.stream().map(RoleVO::getId).collect(Collectors.toList());
                //查询对应资源
                List<ResourceVO> resourceList = resourceMapper.findResourceVOListInRoleId(roleIds);
                //查询对应数据权限
                List<DeptVO> deptVOList = deptMapper.findDeptVOListInRoleId(roleIds);
                records.forEach(n->{
                    //装配资源
                    Set<String> resourceNoSet = Sets.newHashSet();
                    resourceList.forEach(r->{
                        if (n.getId().equals(r.getRoleId())){
                            resourceNoSet.add(r.getResourceNo());
                        }
                    });
                    if (!EmptyUtil.isNullOrEmpty(resourceNoSet))
                        n.setCheckedResourceNos(resourceNoSet.toArray(new String[resourceNoSet.size()]));
                    //装配数据权限
                    Set<String> deptNoSet = Sets.newHashSet();
                    if (!EmptyUtil.isNullOrEmpty(deptVOList)){
                        deptVOList.forEach(d->{
                            if (n.getId().equals(d.getRoleId())){
                                deptNoSet.add(d.getDeptNo());
                            }
                        });
                        if (!EmptyUtil.isNullOrEmpty(deptNoSet)){
                            n.setCheckedDeptNos(deptNoSet.toArray(new String[deptNoSet.size()]));
                        }
                    }
                });
            }
            return records;
        } catch (Exception e) {
            log.error("查询角色表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = RoleCacheConstant.LIST,key ="#userIds.hashCode()")
    public List<RoleVO> findRoleVOListInUserId(List<Long> userIds) {
        try {
            return roleMapper.findRoleVOListInUserId(userIds);
        } catch (Exception e) {
            log.error("查询角色表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.LIST_FAIL);
        }

    }

    @Override
    @Cacheable(value = RoleCacheConstant.LIST,key ="#userId")
    public List<RoleVO> findRoleVOListByUserId(Long userId) {
        try {
            return roleMapper.findRoleVOListByUserId(userId);
        } catch (Exception e) {
            log.error("查询角色表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(RoleEnum.LIST_FAIL);
        }
    }

}
