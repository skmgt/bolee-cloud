package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * PiGraphVO
 *
 * @author: wgl
 * @describe: PiGraphVO对象--饼图VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value = "PiGraphVO对象", description = "统计分析:饼图VO类")
public class PiChartsVO {

    @Builder
    public PiChartsVO(BigDecimal value, String name){
        this.value=value;
        this.name=name;
    }

    @ApiModelProperty(value = "分块比例")
    private BigDecimal value;

    @ApiModelProperty(value = "分块名")
    private String name;
}
