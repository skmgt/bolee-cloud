package com.itheima.bolee.points.vo.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * FunnelPlotGraphIndexVO
 *
 * @author: wgl
 * @describe: 漏斗图每个元素VO类
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="FunnelPlotGraphVO对象", description="统计分析:漏斗图元素VO类")
public class FunnelPlotChartsIndexVO {

    @Builder
    public FunnelPlotChartsIndexVO(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    @ApiModelProperty(value = "阶段名")
    private String name;

    @ApiModelProperty(value = "阶段对应的值")
    private Long value;
}
