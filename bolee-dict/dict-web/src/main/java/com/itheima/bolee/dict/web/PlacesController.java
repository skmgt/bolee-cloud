package com.itheima.bolee.dict.web;

import com.itheima.bolee.dict.service.IPlacesService;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName PlacesFeignController.java
 * @Description 地区controller
 */
@RestController
@RequestMapping("places")
@Api(tags = "地区")
public class PlacesController {

    @Autowired
    IPlacesService placesService;

    /**
     * @Description 保存字典数据
     * @return
     */
    @PostMapping("{parentId}")
    @ApiOperation(value = "地区下拉框",notes = "地区下拉框")
    @ApiImplicitParam(name = "parentId",value = "父层id",required = true,dataType = "Long")
    public ResponseResult<List<PlacesVO>> findPlacesVOListByParentId(@PathVariable("parentId") Long parentId) {
        List<PlacesVO> placesVOList = placesService.findPlacesVOListByParentId(parentId);
        return ResponseResultBuild.successBuild(placesVOList);
    }
}
