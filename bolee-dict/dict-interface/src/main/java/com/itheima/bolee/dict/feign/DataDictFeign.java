package com.itheima.bolee.dict.feign;

import com.itheima.bolee.dict.hystrix.DataDictHystrix;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DataDictFace.java
 * @Description：数字字典feign接口类
 */
@FeignClient(value = "dict-web",fallback = DataDictHystrix.class)
public interface DataDictFeign {

    /**
     * @Description 根据parentKey获取value
     * @return
     */
    @PostMapping("data-dict-feign/parent-key/{parentKey}")
    List<DataDictVO> findDataDictVOByParentKey(@PathVariable("parentKey") String parentKey);

    /**
     * @Description 根据dataKey获取DataDictVO
     * @return
     */
    @PostMapping("data-dict-feign/data-key/{dataKey}")
    DataDictVO findDataDictVOByDataKey(@PathVariable("dataKey") String dataKey) ;

    /**
     * @Description 根据dataKey获取DataDictVO集合
     * @return
     */
    @PostMapping("data-dict-feign/findValueByDataKeys")
    List<DataDictVO> findValueByDataKeys(@RequestBody ArrayList<String> dataKeys);

}
