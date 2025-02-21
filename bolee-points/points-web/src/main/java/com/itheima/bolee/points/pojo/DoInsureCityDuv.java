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
 * @Description：城市日投保用户访问数
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_do_insure_city_duv")
@ApiModel(value="DoInsureCityDuv对象", description="城市日投保用户访问数")
public class DoInsureCityDuv extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DoInsureCityDuv(Long id,String dataState,String cityName,Long doInsureCityDuv,LocalDate reportTime){
        super(id, dataState);
        this.cityName=cityName;
        this.doInsureCityDuv=doInsureCityDuv;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "城市日投保唯一访问量")
    private Long doInsureCityDuv;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
