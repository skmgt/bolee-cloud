package com.itheima.bolee.dict.feign;

import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @ClassName PlacesFeign.java
 * @Description 地区feign服务
 */
@FeignClient(value = "dict-web")
public interface PlacesFeign {

    /***
     * @description 查询下级
     * @param parentId 父层Id
     * @return
     */
    @PostMapping("places-fegin/{parentId}")
    public List<PlacesVO> findPlacesVOListByParentId(@PathVariable("parentId") Long parentId);
}
