package com.itheima.bolee.framework.commons.dto.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：角色表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public RoleVO(Long id, String dataState, String roleName, String label, Integer sortNo, String remark, String[] checkedResourceNos, String[] checkedDeptNos, Long userId, String dataScope, String companyNo) {
        super(id, dataState);
        this.roleName = roleName;
        this.label = label;
        this.sortNo = sortNo;
        this.remark = remark;
        this.checkedResourceNos = checkedResourceNos;
        this.checkedDeptNos = checkedDeptNos;
        this.userId = userId;
        this.dataScope = dataScope;
        this.companyNo = companyNo;
    }

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "TREE结构：选中资源No")
    private String[] checkedResourceNos;

    @ApiModelProperty(value = "TREE结构：选中部门No")
    private String[] checkedDeptNos;

    @ApiModelProperty(value = "人员查询部门：当前人员Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "数据范围（0本人  1自定义）")
    private String dataScope;

    @ApiModelProperty(value = "企业No")
    private String companyNo;

}
