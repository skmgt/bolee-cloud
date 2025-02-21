package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * QuestionTemplateDTO
 *
 * @author: wgl
 * @describe: 问卷对象
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="问卷对象", description="健康咨询问卷对象")
public class QuestionTemplateVO {

    @ApiModelProperty(value = "疾病列表")
    private List<SickVO> sicks;

    @ApiModelProperty(value = "问卷列表,key为疾病key value为对应的问卷")
    private Map<String,List<QuestionVO>> questions;
}
