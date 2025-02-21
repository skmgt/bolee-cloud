package com.itheima.bolee.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName EarningVO.java
 * @Description TODO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value="EarningVO对象", description="收益试算结果对象")
public class EarningVO implements Serializable {

    @ApiModelProperty(value = "每周期保费")
    private BigDecimal premium;

    @ApiModelProperty(value = "投入总保费")
    private BigDecimal premiums;

    @ApiModelProperty(value = "投入总期数")
    private Integer periods;

    @ApiModelProperty(value = "投入周期单位")
    private String periodicUnit;

    @ApiModelProperty(value = "投入周期时长")
    private String periodic;

    @ApiModelProperty(value = "投入结束时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime putInEndTime;

    @ApiModelProperty(value = "领取开始时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime actualGetStartTime;

    @ApiModelProperty(value = "领取周期单位")
    private String actualGetPeriodicUnit;

    @ApiModelProperty(value = "领取周期计划")
    private List<PeriodicVo> periodicVos ;

    @ApiModelProperty(value = "领取总金额")
    private BigDecimal receivedAmounts ;

    @ApiModelProperty(value = "累计收益")
    private BigDecimal accumulatedEarnings;

    @ApiModelProperty(value = "累计倍数")
    private BigDecimal multiple;

    @Builder
    public EarningVO(LocalDateTime putInEndTime,BigDecimal multiple,BigDecimal accumulatedEarnings,BigDecimal premium, BigDecimal premiums, Integer periods, String periodicUnit, String periodic, LocalDateTime actualGetStartTime, String actualGetPeriodicUnit, List<PeriodicVo> periodicVos, BigDecimal receivedAmounts) {
        this.premium = premium;
        this.premiums = premiums;
        this.periods = periods;
        this.periodicUnit = periodicUnit;
        this.periodic = periodic;
        this.putInEndTime = putInEndTime;
        this.actualGetStartTime = actualGetStartTime;
        this.actualGetPeriodicUnit = actualGetPeriodicUnit;
        this.periodicVos = periodicVos;
        this.receivedAmounts = receivedAmounts;
        this.accumulatedEarnings=accumulatedEarnings;
        this.multiple = multiple;
    }
}
