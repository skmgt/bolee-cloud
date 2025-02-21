package com.itheima.bolee.insurance.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description：搜索记录
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SearchRecord对象", description="搜索记录")
public class SearchRecordVO extends BaseVO {

    @Builder
    public SearchRecordVO(Long id,String dataState,String content,Long createBy){
        super(id, dataState);
        this.content=content;
        setCreateBy(createBy);
    }

    @ApiModelProperty(value = "搜索内容")
    private String content;


    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
