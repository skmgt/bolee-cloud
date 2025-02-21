package com.itheima.bolee.framework.commons.dto.security;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName QueryDataSecurityVo.java
 * @Description 查询数据权限对象
 */
@Data
@NoArgsConstructor
public class QueryDataSecurityVO implements Serializable {

    public List<RoleVO> roleVOs;

    public Long userId;

    @Builder
    public QueryDataSecurityVO(List<RoleVO> roleVOs, Long userId) {
        this.roleVOs = roleVOs;
        this.userId = userId;
    }
}
