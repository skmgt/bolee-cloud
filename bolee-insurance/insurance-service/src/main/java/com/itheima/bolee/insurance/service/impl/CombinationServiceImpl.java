package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.pojo.Combination;
import com.itheima.bolee.insurance.mapper.CombinationMapper;
import com.itheima.bolee.insurance.service.ICombinationInsuranceService;
import com.itheima.bolee.insurance.service.ICombinationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.insurance.service.IInsuranceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.seata.spring.annotation.GlobalTransactional;
import com.itheima.bolee.insurance.constant.CombinationCacheConstant;
import com.itheima.bolee.insurance.enums.CombinationEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：组合方案服务实现类
 */
@Slf4j
@Service
public class CombinationServiceImpl extends ServiceImpl<CombinationMapper, Combination> implements ICombinationService {

    @Autowired
    private ICombinationInsuranceService combinationInsuranceService;

    @Autowired
    private IInsuranceService insuranceService;

    @Autowired
    FileBusinessFeign fileBusinessFeign;



    /***
    * @description 组合方案多条件组合
    * @param combinationVO 组合方案
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Combination> queryWrapper(CombinationVO combinationVO){
        QueryWrapper<Combination> queryWrapper = new QueryWrapper<>();
        //组合名称查询
        if (!EmptyUtil.isNullOrEmpty(combinationVO.getCombinationName())) {
            queryWrapper.lambda().eq(Combination::getCombinationName,combinationVO.getCombinationName());
        }
        //风险分析查询
        if (!EmptyUtil.isNullOrEmpty(combinationVO.getRiskAnalysis())) {
            queryWrapper.lambda().eq(Combination::getRiskAnalysis,combinationVO.getRiskAnalysis());
        }
        //主要分析场景json查询
        if (!EmptyUtil.isNullOrEmpty(combinationVO.getRiskScenario())) {
            queryWrapper.lambda().eq(Combination::getRiskScenario,combinationVO.getRiskScenario());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(combinationVO.getDataState())) {
            queryWrapper.lambda().eq(Combination::getDataState,combinationVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Combination::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CombinationCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#combinationVO.hashCode()")
    public Page<CombinationVO> findPage(CombinationVO combinationVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Combination> CombinationPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Combination> queryWrapper = queryWrapper(combinationVO);
            //执行分页查询
            Page<CombinationVO> combinationVOPage = BeanConv.toPage(
                page(CombinationPage, queryWrapper), CombinationVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(combinationVOPage)&&
                !EmptyUtil.isNullOrEmpty(combinationVOPage.getRecords())){
                List<CombinationVO> records = combinationVOPage.getRecords();
                buildResult(records);
                combinationVOPage.setRecords(records);
            }
            //返回结果
            return combinationVOPage;
        }catch (Exception e){
            log.error("组合方案分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.PAGE_FAIL);
        }
    }

    /**
     * 构建结果集对象
     * @param records
     * @return
     */
    private void buildResult(List<CombinationVO> records) {
        try {
            //获得所有业务主键ID
            List<Long> combinationIds = records.stream().
                    map(CombinationVO::getId).
                    collect(Collectors.toList());
            //===================================找到对应的保险组合==================================

            //=====================================封装附件信息=====================================
            //调用fileBusinessFeign附件信息
            List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(combinationIds));
            //构建附件处理List对象
            List<FileVO> fileVOsHandler = Lists.newArrayList();
            records.forEach(combinationVOHandler->{
                fileVOs.forEach(fileVO -> {
                    if (combinationVOHandler.getId().equals(fileVO.getBusinessId())) {
                        fileVOsHandler.add(fileVO);
                    }
                });
                //补全附件信息
                combinationVOHandler.setFileVOs(fileVOsHandler);
            });
            //=====================================================================================
        }catch (Exception e){

        }
    }

    @Override
    @Cacheable(value = CombinationCacheConstant.BASIC,key ="#combinationId")
    public CombinationVO findById(String combinationId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(combinationId),CombinationVO.class);
        }catch (Exception e){
            log.error("组合方案单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = CombinationCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =CombinationCacheConstant.BASIC,key = "#result.id")})
    public CombinationVO save(CombinationVO combinationVO) {
        try {
            //转换CombinationVO为Combination
            Combination combination = BeanConv.toBean(combinationVO, Combination.class);
            boolean flag = save(combination);
            if (!flag){
                throw new RuntimeException("保存组合方案失败");
            }
            //保存附件信息
            if (EmptyUtil.isNullOrEmpty(combinationVO.getFileVOs())){
                throw new RuntimeException("合同附件为空");
            }
            //构建附件对象
            combinationVO.getFileVOs().forEach(fileVO -> {
                fileVO.setBusinessId(combination.getId());
            });
            //调用附件接口
            List<FileVO> fileVOs = fileBusinessFeign.bindBatchFile(Lists.newArrayList(combinationVO.getFileVOs()));
            if (EmptyUtil.isNullOrEmpty(fileVOs)){
                throw new RuntimeException("合同附件绑定失败");
            }
            //转换返回对象CombinationVO
            CombinationVO combinationVOHandler = BeanConv.toBean(combination, CombinationVO.class);
            combinationVOHandler.setFileVOs(fileVOs);
            return combinationVOHandler;
        }catch (Exception e){
            log.error("保存组合方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.SAVE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = CombinationCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CombinationCacheConstant.BASIC,key = "#combinationVO.id")})
    public Boolean update(CombinationVO combinationVO) {
        try {
            //转换CombinationVO为Combination
            Combination combination = BeanConv.toBean(combinationVO, Combination.class);
            boolean flag = updateById(combination);
            if (!flag){
                throw new RuntimeException("修改组合方案失败");
            }
            //替换附件信息
            flag = fileBusinessFeign.replaceBindBatchFile(Lists.newArrayList(combinationVO.getFileVOs()));
            if (!flag){
                throw new RuntimeException("移除组合方案附件失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改组合方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.UPDATE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = CombinationCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CombinationCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CombinationCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除组合方案失败");
            }
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag){
                throw new RuntimeException("删除组合方案附件失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除组合方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = CombinationCacheConstant.LIST,key ="#combinationVO.hashCode()")
    public List<CombinationVO> findList(CombinationVO combinationVO) {
        try {
            //构建查询条件
            QueryWrapper<Combination> queryWrapper = queryWrapper(combinationVO);
            //执行列表查询
            List<CombinationVO> combinationVOs = BeanConv.toBeanList(list(queryWrapper),CombinationVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(combinationVOs)){
                //获得所有业务主键ID
                List<Long> combinationIds = combinationVOs.stream().map(CombinationVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(combinationIds));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                combinationVOs.forEach(combinationVOHandler->{
                    fileVOs.forEach(fileVO -> {
                        if (combinationVOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    combinationVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            return combinationVOs;
        }catch (Exception e){
            log.error("组合方案列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CombinationEnum.LIST_FAIL);
        }
    }

//    /**
//     * 获取保险方案详情信息
//     * @param id
//     * @return
//     */
//    @Override
//    public CombinationDetailVO findCombination(String id) {
//        try{
//            CombinationDetailVO res = new CombinationDetailVO();
//            //1.获取对应的方案信息
//            CombinationVO combination = findById(id);
//            if(combination.getDataState() != SuperConstant.DATA_STATE_0){
//                log.error("组合方案查询异常：当前方案被禁用");
//                throw new ProjectException(CombinationEnum.FIND_ONE_FAIL);
//            }
//            //2.获取包含的保险列表
//            List<CombinationInsuranceVO> combinationList = combinationInsuranceService.findList(CombinationInsuranceVO.builder().combinationId(combination.getId()).build());
//            List<InsuranceInfoVO> inusranceInfoList = insuranceService.getInusranceInfoByIds(combinationList.stream().map(CombinationInsuranceVO::getInsuranceId).collect(Collectors.toList()));
//            //开始封装结果集
//            res.setInsuranceList(inusranceInfoList.stream().map(InsuranceInfoVO::getInsuranceVO).collect(Collectors.toList()));
//            Map<String,List<PlanSafeguardVO>> safes = new HashMap<>();
//            Map<String,InsurancePlanVO> plans = new HashMap<>();
//            inusranceInfoList.stream().forEach(e->{
//                InsuranceVO insuranceVO = e.getInsuranceVO();
//                safes.put(String.valueOf(insuranceVO.getId()),e.getSafeguardVOs());
//                plans.put(String.valueOf(insuranceVO.getId()),e.getInsurancePlanVO());
//            });
//            //封装对应的最低保障项信息
//            res.setSafes(safes);
//            //封装保险计划
//            res.setPlans(plans);
//            //计算总金额
//            BigDecimal totalPrice = BigDecimal.ZERO;
//            for (InsurancePlanVO index : plans.values()) {
//                BigDecimal monthPrice = insuranceService.getMonthPrice(index);
//                totalPrice = totalPrice.add(monthPrice);
//            }
//            res.setTotalMoney(totalPrice.toPlainString());
//            return res;
//        }catch (Exception e){
//            log.error("组合方案查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
//            throw new ProjectException(CombinationEnum.FIND_ONE_FAIL);
//        }
//    }
}
