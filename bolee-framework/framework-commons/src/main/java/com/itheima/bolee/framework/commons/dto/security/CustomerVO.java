package com.itheima.bolee.framework.commons.dto.security;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description：客户表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public CustomerVO(Long id, String dataState, String username, String password, String nickName, String email,
                       String clientId, String realName, String mobile, String sex, String remark,String companyNo,
                       String[] checkedIds, String openId, String userToken, DataSecurityVO dataSecurityVO) {
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
        this.userToken = userToken;
        this.dataSecurityVO = dataSecurityVO;
    }

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "客户账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "客户昵称")
    private String nickName;

    @ApiModelProperty(value = "客户邮箱")
    private String email;

    @ApiModelProperty(value = "客户端")
    private String clientId;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "客户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "三方openId")
    private String openId;

    @ApiModelProperty(value = "客户令牌")
    private String userToken;

    @ApiModelProperty(value = "客户数据权限")
    private DataSecurityVO dataSecurityVO;

    @ApiModelProperty(value = "文件VO对象")
    private List<FileVO> fileVOs;

}
