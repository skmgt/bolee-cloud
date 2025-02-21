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

/**
 * @ClassName PeriodicVo.java
 * @Description 领取周期计划
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value="PeriodicVo对象", description="领取周期计划")
public class PeriodicVo implements Serializable {

    @ApiModelProperty(value = "第几期")
    private String periodic;

    @ApiModelProperty(value = "领取金额")
    private BigDecimal receivedAmount;


    @ApiModelProperty(value = "领取时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime actualGetStartTime;

    @Builder
    public PeriodicVo(String periodic, BigDecimal receivedAmount, LocalDateTime actualGetStartTime) {
        this.periodic = periodic;
        this.receivedAmount = receivedAmount;
        this.actualGetStartTime = actualGetStartTime;
    }
}
