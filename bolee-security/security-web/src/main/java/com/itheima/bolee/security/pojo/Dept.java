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
@TableName("tab_dept")
public class Dept extends BasePojo {

    private String parentDeptNo;

    private String deptNo;

    private String deptName;

    private Integer sortNo;

    private Long leaderId;

    private String companyNo;

    private static final long serialVersionUID = 1L;

    @Builder
    public Dept(Long id, String dataState, String parentDeptNo, String deptNo, String deptName, Integer sortNo, Long leaderId, String companyNo) {
        super(id, dataState);
        this.parentDeptNo = parentDeptNo;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.sortNo = sortNo;
        this.leaderId = leaderId;
        this.companyNo = companyNo;
    }
}
