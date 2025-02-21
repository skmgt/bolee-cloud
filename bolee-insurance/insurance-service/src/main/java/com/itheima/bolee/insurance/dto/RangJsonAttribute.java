package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName JsonAttribute.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
@ApiModel(value="范围json属性对象", description="范围json属性对象")
public class RangJsonAttribute implements Serializable {

    @Builder
    public RangJsonAttribute(String start, String startUnit, String end, String endUnit) {
        this.start = start;
        this.startUnit = startUnit;
        this.end = end;
        this.endUnit = endUnit;
    }

    @ApiModelProperty(value = "起始")
    private String start;

    @ApiModelProperty(value = "起始单位")
    private String startUnit;

    @ApiModelProperty(value = "结束")
    private String end;

    @ApiModelProperty(value = "结束单位")
    private String endUnit;


}
