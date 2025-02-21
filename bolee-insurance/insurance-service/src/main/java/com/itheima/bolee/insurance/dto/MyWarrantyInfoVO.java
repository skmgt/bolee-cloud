package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * MyWarrantyInfoVO
 *
 * @author: wgl
 * @describe: 总的我的保单信息对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "MyWarrantyInfoVO对象", description = "我的tab页：各保险数量以及我的保单数量")
public class MyWarrantyInfoVO implements Serializable {

    @ApiModelProperty(value = "保障类型")
    private List<MyWarrantyInfoItemVO> data;

    @ApiModelProperty(value = "总数")
    private Integer nums;

    @Builder
    public MyWarrantyInfoVO(List<MyWarrantyInfoItemVO> data, Integer nums) {
        this.data = data;
        this.nums = nums;
    }
}
