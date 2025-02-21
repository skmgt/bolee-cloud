package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.constant.SearchRecordCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceTypeInfoVO;
import com.itheima.bolee.insurance.dto.InsuranceVO;
import com.itheima.bolee.insurance.dto.SearchRecordVO;
import com.itheima.bolee.insurance.enums.SearchRecordEnum;
import com.itheima.bolee.insurance.mapper.SearchRecordMapper;
import com.itheima.bolee.insurance.pojo.SearchRecord;
import com.itheima.bolee.insurance.service.IInsuranceService;
import com.itheima.bolee.insurance.service.ISearchRecordService;
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
 * @Description：搜索记录服务实现类
 */
@Slf4j
@Service
public class SearchRecordServiceImpl extends ServiceImpl<SearchRecordMapper, SearchRecord> implements ISearchRecordService {


    @Autowired
    private IInsuranceService insuranceService;

    /***
     * @description 搜索记录多条件组合
     * @param searchRecordVO 搜索记录
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<SearchRecord> queryWrapper(SearchRecordVO searchRecordVO) {
        QueryWrapper<SearchRecord> queryWrapper = new QueryWrapper<>();
        //搜索内容查询
        if (!EmptyUtil.isNullOrEmpty(searchRecordVO.getContent())) {
            queryWrapper.lambda().eq(SearchRecord::getContent, searchRecordVO.getContent());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(searchRecordVO.getDataState())) {
            queryWrapper.lambda().eq(SearchRecord::getDataState, searchRecordVO.getDataState());
        }
        if (!EmptyUtil.isNullOrEmpty(searchRecordVO.getCreateBy())) {
            queryWrapper.lambda().eq(SearchRecord::getCreateBy, searchRecordVO.getCreateBy());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SearchRecord::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SearchRecordCacheConstant.PAGE, key = "#pageNum+'-'+#pageSize+'-'+#searchRecordVO.hashCode()")
    public Page<SearchRecordVO> findPage(SearchRecordVO searchRecordVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<SearchRecord> SearchRecordPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<SearchRecord> queryWrapper = queryWrapper(searchRecordVO);
            //执行分页查询
            Page<SearchRecordVO> searchRecordVOPage = BeanConv.toPage(
                    page(SearchRecordPage, queryWrapper), SearchRecordVO.class);
            //返回结果
            return searchRecordVOPage;
        } catch (Exception e) {
            log.error("搜索记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SearchRecordCacheConstant.BASIC, key = "#searchRecordId")
    public SearchRecordVO findById(String searchRecordId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(searchRecordId), SearchRecordVO.class);
        } catch (Exception e) {
            log.error("搜索记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SearchRecordCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SearchRecordCacheConstant.LIST, allEntries = true)},
            put = {@CachePut(value = SearchRecordCacheConstant.BASIC, key = "#result.id")})
    public SearchRecordVO save(SearchRecordVO searchRecordVO) {
        try {
            //转换SearchRecordVO为SearchRecord
            SearchRecord searchRecord = BeanConv.toBean(searchRecordVO, SearchRecord.class);
            boolean flag = save(searchRecord);
            if (!flag) {
                throw new RuntimeException("保存搜索记录失败");
            }
            //转换返回对象SearchRecordVO
            SearchRecordVO searchRecordVOHandler = BeanConv.toBean(searchRecord, SearchRecordVO.class);
            return searchRecordVOHandler;
        } catch (Exception e) {
            log.error("保存搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SearchRecordCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SearchRecordCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = SearchRecordCacheConstant.BASIC, key = "#searchRecordVO.id")})
    public Boolean update(SearchRecordVO searchRecordVO) {
        try {
            //转换SearchRecordVO为SearchRecord
            SearchRecord searchRecord = BeanConv.toBean(searchRecordVO, SearchRecord.class);
            boolean flag = updateById(searchRecord);
            if (!flag) {
                throw new RuntimeException("修改搜索记录失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SearchRecordCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = SearchRecordCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = SearchRecordCacheConstant.BASIC, allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag) {
                throw new RuntimeException("删除搜索记录失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除搜索记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = SearchRecordCacheConstant.LIST, key = "#searchRecordVO.hashCode()")
    public List<SearchRecordVO> findList(SearchRecordVO searchRecordVO) {
        try {
            //构建查询条件
            QueryWrapper<SearchRecord> queryWrapper = queryWrapper(searchRecordVO);
            //执行列表查询
            List<SearchRecordVO> searchRecordVOs = BeanConv.toBeanList(list(queryWrapper), SearchRecordVO.class);
            return searchRecordVOs;
        } catch (Exception e) {
            log.error("搜索记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.LIST_FAIL);
        }
    }


    /**
     * 查询排名前10的记录
     *
     * @param limit
     * @return
     */
    @Override
    public List<SearchRecordVO> tindTopRecord(int limit) {
        try {
            QueryWrapper<SearchRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("IFNULL(COUNT(*) ,0) as id,content");
            queryWrapper.lambda().
                    eq(SearchRecord::getDataState, SuperConstant.DATA_STATE_0).
                    groupBy(SearchRecord::getContent).
                    orderByDesc(SearchRecord::getId).
                    last("limit " + limit);
            List<SearchRecordVO> searchRecordVOs = BeanConv.toBeanList(list(queryWrapper), SearchRecordVO.class);
            return searchRecordVOs;
        } catch (Exception e) {
            log.error("搜索记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.LIST_FAIL);
        }
    }

    /**
     * ‘
     * 清空历史记录
     *
     * @return
     */
    @Override
    public Boolean cleanSearchRecord() {
        try {
            LambdaQueryWrapper<SearchRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SearchRecord::getCreateBy, SubjectContent.getUserVO().getId());
            boolean remove = this.remove(queryWrapper);
            return remove;
        } catch (Exception e) {
            log.error("搜索记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.LIST_FAIL);
        }
    }

    /**
     * 搜索我的搜索历史保险
     *
     * @param userVO
     * @return
     */
    @Override
    public List<InsuranceVO> findMyHistory(UserVO userVO) {
        try {
            SearchRecordVO searchVO = SearchRecordVO.builder().
                    createBy(SubjectContent.getUserVO().getId()).
                    dataState(SuperConstant.DATA_STATE_0).
                    build();
            List<SearchRecordVO> records = findPage(searchVO, 0, 6).getRecords();
            List<String> contentList = records.stream().map(SearchRecordVO::getContent).collect(Collectors.toList());
            //获取所有的保险id
            List<String> insuranceIds = new ArrayList<>();
            for (String indexContent : contentList) {
                JsonObject jsonObject = JsonParser.parseString(indexContent).getAsJsonObject();
                // 提取insuranceId的内容
                insuranceIds.add(jsonObject.get("insuranceId").getAsString());
            }
            //查询保险id内容
            InsuranceVO insuranceVO = InsuranceVO.builder().dataState(SuperConstant.DATA_STATE_0).checkedIds(insuranceIds.toArray(String[]::new)).build();
            return insuranceService.findList(insuranceVO);
        } catch (Exception e) {
            log.error("搜索记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SearchRecordEnum.LIST_FAIL);
        }
    }
}
