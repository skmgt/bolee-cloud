package com.itheima.bolee.points.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.itheima.bolee.framework.mybatisplus.basic.BasePojo;
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
 * @Description：投保转换率
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_do_insure_conversion_dpv")
@ApiModel(value="DoInsureConversionDpv对象", description="投保转换率")
public class DoInsureConversionDpv extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DoInsureConversionDpv(Long id, String dataState, Long doBrowseDpv, Long doTrialDpv, Long doInsureDpv, LocalDate reportTime){
        super(id, dataState);
        this.doBrowseDpv=doBrowseDpv;
        this.doTrialDpv=doTrialDpv;
        this.doInsureDpv=doInsureDpv;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "投保浏览次数")
    private Long doBrowseDpv;

    @ApiModelProperty(value = "投保试算次数")
    private Long doTrialDpv;

    @ApiModelProperty(value = "投保提交次数")
    private Long doInsureDpv;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
