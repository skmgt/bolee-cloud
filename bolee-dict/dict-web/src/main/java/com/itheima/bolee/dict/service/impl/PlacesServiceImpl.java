package com.itheima.bolee.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.dict.mapper.PlacesMapper;
import com.itheima.bolee.dict.pojo.Places;
import com.itheima.bolee.dict.service.IPlacesService;
import com.itheima.bolee.framework.commons.constant.dict.PlacesCacheConstant;
import com.itheima.bolee.framework.commons.dto.dict.PlacesVO;
import com.itheima.bolee.framework.commons.enums.dict.PlacesEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description：地方表 服务实现类
 */
@Slf4j
@Service
public class PlacesServiceImpl extends ServiceImpl<PlacesMapper, Places> implements IPlacesService {

    @Override
    @Cacheable(value = PlacesCacheConstant.LIST,key = "#parentId")
    public List<PlacesVO> findPlacesVOListByParentId(Long parentId) {
        try {
            QueryWrapper<Places> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Places::getParentId,parentId);
            return BeanConv.toBeanList(list(queryWrapper),PlacesVO.class);
        }catch (Exception e){
            log.error("查询parentId下列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(PlacesEnum.LIST_FAIL);
        }
    }

}
