package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName JsonScoreAttribute.java
 * @Description 保障项打分对象
 */
@Data
@NoArgsConstructor
@ApiModel(value="json打分属性对象", description="json打分属性对象")
public class JsonScoreAttribute implements Serializable {

    @Builder
    public JsonScoreAttribute(String name, String val,String scorre) {
        this.name = name;
        this.val = val;
        this.scorre = scorre;
    }

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "值")
    private String val;

    @ApiModelProperty(value = "单位")
    private String scorre;

}
