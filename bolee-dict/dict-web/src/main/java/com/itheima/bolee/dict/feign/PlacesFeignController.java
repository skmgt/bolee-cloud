package com.itheima.bolee.dict.feign;

import com.itheima.bolee.dict.service.IPlacesService;
import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName PlacesFeignController.java
 * @Description 地区controller
 */
@RestController
@RequestMapping("places-fgign")
@Api(tags = "地区fegin")
public class PlacesFeignController {

    @Autowired
    IPlacesService placesService;

    /**
     * @Description 保存字典数据
     * @return
     */
    @PostMapping("{parentId}")
    @ApiOperation(value = "地区下拉框",notes = "地区下拉框")
    @ApiImplicitParam(name = "parentId",value = "父层id",required = true,dataType = "Long")
    public List<PlacesVO> findPlacesVOListByParentId(@PathVariable("parentId") Long parentId) {
        return placesService.findPlacesVOListByParentId(parentId);
    }
}
