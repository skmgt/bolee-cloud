package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * QuestionDTO
 *
 * @author: wgl
 * @describe: 问题对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="问卷详情", description="每个疾病的问卷详情信息")
public class QuestionVO {

    @ApiModelProperty(value = "问卷唯一标识")
    private String questionKey;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户选项")
    private List<ChosesVO> choses;
}