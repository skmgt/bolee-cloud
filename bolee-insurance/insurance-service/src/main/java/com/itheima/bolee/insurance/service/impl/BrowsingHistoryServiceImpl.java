package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.insurance.pojo.BrowsingHistory;
import com.itheima.bolee.insurance.mapper.BrowsingHistoryMapper;
import com.itheima.bolee.insurance.service.IBrowsingHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.BrowsingHistoryCacheConstant;
import com.itheima.bolee.insurance.dto.BrowsingHistoryVO;
import com.itheima.bolee.insurance.enums.BrowsingHistoryEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @Description：浏览记录服务实现类
 */
@Slf4j
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory> implements IBrowsingHistoryService {


    /***
    * @description 浏览记录多条件组合
    * @param browsingHistoryVO 浏览记录
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<BrowsingHistory> queryWrapper(BrowsingHistoryVO browsingHistoryVO){
        QueryWrapper<BrowsingHistory> queryWrapper = new QueryWrapper<>();
        //保险ID查询
        if (!EmptyUtil.isNullOrEmpty(browsingHistoryVO.getInsuranceId())) {
            queryWrapper.lambda().eq(BrowsingHistory::getInsuranceId,browsingHistoryVO.getInsuranceId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(browsingHistoryVO.getSortNo())) {
            queryWrapper.lambda().eq(BrowsingHistory::getSortNo,browsingHistoryVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(browsingHistoryVO.getDataState())) {
            queryWrapper.lambda().eq(BrowsingHistory::getDataState,browsingHistoryVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(BrowsingHistory::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = BrowsingHistoryCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#browsingHistoryVO.hashCode()")
    public Page<BrowsingHistoryVO> findPage(BrowsingHistoryVO browsingHistoryVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<BrowsingHistory> BrowsingHistoryPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<BrowsingHistory> queryWrapper = queryWrapper(browsingHistoryVO);
            //执行分页查询
            Page<BrowsingHistoryVO> browsingHistoryVOPage = BeanConv.toPage(
                page(BrowsingHistoryPage, queryWrapper), BrowsingHistoryVO.class);
            //返回结果
            return browsingHistoryVOPage;
        }catch (Exception e){
            log.error("浏览记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = BrowsingHistoryCacheConstant.BASIC,key ="#browsingHistoryId")
    public BrowsingHistoryVO findById(String browsingHistoryId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(browsingHistoryId),BrowsingHistoryVO.class);
        }catch (Exception e){
            log.error("浏览记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = BrowsingHistoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BrowsingHistoryCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =BrowsingHistoryCacheConstant.BASIC,key = "#result.id")})
    public BrowsingHistoryVO save(BrowsingHistoryVO browsingHistoryVO) {
        try {
            //转换BrowsingHistoryVO为BrowsingHistory
            BrowsingHistory browsingHistory = BeanConv.toBean(browsingHistoryVO, BrowsingHistory.class);
            boolean flag = save(browsingHistory);
            if (!flag){
                throw new RuntimeException("保存浏览记录失败");
            }
            //转换返回对象BrowsingHistoryVO
            BrowsingHistoryVO browsingHistoryVOHandler = BeanConv.toBean(browsingHistory, BrowsingHistoryVO.class);
            return browsingHistoryVOHandler;
        }catch (Exception e){
            log.error("保存浏览记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = BrowsingHistoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BrowsingHistoryCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = BrowsingHistoryCacheConstant.BASIC,key = "#browsingHistoryVO.id")})
    public Boolean update(BrowsingHistoryVO browsingHistoryVO) {
        try {
            //转换BrowsingHistoryVO为BrowsingHistory
            BrowsingHistory browsingHistory = BeanConv.toBean(browsingHistoryVO, BrowsingHistory.class);
            boolean flag = updateById(browsingHistory);
            if (!flag){
                throw new RuntimeException("修改浏览记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改浏览记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = BrowsingHistoryCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BrowsingHistoryCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = BrowsingHistoryCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除浏览记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除浏览记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = BrowsingHistoryCacheConstant.LIST,key ="#browsingHistoryVO.hashCode()")
    public List<BrowsingHistoryVO> findList(BrowsingHistoryVO browsingHistoryVO) {
        try {
            //构建查询条件
            QueryWrapper<BrowsingHistory> queryWrapper = queryWrapper(browsingHistoryVO);
            //执行列表查询
            List<BrowsingHistoryVO> browsingHistoryVOs = BeanConv.toBeanList(list(queryWrapper),BrowsingHistoryVO.class);
            return browsingHistoryVOs;
        }catch (Exception e){
            log.error("浏览记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.LIST_FAIL);
        }
    }

    /**
     * 找到我近半年的历史记录
     * @return
     */
    @Cacheable(value = BrowsingHistoryCacheConstant.LIST,key ="#userVO.getId()")
    @Override
    public List<BrowsingHistoryVO> findMyHistory(UserVO userVO) {
        try {
            BrowsingHistoryVO browsingHistoryVO = BrowsingHistoryVO.builder().createBy(userVO.getId()).dataState(SuperConstant.DATA_STATE_0).build();
            QueryWrapper<BrowsingHistory> queryWrapper = queryWrapper(browsingHistoryVO);
            queryWrapper.last("limit 0, 10");
            List<BrowsingHistoryVO> browsingHistoryVOs = BeanConv.toBeanList(list(queryWrapper), BrowsingHistoryVO.class);
            return browsingHistoryVOs;
        }catch (Exception e){
            log.error("浏览记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BrowsingHistoryEnum.LIST_FAIL);
        }
    }
}
