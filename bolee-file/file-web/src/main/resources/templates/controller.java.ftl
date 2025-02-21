package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.enums.trade.${entity}Enum;
import com.itheima.easy.face.${entity}Face;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.framework.commons.dto.trade.${entity}Vo;
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
 * @Description：${table.comment!}前端控制器
 */
@Slf4j
@Api(tags = "${table.comment!}controller")
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
    ${entity}Face ${entity?uncap_first}Face;

    /***
     * @description 多条件查询${table.comment!}分页列表
     * @param ${entity?uncap_first}Vo ${table.comment!}Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<${entity}Vo>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "${table.comment!}分页",notes = "${table.comment!}分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "${entity?uncap_first}Vo",value = "${table.comment!}Vo对象",required = true,dataType = "${entity}Vo"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<${entity}Vo>> find${entity}VoPage(
                                    @RequestBody ${entity}Vo ${entity?uncap_first}Vo,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<${entity}Vo> ${entity?uncap_first}VoPage = ${entity?uncap_first}Face.find${entity}VoPage(${entity?uncap_first}Vo, pageNum, pageSize);
        return ResponseResultBuild.build(${entity}Enum.SUCCEED,${entity?uncap_first}VoPage);
    }

    /**
     * @Description 保存${table.comment!}
     * @param ${entity?uncap_first}Vo ${table.comment!}Vo对象
     * @return ${entity}Vo
     */
    @PutMapping
    @ApiOperation(value = "添加${entity}",notes = "添加${entity}")
    @ApiImplicitParam(name = "${entity?uncap_first}Vo",value = "${table.comment!}Vo对象",required = true,dataType = "${entity}Vo")
    public ResponseResult<${entity}Vo> create${entity}(@RequestBody ${entity}Vo ${entity?uncap_first}Vo) {
        ${entity}Vo ${entity?uncap_first}VoResult = ${entity?uncap_first}Face.create${entity}Vo(${entity?uncap_first}Vo);
        return ResponseResultBuild.build(${entity}Enum.SUCCEED,${entity?uncap_first}VoResult);
    }

    /**
     * @Description 修改${table.comment!}
     * @param ${entity?uncap_first}Vo ${table.comment!}Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改${table.comment!}",notes = "修改${table.comment!}")
    @ApiImplicitParam(name = "${entity?uncap_first}Vo",value = "${table.comment!}Vo对象",required = true,dataType = "${entity}Vo")
    public ResponseResult<Boolean> update${entity}(@RequestBody ${entity}Vo ${entity?uncap_first}Vo) {
        Boolean flag = ${entity?uncap_first}Face.update${entity}Vo(${entity?uncap_first}Vo);
        return ResponseResultBuild.build(${entity}Enum.SUCCEED,flag);
    }

    /**
     * @Description 删除${table.comment!}
     * @param ${entity?uncap_first}Vo 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除${table.comment!}",notes = "删除${table.comment!}")
    @ApiImplicitParam(name = "${entity?uncap_first}Vo",value = "${table.comment!}Vo对象",required = true,dataType = "${entity}Vo")
    public ResponseResult<Boolean> delete${entity}(@RequestBody ${entity}Vo ${entity?uncap_first}Vo) {
        Boolean flag = ${entity?uncap_first}Face.delete${entity}Vo(${entity?uncap_first}Vo.getCheckedIds());
        return ResponseResultBuild.build(${entity}Enum.SUCCEED,flag);
    }

    /***
     * @description 多条件查询${table.comment!}列表
     * @param ${entity?uncap_first}Vo ${table.comment!}Vo对象
     * @return List<${entity}Vo>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询${table.comment!}列表",notes = "多条件查询${table.comment!}列表")
    @ApiImplicitParam(name = "${entity?uncap_first}Vo",value = "${table.comment!}Vo对象",required = true,dataType = "${entity}Vo")
    public ResponseResult<List<${entity}Vo>> ${entity?uncap_first}List(@RequestBody ${entity}Vo ${entity?uncap_first}Vo) {
        List<${entity}Vo> ${entity?uncap_first}VoList = ${entity?uncap_first}Face.find${entity}VoList(${entity?uncap_first}Vo);
        return ResponseResultBuild.build(${entity}Enum.SUCCEED,${entity?uncap_first}VoList);
    }

}
</#if>
