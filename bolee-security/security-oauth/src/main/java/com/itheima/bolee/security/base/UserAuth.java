package com.itheima.bolee.security.base;

import com.itheima.bolee.framework.commons.dto.security.DataSecurityVO;
import com.itheima.bolee.framework.commons.dto.security.DeptPostUserVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @ClassName AuthUser.java
 * @Description 自定认证用户
 */
@Data
@NoArgsConstructor
public class UserAuth implements UserDetails {

    private String id;

    //"用户账号
    private String username;

    //"密码
    private String password;

    //权限内置
    private Collection<SimpleGrantedAuthority> authorities;

    //"用户昵称
    private String nickName;

    //"用户邮箱
    private String email;

    //"真实姓名
    private String realName;

    //"手机号码
    private String mobile;

    //"用户性别（0男 1女 2未知）
    private String sex;

    //"创建者
    private Long createBy;

    //"创建时间
    private LocalDateTime createTime;

    //"更新者
    private Long updateBy;

    //"更新时间
    private LocalDateTime updateTime;

    //"备注
    private String remark;

    //"三方openId
    private String openId;

    //"查询用户：所属单位职位
    private Set<DeptPostUserVO>  deptPostUserVOset;

    //"构建令牌：用户角色标识
    private Set<String> roleLabels;

    //"构建令牌：用户权限路径
    private Set<String> resourceRequestPaths;

    //"部门编号【当前】
    private String deptNo;

    //"职位编号【当前】
    private String postNo;

    private String clientId;

    private DataSecurityVO dataSecurityVO;

    private Boolean onlyAuthenticate ;

    private String companyNo;

    public UserAuth(UserVO userVO) {
        this.setId(userVO.getId().toString());
        this.setUsername(userVO.getUsername());
        this.setPassword(userVO.getPassword());
        if (!EmptyUtil.isNullOrEmpty(userVO.getResourceRequestPaths())) {
            authorities = new ArrayList<>();
            userVO.getResourceRequestPaths().forEach(resourceRequestPath -> authorities.add(new SimpleGrantedAuthority(resourceRequestPath)));
        }
        this.setNickName(userVO.getNickName());
        this.setEmail(userVO.getEmail());
        this.setRealName(userVO.getRealName());
        this.setMobile(userVO.getMobile());
        this.setSex(userVO.getSex());
        this.setCreateTime(userVO.getCreateTime());
        this.setCreateBy(userVO.getCreateBy());
        this.setUpdateTime(userVO.getUpdateTime());
        this.setUpdateBy(userVO.getUpdateBy());
        this.setRemark(userVO.getRemark());
        this.setOpenId(userVO.getOpenId());
        this.setDeptPostUserVOset(userVO.getDeptPostUserVOs());
        this.setRoleLabels(userVO.getRoleLabels());
        this.setResourceRequestPaths(userVO.getResourceRequestPaths());
        this.setDeptNo(userVO.getDeptNo());
        this.setPostNo(userVO.getPostNo());
        this.setClientId(userVO.getClientId());
        this.setDataSecurityVO(userVO.getDataSecurityVO());
        this.setOnlyAuthenticate(userVO.getOnlyAuthenticate());
        this.setCompanyNo(userVO.getCompanyNo());
        this.setRealName(userVO.getRealName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
