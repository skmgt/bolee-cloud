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
@TableName("tab_role")
public class Role extends BasePojo {

    private String roleName;

    private String label;

    private Integer sortNo;

    private String dataState;

    private String remark;

    private String dataScope;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public Role(Long id, String dataState, String roleName, String label, Integer sortNo, String dataState1, String remark, String dataScope, String companyNo) {
        super(id, dataState);
        this.roleName = roleName;
        this.label = label;
        this.sortNo = sortNo;
        this.dataState = dataState1;
        this.remark = remark;
        this.dataScope = dataScope;
        this.companyNo = companyNo;
    }
}
