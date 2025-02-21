package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SeekAdviceVO
 *
 * @author: wgl
 * @describe: 健康咨询VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "健康咨询对象", description = "健康咨询对象")
public class SeekAdviceVO {

    @ApiModelProperty(value = "用户选择的疾病列表")
    private List<String>  sickList;


    @ApiModelProperty(value = "保险id")
    private String  insuranceId;

}
