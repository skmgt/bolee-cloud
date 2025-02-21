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
 * @Description：日保险分类访问页面量
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_category_dpv")
@ApiModel(value="CategoryDpv对象", description="日保险分类访问页面量")
public class CategoryDpv extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public CategoryDpv(Long id, String dataState, Long categoryDpv, String categoryName, LocalDate reportTime){
        super(id, dataState);
        this.categoryDpv=categoryDpv;
        this.categoryName=categoryName;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "日保险分类访问页面量")
    private Long categoryDpv;

    @ApiModelProperty(value = "保险分类名称")
    private String categoryName;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
