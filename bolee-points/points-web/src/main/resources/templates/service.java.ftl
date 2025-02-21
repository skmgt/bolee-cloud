package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.itheima.bolee.insurance.dto.${entity}VO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：${table.comment!}服务类
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * @Description 多条件查询${table.comment!}分页
     * @param ${entity?uncap_first}VO ${table.comment!}查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<${entity}VO>
     */
    Page<${entity}VO> findPage(${entity}VO ${entity?uncap_first}VO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询${table.comment!}
    * @param ${entity?uncap_first}Id 合同信息ID
    * @return ${entity}VO
    */
    ${entity}VO findById(String ${entity?uncap_first}Id);

    /**
     * @Description ${table.comment!}新增
     * @param ${entity?uncap_first}VO ${table.comment!}查询条件
     * @return ${entity}VO
     */
    ${entity}VO save(${entity}VO ${entity?uncap_first}VO);

    /**
     * @Description ${table.comment!}修改
     * @param ${entity?uncap_first}VO ${table.comment!}对象
     * @return ${entity}VO
     */
    Boolean update(${entity}VO ${entity?uncap_first}VO);

    /**
     * @Description ${table.comment!}删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询${table.comment!}列表
     * @param ${entity?uncap_first}VO 查询条件
     * @return: List<${entity}VO>
     */
    List<${entity}VO> findList(${entity}VO ${entity?uncap_first}VO);
}
</#if>
