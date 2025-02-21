package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChosesDTO
 *
 * @author: wgl
 * @describe: 选项VO
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="问卷详情", description="每个疾病的问卷详情信息")
public class ChosesVO {
    @ApiModelProperty(value = "选项名")
    private String choseName;
    @ApiModelProperty(value = "选项值")
    private String choseValue;
}