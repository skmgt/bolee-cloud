package com.itheima.easy.face;

import com.itheima.bolee.framework.commons.dto.trade.${entity}Vo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：${table.comment!}Face服务类
 */
<#if kotlin>
interface ${entity}Face
<#else>
public interface ${entity}Face {

    /**
     * @Description 多条件查询${table.comment!}分页列表
     * @param ${entity?uncap_first}Vo 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<${entity}>
     */
    Page<${entity}Vo> find${entity}VoPage(${entity}Vo ${entity?uncap_first}Vo, int pageNum, int pageSize);

    /**
     * @Description 创建${table.comment!}
     * @param ${entity?uncap_first}Vo 对象信息
     * @return ${entity}
     */
    ${entity}Vo create${entity}Vo(${entity}Vo ${entity?uncap_first}Vo);

    /**
     * @Description 修改${table.comment!}
     * @param ${entity?uncap_first}Vo 对象信息
     * @return Boolean
     */
    Boolean update${entity}Vo(${entity}Vo ${entity?uncap_first}Vo);

    /**
     * @Description 删除${table.comment!}
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete${entity}Vo(String[] checkedIds);

    /**
     * @description 多条件查询${table.comment!}列表
     * @param ${entity?uncap_first}Vo 查询条件
     * @return: List<${entity}>
     */
    List<${entity}Vo> find${entity}VoList(${entity}Vo ${entity?uncap_first}Vo);
}
</#if>
