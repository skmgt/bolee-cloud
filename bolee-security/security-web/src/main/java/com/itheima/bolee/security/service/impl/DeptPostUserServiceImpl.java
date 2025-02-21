package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.DeptPostUserCacheConstant;
import com.itheima.bolee.framework.commons.dto.security.DeptPostUserVO;
import com.itheima.bolee.framework.commons.enums.security.DeptPostUserEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.mapper.DeptPostUserMapper;
import com.itheima.bolee.security.pojo.DeptPostUser;
import com.itheima.bolee.security.service.IDeptPostUserService;
import com.itheima.bolee.security.service.IDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description：部门岗位用户关联表服务实现类
 */
@Slf4j
@Service
public class DeptPostUserServiceImpl extends ServiceImpl<DeptPostUserMapper, DeptPostUser> implements IDeptPostUserService {

    @Autowired
    IDeptService deptService;

    @Override
    @Cacheable(value = DeptPostUserCacheConstant.DEPT_POST_USER_VO,key ="#userIds.hashCode()")
    public List<DeptPostUserVO> findDeptPostUserVOListInUserId(List<Long> userIds) {
        try {
            QueryWrapper<DeptPostUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(DeptPostUser::getUserId,userIds)
                    .eq(DeptPostUser::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBeanList(list(queryWrapper),DeptPostUserVO.class);
        } catch (Exception e) {
            log.error("查询部门职位人员异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptPostUserEnum.LIST_FAIL);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value = DeptPostUserCacheConstant.DEPT_POST_USER_VO,key ="#userId")
    public Boolean deleteDeptPostUserByUserId(Long userId) {
        try {
            UpdateWrapper<DeptPostUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(DeptPostUser::getUserId,userId);
            return remove(updateWrapper);
        } catch (Exception e) {
            log.error("查询部门职位人员异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptPostUserEnum.DEL_FAIL);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value = DeptPostUserCacheConstant.DEPT_POST_USER_VO,allEntries = true)
    public Boolean deleteDeptPostUserInUserId(List<String> userIds) {
        try {
            UpdateWrapper<DeptPostUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(DeptPostUser::getUserId,userIds);
            return remove(updateWrapper);
        } catch (Exception e) {
            log.error("查询部门职位人员异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptPostUserEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = DeptPostUserCacheConstant.DEPT_POST_USER_VO,key ="#userId")
    public DeptPostUserVO findDeptPostUserVOByUserId(Long userId) {
        try {
            QueryWrapper<DeptPostUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(DeptPostUser::getUserId,userId)
                    .eq(DeptPostUser::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper),DeptPostUserVO.class);
        } catch (Exception e) {
            log.error("查询部门职位人员异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(DeptPostUserEnum.LIST_FAIL);
        }

    }
}
