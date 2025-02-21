package com.itheima.bolee.dict.hystrix;

import com.itheima.bolee.dict.feign.PlacesFeign;
import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName PlacesHystrix.java
 * @Description PlacesFeign的Hystrix
 */
@Component
public class PlacesHystrix implements PlacesFeign {

    @Override
    public List<PlacesVO> findPlacesVOListByParentId(Long parentId) {
        return null;
    }
}
