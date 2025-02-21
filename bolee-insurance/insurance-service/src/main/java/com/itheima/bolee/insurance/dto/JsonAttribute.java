package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName JsonAttribute.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
@ApiModel(value="json属性对象", description="json属性对象")
public class JsonAttribute implements Serializable {

    @Builder
    public JsonAttribute(String name, String val) {
        this.name = name;
        this.val = val;
    }

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String val;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "计算值：通常用于投入周期时长，保障时长计算")
    private String calculatedVal;

}
