package com.itheima.bolee.security.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_user_role")
public class UserRole extends BasePojo {

    private Long userId;

    private Long roleId;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public UserRole(Long id, String dataState, Long userId, Long roleId,String companyNo) {
        super(id, dataState);
        this.userId = userId;
        this.roleId = roleId;
        this.companyNo = companyNo;
    }
}
