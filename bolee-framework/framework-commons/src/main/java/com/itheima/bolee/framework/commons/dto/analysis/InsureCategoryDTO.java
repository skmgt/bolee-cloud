package com.itheima.bolee.framework.commons.dto.analysis;

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

import java.time.LocalDate;
import java.util.Date;

/**
 * @ClassName InsureCategoryDTO.java
 * @Description 投保分类报表DTO
 */
@Data
@NoArgsConstructor
@ApiModel(value="InsureCategoryDTO对象", description="投保分类报表DTO")
public class InsureCategoryDTO {

    @Builder
    public InsureCategoryDTO(String categoryName, Long doInsureNums, LocalDate reportTime) {
        this.categoryName = categoryName;
        this.doInsureNums = doInsureNums;
        this.reportTime = reportTime;
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
