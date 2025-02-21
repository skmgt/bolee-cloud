package com.itheima.bolee.insurance.pojo;

import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
@TableName("tab_insurance_approve")
@ApiModel(value="InsuranceApprove对象", description="保批信息")
public class InsuranceApprove extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceApprove(Long id,String dataState,String companyNo,String warrantyNo,String approveState,Date approveTime,Integer sortNo){
        super(id, dataState);
        this.companyNo=companyNo;
        this.warrantyNo=warrantyNo;
        this.approveState=approveState;
        this.approveTime=approveTime;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "公司编号")
    private String companyNo;

    @ApiModelProperty(value = "保险合同编号")
    private String warrantyNo;

    @ApiModelProperty(value = "批保状态(0未批保 1批保发送失败 2批保中 3批保通过 4.保批不通过)")
    private String approveState;

    @ApiModelProperty(value = "修改时间")
    private Date approveTime;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
