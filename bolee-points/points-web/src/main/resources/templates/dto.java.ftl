package com.itheima.bolee.points.dto;

import com.itheima.bolee.framework.commons.dto.basic.BaseVO;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
</#if>

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
<#list cfg.tablesFile as tableFiile>
<#if tableFiile==table.name>
import java.util.List;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
</#if>
</#list>
/**
 * @Description：${table.comment!}
 */
<#if entityLombokModel>
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
</#if>
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
public class ${entity}VO extends BaseVO {

    @Builder
    public ${entity}VO(Long id,String dataState,<#list cfg.tablesFile as tableFiile><#if tableFiile==table.name>List<FileVO> fileVOs,</#if></#list><#list table.fields as field>${field.propertyType} ${field.propertyName}<#if field_has_next>,</#if></#list>){
        super(id, dataState);
        <#list table.fields as field>
        this.${field.propertyName}=${field.propertyName};
        </#list>
        <#list cfg.tablesFile as tableFiile><#if tableFiile==table.name>
        this.fileVOs=fileVOs;
        </#if>
        </#list>
    }
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.propertyType=='Long'>
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    </#if>
    <#if field.propertyType=='Date'>
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    <#if swagger2>
    @ApiModelProperty(value = "文件VO对象")
    </#if>
    private List<FileVO> fileVOs;
    </#if>
    </#list>

    @ApiModelProperty(value = "批量操作：主键ID")
    private String[] checkedIds;
}
