package com.itheima.bolee.framework.commons.dto.analysis;

import com.baomidou.mybatisplus.annotation.TableName;
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @ClassName SaleReportDTO.java
 * @Description 销售报表DTO
 */
@Data
@NoArgsConstructor
@ApiModel(value="SaleReportDTO对象", description="销售报表DTO")
public class SaleReportDTO implements Serializable {

    @Builder
    public SaleReportDTO(BigDecimal totalAmountDay, BigDecimal avgPieceAmountDay, Long persons,
                         BigDecimal avgPersonAmountDay, Long totalWarrantyDay, LocalDate reportTime) {
        this.totalAmountDay = totalAmountDay;
        this.avgPieceAmountDay = avgPieceAmountDay;
        this.avgPersonAmountDay = avgPersonAmountDay;
        this.totalWarrantyDay = totalWarrantyDay;
        this.persons = persons;
        this.reportTime = reportTime;
    }

    @ApiModelProperty(value = "日投保总额度")
    private BigDecimal totalAmountDay;

    @ApiModelProperty(value = "日投保件均额度")
    private BigDecimal avgPieceAmountDay;

    @ApiModelProperty(value = "日投保人均额度")
    private BigDecimal avgPersonAmountDay;

    @ApiModelProperty(value = "日总合同数")
    private Long totalWarrantyDay;

    @ApiModelProperty(value = "投保人数")
    private Long persons;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;

}
