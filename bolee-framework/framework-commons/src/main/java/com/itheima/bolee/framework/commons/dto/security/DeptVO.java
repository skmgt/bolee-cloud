package com.itheima.bolee.framework.commons.dto.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：部门表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeptVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public DeptVO(Long id, String dataState, String parentDeptNo, String deptNo, String deptName, Integer sortNo, Long leaderId, String[] checkedDeptNos, Long roleId, String companyNo) {
        super(id, dataState);
        this.parentDeptNo = parentDeptNo;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.sortNo = sortNo;
        this.leaderId = leaderId;
        this.checkedDeptNos = checkedDeptNos;
        this.roleId = roleId;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "父部门编号")
    private String parentDeptNo;

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "负责人Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long leaderId;

    @ApiModelProperty(value = "TREE结构：选中部门No")
    private String[] checkedDeptNos;

    @ApiModelProperty(value = "角色查询部门：部门对应角色id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;

    @ApiModelProperty(value = "部门No")
    private String companyNo;

}
