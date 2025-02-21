package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.constant.InsuranceTopCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceTopVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.enums.InsuranceTopEnum;
import com.itheima.bolee.insurance.mapper.InsuranceTopMapper;
import com.itheima.bolee.insurance.pojo.InsuranceTop;
import com.itheima.bolee.insurance.service.IInsurancePlanService;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.IInsuranceTopService;
import com.itheima.bolee.insurance.service.IWarrantyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：人气保险服务实现类
 */
@Slf4j
@Service
public class InsuranceTopServiceImpl extends ServiceImpl<InsuranceTopMapper, InsuranceTop> implements IInsuranceTopService {

    @Autowired
    private InsuranceTopMapper insuranceTopMapper;

    @Autowired
    private IInsuranceService insuranceService;

    @Autowired
    private IInsurancePlanService insurancePlanService;


    @Autowired
    private IWarrantyService iWarrantyService;

    /***
     * @description 人气保险多条件组合
     * @param insuranceTopVO 人气保险
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<InsuranceTop> queryWrapper(InsuranceTopVO insuranceTopVO) {
        QueryWrapper<InsuranceTop> queryWrapper = new QueryWrapper<>();
        //保险ID查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsuranceTop::getInsuranceId, insuranceTopVO.getInsuranceId());
        }
        //保险名称查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getInsuranceName())) {
            queryWrapper.lambda().eq(InsuranceTop::getInsuranceName, insuranceTopVO.getInsuranceName());
        }
        //分类编号查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getCategoryNo())) {
            queryWrapper.lambda().eq(InsuranceTop::getCategoryNo, insuranceTopVO.getCategoryNo());
        }
        //保障类型： 1推荐分类  2产品分类 查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getCategoryType())) {
            queryWrapper.lambda().eq(InsuranceTop::getCategoryType, insuranceTopVO.getCategoryType());
        }
        //分类名称查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getCategoryName())) {
            queryWrapper.lambda().eq(InsuranceTop::getCategoryName, insuranceTopVO.getCategoryName());
        }
        //方案ID查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getPalnId())) {
            queryWrapper.lambda().eq(InsuranceTop::getPalnId, insuranceTopVO.getPalnId());
        }
        //默认定价查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getPrice())) {
            queryWrapper.lambda().eq(InsuranceTop::getPrice, insuranceTopVO.getPrice());
        }
        //默认定价单位：y/d,y/m,y/y查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getPriceUnit())) {
            queryWrapper.lambda().eq(InsuranceTop::getPriceUnit, insuranceTopVO.getPriceUnit());
        }
        //销量查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getSalesVolume())) {
            queryWrapper.lambda().eq(InsuranceTop::getSalesVolume, insuranceTopVO.getSalesVolume());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceTopVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceTop::getDataState, insuranceTopVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceTop::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceTopCacheConstant.PAGE, key = "#pageNum+'-'+#pageSize+'-'+#insuranceTopVO.hashCode()")
    public Page<InsuranceTopVO> findPage(InsuranceTopVO insuranceTopVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceTop> InsuranceTopPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<InsuranceTop> queryWrapper = queryWrapper(insuranceTopVO);
            //执行分页查询
            Page<InsuranceTopVO> insuranceTopVOPage = BeanConv.toPage(
                    page(InsuranceTopPage, queryWrapper), InsuranceTopVO.class);
            //返回结果
            return insuranceTopVOPage;
        } catch (Exception e) {
            log.error("人气保险分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceTopCacheConstant.BASIC, key = "#insuranceTopId")
    public InsuranceTopVO findById(String insuranceTopId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceTopId), InsuranceTopVO.class);
        } catch (Exception e) {
            log.error("人气保险单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceTopCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = InsuranceTopCacheConstant.LIST, allEntries = true)},
            put = {@CachePut(value = InsuranceTopCacheConstant.BASIC, key = "#result.id")})
    public InsuranceTopVO save(InsuranceTopVO insuranceTopVO) {
        try {
            //转换InsuranceTopVO为InsuranceTop
            InsuranceTop insuranceTop = BeanConv.toBean(insuranceTopVO, InsuranceTop.class);
            boolean flag = save(insuranceTop);
            if (!flag) {
                throw new RuntimeException("保存人气保险失败");
            }
            //转换返回对象InsuranceTopVO
            InsuranceTopVO insuranceTopVOHandler = BeanConv.toBean(insuranceTop, InsuranceTopVO.class);
            return insuranceTopVOHandler;
        } catch (Exception e) {
            log.error("保存人气保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceTopCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = InsuranceTopCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = InsuranceTopCacheConstant.BASIC, key = "#insuranceTopVO.id")})
    public Boolean update(InsuranceTopVO insuranceTopVO) {
        try {
            //转换InsuranceTopVO为InsuranceTop
            InsuranceTop insuranceTop = BeanConv.toBean(insuranceTopVO, InsuranceTop.class);
            boolean flag = updateById(insuranceTop);
            if (!flag) {
                throw new RuntimeException("修改人气保险失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改人气保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceTopCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = InsuranceTopCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = InsuranceTopCacheConstant.BASIC, allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag) {
                throw new RuntimeException("删除人气保险失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除人气保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceTopCacheConstant.LIST, key = "#insuranceTopVO.hashCode()")
    public List<InsuranceTopVO> findList(InsuranceTopVO insuranceTopVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceTop> queryWrapper = queryWrapper(insuranceTopVO);
            //执行列表查询
            List<InsuranceTopVO> insuranceTopVOs = BeanConv.toBeanList(list(queryWrapper), InsuranceTopVO.class);
            return insuranceTopVOs;
        } catch (Exception e) {
            log.error("人气保险列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.LIST_FAIL);
        }
    }


    /**
     * 查询保险销售top榜内容
     *
     * @param num       展示条数
     * @param dateLimit top榜时间范围（默认一周）
     * @return
     */
    @Override
    public List<InsuranceVO> findTopInsurance(Integer num, Integer dateLimit) {
        try {
            //查询top榜中dateLimit天内有效的num条保险产品数据，并按照销量进行排序
            List<InsuranceTop> pojoList = insuranceTopMapper.findTopInsurance(SuperConstant.DATA_STATE_0, num, dateLimit);
            if(CollectionUtil.isNotEmpty(pojoList)){
                List<Long> insuranceIds = pojoList.stream().map(InsuranceTop::getInsuranceId).collect(Collectors.toList());
                InsuranceVO insuranceVO = InsuranceVO.builder().checkedIds(insuranceIds.toArray(String[]::new)).build();
                List<InsuranceVO> res = insuranceService.findList(insuranceVO);
                return res;
            }
            return Arrays.asList();
        } catch (Exception e) {
            log.error("保险销售top榜查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceTopEnum.LIST_FAIL);
        }
    }
}
