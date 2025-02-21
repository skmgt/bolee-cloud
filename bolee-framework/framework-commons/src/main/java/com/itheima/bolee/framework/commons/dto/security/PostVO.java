package com.itheima.bolee.framework.commons.dto.security;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：岗位表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public PostVO(Long id, String dataState, String deptNo, String postNo, String postName, Integer sortNo, String remark, String companyNo, DeptVO deptVO) {
        super(id, dataState);
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.postName = postName;
        this.sortNo = sortNo;
        this.remark = remark;
        this.companyNo = companyNo;
        this.deptVO = deptVO;
    }

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "岗位编码：父部门编号+001【3位】")
    private String postNo;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "显示顺序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

    @ApiModelProperty(value = "职位对应部门")
    private DeptVO deptVO;

}
