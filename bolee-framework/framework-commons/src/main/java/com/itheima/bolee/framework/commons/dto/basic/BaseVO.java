package com.itheima.bolee.framework.commons.dto.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName BaseVo.java
 * @Description 基础请求
 */
@Data
@NoArgsConstructor
public class BaseVO implements Serializable {

    @ApiModelProperty(value = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    protected LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    protected LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者:username")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createBy;

    @ApiModelProperty(value = "更新者:username")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long updateBy;

    @ApiModelProperty(value = "是否有效")
    protected String dataState;

    @ApiModelProperty(value = "创建人名称")
    private String creator;

    public BaseVO(Long id, String dataState) {
        this.id = id;
        this.dataState = dataState;
    }
}
