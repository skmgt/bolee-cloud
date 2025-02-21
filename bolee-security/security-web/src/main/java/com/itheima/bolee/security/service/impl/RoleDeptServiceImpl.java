package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.security.mapper.RoleDeptMapper;
import com.itheima.bolee.security.pojo.RoleDept;
import com.itheima.bolee.security.service.IRoleDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：权限表服务实现类
 */
@Service
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements IRoleDeptService {

    @Override
    public Boolean deleteRoleDeptByRoleId(Long roleId) {
        UpdateWrapper<RoleDept> updateWrapper = new UpdateWrapper();
        updateWrapper.lambda().eq(RoleDept::getRoleId,roleId);
        return remove(updateWrapper);
    }

    @Override
    public Boolean deleteRoleDeptInRoleId(List<Long> roleIds) {
        UpdateWrapper<RoleDept> updateWrapper = new UpdateWrapper();
        updateWrapper.lambda().in(RoleDept::getRoleId,roleIds);
        return remove(updateWrapper);
    }

}
