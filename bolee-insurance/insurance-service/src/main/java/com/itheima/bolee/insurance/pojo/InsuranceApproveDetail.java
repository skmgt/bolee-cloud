package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @Description：保批信息
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_approve_detail")
@ApiModel(value="InsuranceApproveDetail对象", description="保批信息")
public class InsuranceApproveDetail extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceApproveDetail(Long id,String dataState,Long approveId,String type,String changeFieldKey,String title,String label,String modifyCont,String modifyReason,String approveTime,Integer sortNo){
        super(id, dataState);
        this.approveId=approveId;
        this.type=type;
        this.changeFieldKey=changeFieldKey;
        this.title=title;
        this.label=label;
        this.modifyCont=modifyCont;
        this.modifyReason=modifyReason;
        this.approveTime=approveTime;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "报批id")
    private Long approveId;

    @ApiModelProperty(value = "投保人：0  被保人：1")
    private String type;

    @ApiModelProperty(value = "修改字段键")
    private String changeFieldKey;

    @ApiModelProperty(value = "修改字段名")
    private String title;

    @ApiModelProperty(value = "修改前的内容")
    private String label;

    @ApiModelProperty(value = "修改后的内容")
    private String modifyCont;

    @ApiModelProperty(value = "原因")
    private String modifyReason;

    @ApiModelProperty(value = "修改时间")
    private String approveTime;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
