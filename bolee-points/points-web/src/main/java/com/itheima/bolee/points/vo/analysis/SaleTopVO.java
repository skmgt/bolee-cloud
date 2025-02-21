package com.itheima.bolee.points.vo.analysis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * SaleTopVO
 *
 * @author: wgl
 * @describe: 销售榜单顶部数据VO
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@ApiModel(value="SaleTopVO投保顶部对象", description="统计分析:投保顶部对象")
public class SaleTopVO {

    @Builder
    public SaleTopVO(BigDecimal totalInsurance, Long totalInsuranceNum, BigDecimal insuranceAvgMoney,
                     BigDecimal insuranceAvgPerson,LocalDate reportTime) {
        this.totalInsurance = totalInsurance;
        this.totalInsuranceNum = totalInsuranceNum;
        this.insuranceAvgMoney = insuranceAvgMoney;
        this.insuranceAvgPerson = insuranceAvgPerson;
        this.reportTime = reportTime;
    }

    /**
     * 投保总保费
     */
    @ApiModelProperty(value = "投保总保费")
    private BigDecimal totalInsurance= BigDecimal.ZERO;

    /**
     * 投保总保单
     */
    @ApiModelProperty(value = "投保总保单")
    private Long totalInsuranceNum=0L;

    /**
     * 投保件均保费
     */
    @ApiModelProperty(value = "投保件均保费")
    private BigDecimal insuranceAvgMoney= BigDecimal.ZERO;

    /**
     * 投保人均保费
     */
    @ApiModelProperty(value = "投保人均保费")
    private BigDecimal insuranceAvgPerson= BigDecimal.ZERO;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;

}
