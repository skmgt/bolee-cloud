package com.itheima.easy.vo;

import com.itheima.easy.basic.BasicVo;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
</#if>

import com.fasterxml.jackson.annotation.JsonFormat;
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
</#if>
public class ${entity}Vo extends BasicVo {

    @Builder
    public ${entity}Vo(Long id,<#list table.fields as field>${field.propertyType} ${field.propertyName}<#if field_has_next>,</#if></#list>){
        super(id);
        <#list table.fields as field>
        this.${field.propertyName}=${field.propertyName};
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

    @ApiModelProperty(value = "选中节点")
    private String[] checkedIds;

    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    <#if swagger2>
    @ApiModelProperty(value = "文件Vo对象")
    </#if>
    private List<FileVO> fileVOs;
    </#if>
    </#list>
}
