package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * SelfWarrantyVO
 *
 * @author: wgl
 * @describe: 个人保险数据对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="个人保险数据对象", description="个人保险数据对象")
public class SelfWarrantyVO{


    @Builder
    public SelfWarrantyVO(Integer insureNums, Integer insureIngNums,Integer notRenewalNums) {
        this.insureNums = insureNums;
        this.insureIngNums = insureIngNums;
        this.notRenewalNums = notRenewalNums;
    }

    @ApiModelProperty(value = "保单数量")
    private Integer insureNums;

    @ApiModelProperty(value = "持续中的保单数量")
    private Integer insureIngNums;

    @ApiModelProperty(value = "待续保单数量")
    private Integer notRenewalNums;

}
