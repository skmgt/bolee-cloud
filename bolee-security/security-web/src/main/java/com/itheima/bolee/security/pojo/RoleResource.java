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
@TableName("tab_role_resource")
public class RoleResource extends BasePojo {

    private Long roleId;

    private String resourceNo;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public RoleResource(Long id, String dataState, Long roleId, String resourceNo,String companyNo) {
        super(id, dataState);
        this.roleId = roleId;
        this.resourceNo = resourceNo;
        this.companyNo=companyNo;
    }
}
