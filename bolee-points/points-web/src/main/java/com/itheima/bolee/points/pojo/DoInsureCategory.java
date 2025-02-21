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
 * @Description：日投保分类明细
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("tab_do_insure_category")
@ApiModel(value="DoInsureCategory对象", description="日投保分类明细")
public class DoInsureCategory extends BasePojo {

    private static final long serialVersionUID = 1L;

    @Builder
    public DoInsureCategory(Long id, String dataState, String categoryName, Long doInsureNums, LocalDate reportTime){
        super(id, dataState);
        this.categoryName=categoryName;
        this.doInsureNums=doInsureNums;
        this.reportTime=reportTime;
    }

    @ApiModelProperty(value = "保险分类名称")
    private String categoryName;

    @ApiModelProperty(value = "投保次数")
    private Long doInsureNums;

    @ApiModelProperty(value = "统计时间")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportTime;


}
