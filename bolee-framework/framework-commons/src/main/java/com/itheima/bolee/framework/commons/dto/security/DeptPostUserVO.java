package com.itheima.bolee.framework.commons.dto.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description：部门岗位用户关联表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeptPostUserVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Builder
    public DeptPostUserVO(Long id, String dataState, Long userId, String deptNo, String postNo,String companyNo) {
        super(id, dataState);
        this.userId = userId;
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.companyNo=companyNo;
    }

    @ApiModelProperty(value = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "岗位编码")
    private String postNo;

    @ApiModelProperty(value = "企业编号")
    private String companyNo;

}
