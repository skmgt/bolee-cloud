package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.ResourceCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.RoleCacheConstant;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.basic.TreeItemVO;
import com.itheima.bolee.framework.commons.dto.security.MenuVO;
import com.itheima.bolee.framework.commons.dto.security.MenuMetaVO;
import com.itheima.bolee.framework.commons.dto.security.ResourceVO;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.framework.commons.enums.security.ResourceEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.security.mapper.ResourceMapper;
import com.itheima.bolee.security.mapper.RoleMapper;
import com.itheima.bolee.security.pojo.Resource;
import com.itheima.bolee.security.service.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：权限表服务实现类
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleMapper roleMapper;

    /***
     * @description 多条件查询
     * @param queryWrapper
     * @param resourceVO
     * @return
     */
    private QueryWrapper<Resource> queryWrapper(QueryWrapper<Resource> queryWrapper, ResourceVO resourceVO){
        //资源编号
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getResourceNo())) {
            queryWrapper.lambda().eq(Resource::getResourceNo,resourceVO.getResourceNo());
        }
        //父资源编号查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getParentResourceNo())) {
            queryWrapper.lambda().likeRight(Resource::getParentResourceNo,
                    NoProcessing.processString(resourceVO.getParentResourceNo()));
        }
        //资源名称查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getResourceName())) {
            queryWrapper.lambda().likeRight(Resource::getResourceName,resourceVO.getResourceName());
        }
        //资源类型（m目录 c菜单 f按钮 r微服务）查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getResourceType())) {
            queryWrapper.lambda().eq(Resource::getResourceType,resourceVO.getResourceType());
        }
        //请求地址查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getRequestPath())) {
            queryWrapper.lambda().likeRight(Resource::getRequestPath,resourceVO.getRequestPath());
        }
        //权限标识查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getLabel())) {
            queryWrapper.lambda().likeRight(Resource::getLabel,resourceVO.getLabel());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getSortNo())) {
            queryWrapper.lambda().eq(Resource::getSortNo,resourceVO.getSortNo());
        }
        //图标查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getIcon())) {
            queryWrapper.lambda().eq(Resource::getIcon,resourceVO.getIcon());
        }
        //创建者查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getCreateBy())) {
            queryWrapper.lambda().eq(Resource::getCreateBy,resourceVO.getCreateBy());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Resource::getUpdateBy,resourceVO.getUpdateBy());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getRemark())) {
            queryWrapper.lambda().eq(Resource::getRemark,resourceVO.getRemark());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(resourceVO.getDataState())) {
            queryWrapper.lambda().eq(Resource::getDataState,resourceVO.getDataState());
        }
        //按创sortNo排序
        queryWrapper.lambda().orderByAsc(Resource::getSortNo);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#resourceVO.hashCode()")
    public Page<ResourceVO> findResourcePage(ResourceVO resourceVO, int pageNum, int pageSize) {

        try {
            //构建分页对象
            Page<Resource> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,resourceVO);
            //执行分页查询
            return BeanConv.toPage(page(page, queryWrapper),ResourceVO.class);
        }catch (Exception e){
            log.error("权限表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = ResourceCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.TREE,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.MENUS,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =ResourceCacheConstant.BASIC,key = "#result.id")})
    public ResourceVO createResource(ResourceVO resourceVO) {
        try {
            //转换ResourceVO为Resource
            Resource resource = BeanConv.toBean(resourceVO, Resource.class);
            resource.setResourceNo(this.createResourceNo(resource.getParentResourceNo()));
            boolean flag = save(resource);
            if (flag){
                return BeanConv.toBean(resource,ResourceVO.class);
            }
            return null;
        } catch (Exception e) {
            log.error("保存权限表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = ResourceCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.TREE,allEntries = true),
            @CacheEvict(value = ResourceCacheConstant.MENUS,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = RoleCacheConstant.LIST,allEntries = true),
            @CacheEvict(value =ResourceCacheConstant.BASIC,key = "#resourceVO.id")})
    public Boolean updateResource(ResourceVO resourceVO) {
        try {
            //转换ResourceVO为Resource
            Resource resource = BeanConv.toBean(resourceVO, Resource.class);
            return updateById(resource);
        } catch (Exception e) {
            log.error("修改权限表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.LIST,key ="#resourceVO.hashCode()")
    public List<ResourceVO> findResourceList(ResourceVO resourceVO) {
        try {
            //构建查询条件
            QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
            this.queryWrapper(queryWrapper,resourceVO);
            return BeanConv.toBeanList(list(queryWrapper),ResourceVO.class);
        } catch (Exception e) {
            log.error("删除权限表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.TREE,key ="#parentResourceNo+'-'+#checkedResourceNos")
    public TreeVO resourceTreeVO(String parentResourceNo, String[] checkedResourceNos) {
        try {
            List<Resource> resourceList = Lists.newLinkedList();
            QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
            //根节点查询树形结构
            if (EmptyUtil.isNullOrEmpty(parentResourceNo)){
                parentResourceNo = SuperConstant.ROOT_PARENT_ID;
            }
            //指定节点查询树形结构
            queryWrapper.lambda()
                    .eq(Resource::getDataState, SuperConstant.DATA_STATE_0)
                    .likeRight(Resource::getParentResourceNo, NoProcessing.processString(parentResourceNo))
                    .orderByAsc(Resource::getResourceNo);
            resourceList.addAll(list(queryWrapper));
            if (EmptyUtil.isNullOrEmpty(resourceList)){
                throw new RuntimeException("部门信息为定义！");
            }
            List<TreeItemVO> treeItemVOList  = new ArrayList<>();
            List<String> expandedIds = new ArrayList<>();
            //递归构建树形结构
            List<String> checkedResourceNoList = Lists.newArrayList();
            if (!EmptyUtil.isNullOrEmpty(checkedResourceNos)){
                checkedResourceNoList = Arrays.asList(checkedResourceNos);
            }
            recursionTreeItem(treeItemVOList,resourceList.get(0),resourceList,checkedResourceNoList,expandedIds);
            return TreeVO.builder()
                    .items(treeItemVOList)
                    .checkedIds(checkedResourceNoList)
                    .expandedIds(expandedIds)
                    .build();
        } catch (Exception e) {
            log.error("查询资源表TREE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.TREE_FAIL);
        }
    }

    private void recursionTreeItem(List<TreeItemVO> treeItemVOList, Resource ResourceRoot, List<Resource> resourceList,
                                   List<String> checkedResourceNos, List<String> expandedIds) {
        TreeItemVO treeItem = TreeItemVO.builder()
                .id(ResourceRoot.getResourceNo())
                .label(ResourceRoot.getResourceName())
                .build();
        //判断是否选择
        if (!EmptyUtil.isNullOrEmpty(checkedResourceNos)&&
                checkedResourceNos.contains(ResourceRoot.getResourceNo())){
            treeItem.setIsChecked(true);
        }else {
            treeItem.setIsChecked(false);
        }
        //是否默认展开:如果当前的资源为第二层或者第三层则展开
        if(NoProcessing.processString(ResourceRoot.getResourceNo()).length()/3==2||
           NoProcessing.processString(ResourceRoot.getResourceNo()).length()/3==3){
            expandedIds.add(ResourceRoot.getResourceNo());
        }
        //获得当前资源下子资源
        List<Resource> childrenResource = resourceList.stream()
            .filter(n -> n.getParentResourceNo().equals(ResourceRoot.getResourceNo()))
            .collect(Collectors.toList());
        if (!EmptyUtil.isNullOrEmpty(childrenResource)){
            List<TreeItemVO> listChildren  = Lists.newArrayList();
            childrenResource.forEach(n->{
                this.recursionTreeItem(listChildren,n,resourceList,checkedResourceNos,expandedIds);});
            treeItem.setChildren(listChildren);
        }
        treeItemVOList.add(treeItem);
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.LIST,key ="#roleIds.hashCode()")
    public List<ResourceVO> findResourceVOListInRoleId(List<Long> roleIds) {
        try {
            return resourceMapper.findResourceVOListInRoleId(roleIds);
        } catch (Exception e) {
            log.error("删除权限表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.LIST,key ="#userId")
    public List<ResourceVO> findResourceVOListByUserId(Long userId) {
        try {
            return resourceMapper.findResourceVOListByUserId(userId);
        } catch (Exception e) {
            log.error("查询权限表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = ResourceCacheConstant.MENUS,key ="#systemCode")
    public List<MenuVO> menus(String systemCode) {
        try {
            //查询当前系统的根节点
            QueryWrapper<Resource> parentQueryWrapper =new QueryWrapper<>();
            parentQueryWrapper.lambda()
                    .eq(Resource::getParentResourceNo, SuperConstant.ROOT_PARENT_ID)
                    .eq(Resource::getDataState,SuperConstant.DATA_STATE_0)
                    .eq(Resource::getResourceType,SuperConstant.SYSTEM)
                    .orderByAsc(Resource::getSortNo);
            Resource parentResource = resourceMapper.selectOne(parentQueryWrapper);
            //构建一级菜单
            QueryWrapper<Resource> queryWrapper =new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(Resource::getParentResourceNo,parentResource.getResourceNo())
                    .eq(Resource::getDataState,SuperConstant.DATA_STATE_0)
                    .eq(Resource::getResourceType,SuperConstant.CATALOGUE)
                    .orderByAsc(Resource::getSortNo);
            List<Resource> resources = resourceMapper.selectList(queryWrapper);
            List<MenuVO> list  = new ArrayList<>();
            recursionMenuVO(list,resources,SuperConstant.COMPONENT_LAYOUT);
            return list;
        } catch (Exception e) {
            log.error("查询资源表TREE异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(ResourceEnum.MENUS_FAIL);
        }
    }

    /**
     * @Description 递归菜单
     */
    public List<MenuVO> recursionMenuVO(List<MenuVO> list,List<Resource> resources,String component){

        for (Resource resource : resources) {
            List<RoleVO> roleVOList = roleMapper.findRoleVOListByResourceNo(resource.getResourceNo());
            List<String> roleLabels = new ArrayList<>();
            roleVOList.forEach(n->{
                roleLabels.add(n.getLabel());
            });
            MenuMetaVO menuMetaVO = MenuMetaVO.builder()
                .icon(resource.getIcon())
                .roles(roleLabels)
                .title(resource.getResourceName())
                .build();
            MenuVO menuVO = MenuVO.builder()
                .name(resource.getResourceName())
                .hidden(false)
                .component(resource.getRequestPath())
                .meta(menuMetaVO)
                .build();
            if (SuperConstant.COMPONENT_LAYOUT.equals(component)){
                menuVO.setPath("/"+resource.getRequestPath());
                menuVO.setComponent(SuperConstant.COMPONENT_LAYOUT);
            }else {
                menuVO.setPath(resource.getRequestPath());
                menuVO.setComponent(component+"/"+resource.getRequestPath());
            }
            QueryWrapper<Resource> queryWrapper =new QueryWrapper<>();
            queryWrapper.lambda()
                .eq(Resource::getParentResourceNo,resource.getResourceNo())
                .eq(Resource::getResourceType,SuperConstant.MENU)
                .eq(Resource::getDataState,SuperConstant.DATA_STATE_0)
                .orderByAsc(Resource::getSortNo);
            List<Resource> resourceChildren = resourceMapper.selectList(queryWrapper);
            if (resourceChildren.size()>0){
                menuVO.setRedirect("/"+resource.getResourceName()+"/"+resourceChildren.get(0).getResourceName());
                List<MenuVO> listChildren  = new ArrayList<>();
                this.recursionMenuVO(listChildren,resourceChildren,resource.getRequestPath());
                menuVO.setChildren(listChildren);
            }
            list.add(menuVO);
        }
        return list;
    }

    @Override
    public String createResourceNo(String parentResourceNo) {
        ResourceVO resourceVO = ResourceVO.builder()
            .parentResourceNo(parentResourceNo)
            .build();
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Resource::getParentResourceNo,parentResourceNo);
        List<Resource> resourceList = list(queryWrapper);
        //无下属节点则创建下属节点
        if (EmptyUtil.isNullOrEmpty(resourceList)){
            return NoProcessing.createNo(parentResourceNo,false);
        //有下属节点则累加下属节点
        }else {
            Long resourceNo = resourceList.stream()
                .map(resource -> { return Long.valueOf(resource.getResourceNo());})
                .max(Comparator.comparing(i -> i)).get();
            return NoProcessing.createNo(String.valueOf(resourceNo),true);
        }
    }
}
