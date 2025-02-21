package com.itheima.bolee.dict.feign;

import com.itheima.bolee.dict.service.IDataDictService;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName DataDictController.java
 * @Description 数字字典controller
 */
@Slf4j
@RestController
@RequestMapping("data-dict-feign")
@Api(tags = "数字字典feign")
public class DataDictFeignController {

    @Autowired
    IDataDictService dataDictService;

    /**
     * @Description 父项键查询
     * @return List<DataDictVO>
     */
    @PostMapping("parent-key/{parentKey}")
    @ApiOperation(value = "父项键查询",notes = "父项键查询")
    @ApiImplicitParam(paramType = "path",name = "parentKey",value = "字典parentKey",example = "URGE_TYPE",dataType = "String")
    List<DataDictVO> findDataDictVOByParentKey(@PathVariable("parentKey") String parentKey) {
        return dataDictService.findDataDictVOByParentKey(parentKey);
    }

    /**
     * @Description 子项键查询
     * @return DataDictVO
     */
    @PostMapping("data-key/{dataKey}")
    @ApiOperation(value = "子项键查询",notes = "子项键查询")
    @ApiImplicitParam(paramType = "path",name = "dataKey",value = "字典dataKey",example = "URGE_TYPE",dataType = "String")
    DataDictVO findDataDictVOByDataKey(@PathVariable("dataKey")String dataKey){
        return dataDictService.findDataDictVOByDataKey(dataKey);
    }

}

