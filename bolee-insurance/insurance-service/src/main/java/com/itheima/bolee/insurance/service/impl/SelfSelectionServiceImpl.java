package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.constant.SelfSelectionCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceTypeInfoVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.dto.SelfSelectionVO;
import com.itheima.bolee.insurance.enums.SelfSelectionEnum;
import com.itheima.bolee.insurance.mapper.SelfSelectionMapper;
import com.itheima.bolee.insurance.pojo.SelfSelection;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.ISelfSelectionService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description：自选保险服务实现类
 */
@Slf4j
@Service
public class SelfSelectionServiceImpl extends ServiceImpl<SelfSelectionMapper, SelfSelection> implements ISelfSelectionService {

    @Autowired
    private IInsuranceService insuranceService;

    /***
     * @description 自选保险多条件组合
     * @param selfSelectionVO 自选保险
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<SelfSelection> queryWrapper(SelfSelectionVO selfSelectionVO) {
        QueryWrapper<SelfSelection> queryWrapper = new QueryWrapper<>();
        //保险ID查询
        if (!EmptyUtil.isNullOrEmpty(selfSelectionVO.getInsuranceId())) {
            queryWrapper.lambda().eq(SelfSelection::getInsuranceId, selfSelectionVO.getInsuranceId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(selfSelectionVO.getSortNo())) {
            queryWrapper.lambda().eq(SelfSelection::getSortNo, selfSelectionVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(selfSelectionVO.getDataState())) {
            queryWrapper.lambda().eq(SelfSelection::getDataState, selfSelectionVO.getDataState());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(selfSelectionVO.getCreateBy())) {
            queryWrapper.lambda().eq(SelfSelection::getCreateBy, selfSelectionVO.getCreateBy());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SelfSelection::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SelfSelectionCacheConstant.PAGE, key = "#pageNum+'-'+#pageSize+'-'+#selfSelectionVO.hashCode()")
    public Page<SelfSelectionVO> findPage(SelfSelectionVO selfSelectionVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<SelfSelection> SelfSelectionPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<SelfSelection> queryWrapper = queryWrapper(selfSelectionVO);
            //执行分页查询
            Page<SelfSelectionVO> selfSelectionVOPage = BeanConv.toPage(
                    page(SelfSelectionPage, queryWrapper), SelfSelectionVO.class);
            //返回结果
            return selfSelectionVOPage;
        } catch (Exception e) {
            log.error("自选保险分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SelfSelectionCacheConstant.BASIC, key = "#selfSelectionId")
    public SelfSelectionVO findById(String selfSelectionId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(selfSelectionId), SelfSelectionVO.class);
        } catch (Exception e) {
            log.error("自选保险单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SelfSelectionCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SelfSelectionCacheConstant.LIST, allEntries = true)},
            put = {@CachePut(value = SelfSelectionCacheConstant.BASIC, key = "#result.id")})
    public SelfSelectionVO save(SelfSelectionVO selfSelectionVO) {
        try {
            //首先查询当前登录人是否已经添加了当前保险，不能重复添加
            QueryWrapper<SelfSelection> selfSelectionQueryWrapper = queryWrapper(selfSelectionVO);
            int count = count(selfSelectionQueryWrapper);
            if(count>0){
                log.error("保存自选保险异常：重复添加重复的自选保险，用户:{},用户id:{},添加重复的保险为：{}", SubjectContent.getUserVO().getUsername(),SubjectContent.getUserVO().getId(),selfSelectionVO.getInsuranceId());
                throw new ProjectException(SelfSelectionEnum.EXIST_FAIL);
            }
            //转换SelfSelectionVO为SelfSelection
            SelfSelection selfSelection = BeanConv.toBean(selfSelectionVO, SelfSelection.class);
            boolean flag = save(selfSelection);
            if (!flag) {
                throw new RuntimeException("保存自选保险失败");
            }
            //转换返回对象SelfSelectionVO
            SelfSelectionVO selfSelectionVOHandler = BeanConv.toBean(selfSelection, SelfSelectionVO.class);
            return selfSelectionVOHandler;
        } catch (Exception e) {
            log.error("保存自选保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.SAVE_FAIL);
        }
    }



    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SelfSelectionCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SelfSelectionCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = SelfSelectionCacheConstant.BASIC, key = "#selfSelectionVO.id")})
    public Boolean update(SelfSelectionVO selfSelectionVO) {
        try {
            //转换SelfSelectionVO为SelfSelection
            SelfSelection selfSelection = BeanConv.toBean(selfSelectionVO, SelfSelection.class);
            boolean flag = updateById(selfSelection);
            if (!flag) {
                throw new RuntimeException("修改自选保险失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改自选保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SelfSelectionCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SelfSelectionCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = SelfSelectionCacheConstant.BASIC, allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag) {
                throw new RuntimeException("删除自选保险失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除自选保险异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = SelfSelectionCacheConstant.LIST, key = "#selfSelectionVO.hashCode()")
    public List<SelfSelectionVO> findList(SelfSelectionVO selfSelectionVO) {
        try {
            //构建查询条件
            QueryWrapper<SelfSelection> queryWrapper = queryWrapper(selfSelectionVO);
            //执行列表查询
            List<SelfSelectionVO> selfSelectionVOs = BeanConv.toBeanList(list(queryWrapper), SelfSelectionVO.class);
            return selfSelectionVOs;
        } catch (Exception e) {
            log.error("自选保险列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.LIST_FAIL);
        }
    }


    /**
     * 查询我的自选列表方法
     * 自选列表：已经添加的自选保险【添加过的保险将不出现在为你推荐的热门保险】-->tab_self_selection-->tab_insurance-->tab_insurance_plan
     */
    @Override
    @Cacheable(value = SelfSelectionCacheConstant.LIST, key = "#userVO.getId()")
    public List<InsuranceTypeInfoVO> findMySelection(UserVO userVO) {
        try {
            SelfSelectionVO selfSelect = SelfSelectionVO.builder().dataState(SuperConstant.DATA_STATE_0).build();
            selfSelect.setCreateBy(userVO.getId());
            //查询当前登录人的所有保险
            List<SelfSelectionVO> selfSelectList = findList(selfSelect);
            if (!EmptyUtil.isNullOrEmpty(selfSelectList)){
                //获取所有的保险ID集合
                List<Long> insuranceIds = selfSelectList.stream().map(SelfSelectionVO::getInsuranceId).collect(Collectors.toList());
                String[] stringInsuranceIds = insuranceIds.stream().map(Object::toString).toArray(String[]::new);
                InsuranceVO insuranceVO = InsuranceVO.builder().checkedIds(stringInsuranceIds).build();
                List<InsuranceVO> insuranceList = insuranceService.findList(insuranceVO);
                //按照checkRule进行分组
                Map<String, List<InsuranceVO>> groupedByCheckRule = insuranceList.stream()
                    .collect(Collectors.groupingBy(InsuranceVO::getCheckRule));
                //构建结果集返回内容
                return groupedByCheckRule.entrySet().stream()
                    .map(entry -> new InsuranceTypeInfoVO(InsureConstant.getRuleNameById(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("自选保险列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SelfSelectionEnum.LIST_FAIL);
        }
    }
}
