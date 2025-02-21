package com.itheima.bolee.framework.commons.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 返回结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "状态信息")
    private String msg;

    @ApiModelProperty(value = "返回结果")
    private T data;

    @ApiModelProperty(value = "操作人Id")
    private Long operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人性别")
    private String operatorSex;

    @ApiModelProperty(value = "VO类")
    private String _class;

    @ApiModelProperty(value = "数据说明")
    private String tip;

    @ApiModelProperty(value = "操作时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//set
    private LocalDateTime operationTime;

}
