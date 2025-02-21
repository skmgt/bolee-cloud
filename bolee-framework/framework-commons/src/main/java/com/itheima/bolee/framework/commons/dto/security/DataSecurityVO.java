package com.itheima.bolee.framework.commons.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName DataSecurity.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DataSecurityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "仅本人数据权限")
    private Boolean youselfData;

    @ApiModelProperty(value = "数据权限对应部门编号集合")
    private List<String> deptNos;

    @Builder
    public DataSecurityVO(Boolean youselfData, List<String> deptNos) {
        this.youselfData = youselfData;
        this.deptNos = deptNos;
    }
}
