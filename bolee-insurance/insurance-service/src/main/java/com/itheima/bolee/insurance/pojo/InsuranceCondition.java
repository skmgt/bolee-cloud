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
 * @Description：保险筛选项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_insurance_condition")
@ApiModel(value="InsuranceCondition对象", description="保险筛选项")
public class InsuranceCondition extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public InsuranceCondition(Long id,String dataState,String insuranceId,String conditionKey,String conditionKeyName,String conditionVal,Integer sortNo,String remake){
        super(id, dataState);
        this.insuranceId=insuranceId;
        this.conditionKey=conditionKey;
        this.conditionKeyName=conditionKeyName;
        this.conditionVal=conditionVal;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "保险产品ID")
    private String insuranceId;

    @ApiModelProperty(value = "条件项key")
    private String conditionKey;

    @ApiModelProperty(value = "条件项名称")
    private String conditionKeyName;

    @ApiModelProperty(value = "条件项值")
    private String conditionVal;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;


}
