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
 * @Description：日访问页面量
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_dpv")
@ApiModel(value="Dpv对象", description="日访问页面量")
public class Dpv extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Dpv(Long id,String dataState,Long dpv,LocalDate reportTime){
        super(id, dataState);
        this.dpv=dpv;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "日访问页面量")
    private Long dpv;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
