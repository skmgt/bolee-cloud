package ${package.Controller};

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.insurance.enums.${entity}Enum;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.insurance.dto.${entity}VO;
import com.itheima.bolee.insurance.service.I${entity}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * @Description：${table.comment!}响应接口
 */
@Slf4j
@Api(tags = "${table.comment!}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    I${entity}Service ${entity?uncap_first}Service;

    /***
     * @description 多条件查询${table.comment!}分页
     * @param ${entity?uncap_first}VO ${table.comment!}VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<${entity}VO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "${table.comment!}分页",notes = "${table.comment!}分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "${entity?uncap_first}VO",value = "${table.comment!}VO对象",required = true,dataType = "${entity}VO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {<#list table.fields as field>"${entity?uncap_first}VO.${field.propertyName}"<#if field_has_next>,</#if></#list>})
    public ResponseResult<Page<${entity}VO>> find${entity}VOPage(
                                    @RequestBody ${entity}VO ${entity?uncap_first}VO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<${entity}VO> ${entity?uncap_first}VOPage = ${entity?uncap_first}Service.findPage(${entity?uncap_first}VO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(${entity?uncap_first}VOPage);
    }

    /**
     * @Description 保存${table.comment!}
     * @param ${entity?uncap_first}VO ${table.comment!}VO对象
     * @return ${entity}VO
     */
    @PutMapping
    @ApiOperation(value = "保存${entity}",notes = "添加${entity}")
    @ApiImplicitParam(name = "${entity?uncap_first}VO",value = "${table.comment!}VO对象",required = true,dataType = "${entity}VO")
    @ApiOperationSupport(includeParameters = {"${entity?uncap_first}VO.dataState",<#list table.fields as field>"${entity?uncap_first}VO.${field.propertyName}"<#if field_has_next>,</#if></#list>})
    public ResponseResult<${entity}VO> create${entity}(@RequestBody ${entity}VO ${entity?uncap_first}VO) {
        ${entity}VO ${entity?uncap_first}VOResult = ${entity?uncap_first}Service.save(${entity?uncap_first}VO);
        return ResponseResultBuild.successBuild(${entity?uncap_first}VOResult);
    }

    /**
     * @Description 修改${table.comment!}
     * @param ${entity?uncap_first}VO ${table.comment!}VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改${table.comment!}",notes = "修改${table.comment!}")
    @ApiImplicitParam(name = "${entity?uncap_first}VO",value = "${table.comment!}VO对象",required = true,dataType = "${entity}VO")
    @ApiOperationSupport(includeParameters = {"${entity?uncap_first}VO.id","${entity?uncap_first}VO.dataState",<#list table.fields as field>"${entity?uncap_first}VO.${field.propertyName}"<#if field_has_next>,</#if></#list>})
    public ResponseResult<Boolean> update${entity}(@RequestBody ${entity}VO ${entity?uncap_first}VO) {
        Boolean flag = ${entity?uncap_first}Service.update(${entity?uncap_first}VO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除${table.comment!}
     * @param ${entity?uncap_first}VO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除${table.comment!}",notes = "删除${table.comment!}")
    @ApiImplicitParam(name = "${entity?uncap_first}VO",value = "${table.comment!}VO对象",required = true,dataType = "${entity}VO")
    @ApiOperationSupport(includeParameters = {"${entity?uncap_first}VO.checkedIds"})
    public ResponseResult<Boolean> delete${entity}(@RequestBody ${entity}VO ${entity?uncap_first}VO) {
        Boolean flag = ${entity?uncap_first}Service.delete(${entity?uncap_first}VO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询${table.comment!}列表
     * @param ${entity?uncap_first}VO ${table.comment!}VO对象
     * @return List<${entity}VO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询${table.comment!}列表",notes = "多条件查询${table.comment!}列表")
    @ApiImplicitParam(name = "${entity?uncap_first}VO",value = "${table.comment!}VO对象",required = true,dataType = "${entity}VO")
    @ApiOperationSupport(includeParameters = {<#list table.fields as field>"${entity?uncap_first}VO.${field.propertyName}"<#if field_has_next>,</#if></#list>})
    public ResponseResult<List<${entity}VO>> ${entity?uncap_first}List(@RequestBody ${entity}VO ${entity?uncap_first}VO) {
        List<${entity}VO> ${entity?uncap_first}VOList = ${entity?uncap_first}Service.findList(${entity?uncap_first}VO);
        return ResponseResultBuild.successBuild(${entity?uncap_first}VOList);
    }

}
</#if>
