package com.itheima.bolee.security.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.*;
import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.dto.security.DeptPostUserVO;
import com.itheima.bolee.framework.commons.dto.security.RoleVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.UserEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.NoProcessing;
import com.itheima.bolee.security.mapper.RoleDeptMapper;
import com.itheima.bolee.security.mapper.RoleMapper;
import com.itheima.bolee.security.mapper.UserMapper;
import com.itheima.bolee.security.pojo.DeptPostUser;
import com.itheima.bolee.security.pojo.RoleDept;
import com.itheima.bolee.security.pojo.User;
import com.itheima.bolee.security.pojo.UserRole;
import com.itheima.bolee.security.service.IDeptPostUserService;
import com.itheima.bolee.security.service.IUserRoleService;
import com.itheima.bolee.security.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description：用户表服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleDeptMapper roleDeptMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    IUserRoleService userRoleService;

    @Autowired
    IDeptPostUserService deptPostUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    /***
     * @description 构建多条件查询
     *
     * @param queryWrapper 查询条件
     * @param userVO 查询对象
     * @return
     */
    private QueryWrapper<User> queryWrapper(QueryWrapper<User> queryWrapper,UserVO userVO){
        //角色Id查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getRoleId())){
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.lambda().eq(UserRole::getRoleId,userVO.getRoleId());
            List<UserRole> roles = userRoleService.list(userRoleQueryWrapper);
            if (!EmptyUtil.isNullOrEmpty(roles)){
                List<Long> userIds = roles.stream().map(UserRole::getUserId).collect(Collectors.toList());
                queryWrapper.lambda().in(User::getId,userIds);
            }else {
                queryWrapper.lambda().in(User::getId,-1L);
            }
        }
        //部门No查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getDeptNo())){
            QueryWrapper<DeptPostUser> deptPostUserQueryWrapper = new QueryWrapper<>();
            deptPostUserQueryWrapper.lambda().likeRight(DeptPostUser::getDeptNo,NoProcessing.processString(userVO.getDeptNo()));
            List<DeptPostUser> deptPostUsers = deptPostUserService.list(deptPostUserQueryWrapper);
            if (!EmptyUtil.isNullOrEmpty(deptPostUsers)){
                List<Long> userIds= deptPostUsers.stream().map(DeptPostUser::getUserId).collect(Collectors.toList());
                queryWrapper.lambda().in(User::getId,userIds);
            }else {
                queryWrapper.lambda().in(User::getId,-1L);
            }
        }
        //用户账号查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getUsername())) {
            queryWrapper.lambda().eq(User::getUsername,userVO.getUsername());
        }
        //open_id标识查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getOpenId())) {
            queryWrapper.lambda().eq(User::getOpenId,userVO.getOpenId());
        }
        //用户昵称查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getNickName())) {
            queryWrapper.lambda().likeRight(User::getNickName,userVO.getNickName());
        }
        //用户邮箱查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getEmail())) {
            queryWrapper.lambda().likeRight(User::getEmail,userVO.getEmail());
        }
        //真实姓名查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getRealName())) {
            queryWrapper.lambda().likeRight(User::getRealName,userVO.getRealName());
        }
        //手机号码查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getMobile())) {
            queryWrapper.lambda().likeRight(User::getMobile,userVO.getMobile());
        }
        //用户性别（0男 1女 2未知）查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getSex())) {
            queryWrapper.lambda().eq(User::getSex,userVO.getSex());
        }
        //创建者查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getCreateBy())) {
            queryWrapper.lambda().eq(User::getCreateBy,userVO.getCreateBy());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getUpdateBy())) {
            queryWrapper.lambda().eq(User::getUpdateBy,userVO.getUpdateBy());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(userVO.getDataState())) {
            queryWrapper.lambda().eq(User::getDataState,userVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(User::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = UserCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#userVO.hashCode()")
    public Page<UserVO> findUserPage(UserVO userVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<User> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,userVO);
            //执行分页查询
            Page<UserVO> pageVo = BeanConv.toPage(page(page, queryWrapper),UserVO.class);
            if (!EmptyUtil.isNullOrEmpty(pageVo.getRecords())){
                List<Long> userIds = pageVo.getRecords().stream().map(UserVO::getId).collect(Collectors.toList());
                //查询对应角色
                List<RoleVO> roleVOList = roleMapper.findRoleVOListInUserId(userIds);
                //查询对应部门、职位
                List<DeptPostUserVO> deptPostUserVOList = deptPostUserService.findDeptPostUserVOListInUserId(userIds);
                //查询对应部门、职位
                pageVo.getRecords().forEach(n->{
                    //装配角色
                    Set<String> roleVOIds = Sets.newHashSet();
                    roleVOList.forEach(r->{
                        if (n.getId().equals(r.getUserId())){
                            roleVOIds.add(String.valueOf(r.getId()));
                        }
                    });
                    n.setRoleVOIds(roleVOIds);
                    //装配对应部门、职位、数据权限
                    Set<DeptPostUserVO> deptPostUserVOs = Sets.newHashSet();
                    deptPostUserVOList.forEach(r->{
                        if (n.getId().equals(r.getUserId())){
                            deptPostUserVOs.add(r);
                        }
                    });
                    n.setDeptPostUserVOs(deptPostUserVOs);
                });
            }
            return pageVo;
        }catch (Exception e){
            log.error("用户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = UserCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =UserCacheConstant.BASIC,key = "#result.id")})
    public UserVO createUser(UserVO userVO) {
        try {
            String password = bCryptPasswordEncoder.encode(securityConfigProperties.getPassworddDfaule());
            userVO.setPassword(password);
            //转换UserVO为User
            User user = BeanConv.toBean(userVO, User.class);
            user.setUsername(userVO.getEmail());
            boolean flag = save(user);
            if (!flag) {
                throw new RuntimeException("保存用户信息出错");
            }
            //保存用户角色中间表
            List<UserRole> userRoles = Lists.newArrayList();
            if(CollectionUtil.isEmpty(userVO.getRoleVOIds())){
                throw new ProjectException(UserEnum.ROLE_NOT_BE_NULL);
            }
            userVO.getRoleVOIds().forEach(r -> {
                userRoles.add(UserRole.builder().userId(user.getId()).roleId(Long.valueOf(r)).build());
            });
            flag = userRoleService.saveBatch(userRoles);
            if (!flag) {
                throw new RuntimeException("保存用户角色中间表出错");
            }
            //保存部门职位中间表
            List<DeptPostUser> deptPostUsers = Lists.newArrayList();
            if(CollectionUtil.isEmpty(userVO.getDeptPostUserVOs())){
                throw new ProjectException(UserEnum.POST_NOT_BE_NULL);
            }
            userVO.getDeptPostUserVOs().forEach(dpu -> {
                dpu.setUserId(user.getId());
                deptPostUsers.add(BeanConv.toBean(dpu, DeptPostUser.class));
            });
            flag = deptPostUserService.saveBatch(deptPostUsers);
            if (!flag) {
                throw new RuntimeException("保存部门职位中间表出错");
            }
            return BeanConv.toBean(user, UserVO.class);
        }catch (ProjectException e){
            throw e;
        } catch (Exception e) {
            log.error("保存用户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = UserCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LOGIN,allEntries = true),
            @CacheEvict(value = CustomerCacheConstant.LOGIN,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.LIST,key = "#userVO.id"),
            @CacheEvict(value = PostCacheConstant.LIST,key = "#userVO.id"),
            @CacheEvict(value = ResourceCacheConstant.LIST,key = "#userVO.id"),
            @CacheEvict(value = RoleCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = UserCacheConstant.LIST,key = "#userVO.id"),
            @CacheEvict(value = DeptPostUserCacheConstant.LIST,key ="#userVO.id"),
            @CacheEvict(value = DeptPostUserCacheConstant.DEPT_POST_USER_VO,key ="#userVO.id"),
            @CacheEvict(value = UserCacheConstant.BASIC,key = "#userVO.id")})
    public Boolean updateUser(UserVO userVO) {
        try {
            //转换UserVO为User
            User user = BeanConv.toBean(userVO, User.class);
            boolean flag = updateById(user);
            if (!flag){
                throw new RuntimeException("修改用户信息出错");
            }
            //删除角色中间表
            flag = userRoleService.deleteUserRoleByUserId(user.getId());
            if (!flag){
                throw new RuntimeException("删除角色中间表出错");
            }
            //重新保存角色中间表
            List<UserRole> userRoles = Lists.newArrayList();
            userVO.getRoleVOIds().forEach(r->{
                userRoles.add(UserRole.builder().userId(user.getId()).roleId(Long.valueOf(r)).build());
            });
            userRoleService.saveBatch(userRoles);
            //删除部门职位中间表
            flag = deptPostUserService.deleteDeptPostUserByUserId(user.getId());
            if (!flag){
                throw new RuntimeException("删除角色中间表出错");
            }
            //重新保存部门职位中间表
            List<DeptPostUser> deptPostUsers = Lists.newArrayList();
            userVO.getDeptPostUserVOs().forEach(dpu->{
                dpu.setUserId(user.getId());
                deptPostUsers.add(BeanConv.toBean(dpu,DeptPostUser.class));
            });
            flag = deptPostUserService.saveBatch(deptPostUsers);
            if (!flag){
                throw new RuntimeException("保存部门职位中间表出错");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改用户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LIST,key ="#userVO.hashCode()")
    public List<UserVO> findUserList(UserVO userVO) {
        try {
            //构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,userVO);
            List<UserVO> records =  BeanConv.toBeanList(list(queryWrapper),UserVO.class);
            if (!EmptyUtil.isNullOrEmpty(records)){
                List<Long> userIds = records.stream().map(UserVO::getId).collect(Collectors.toList());
                //查询对应角色
                List<RoleVO> roleVOList = roleMapper.findRoleVOListInUserId(userIds);
                //查询对应部门、职位
                List<DeptPostUserVO> deptPostUserVOList = deptPostUserService.findDeptPostUserVOListInUserId(userIds);
                //查询对应部门、职位
                records.forEach(n->{
                    //装配角色
                    Set<String> roleVOIds = Sets.newHashSet();
                    roleVOList.forEach(r->{
                        if (n.getId().equals(r.getUserId())){
                            roleVOIds.add(String.valueOf(r.getId()));
                        }
                    });
                    n.setRoleVOIds(roleVOIds);
                    //装配对应部门、职位、数据权限
                    Set<DeptPostUserVO> deptPostUserVOs = Sets.newHashSet();
                    deptPostUserVOList.forEach(r->{
                        if (n.getId().equals(r.getUserId())){
                            deptPostUserVOs.add(r);
                        }
                    });
                    n.setDeptPostUserVOs(deptPostUserVOs);
                });
            }
            return records;
        } catch (Exception e) {
            log.error("查询用户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LIST,key ="#deptNo")
    public  List<UserVO> findUserVOListByDeptNo(String deptNo) {
        try {
            return userMapper.findUserVOListByDeptNo(deptNo);
        } catch (Exception e) {
            log.error("查询用户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LIST,key ="#roleId")
    public List<UserVO> findUserVOListByRoleId(Long roleId) {
        try {
            return userMapper.findUserVOListByRoleId(roleId);
        } catch (Exception e) {
            log.error("查询用户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean resetPasswords(String userId) {
        String password = bCryptPasswordEncoder.encode(securityConfigProperties.getPassworddDfaule());
        User user = User.builder().id(Long.valueOf(userId)).password(password).build();
        return updateById(user);
    }

    @Override
    @Cacheable(value = UserCacheConstant.DATA_SECURITY,key ="#userId")
    public DataSecurityVO userDataSecurity(List<RoleVO> roleVOList, Long userId) {
        try {
            //角色集合不存在，直接返回空
            if (EmptyUtil.isNullOrEmpty(roleVOList)){
                return null;
            }
            DataSecurityVO dataSecurityVO = new DataSecurityVO();
            //角色集合中是否有本人权限
            List<Long> roleIds = roleVOList.stream()
                    .filter(n -> SecurityConstant.DATA_SCOPE_0.equals(n.getDataScope()))
                    .map(RoleVO::getId).collect(Collectors.toList());
            //角色集合有本人权限,返回youselfData为true,只能查看本人权限
            if (EmptyUtil.isNullOrEmpty(roleIds)){
                dataSecurityVO.setYouselfData(Boolean.FALSE);
                //角色集合中有自定义数据权限,返回youselfData为false,查询角色对应的数据权限
                QueryWrapper<RoleDept> queryWrapper = new QueryWrapper<>();
                roleIds = roleVOList.stream().map(RoleVO::getId).collect(Collectors.toList());
                queryWrapper.lambda().in(RoleDept::getRoleId,roleIds);
                List<RoleDept> roleDeptList = roleDeptMapper.selectList(queryWrapper);
                dataSecurityVO.setDeptNos(roleDeptList.stream().map(RoleDept::getDeptNo ).collect(Collectors.toList()));
            }else {
                dataSecurityVO.setYouselfData(Boolean.TRUE);
            }
            return dataSecurityVO;
        } catch (Exception e) {
            log.error("查询用户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.LIST_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LOGIN,key ="#username+'-'+#companyNo")
    public UserVO usernameLogin(String username,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername,username).eq(User::getCompanyNo,companyNo)
                    .eq(User::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper), UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.FIND_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LOGIN,key ="#mobile+'-'+#companyNo")
    public UserVO mobileLogin(String mobile,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getMobile,mobile).eq(User::getCompanyNo,companyNo)
                    .eq(User::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper), UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.FIND_FAIL);
        }
    }

    @Override
    @Cacheable(value = UserCacheConstant.LOGIN,key ="#openId+'-'+#companyNo")
    public UserVO wechatLogin(String openId,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getOpenId,openId).eq(User::getCompanyNo,companyNo)
                    .eq(User::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper), UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(UserEnum.FIND_FAIL);
        }
    }

}
