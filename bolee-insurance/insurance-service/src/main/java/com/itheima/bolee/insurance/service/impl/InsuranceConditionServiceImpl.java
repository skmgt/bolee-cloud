package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.constant.InsuranceCacheConstant;
import com.itheima.bolee.insurance.constant.InsuranceConditionCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceConditionVO;
import com.itheima.bolee.insurance.enums.InsuranceConditionEnum;
import com.itheima.bolee.insurance.mapper.InsuranceConditionMapper;
import com.itheima.bolee.insurance.pojo.InsuranceCondition;
import com.itheima.bolee.insurance.pojo.InsurancePlan;
import com.itheima.bolee.insurance.service.IInsuranceConditionService;
import com.itheima.bolee.insurance.service.IInsuranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：保险筛选项服务实现类
 */
@Slf4j
@Service
public class InsuranceConditionServiceImpl extends ServiceImpl<InsuranceConditionMapper, InsuranceCondition> implements IInsuranceConditionService {

    @Autowired
    private IInsuranceService insuranceService;

    /***
    * @description 保险筛选项多条件组合
    * @param insuranceConditionVO 保险筛选项
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceCondition> queryWrapper(InsuranceConditionVO insuranceConditionVO){
        QueryWrapper<InsuranceCondition> queryWrapper = new QueryWrapper<>();
        //保险产品ID查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsuranceCondition::getInsuranceId,insuranceConditionVO.getInsuranceId());
        }
        //保险商品ids列表查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getInsuranceIds())) {
            queryWrapper.lambda().in(InsuranceCondition::getInsuranceId,insuranceConditionVO.getInsuranceIds());
        }
        //条件key查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getConditionKey())) {
            queryWrapper.lambda().eq(InsuranceCondition::getConditionKey,insuranceConditionVO.getConditionKey());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getSortNo())) {
            queryWrapper.lambda().eq(InsuranceCondition::getSortNo,insuranceConditionVO.getSortNo());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getRemake())) {
            queryWrapper.lambda().eq(InsuranceCondition::getRemake,insuranceConditionVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceConditionVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceCondition::getDataState,insuranceConditionVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceCondition::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceConditionCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceConditionVO.hashCode()")
    public Page<InsuranceConditionVO> findPage(InsuranceConditionVO insuranceConditionVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceCondition> InsuranceConditionPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceCondition> queryWrapper = queryWrapper(insuranceConditionVO);
            //执行分页查询
            Page<InsuranceConditionVO> insuranceConditionVOPage = BeanConv.toPage(
                page(InsuranceConditionPage, queryWrapper), InsuranceConditionVO.class);
            //返回结果
            return insuranceConditionVOPage;
        }catch (Exception e){
            log.error("保险筛选项分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceConditionCacheConstant.BASIC,key ="#insuranceConditionId")
    public InsuranceConditionVO findById(String insuranceConditionId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceConditionId),InsuranceConditionVO.class);
        }catch (Exception e){
            log.error("保险筛选项单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceConditionCacheConstant.LIST,allEntries = true)})
    public Boolean save(List<InsuranceConditionVO> insuranceConditionListVOs) {
        try {
            //转换InsuranceConditionVO为InsuranceCondition
            List<InsuranceCondition> insuranceConditions = BeanConv.toBeanList(insuranceConditionListVOs, InsuranceCondition.class);
            boolean flag = saveBatch(insuranceConditions);
            if (!flag){
                throw new RuntimeException("保存保险筛选项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("保存保险筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceConditionCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.PAGE,allEntries = true)})
    public Boolean update(List<InsuranceConditionVO> insuranceConditionVOs) {
        try {
            //删除保险下所有的筛选项
            deleteByInsuranceId(insuranceConditionVOs.get(0).getInsuranceId());
            //转换InsuranceConditionVO为InsuranceCondition
            List<InsuranceCondition> insuranceConditions = BeanConv.toBeanList(insuranceConditionVOs, InsuranceCondition.class);
            boolean flag = saveBatch(insuranceConditions);
            if (!flag){
                throw new RuntimeException("修改保险筛选项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改保险筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.UPDATE_FAIL);
        }
    }

    /**
     * 根据保险id删除保险筛选项中所有的筛选项
     * @param insuranceId
     */
    private Boolean deleteByInsuranceId(String insuranceId) {
        QueryWrapper<InsuranceCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InsuranceCondition::getInsuranceId,insuranceId);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceConditionCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceConditionCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceConditionCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险筛选项失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险筛选项异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceConditionCacheConstant.LIST,key ="#insuranceConditionVO.hashCode()")
    public List<InsuranceConditionVO> findList(InsuranceConditionVO insuranceConditionVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceCondition> queryWrapper = queryWrapper(insuranceConditionVO);
            //执行列表查询
            List<InsuranceConditionVO> insuranceConditionVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceConditionVO.class);
            return insuranceConditionVOs;
        }catch (Exception e){
            log.error("保险筛选项列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceConditionEnum.LIST_FAIL);
        }
    }

}
