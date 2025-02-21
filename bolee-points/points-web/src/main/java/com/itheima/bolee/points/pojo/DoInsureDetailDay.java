package com.itheima.bolee.points.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description：日投保明细
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_do_insure_detail_day")
@ApiModel(value="DoInsureDetailDay对象", description="日投保明细")
public class DoInsureDetailDay extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DoInsureDetailDay(Long persons,Long id, String dataState, BigDecimal totalAmountDay, BigDecimal avgPieceAmountDay, BigDecimal avgPersonAmountDay, Long totalWarrantyDay, LocalDate reportTime) {
        super(id, dataState);
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
