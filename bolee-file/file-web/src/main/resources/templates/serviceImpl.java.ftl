package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import com.itheima.bolee.framework.commons.dto.trade.${entity}Vo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @Description：${table.comment!}服务实现类
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public Page<${entity}> find${entity}Page(${entity}Vo ${entity?uncap_first}Vo, int pageNum, int pageSize) {
        //构建分页对象
        Page<${entity}> page = new Page<>(pageNum,pageSize);
        //构建查询条件
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        //构建多条件查询，代码生成后自己可自行调整
        <#list table.fields as field>
        //${field.comment}查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.get${field.propertyName?cap_first}())) {
            queryWrapper.lambda().eq(${entity}::get${field.propertyName?cap_first},${entity?uncap_first}Vo.get${field.propertyName?cap_first}());
        }
        </#list>
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.getEnableFlag())) {
            queryWrapper.lambda().eq(${entity}::getEnableFlag,${entity?uncap_first}Vo.getEnableFlag());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(${entity}::getCreatedTime);
        //执行分页查询
        return page(page, queryWrapper);
    }

    @Override
    public ${entity} create${entity}(${entity}Vo ${entity?uncap_first}Vo) {
        //转换${entity}Vo为${entity}
        ${entity} ${entity?uncap_first} = BeanConv.toBean(${entity?uncap_first}Vo, ${entity}.class);
        boolean flag = save(${entity?uncap_first});
        if (flag){
        return ${entity?uncap_first};
        }
        return null;
    }

    @Override
    public Boolean update${entity}(${entity}Vo ${entity?uncap_first}Vo) {
        //转换${entity}Vo为${entity}
        ${entity} ${entity?uncap_first} = BeanConv.toBean(${entity?uncap_first}Vo, ${entity}.class);
        return updateById(${entity?uncap_first});
    }

    @Override
    public Boolean delete${entity}(String[] checkedIds) {
        //转换数组为集合
        List<String> ids = Arrays.asList(checkedIds);
        List<Long> idsLong = new ArrayList<>();
        ids.forEach(n->{
            idsLong.add(Long.valueOf(n));
        });
        return removeByIds(idsLong);
    }

    @Override
    public List<${entity}> find${entity}List(${entity}Vo ${entity?uncap_first}Vo) {
        //构建查询条件
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.getId())) {
            queryWrapper.lambda().eq(${entity}::getId,${entity?uncap_first}Vo.getId());
        }
        <#list table.fields as field>
        //${field.comment}查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.get${field.propertyName?cap_first}())) {
            queryWrapper.lambda().eq(${entity}::get${field.propertyName?cap_first},${entity?uncap_first}Vo.get${field.propertyName?cap_first}());
        }
        </#list>
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(${entity?uncap_first}Vo.getEnableFlag())) {
            queryWrapper.lambda().eq(${entity}::getEnableFlag,${entity?uncap_first}Vo.getEnableFlag());
        }
        return list(queryWrapper);
    }
}
</#if>
