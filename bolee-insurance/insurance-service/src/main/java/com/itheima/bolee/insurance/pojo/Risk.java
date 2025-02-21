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
 * @Description：风险表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_risk")
@ApiModel(value="Risk对象", description="风险表")
public class Risk extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Risk(Long id,String dataState,String riskTypeKey,String riskTypeName,String riskKey,String riskName,Integer sortNo){
        super(id, dataState);
        this.riskTypeKey=riskTypeKey;
        this.riskTypeName=riskTypeName;
        this.riskKey=riskKey;
        this.riskName=riskName;
        this.sortNo=sortNo;
    }

    @ApiModelProperty(value = "风险项类型key")
    private String riskTypeKey;

    @ApiModelProperty(value = "风险项类型name")
    private String riskTypeName;

    @ApiModelProperty(value = "风险项key")
    private String riskKey;

    @ApiModelProperty(value = "风险项名称")
    private String riskName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;


}
