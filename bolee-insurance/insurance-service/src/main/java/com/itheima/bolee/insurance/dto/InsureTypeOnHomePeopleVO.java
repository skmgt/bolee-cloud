package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InsureTypeOnHomePeopleVO
 *
 * @author: wgl
 * @describe: 家庭人身保障向项DTP
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsureTypeOnHomePeopleVO {

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String checkRule;

    /**
     * 对应的值
     */
    @ApiModelProperty(value = "是否保障")
    private Boolean value;
}
