package com.itheima.bolee.points.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
 * @Description：用户日活跃数
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_dau")
@ApiModel(value="Dau对象", description="用户日活跃数")
public class Dau extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public Dau(Long id,String dataState,Long allDau,Long newDau,Long oldDau,LocalDate reportTime){
        super(id, dataState);
        this.allDau=allDau;
        this.newDau=newDau;
        this.oldDau=oldDau;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "总用户活跃数")
    private Long allDau;

    @ApiModelProperty(value = "老用户活跃数")
    private Long newDau;

    @ApiModelProperty(value = "新用户活跃数")
    private Long oldDau;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
