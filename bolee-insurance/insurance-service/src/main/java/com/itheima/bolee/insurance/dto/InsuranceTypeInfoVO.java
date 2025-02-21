package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * InsuranceTypeInfoVO
 *
 * @author: wgl
 * @describe: 保险类型下的保险VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="保险类型下的保险VO类", description="保险分类列表")
public class InsuranceTypeInfoVO {

    @ApiModelProperty(value = "保险类型")
    private String checkRule;

    @ApiModelProperty(value = "当前类型下的保险列表")
    private List<InsuranceVO> data;
}
