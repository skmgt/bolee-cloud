package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
<#list cfg.tablesFile as tableFiile>
<#if tableFiile==table.name>
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.seata.spring.annotation.GlobalTransactional;
</#if>
</#list>
import com.itheima.bolee.insurance.constant.${entity}CacheConstant;
import com.itheima.bolee.insurance.dto.${entity}VO;
import com.itheima.bolee.insurance.enums.${entity}Enum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：${table.comment!}服务实现类
 */
@Slf4j
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    @Autowired
    FileBusinessFeign fileBusinessFeign;
    </#if>
    </#list>

    /***
    * @description ${table.comment!}多条件组合
    * @param ${entity?uncap_first}VO ${table.comment!}
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<${entity}> queryWrapper(${entity}VO ${entity?uncap_first}VO){
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        <#list table.fields as field>
        //${field.comment}查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}VO.get${field.propertyName?cap_first}())) {
            queryWrapper.lambda().eq(${entity}::get${field.propertyName?cap_first},${entity?uncap_first}VO.get${field.propertyName?cap_first}());
        }
        </#list>
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}VO.getDataState())) {
            queryWrapper.lambda().eq(${entity}::getDataState,${entity?uncap_first}VO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(${entity}::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = ${entity}CacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#${entity?uncap_first}VO.hashCode()")
    public Page<${entity}VO> findPage(${entity}VO ${entity?uncap_first}VO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<${entity}> ${entity}Page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<${entity}> queryWrapper = queryWrapper(${entity?uncap_first}VO);
            //执行分页查询
            Page<${entity}VO> ${entity?uncap_first}VOPage = BeanConv.toPage(
                page(${entity}Page, queryWrapper), ${entity}VO.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}VOPage)&&
                !EmptyUtil.isNullOrEmpty(${entity?uncap_first}VOPage.getRecords())){
                //获得所有业务主键ID
                List<Long> ${entity?uncap_first}Ids = ${entity?uncap_first}VOPage.getRecords()
                    .stream().map(${entity}VO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(${entity?uncap_first}Ids));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                ${entity?uncap_first}VOPage.getRecords().forEach(${entity?uncap_first}VOHandler->{
                    fileVOs.forEach(fileVO -> {
                        if (${entity?uncap_first}VOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    ${entity?uncap_first}VOHandler.setFileVOs(fileVOsHandler);
                });
            }
            </#if>
            </#list>
            //返回结果
            return ${entity?uncap_first}VOPage;
        }catch (Exception e){
            log.error("${table.comment!}分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = ${entity}CacheConstant.BASIC,key ="#${entity?uncap_first}Id")
    public ${entity}VO findById(String ${entity?uncap_first}Id) {
        try {
            //执行查询
            return BeanConv.toBean(getById(${entity?uncap_first}Id),${entity}VO.class);
        }catch (Exception e){
            log.error("${table.comment!}单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.FIND_ONE_FAIL);
        }
    }

    @Override
    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    @GlobalTransactional
    <#break>
    </#if>
    <#if !tableFiile_has_next>
    @Transactional
    </#if>
    </#list>
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =${entity}CacheConstant.BASIC,key = "#result.id")})
    public ${entity}VO save(${entity}VO ${entity?uncap_first}VO) {
        try {
            //转换${entity}VO为${entity}
            ${entity} ${entity?uncap_first} = BeanConv.toBean(${entity?uncap_first}VO, ${entity}.class);
            boolean flag = save(${entity?uncap_first});
            if (!flag){
                throw new RuntimeException("保存${table.comment!}失败");
            }
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //保存附件信息
            if (EmptyUtil.isNullOrEmpty(${entity?uncap_first}VO.getFileVOs())){
                throw new RuntimeException("合同附件为空");
            }
            //构建附件对象
            ${entity?uncap_first}VO.getFileVOs().forEach(fileVO -> {
                fileVO.setBusinessId(${entity?uncap_first}.getId());
            });
            //调用附件接口
            List<FileVO> fileVOs = fileBusinessFeign.bindBatchFile(Lists.newArrayList(${entity?uncap_first}VO.getFileVOs()));
            if (EmptyUtil.isNullOrEmpty(fileVOs)){
                throw new RuntimeException("合同附件绑定失败");
            }
            </#if>
            </#list>
            //转换返回对象${entity}VO
            ${entity}VO ${entity?uncap_first}VOHandler = BeanConv.toBean(${entity?uncap_first}, ${entity}VO.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            ${entity?uncap_first}VOHandler.setFileVOs(fileVOs);
            </#if>
            </#list>
            return ${entity?uncap_first}VOHandler;
        }catch (Exception e){
            log.error("保存${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.SAVE_FAIL);
        }
    }

    @Override
    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    @GlobalTransactional
    <#break>
    </#if>
    <#if !tableFiile_has_next>
    @Transactional
    </#if>
    </#list>
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.BASIC,key = "#${entity?uncap_first}VO.id")})
    public Boolean update(${entity}VO ${entity?uncap_first}VO) {
        try {
            //转换${entity}VO为${entity}
            ${entity} ${entity?uncap_first} = BeanConv.toBean(${entity?uncap_first}VO, ${entity}.class);
            boolean flag = updateById(${entity?uncap_first});
            if (!flag){
                throw new RuntimeException("修改${table.comment!}失败");
            }
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //替换附件信息
            flag = fileBusinessFeign.replaceBindBatchFile(Lists.newArrayList(${entity?uncap_first}VO.getFileVOs()));
            if (!flag){
                throw new RuntimeException("移除${table.comment!}附件失败");
            }
            </#if>
            </#list>
            return flag;
        }catch (Exception e){
            log.error("修改${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.UPDATE_FAIL);
        }
    }

    @Override
    <#list cfg.tablesFile as tableFiile>
    <#if tableFiile==table.name>
    @GlobalTransactional
    <#break>
    </#if>
    <#if !tableFiile_has_next>
    @Transactional
    </#if>
    </#list>
    @Caching(evict = {@CacheEvict(value = ${entity}CacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.LIST,allEntries = true),
        @CacheEvict(value = ${entity}CacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除${table.comment!}失败");
            }
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag){
                throw new RuntimeException("删除${table.comment!}附件失败");
            }
            </#if>
            </#list>
            return flag;
        }catch (Exception e){
            log.error("删除${table.comment!}异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = ${entity}CacheConstant.LIST,key ="#${entity?uncap_first}VO.hashCode()")
    public List<${entity}VO> findList(${entity}VO ${entity?uncap_first}VO) {
        try {
            //构建查询条件
            QueryWrapper<${entity}> queryWrapper = queryWrapper(${entity?uncap_first}VO);
            //执行列表查询
            List<${entity}VO> ${entity?uncap_first}VOs = BeanConv.toBeanList(list(queryWrapper),${entity}VO.class);
            <#list cfg.tablesFile as tableFiile>
            <#if tableFiile==table.name>
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}VOs)){
                //获得所有业务主键ID
                List<Long> ${entity?uncap_first}Ids = ${entity?uncap_first}VOs.stream().map(${entity}VO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(${entity?uncap_first}Ids));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                ${entity?uncap_first}VOs.forEach(${entity?uncap_first}VOHandler->{
                    fileVOs.forEach(fileVO -> {
                        if (${entity?uncap_first}VOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    ${entity?uncap_first}VOHandler.setFileVOs(fileVOsHandler);
                });
            }
            </#if>
            </#list>
            return ${entity?uncap_first}VOs;
        }catch (Exception e){
            log.error("${table.comment!}列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(${entity}Enum.LIST_FAIL);
        }
    }
}
</#if>
