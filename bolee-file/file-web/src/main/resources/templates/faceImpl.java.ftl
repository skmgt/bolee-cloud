package com.itheima.easy.face.impl;

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import com.itheima.bolee.framework.commons.dto.trade.${entity}Vo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.easy.face.${entity}Face;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.constant.trade.${entity}CacheConstant;
import com.itheima.bolee.framework.commons.enums.trade.${entity}Enum;
import org.springframework.transaction.annotation.Transactional;
<#list cfg.tablesFile as tableFiile>
<#if tableFiile==table.name>
import java.util.List;
import java.util.ArrayList;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.easy.feign.FileFeign;
</#if>
</#list>
import java.util.List;
/**
 * @Description：${table.comment!}Face服务实现类
 */
<#if kotlin>
open class ${entity}FaceImpl : ${entity}Face {

}
<#else>
@Slf4j
@Component
public class ${entity}FaceImpl implements ${entity}Face {

    @Autowired
    I${entity}Service ${entity?uncap_first}Service;

    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    @Autowired
    FileFeign fileFeign;
    </#if>
    </#list>

    @Override
    @Cacheable(value = ${entity}CacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#${entity?uncap_first}Vo.hashCode()")
    public Page<${entity}Vo> find${entity}VoPage(${entity}Vo ${entity?uncap_first}Vo, int pageNum, int pageSize) {
        try {
            Page<${entity}> page = ${entity?uncap_first}Service.find${entity}Page(${entity?uncap_first}Vo, pageNum, pageSize);
            Page<${entity}Vo> pageVo =BeanConv.toPage(page,${entity}Vo.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //查询记录结果
            List<${entity}Vo> records = pageVo.getRecords();
            //处理图片
            if (!EmptyUtil.isNullOrEmpty(records)){
                    records.forEach(n->{
                        ResponseResult<List<FileVO>> responseWrap = fileFeign
                            .findFileVOByBusinessId(FileVO.builder().businessId(n.getId()).build());
                    if (!EmptyUtil.isNullOrEmpty(responseWrap.getDatas())){
                        n.setFileVOs(responseWrap.getDatas());
                    }else {
                        n.setFileVOs(new ArrayList<>());
                    }
                });
            }
            </#if>
            </#list>
            return pageVo;
        }catch (Exception e){
            log.error("${table.comment!}列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.PAGE_FAIL);
        }
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true)},
    put={@CachePut(value =${entity}CacheConstant.BASIC,key = "#result.id")})
    @Override
    public ${entity}Vo create${entity}Vo(${entity}Vo ${entity?uncap_first}Vo) {
        try {
            ${entity}Vo ${entity?uncap_first}VoResult = BeanConv.toBean(${entity?uncap_first}Service.create${entity}(${entity?uncap_first}Vo), ${entity}Vo.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //绑定附件
            if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}VoResult)&&!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.getFileVOs())){
                ${entity?uncap_first}Vo.getFileVOs().forEach(n->{n.setBusinessId(${entity?uncap_first}VoResult.getId());});
                //绑定图片
                ResponseResult<List<FileVO>> listResponseResult = fileFeign.bindBatchBusinessId(${entity?uncap_first}Vo.getFileVOs());
                ${entity?uncap_first}VoResult.setFileVOs(listResponseResult.getDatas());
            }
            </#if>
            </#list>
            return ${entity?uncap_first}VoResult;
        } catch (Exception e) {
            log.error("保存${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.SAVE_FAIL);
        }
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true),
        @CacheEvict(value =${entity}CacheConstant.BASIC,key = "#${entity?uncap_first}Vo.id")})
    @Override
    public Boolean update${entity}Vo(${entity}Vo ${entity?uncap_first}Vo) {
        try {
            Boolean flag = ${entity?uncap_first}Service.update${entity}(${entity?uncap_first}Vo);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            if (flag){
                //移除业务原图片，并绑定新的图片到业务上
                fileFeign.replaceBindBatchBusinessId(${entity?uncap_first}Vo.getFileVOs());
            }
            </#if>
            </#list>
            return flag;
        } catch (Exception e) {
            log.error("修改${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.UPDATE_FAIL);
        }
    }

    @Transactional
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true)})
    @Override
    public Boolean delete${entity}Vo(String[] checkedIds) {
        try {
            Boolean flag = ${entity?uncap_first}Service.delete${entity}(checkedIds);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            if (flag){
                //删除图片
                for (String checkedId : checkedIds) {
                    fileFeign.deleteFileVOByBusinessId(FileVO.builder()
                        .businessId(Long.valueOf(checkedId))
                        .build());
                }
            }
            </#if>
            </#list>
            return flag;
        } catch (Exception e) {
            log.error("删除${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = ${entity}CacheConstant.LIST,key ="#${entity?uncap_first}Vo.hashCode()")
    public List<${entity}Vo> find${entity}VoList(${entity}Vo ${entity?uncap_first}Vo) {
        try {
            List<${entity}Vo> records = BeanConv.toBeanList(${entity?uncap_first}Service.find${entity}List(${entity?uncap_first}Vo),${entity}Vo.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //处理图片
            if (!EmptyUtil.isNullOrEmpty(records)){
                records.forEach(n->{
                    ResponseResult<List<FileVO>> responseWrap = fileFeign
                    .findFileVOByBusinessId(FileVO.builder().businessId(n.getId()).build());
                    if (!EmptyUtil.isNullOrEmpty(responseWrap.getDatas())){
                        n.setFileVOs(responseWrap.getDatas());
                    }else {
                        n.setFileVOs(new ArrayList<>());
                    }
                });
            }
            </#if>
            </#list>
            return records;
        } catch (Exception e) {
            log.error("删除${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.LIST_FAIL);
        }
    }
}
</#if>
