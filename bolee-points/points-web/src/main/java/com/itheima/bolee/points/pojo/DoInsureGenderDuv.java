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
 * @Description：性别日投保用户访问数
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_do_insure_gender_duv")
@ApiModel(value="DoInsureGenderDuv对象", description="性别日投保用户访问数")
public class DoInsureGenderDuv extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DoInsureGenderDuv(Long id, String dataState, Long manDpv, Long womanDpv, LocalDate reportTime){
        super(id, dataState);
        this.manDpv=manDpv;
        this.womanDpv=womanDpv;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "男：日投保访页面问量")
    private Long manDpv;

    @ApiModelProperty(value = "女：日投保访页面问量")
    private Long womanDpv;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
