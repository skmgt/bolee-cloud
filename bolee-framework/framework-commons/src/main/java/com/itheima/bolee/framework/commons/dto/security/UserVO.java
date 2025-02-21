package com.itheima.bolee.framework.commons.dto.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.validation.Create;
import com.itheima.bolee.framework.commons.validation.Update;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @Description：用户表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public UserVO(Long id, String dataState, String companyNo, String username, String password, String nickName, String email, String clientId, String realName, String mobile, String sex, String remark, String openId, Set<String> roleVOIds, Set<DeptPostUserVO> deptPostUserVOs, Set<String> roleLabels, Set<String> resourceRequestPaths, String userToken, DataSecurityVO dataSecurityVO, String deptNo, String postNo, Long roleId, Boolean onlyAuthenticate) {
        super(id, dataState);
        this.companyNo = companyNo;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.clientId = clientId;
        this.realName = realName;
        this.mobile = mobile;
        this.sex = sex;
        this.remark = remark;
        this.openId = openId;
        this.roleVOIds = roleVOIds;
        this.deptPostUserVOs = deptPostUserVOs;
        this.roleLabels = roleLabels;
        this.resourceRequestPaths = resourceRequestPaths;
        this.userToken = userToken;
        this.dataSecurityVO = dataSecurityVO;
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.roleId = roleId;
        this.onlyAuthenticate = onlyAuthenticate;
    }

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "客户端")
    private String clientId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "三方openId")
    private String openId;

    @ApiModelProperty(value = "查询用户：用户角色主键集合")
    private Set<String>  roleVOIds;

    @ApiModelProperty(value = "查询用户：所属部门职位")
    private Set<DeptPostUserVO>  deptPostUserVOs;

    @ApiModelProperty(value = "构建令牌：用户角色标识")
    private Set<String> roleLabels;

    @ApiModelProperty(value = "构建令牌：用户权限路径")
    private Set<String> resourceRequestPaths;

    @ApiModelProperty(value = "用户令牌")
    private String userToken;

    @ApiModelProperty(value = "数据权限")
    private DataSecurityVO dataSecurityVO;

    @ApiModelProperty(value = "部门编号【查询关联】")
    private String deptNo;

    @ApiModelProperty(value = "职位编号【查询关联】")
    private String postNo;

    @ApiModelProperty(value = "角色主键【查询关联】")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;

    @ApiModelProperty(value = "只做请求认证,客户只需请求认证，员工除请求认证，还需访问授权")
    private Boolean onlyAuthenticate ;

}
