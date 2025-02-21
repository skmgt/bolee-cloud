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
 * @Description：筛选项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_condition")
@ApiModel(value="Condition对象", description="筛选项")
public class Condition extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Condition(Long id,String dataState,String conditionKey,String conditionKeyName,String conditionVal,Integer sortNo,String remake){
        super(id, dataState);
        this.conditionKey=conditionKey;
        this.conditionKeyName=conditionKeyName;
        this.conditionVal=conditionVal;
        this.sortNo=sortNo;
        this.remake=remake;
    }

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
