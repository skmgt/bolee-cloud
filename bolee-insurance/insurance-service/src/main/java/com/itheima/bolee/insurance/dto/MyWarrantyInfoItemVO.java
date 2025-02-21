package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName MyWarrantyInfoVO.java
 * @Description 我的保险信息VO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "MyWarrantyInfoVO对象", description = "我的tab页：各保险数量")
public class MyWarrantyInfoItemVO implements Serializable {

    @ApiModelProperty(value = "保障类型")
    private String warrantyType;

    @ApiModelProperty(value = "保单数量")
    private Integer nums;

    @Builder
    public MyWarrantyInfoItemVO(String warrantyType, Integer nums) {
        this.warrantyType = warrantyType;
        this.nums = nums;
    }
}
