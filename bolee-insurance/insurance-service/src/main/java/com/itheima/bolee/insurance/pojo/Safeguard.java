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
 * @Description：保障项
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_safeguard")
@ApiModel(value="Safeguard对象", description="保障项")
public class Safeguard extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Safeguard(Long id,String dataState,String safeguardKey,String safeguardKeyName,String safeguardVal,String safeguardType,Integer sortNo,String remake){
        super(id, dataState);
        this.safeguardKey=safeguardKey;
        this.safeguardKeyName=safeguardKeyName;
        this.safeguardVal=safeguardVal;
        this.safeguardType=safeguardType;
        this.sortNo=sortNo;
        this.remake=remake;
    }

    @ApiModelProperty(value = "保障项key：mzyl")
    private String safeguardKey;

    @ApiModelProperty(value = "保障项名称：门诊医疗")
    private String safeguardKeyName;

    @ApiModelProperty(value = "保障项值")
    private String safeguardVal;

    @ApiModelProperty(value = "保障项分类：0保障规则 1保障信息")
    private String safeguardType;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remake;


}
