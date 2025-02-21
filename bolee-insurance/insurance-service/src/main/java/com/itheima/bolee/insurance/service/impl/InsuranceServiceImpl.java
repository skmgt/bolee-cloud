package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.*;
import com.itheima.bolee.insurance.constant.InsuranceCacheConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.enums.InsuranceEnum;
import com.itheima.bolee.insurance.handler.InsureHandler;
import com.itheima.bolee.insurance.mapper.InsuranceMapper;
import com.itheima.bolee.insurance.pojo.Insurance;
import com.itheima.bolee.insurance.pojo.InsuranceCondition;
import com.itheima.bolee.insurance.pojo.SearchRecord;
import com.itheima.bolee.insurance.service.*;
import com.itheima.bolee.security.feign.CompanyFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Description：保险产品服务实现类
 */
@Slf4j
@Service
public class InsuranceServiceImpl extends ServiceImpl<InsuranceMapper, Insurance> implements IInsuranceService {

    @Autowired
    private FileBusinessFeign fileBusinessFeign;

    @Autowired
    private IInsurancePlanService insurancePlanService;

    @Autowired
    private CompanyFeign companyFeign;

    @Autowired
    private IInsuranceConditionService insuranceConditionService;

    @Autowired
    private IInsuranceCoefficentService insuranceCoefficentService;

    @Autowired
    private ICustomerRelationService customerRelationService;

    @Autowired
    private ISearchRecordService searchRecordService;

    @Resource(name = "searchRecordExecutor")
    private Executor searchRecordExecutor;

    /***
    * @description 保险产品多条件组合
    * @param insuranceVO 保险产品
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Insurance> queryWrapper(InsuranceVO insuranceVO){
        QueryWrapper<Insurance> queryWrapper = new QueryWrapper<>();
        //二级分类编号查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getCategoryNo())) {
            queryWrapper.lambda().eq(Insurance::getCategoryNo,insuranceVO.getCategoryNo());
        }
        //推荐分类查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRecommendCategoryNo())) {
            queryWrapper.lambda().eq(Insurance::getRecommendCategoryNo,insuranceVO.getRecommendCategoryNo());
        }
        //保险名称查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getInsuranceName())) {
            queryWrapper.lambda().like(Insurance::getInsuranceName,insuranceVO.getInsuranceName());
        }
        //金选：0是 1否
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getGoldSelection())) {
            queryWrapper.lambda().eq(Insurance::getGoldSelection,insuranceVO.getGoldSelection());
        }
        //安心赔：0是 1否
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getCarefree())) {
            queryWrapper.lambda().eq(Insurance::getCarefree,insuranceVO.getCarefree());
        }
        //首页热点显示（是0 否1）查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getShowIndex())) {
            queryWrapper.lambda().eq(Insurance::getShowIndex,insuranceVO.getShowIndex());
        }
        //保险标签格式：[{key:200万医疗金，val:指定私立意义}]查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getLabelsJson())) {
            queryWrapper.lambda().eq(Insurance::getLabelsJson,insuranceVO.getLabelsJson());
        }
        //补充说明格式[{key:失去保障，val:断保将失去相应保障，不能理赔}]查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRemakeJson())) {
            queryWrapper.lambda().eq(Insurance::getRemakeJson,insuranceVO.getRemakeJson());
        }
        //可购买起始点查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getTimeStart())) {
            queryWrapper.lambda().eq(Insurance::getTimeStart,insuranceVO.getTimeStart());
        }
        //可购买起始点单位：天、年查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getTimeStartUnit())) {
            queryWrapper.lambda().eq(Insurance::getTimeStartUnit,insuranceVO.getTimeStartUnit());
        }
        //可购买结束点查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getTimeEnd())) {
            queryWrapper.lambda().eq(Insurance::getTimeEnd,insuranceVO.getTimeEnd());
        }
        //可购买结束点单位：天、年查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getTimeEndUnit())) {
            queryWrapper.lambda().eq(Insurance::getTimeEndUnit,insuranceVO.getTimeEndUnit());
        }
        //可投保关系：self,children:spouse,parents查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRelation())) {
            queryWrapper.lambda().eq(Insurance::getRelation,insuranceVO.getRelation());
        }
        //投保人多选（0是 1否）查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getMultiple())) {
            queryWrapper.lambda().eq(Insurance::getMultiple,insuranceVO.getMultiple());
        }
        //连续投保年龄查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getContinuousInsuranceAge())) {
            queryWrapper.lambda().eq(Insurance::getContinuousInsuranceAge,insuranceVO.getContinuousInsuranceAge());
        }
        //校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getCheckRule())) {
            queryWrapper.lambda().eq(Insurance::getCheckRule,insuranceVO.getCheckRule());
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getCompanyNo())) {
            queryWrapper.lambda().eq(Insurance::getCompanyNo,insuranceVO.getCompanyNo());
        }
        //保险状态（上架0 下架1）查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getInsuranceState())) {
            queryWrapper.lambda().eq(Insurance::getInsuranceState,insuranceVO.getInsuranceState());
        }
        //保单宽限查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getGrace())) {
            queryWrapper.lambda().eq(Insurance::getGrace,insuranceVO.getGrace());
        }
        //宽限单位查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getGraceUnit())) {
            queryWrapper.lambda().eq(Insurance::getGraceUnit,insuranceVO.getGraceUnit());
        }
        //保单复效查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRevival())) {
            queryWrapper.lambda().eq(Insurance::getRevival,insuranceVO.getRevival());
        }
        //复效单位查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRevivalUnit())) {
            queryWrapper.lambda().eq(Insurance::getRevivalUnit,insuranceVO.getRevivalUnit());
        }
        //产品点评查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getComment())) {
            queryWrapper.lambda().eq(Insurance::getComment,insuranceVO.getComment());
        }
        //产品描述查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getRemake())) {
            queryWrapper.lambda().eq(Insurance::getRemake,insuranceVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getDataState())) {
            queryWrapper.lambda().eq(Insurance::getDataState,insuranceVO.getDataState());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceVO.getCheckedIds())) {
            queryWrapper.lambda().in(Insurance::getId,insuranceVO.getCheckedIds());
        }
        //金牌保险优先展示 按创建时间降序
        queryWrapper.lambda().orderByAsc(Insurance::getGoldSelection).orderByDesc(Insurance::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceVO.hashCode()")
    public Page<InsuranceVO> findPage(InsuranceVO insuranceVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Insurance> InsurancePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Insurance> queryWrapper = queryWrapper(insuranceVO);
            Page<Insurance> page = page(InsurancePage, queryWrapper);
            //执行分页查询
            Page<InsuranceVO> insuranceVOPage = BeanConv.toPage( page , InsuranceVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(insuranceVOPage)&&
                !EmptyUtil.isNullOrEmpty(insuranceVOPage.getRecords())){
                List<InsuranceVO> records = insuranceVOPage.getRecords();
                insuranceVOPage.setRecords(buildResult(records));
            }
            //返回结果
            return insuranceVOPage;
        }catch (Exception e){
            log.error("保险产品分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceCacheConstant.BASIC,key ="#insuranceId")
    public InsuranceVO findById(String insuranceId) {
        try {
            //查询产品基本信息
            InsuranceVO insuranceVO = BeanConv.toBean(getById(insuranceId), InsuranceVO.class);
            if(!EmptyUtil.isNullOrEmpty(insuranceVO)){
                //补全产品从属性
                insuranceVO = buildResult(Lists.newArrayList(insuranceVO)).get(0);
                //异步保存保险搜索记录
                createSearchContent(Lists.newArrayList(insuranceVO), SubjectContent.getUserVO());
            }
            return insuranceVO;
        }catch (Exception e){
            log.error("保险产品单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.FIND_ONE_FAIL);
        }
    }



    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = InsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =InsuranceCacheConstant.BASIC,key = "#result.id")})
    public InsuranceVO save(InsuranceVO insuranceVO) {
        try {
            //转换InsuranceVO为Insurance
            Insurance insurance = BeanConv.toBean(insuranceVO, Insurance.class);
            boolean flag = save(insurance);
            if (!flag){
                throw new RuntimeException("保存保险产品失败");
            }
            //转换返回对象InsuranceVO
            return BeanConv.toBean(insurance, InsuranceVO.class);
        }catch (Exception e){
            log.error("保存保险产品异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.SAVE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = InsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.BASIC,key = "#insuranceVO.id")})
    public Boolean update(InsuranceVO insuranceVO) {
        try {
            //转换InsuranceVO为Insurance
            Insurance insurance = BeanConv.toBean(insuranceVO, Insurance.class);
            return updateById(insurance);
        }catch (Exception e){
            log.error("修改保险产品异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险产品失败");
            }
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag){
                throw new RuntimeException("删除保险产品附件失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险产品异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceCacheConstant.LIST,key ="#insuranceVO.hashCode()")
    public List<InsuranceVO> findList(InsuranceVO insuranceVO) {
        try {
            //构建查询条件
            QueryWrapper<Insurance> queryWrapper = queryWrapper(insuranceVO);
            //筛选条件组合
            if (!EmptyUtil.isNullOrEmpty(insuranceVO.getConditionVals())){
                String conditionVals = com.alibaba.fastjson.JSONObject
                        .toJSONString(insuranceVO.getConditionVals())
                        .replace("[","")
                        .replace("]","");
                QueryWrapper<InsuranceCondition> queryWrapperInsuranceCondition = new QueryWrapper<>();
                queryWrapperInsuranceCondition.lambda().apply(true,
                "JSON_CONTAINS(condition_val, JSON_ARRAY("+conditionVals+"))");
                List<InsuranceCondition> insuranceConditionList = insuranceConditionService.list(queryWrapperInsuranceCondition);
                if (EmptyUtil.isNullOrEmpty(insuranceConditionList)){
                    return Lists.newArrayList();
                }
                List<String> insuranceIds = insuranceConditionList.stream()
                        .map(InsuranceCondition::getInsuranceId)
                        .collect(Collectors.toList());
                queryWrapper.lambda().in(Insurance::getId,insuranceIds);
            }
            List<Insurance> insuranceList = list(queryWrapper);
            //执行列表查询
            List<InsuranceVO> insuranceVOs = BeanConv.toBeanList(insuranceList,InsuranceVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(insuranceVOs)){
                return buildResult(insuranceVOs);
            }
            //异步添加保险搜索记录
            createSearchContent(insuranceVOs,SubjectContent.getUserVO());
            return insuranceVOs;
        }catch (Exception e){
            log.error("保险产品列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.LIST_FAIL);
        }
    }

    /**
     * 封装结果集
     * 基于查询的基础数据，补全其他业务数据的方法 (核心方法)
     * @param insuranceVOs
     * @return
     */
    private List<InsuranceVO>  buildResult(List<InsuranceVO> insuranceVOs){
        //构建产品ID
        List<Long> insuranceIds = insuranceVOs.stream().map(InsuranceVO::getId).collect(Collectors.toList());
        //=======================================封装产品系数项=======================================
        InsuranceCoefficentVO insuranceCoefficentVO = InsuranceCoefficentVO.builder().
            instanceIds(insuranceIds.toArray(Long[]::new)).
            dataState(SuperConstant.DATA_STATE_0).
            build();
        List<InsuranceCoefficentVO> insuranceCoefficentList = insuranceCoefficentService.findList(insuranceCoefficentVO);
        if(!EmptyUtil.isNullOrEmpty(insuranceCoefficentList)) {
            // 封装保险系数项到InsuranceVO的coefficents属性
            insuranceVOs.forEach(insuranceVOHandler -> {
                //过滤出同一个保险id下的数据
                List<InsuranceCoefficentVO> matchingCoefficents = insuranceCoefficentList.stream()
                    .filter(coefficent -> coefficent.getInsuranceId().equals(insuranceVOHandler.getId()))
                    .collect(Collectors.toList());
                insuranceVOHandler.setInsuranceCoefficentVOs(matchingCoefficents);
            });
        }
        //=======================================封装产品方案=======================================
        InsurancePlanVO insurancePlanVO = InsurancePlanVO.builder().
            insuranceIds(insuranceIds.toArray(Long[]::new)).
            dataState(SuperConstant.DATA_STATE_0).
            build();
        List<InsurancePlanVO> insurancePlanVOList = insurancePlanService.findList(insurancePlanVO);
        if(!EmptyUtil.isNullOrEmpty(insurancePlanVOList)) {
            // 封装保险系数项到InsuranceVO的insurancePlan属性
            insuranceVOs.forEach(insuranceVOHandler -> {
                List<InsurancePlanVO> insurancePlanVOs = insurancePlanVOList.stream()
                    .filter(insurancePlanIndex -> insurancePlanIndex.getInsuranceId().equals(insuranceVOHandler.getId()))
                    .collect(Collectors.toList());
                insuranceVOHandler.setInsurancePlanVOs(insurancePlanVOs);
            });
        }
        //=======================================封装产品筛选项=======================================
        InsuranceConditionVO insuranceConditionVO = InsuranceConditionVO.builder()
            .insuranceIds(insuranceIds.toArray(Long[]::new))
            .dataState(SuperConstant.DATA_STATE_0)
            .build();
        List<InsuranceConditionVO> insuranceConditionList = insuranceConditionService.findList(insuranceConditionVO);
        if(!EmptyUtil.isNullOrEmpty(insuranceConditionList)) {
            // 封装保险系数项到InsuranceVO的insurancePlan属性
            insuranceVOs.forEach(insuranceVOHandler -> {
                List<InsuranceConditionVO> insuranceConditionVOS = insuranceConditionList.stream()
                    .filter(insuranceConditionItem -> String.valueOf(insuranceVOHandler.getId()).equals(insuranceConditionItem.getInsuranceId()))
                    .collect(Collectors.toList());
                insuranceVOHandler.setInsuranceConditionVOs(insuranceConditionVOS);
            });
        }
        //=======================================封装附件=============================================
        //调用fileBusinessFeign附件信息
        List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(insuranceIds);
        if(!EmptyUtil.isNullOrEmpty(fileVOs)) {
            insuranceVOs.forEach(insuranceVOHandler -> {
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                fileVOs.forEach(fileVO -> {
                    if (insuranceVOHandler.getId().equals(fileVO.getBusinessId())) {
                        fileVOsHandler.add(fileVO);
                    }
                });
                insuranceVOHandler.setFileVOs(fileVOsHandler);
            });
        }
        //====================================================================================
        return insuranceVOs;
    }

    public static Map<String, String> insuranceTypeMap = new HashMap<>();

    static {
        //校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_0,"medicalInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_1,"severeIllnessInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_2,"accidentInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_3,"agedInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_4,"annuityInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_5,"travelInsureHandler");
        //算法可后续扩展
    }

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Override
    public String doPremium(DoInsureVo doInsureVo) {
        try {
            //传入checkRule参数从insuranceTypeMap中依据K值找到V值，也就是拿到处理类的bean名称
            String checkRule = insuranceTypeMap.get(doInsureVo.getCheckRule());
            //依据bean名称通过IOC容器工具类从IOC中获得具体的保处理接口的某个实现类去处理具体的业务
            InsureHandler insureHandler = registerBeanHandler.getBean(checkRule, InsureHandler.class);
            return insureHandler.doPremium(doInsureVo);
        }catch (Exception e){
            log.error("保费计算异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.CALCULATION_FAIL);
        }
    }

    @Override
    public EarningVO doEarnings(DoInsureVo doInsureVo) {
        try {
            String checkRule = insuranceTypeMap.get(doInsureVo.getCheckRule());
            InsureHandler insureHandler = registerBeanHandler.getBean(checkRule, InsureHandler.class);
            return insureHandler.doEarnings(doInsureVo);
        }catch (Exception e){
            log.error("收益计算异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceEnum.INCOME_FAIL);
        }
    }

    /**
     * 异步添加保险搜索记录
     * @param insuranceVOs
     * @return
     */
    private void createSearchContent(List<InsuranceVO> insuranceVOs, UserVO userVO) {
        searchRecordExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<SearchRecord> searchRecordList = new ArrayList<>();
                for (InsuranceVO insuranceVO: insuranceVOs) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.set("insuranceId",insuranceVO.getId());
                    jsonObject.set("insuranceName",insuranceVO.getInsuranceName());
                    SearchRecord searchRecord = SearchRecord.builder()
                        .dataState(SuperConstant.DATA_STATE_0)
                        .content(jsonObject.toString())
                        .createBy(userVO.getId()).
                    build();
                    searchRecordList.add(searchRecord);
                }
                searchRecordService.saveBatch(searchRecordList);
            }
        });
    }
}
