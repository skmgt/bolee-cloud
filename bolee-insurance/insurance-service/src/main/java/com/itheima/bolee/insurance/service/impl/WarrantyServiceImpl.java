package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.RequestTemplate;
import com.itheima.bolee.dto.ResponseDTO;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.insure.InsureConstant;
import com.itheima.bolee.framework.commons.constant.security.AuthChannelConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyOrderConstant;
import com.itheima.bolee.framework.commons.dto.analysis.InsureCategoryDTO;
import com.itheima.bolee.framework.commons.dto.analysis.SaleReportDTO;
import com.itheima.bolee.framework.commons.dto.analysis.TimeDTO;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.*;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.WarrantySource;
import com.itheima.bolee.insurance.constant.WarrantyCacheConstant;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.enums.WarrantyEnum;
import com.itheima.bolee.insurance.handler.InsureHandler;
import com.itheima.bolee.insurance.handler.InsureProcessHandler;
import com.itheima.bolee.insurance.mapper.WarrantyMapper;
import com.itheima.bolee.insurance.pojo.*;
import com.itheima.bolee.insurance.service.*;
import com.itheima.bolee.security.feign.AuthChannelFeign;
import com.itheima.bolee.security.feign.CompanyFeign;
import com.itheima.bolee.security.feign.UserFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description：合同信息服务实现类
 */
@Slf4j
@Service
public class WarrantyServiceImpl extends ServiceImpl<WarrantyMapper, Warranty> implements IWarrantyService {

    @Autowired
    private ICustomerRelationService customerRelationService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private IInsuranceService insuranceService;

    @Autowired
    private IdentifierGenerator identifierGenerator;

    @Autowired
    private IPlanSafeguardService planSafeguardService;

    @Autowired
    private IInsurancePlanService insurancePlanService;

    @Autowired
    private CompanyFeign companyFeign;

    @Autowired
    private IInsuranceCoefficentService insuranceCoefficentService;

    @Autowired
    private InsureHandler earningsInsureHandler;

    @Autowired
    private IWarrantyOrderService warrantyOrderService;

    @Autowired
    private WarrantyMapper warrantyMapper;

    @Autowired
    private IInsuranceApproveService insuranceApproveService;

    @Autowired
    private IWarrantyInsuredService warrantyInsuredService;

    @Autowired
    AuthChannelFeign authChannelFeign;

    @Autowired
    IWarrantyVerifyService warrantyVerifyService;

    @Autowired
    IWarrantyEarningsOrderService warrantyEarningsOrderService;

    /***
     * @description 合同信息多条件组合
     * @param warrantyVO 合同信息
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<Warranty> queryWrapper(WarrantyVO warrantyVO) {
        QueryWrapper<Warranty> queryWrapper = new QueryWrapper<>();
        //保险ID查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getInsuranceId())) {
            queryWrapper.lambda().eq(Warranty::getInsuranceId, warrantyVO.getInsuranceId());
        }
        //保险名称查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getInsuranceName())) {
            queryWrapper.lambda().eq(Warranty::getInsuranceName, warrantyVO.getInsuranceName());
        }
        //投保详情查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getInsuranceJson())) {
            queryWrapper.lambda().eq(Warranty::getInsuranceJson, warrantyVO.getInsuranceJson());
        }
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(Warranty::getWarrantyNo, warrantyVO.getWarrantyNo());
        }
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getWarrantyNos())) {
            queryWrapper.lambda().in(Warranty::getWarrantyNo, warrantyVO.getWarrantyNos());
        }
        //投保人姓名查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getApplicantName())) {
            queryWrapper.lambda().eq(Warranty::getApplicantName, warrantyVO.getApplicantName());
        }
        //投保人身份证号码查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getApplicantIdentityCard())) {
            queryWrapper.lambda().eq(Warranty::getApplicantIdentityCard, warrantyVO.getApplicantIdentityCard());
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getCompanyNo())) {
            queryWrapper.lambda().eq(Warranty::getCompanyNo, warrantyVO.getCompanyNo());
        }
        //企业名称查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getCompanyName())) {
            queryWrapper.lambda().eq(Warranty::getCompanyName, warrantyVO.getCompanyName());
        }
        //保障起始时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getSafeguardStartTime())) {
            queryWrapper.lambda().eq(Warranty::getSafeguardStartTime, warrantyVO.getSafeguardStartTime());
        }
        //保障截止时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getSafeguardEndTime())) {
            queryWrapper.lambda().eq(Warranty::getSafeguardEndTime, warrantyVO.getSafeguardEndTime());
        }
        //保费查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getPremium())) {
            queryWrapper.lambda().eq(Warranty::getPremium, warrantyVO.getPremium());
        }
        //自动延保（0是 1否）查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getAutoWarrantyExtension())) {
            queryWrapper.lambda().eq(Warranty::getAutoWarrantyExtension, warrantyVO.getAutoWarrantyExtension());
        }
        //状态（0待付款 1待生效 2保障中 3已失效 4 宽限期 5效力终止 ）查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getWarrantyState())) {
            queryWrapper.lambda().eq(Warranty::getWarrantyState, warrantyVO.getWarrantyState());
        }
        //in状态（0待付款 1待生效 2保障中 3已失效 4 宽限期 5效力终止 ）查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getWarrantyStates())) {
            queryWrapper.lambda().in(Warranty::getWarrantyState, warrantyVO.getWarrantyStates());
        }
        //状态（0待付款 1待生效 2保障中 3已失效 4 宽限期 5效力终止 ）查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getApproveState())) {
            queryWrapper.lambda().eq(Warranty::getApproveState, warrantyVO.getApproveState());
        }
        //总期数查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getPeriods())) {
            queryWrapper.lambda().eq(Warranty::getPeriods, warrantyVO.getPeriods());
        }
        //已缴纳期数查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getDutyPeriod())) {
            queryWrapper.lambda().eq(Warranty::getDutyPeriod, warrantyVO.getDutyPeriod());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getSortNo())) {
            queryWrapper.lambda().eq(Warranty::getSortNo, warrantyVO.getSortNo());
        }
        //犹豫期截止时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getHesitationTime())) {
            queryWrapper.lambda().eq(Warranty::getHesitationTime, warrantyVO.getHesitationTime());
        }
        //保单宽限查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getGrace())) {
            queryWrapper.lambda().eq(Warranty::getGrace, warrantyVO.getGrace());
        }
        //宽限单位查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getGraceUnit())) {
            queryWrapper.lambda().eq(Warranty::getGraceUnit, warrantyVO.getGraceUnit());
        }
        //保单复效查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getRevival())) {
            queryWrapper.lambda().eq(Warranty::getRevival, warrantyVO.getRevival());
        }
        //复效单位查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getRevivalUnit())) {
            queryWrapper.lambda().eq(Warranty::getRevivalUnit, warrantyVO.getRevivalUnit());
        }
        //代理人ID
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getAgentId())) {
            queryWrapper.lambda().eq(Warranty::getAgentId, warrantyVO.getAgentId());
        }
        //代理人姓名
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getAgentName())) {
            queryWrapper.lambda().likeRight(Warranty::getAgentName, warrantyVO.getAgentName());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getDataState())) {
            queryWrapper.lambda().eq(Warranty::getDataState, warrantyVO.getDataState());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyVO.getUnderwritingState())) {
            queryWrapper.lambda().eq(Warranty::getUnderwritingState, warrantyVO.getUnderwritingState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Warranty::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyCacheConstant.PAGE, key = "#pageNum+'-'+#pageSize+'-'+#warrantyVO.hashCode()")
    public Page<WarrantyVO> findPage(WarrantyVO warrantyVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Warranty> WarrantyPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<Warranty> queryWrapper = queryWrapper(warrantyVO);
            //执行分页查询
            Page<WarrantyVO> warrantyVOPage = BeanConv.toPage(
                    page(WarrantyPage, queryWrapper), WarrantyVO.class);
            //返回结果
            return warrantyVOPage;
        } catch (Exception e) {
            log.error("合同信息分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.PAGE_FAIL);
        }
    }



    @Override
    @Cacheable(value = WarrantyCacheConstant.BASIC, key = "#warrantyId")
    public WarrantyVO findById(String warrantyId) {
        try {
            //执行查询
            WarrantyVO warrantyVO = BeanConv.toBean(getById(warrantyId), WarrantyVO.class);
            WarrantyOrderVO warrantyOrderVO = WarrantyOrderVO.builder()
                .warrantyNo(warrantyVO.getWarrantyNo())
                .build();
            List<WarrantyOrderVO> list = warrantyOrderService.findList(warrantyOrderVO);
            warrantyVO.setWarrantyOrderVOList(list);
            return warrantyVO;
        } catch (Exception e) {
            log.error("合同信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = WarrantyCacheConstant.LIST, allEntries = true)},
            put = {@CachePut(value = WarrantyCacheConstant.BASIC, key = "#result.id")})
    public WarrantyVO save(WarrantyVO warrantyVO) {
        try {
            //转换WarrantyVO为Warranty
            Warranty warranty = BeanConv.toBean(warrantyVO, Warranty.class);
            boolean flag = save(warranty);
            if (!flag) {
                throw new RuntimeException("保存合同信息失败");
            }
            //转换返回对象WarrantyVO
            WarrantyVO warrantyVOHandler = BeanConv.toBean(warranty, WarrantyVO.class);
            return warrantyVOHandler;
        } catch (Exception e) {
            log.error("保存合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = WarrantyCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = WarrantyCacheConstant.BASIC, key = "#warrantyVO.id")})
    public Boolean update(WarrantyVO warrantyVO) {
        try {
            //转换WarrantyVO为Warranty
            Warranty warranty = BeanConv.toBean(warrantyVO, Warranty.class);
            boolean flag = updateById(warranty);
            if (!flag) {
                throw new RuntimeException("修改合同信息失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyCacheConstant.PAGE, allEntries = true),
            @CacheEvict(value = WarrantyCacheConstant.LIST, allEntries = true),
            @CacheEvict(value = WarrantyCacheConstant.BASIC, allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag) {
                throw new RuntimeException("删除合同信息失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyCacheConstant.LIST, key = "#warrantyVO.hashCode()")
    public List<WarrantyVO> findList(WarrantyVO warrantyVO) {
        try {
            //构建查询条件
            QueryWrapper<Warranty> queryWrapper = queryWrapper(warrantyVO);
            //执行列表查询
            List<WarrantyVO> warrantyVOs = BeanConv.toBeanList(list(queryWrapper), WarrantyVO.class);
            if (!EmptyUtil.isNullOrEmpty(warrantyVOs)){
                //获取合同合同编号
                List<String> warrantyNo = warrantyVOs.stream().map(WarrantyVO::getWarrantyNo).collect(Collectors.toList());
                LambdaQueryWrapper<WarrantyInsured> insuredLambdaQueryWrapper = new LambdaQueryWrapper<>();
                insuredLambdaQueryWrapper.in(WarrantyInsured::getWarrantyNo, warrantyNo);
                insuredLambdaQueryWrapper.eq(WarrantyInsured::getDataState, SuperConstant.DATA_STATE_0);
                //查询被投保人信息
                List<WarrantyInsured> warrantyInsureds = warrantyInsuredService.list(insuredLambdaQueryWrapper);
                //按照合同编号进行分组
                Map<String, List<WarrantyInsured>> warrantyNoByInsured = warrantyInsureds.stream().collect(Collectors.groupingBy(WarrantyInsured::getWarrantyNo));
                //查询客户的所有关系网
                List<CustomerRelationVO> homeList = customerRelationService.findHomeList(SubjectContent.getUserVO());
                //封装被投保人信息
                warrantyVOs.stream().forEach(e->{
                    ArrayList<CustomerRelationVO> customerRelationVOS = new ArrayList<>();
                    List<WarrantyInsured> insureds = warrantyNoByInsured.get(e.getWarrantyNo());
                    insureds.stream().forEach(warrantyInsured -> {
                        //根据合同中的被保人身份证号获取客户关系
                        Optional<CustomerRelationVO> first = homeList.stream().filter(y -> y.getIdentityCard().equals(warrantyInsured.getInsuredIdentityCard())).findFirst();
                        if(first.isPresent()){
                            customerRelationVOS.add(first.get());
                        }
                    });
                    e.setCustomerRelationVOs(customerRelationVOS);
                });
            }
            //封装被投保人信息
            return warrantyVOs;
        } catch (Exception e) {
            log.error("合同信息列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.LIST_FAIL);
        }
    }

    /**
     * 获取我的保险数量
     * 进行中的保险数量
     *
     * @return
     */
    @Override
    @Cacheable(value = WarrantyCacheConstant.LIST, key = "#userVO.getId()")
    public SelfWarrantyVO findMyWarrantyNums(UserVO userVO) {
        try {
            CustomerRelationVO customerRelationVO = customerRelationService.findOne(userVO.getId());
            if (ObjectUtil.isNull(customerRelationVO)) {
                return null;
            }
            //获取身份证号
            String idCard = customerRelationVO.getIdentityCard();
            //保险对象
            WarrantyVO warrantyVO = WarrantyVO.builder().
                applicantIdentityCard(idCard).
                dataState(SuperConstant.DATA_STATE_0).
                warrantyStates(new String[]{
                    WarrantyConstant.STATE_NOT_PAY,
                    WarrantyConstant.STATE_TO_BE_WORK,
                    WarrantyConstant.STATE_SAFEING
                }).
                build();
            //保险对象
            List<WarrantyVO> warrantyVOList = findList(warrantyVO);
            //使用stream统计
            List<WarrantyVO> ingWarrantList = warrantyVOList.stream()
                    .filter(warrantyIndex -> WarrantyConstant.STATE_SAFEING.equals(warrantyIndex.getWarrantyState()))
                    .collect(Collectors.toList());
            LocalDateTime overTimes = LocalDateTimeUtil.offset(LocalDateTime.now(), 1, ChronoUnit.MONTHS);
            List<WarrantyVO> insureNumsList = ingWarrantList.stream()
                .filter(warrantyIndex -> {
                    if (!EmptyUtil.isNullOrEmpty(warrantyIndex.getSafeguardEndTime())){
                        return warrantyIndex.getSafeguardEndTime().isAfter(overTimes);
                    }else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
            return SelfWarrantyVO.builder().
                insureIngNums(ingWarrantList.size()).
                insureNums(warrantyVOList.size()).
                notRenewalNums(insureNumsList.size()).
                build();
        } catch (Exception e) {
            log.error("获取我的保险,和在保保险数量查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 获取家庭人身保障
     *
     * @return
     */
    @Override
    @SensitiveResponse
    public List<HomePeopleVO> findHomeSafeInfo(UserVO loginUser) {
        try {
            List<HomePeopleVO> res = new ArrayList<>();
            List<CustomerRelationVO> customerRelationList = customerRelationService.findHomeList(loginUser);
            //拿到家庭所有人员的身份证号
            List<String> idCardList = customerRelationList.stream().map(CustomerRelationVO::getIdentityCard).collect(Collectors.toList());
            //转sql中字符串
            String idCard = idCardList.stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(","));
            //拿到所有家庭人员的身份证号对应保险的保障类型
            List<HomeSafeguardDTO> homeSafeInfo = warrantyMapper.findHomeSafeInfo(idCard, SuperConstant.DATA_STATE_0, WarrantyConstant.STATE_SAFEING);
            List<String> allCheckRule = InsureConstant.getAllCheckRule();
            if (CollectionUtil.isEmpty(homeSafeInfo)) {
                for (CustomerRelationVO relation : customerRelationList) {
                    HomePeopleVO label = new HomePeopleVO();
                    List<InsureTypeOnHomePeopleVO> value = new ArrayList<>();
                    for (String index : allCheckRule) {
                        InsureTypeOnHomePeopleVO insureIndex = new InsureTypeOnHomePeopleVO();
                        insureIndex.setValue(false);
                        insureIndex.setCheckRule(InsureConstant.getRuleNameById(index));
                        value.add(insureIndex);
                    }
                    label.setName(relation.getName());
                    label.setType(value);
                    label.setRelation(relation.getRelation());
                    label.setSex(CustomerInfoUtil.setSexByIdCard(relation.getIdentityCard()));
                    res.add(label);
                }
            } else {
                for (CustomerRelationVO relation : customerRelationList) {
                    HomePeopleVO label = new HomePeopleVO();
                    List<InsureTypeOnHomePeopleVO> value = new ArrayList<>();
                    for (String index : allCheckRule) {
                        InsureTypeOnHomePeopleVO insureIndex = new InsureTypeOnHomePeopleVO();
                        Optional<HomeSafeguardDTO> first = homeSafeInfo.stream().filter(e -> e.getCheckRule().equals(index)).findFirst();
                        if (first.isPresent()) {
                            insureIndex.setValue(true);
                        } else {
                            insureIndex.setValue(false);
                        }
                        insureIndex.setCheckRule(InsureConstant.getRuleNameById(index));
                        value.add(insureIndex);
                    }
                    label.setName(relation.getName());
                    label.setType(value);
                    label.setRelation(relation.getRelation());
                    label.setSex(CustomerInfoUtil.setSexByIdCard(relation.getIdentityCard()));
                    res.add(label);
                }
            }
            return res;
        } catch (Exception e) {
            log.error("获取我的保险,和在保保险数量查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.FIND_ONE_FAIL);
        }
    }

    public static Map<String, String> insuranceTypeMap = new HashMap<>();

    static {
        //校验规则：0医疗 1重疾 2意外 3养老 4储蓄 5旅游 6宠物 7定寿
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_0, "medicalInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_1, "severeIllnessInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_2, "accidentInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_3, "agedInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_4, "annuityInsureHandler");
        insuranceTypeMap.put(InsureConstant.CHECK_RULE_5, "travelInsureHandler");
        //算法可后续扩展
    }

    @Autowired
    RegisterBeanHandler registerBeanHandler;

    @Autowired
    TripartiteInsureService tripartiteInsureService;

    @Autowired
    InsureProcessHandler insureProcessHandler;

    @Value("${warranty-delay-time}")
    String warrantyDelayTime;

    @Autowired
    WarrantySource warrantySource;

    @Override
    @GlobalTransactional
    public WarrantyVO doInsure(DoInsureVo doInsureVo) {
        try {
            //投保路由
            InsureHandler insureHandler = registerBeanHandler
                .getBean(insuranceTypeMap.get(doInsureVo.getCheckRule()), InsureHandler.class);
            //保险合同生成
            WarrantyVO warrantyVOHandler = insureHandler.doInsure(doInsureVo);
            WarrantyVO warrantyVO =  save(warrantyVOHandler);
            //保司承保
            ResponseDTO responseDTO = tripartiteInsureService.insure(warrantyVOHandler);
            if (!"2000".equals(responseDTO.getCode())){
                throw new RuntimeException("核保不通过！");
            }
            warrantyVO.setUnderwritingState(WarrantyConstant.UNDERWRITING_STATE_3);
            //同步合同状态
            update(warrantyVO);
            //合同订单生成
            List<WarrantyOrderVO> warrantyOrderVO = insureHandler.createWarrantyOrderVO(warrantyVO);
            warrantyOrderService.saveBatch(BeanConv.toBeanList(warrantyOrderVO, WarrantyOrder.class));
            //保存被保人
            List<CustomerRelationVO> customerRelationVOs = insureProcessHandler.buildInsureds(doInsureVo.getCustomerRelationIds());
            List<WarrantyInsured> warrantyInsureds = Lists.newArrayList();
            customerRelationVOs.forEach(n->{
                WarrantyInsured warrantyInsured = WarrantyInsured.builder()
                    .companyNo(warrantyVO.getCompanyNo())
                    .warrantyNo(warrantyVO.getWarrantyNo())
                    .insuredName(n.getName())
                    .insuredIdentityCard(n.getIdentityCard())
                    .build();
                warrantyInsureds.add(warrantyInsured);
            });
            warrantyInsuredService.saveBatch(warrantyInsureds);
            //发送队列信息:合同如果超过10分钟不进行支付处理，则会被消息队列清空
            Long messageId = (Long) identifierGenerator.nextId(doInsureVo);
            MqMessage mqMessage = MqMessage.builder()
                .id(messageId)
                .title("warranty-message")
                .content(warrantyVO.getWarrantyNo())
                .messageType("warranty-request")
                .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                .sender("system")
                .build();
            Message<MqMessage> message = MessageBuilder.withPayload(mqMessage).setHeader("x-delay", warrantyDelayTime).build();
            boolean send = warrantySource.warrantyOutput().send(message);
            return warrantyVO;
        }catch (Exception e){
            log.error("保存合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.SAVE_FAIL);
        }
    }

    @Override
    public List<WarrantyVO> myWarrantyVOs(UserVO userVO,CustomerRelationVO userRelationVO) {
        try {
            List<WarrantyInsuredVO> warrantyInsuredVOS = new ArrayList<>();
            //如果查询的是用户的某一个关系用户，则查询该关系用户
            if(ObjectUtil.isNotNull(userRelationVO.getId())){
                //如果查询的是用户的某一个关系用户，则查询该关系用户
                //查询被保人身份证号
                CustomerRelationVO customerRelationVO = customerRelationService.findById(String.valueOf(userRelationVO.getId()));
                //根据对应的身份证号查询被保人合同编号
                warrantyInsuredVOS = warrantyInsuredService.findList(WarrantyInsuredVO.builder().insuredIdentityCard(customerRelationVO.getIdentityCard()).build());
            }
            CustomerRelationVO customerRelationVO = customerRelationService.findOne(userVO.getId());
            //获取身份证号
            if (ObjectUtil.isNull(customerRelationVO)) {
                return null;
            }
            String idCard = customerRelationVO.getIdentityCard();
            //保险对象
            WarrantyVO warrantyVO = WarrantyVO.builder().
                    applicantIdentityCard(idCard).
                    dataState(SuperConstant.DATA_STATE_0).
                    warrantyStates(new String[]{
                        WarrantyConstant.STATE_NOT_PAY,
                        WarrantyConstant.STATE_TO_BE_WORK,
                        WarrantyConstant.STATE_SAFEING
                    }).
                    build();
            //保险投保数据
            if(CollectionUtil.isNotEmpty(warrantyInsuredVOS)){
                //查询被保人合同编号
                warrantyVO.setWarrantyNos(warrantyInsuredVOS.stream().map(WarrantyInsuredVO::getWarrantyNo).collect(Collectors.toList()));
            }
            //保险对象
            List<WarrantyVO> warrantyVOList = findList(warrantyVO);
            //封装保单订单
            if (!EmptyUtil.isNullOrEmpty(warrantyVOList)){
                //执行查询
                List<WarrantyOrderVO> warrantyOrderVOS = BeanConv.toBeanList(warrantyVOList, WarrantyOrderVO.class);
                List<String> warrantyNos = warrantyOrderVOS.stream().map(e -> e.getWarrantyNo()).collect(Collectors.toList());
                LambdaQueryWrapper<WarrantyOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.in(WarrantyOrder::getWarrantyNo, warrantyNos);
                lambdaQueryWrapper.eq(WarrantyOrder::getDataState, SuperConstant.DATA_STATE_0);
                List<WarrantyOrderVO> warrantyOrders = BeanConv.toBeanList(warrantyOrderService.list(lambdaQueryWrapper), WarrantyOrderVO.class);
                Map<String, List<WarrantyOrderVO>> collect = warrantyOrders.stream().collect(Collectors.groupingBy(WarrantyOrderVO::getWarrantyNo));
                warrantyVOList.forEach(e->{
                    e.setWarrantyOrderVOList(collect.get(e.getWarrantyNo()));
                });
            }
            return warrantyVOList;
        } catch (Exception e) {
            log.error("查询合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.LIST_FAIL);
        }
    }

    /**
     * 我的保单数量
     *
     * @param userVO
     * @return
     */
    @Override
    public MyWarrantyInfoVO myWarrantyNums(UserVO userVO) {
        try {
            CustomerRelationVO customerRelationVO = customerRelationService.findOne(userVO.getId());
            //获取身份证号
            if (EmptyUtil.isNullOrEmpty(customerRelationVO)) {
               return new MyWarrantyInfoVO();
            }
            String idCard = customerRelationVO.getIdentityCard();
            //保险对象
            WarrantyVO warrantyVO = WarrantyVO.builder().
                    applicantIdentityCard(idCard).
                    dataState(SuperConstant.DATA_STATE_0).
                    warrantyStates(new String[]{
                            WarrantyConstant.STATE_NOT_PAY,
                            WarrantyConstant.STATE_TO_BE_WORK,
                            WarrantyConstant.STATE_SAFEING
                    }).
                    build();
            List<WarrantyVO> warrantyVOS = findList(warrantyVO);
            MyWarrantyInfoVO res = MyWarrantyInfoVO.builder().nums(warrantyVOS.size()).build();
            if (warrantyVOS.size() <= 0) {
                ArrayList<MyWarrantyInfoItemVO> items = new ArrayList<>();
                List<String> allCheckRule = InsureConstant.getAllCheckRule();
                for (String type : allCheckRule) {
                    MyWarrantyInfoItemVO warrantyInfoItemVO = new MyWarrantyInfoItemVO();
                    warrantyInfoItemVO.setWarrantyType(type);
                    warrantyInfoItemVO.setNums(0);
                    items.add(warrantyInfoItemVO);
                }
                res.setData(items);
                return res;
            }
            List<MyWarrantyInfoItemVO> items = new ArrayList<MyWarrantyInfoItemVO>();
            List<Long> insuranceId = warrantyVOS.stream().map(WarrantyVO::getInsuranceId).collect(Collectors.toList());
            //insuranceId转String[]
            String[] insuranceIdArr = insuranceId.stream().map(Object::toString).toArray(String[]::new);
            //拿到各个保险的id后查询对应保险的保障类型
            InsuranceVO insuranceVO = InsuranceVO.builder().checkedIds(insuranceIdArr).dataState(SuperConstant.DATA_STATE_0).build();
            List<InsuranceVO> insuranceList = insuranceService.findList(insuranceVO);
            List<String> checkRuleList = insuranceList.stream().map(InsuranceVO::getCheckRule).collect(Collectors.toList());
            Set<String> cheRuleSet = insuranceList.stream().map(InsuranceVO::getCheckRule).collect(Collectors.toSet());
            for (String type : cheRuleSet) {
                //判断当前类型在checkRuleList中出现了几次
                int i = 0;
                for (String index : checkRuleList) {
                    if (index.equals(type)) {
                        i++;
                    }
                }
                MyWarrantyInfoItemVO warrantyInfoItemVO = MyWarrantyInfoItemVO.builder().warrantyType(type).nums(i).build();
                items.add(warrantyInfoItemVO);
            }
            //将处理完成的数据添加到结果集中
            res.setData(items);
            return res;
        } catch (Exception e) {
            log.error("查询合同信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.LIST_FAIL);
        }
    }

    /**
     * 核保补发
     */
    @Override
    public void sendCheckInfo() {
        //查询所有状态时未核保的保单
        WarrantyVO warrantyVO = WarrantyVO.builder().
                underwritingState(WarrantyConstant.UNDERWRITING_STATE_0).
                dataState(SuperConstant.DATA_STATE_0).
                build();
        List<WarrantyVO> warrantyVOS = findList(warrantyVO);
        if (warrantyVOS.size() <= 0) {
            return;
        } else {
            for (WarrantyVO index : warrantyVOS) {
                //查询三方接口配置信息
                AuthChannelVO authChannelVO = authChannelFeign.findAuthChannelByCompanyNoAndChannelLabel(
                        warrantyVO.getCompanyNo(),
                        AuthChannelConstant.CHANNEL_LABEL_INSURE);
                List<OtherConfigVO> otherConfigVOs = JSONArray.parseArray(authChannelVO.getOtherConfig(), OtherConfigVO.class);
                List<OtherConfigVO> otherConfigVOsHandler = otherConfigVOs.stream().filter(n -> {
                    return n.getConfigKey().equals(AuthChannelConstant.PUBLICKEY);
                }).collect(Collectors.toList());
                //获取所有保单的id
                URIBuilder urlBuilder = new URIBuilder()
                        .setScheme("http")
                        .setHost(authChannelVO.getDomain())
                        .setPath("/insure");
                RequestTemplate requestTemplate = RequestTemplate.builder()
                        .appId(authChannelVO.getAppId())
                        .privateKey(authChannelVO.getAppSecret())
                        .publicKey(otherConfigVOsHandler.get(0).getConfigValue())
                        .uriBuilder(urlBuilder)
                        .build();
                try {
                    ResponseDTO responseDTO = requestTemplate.doRequest(index, ResponseDTO.class);
                    //保存审核信息
                    WarrantyVerifyVO warrantyVerifyVO = WarrantyVerifyVO.builder()
                            .warrantyNo(warrantyVO.getWarrantyNo())
                            .companyNo(warrantyVO.getCompanyNo())
                            .verifyCode(responseDTO.getCode())
                            .verifyMsg(responseDTO.getMsg())
                            .build();
                    warrantyVerifyService.save(warrantyVerifyVO);
                    index.setUnderwritingState(WarrantyConstant.UNDERWRITING_STATE_3);
                    this.update(index);
                } catch (Exception e) {
                    log.error("核保补发异常：{}", ExceptionsUtil.getStackTraceAsString(e));
                    index.setUnderwritingState(WarrantyConstant.UNDERWRITING_STATE_2);
                    this.update(index);
                }
            }
        }
    }

    /**
     * 保批补发
     */
    @Override
    public void sendApproveInfo() {
        //查询所有状态时未核保的保单
        InsuranceApproveVO insuranceApproveVO = InsuranceApproveVO.builder().
                approveState(WarrantyConstant.APPROVE_STATE_SEND_ERROR).
                dataState(SuperConstant.DATA_STATE_0).
                build();
        List<InsuranceApproveVO> approveVOS = insuranceApproveService.findList(insuranceApproveVO);
        if (approveVOS.size() <= 0) {
            return;
        } else {
            for (InsuranceApproveVO index : approveVOS) {
                //查询三方接口配置信息
                AuthChannelVO authChannelVO = authChannelFeign.findAuthChannelByCompanyNoAndChannelLabel(
                        index.getCompanyNo(),
                        AuthChannelConstant.CHANNEL_LABEL_INSURE);
                List<OtherConfigVO> otherConfigVOs = JSONArray.parseArray(authChannelVO.getOtherConfig(), OtherConfigVO.class);
                List<OtherConfigVO> otherConfigVOsHandler = otherConfigVOs.stream().filter(n -> {
                    return n.getConfigKey().equals(AuthChannelConstant.PUBLICKEY);
                }).collect(Collectors.toList());
                //获取所有保单的id
                URIBuilder urlBuilder = new URIBuilder()
                        .setScheme("http")
                        .setHost(authChannelVO.getDomain())
                        .setPath("/approve-info");
                RequestTemplate requestTemplate = RequestTemplate.builder()
                        .appId(authChannelVO.getAppId())
                        .privateKey(authChannelVO.getAppSecret())
                        .publicKey(otherConfigVOsHandler.get(0).getConfigValue())
                        .uriBuilder(urlBuilder)
                        .build();
                try {
                    ResponseDTO responseDTO = requestTemplate.doRequest(index, ResponseDTO.class);
                    if(responseDTO.getCode().equals("2000")){
                        //说明保批成功
                        insuranceApproveVO.setApproveState(WarrantyConstant.APPROVE_STATE_APPROVE);
                        insuranceApproveService.update(insuranceApproveVO);
                        //保存审核信息
                        String warrantyNo = index.getWarrantyNo();
                        WarrantyVO warrantyVO = WarrantyVO.builder().warrantyNo(warrantyNo).dataState(SuperConstant.DATA_STATE_0).build();
                        QueryWrapper<Warranty> warrantyQueryWrapper = queryWrapper(warrantyVO);
                        WarrantyVO warranty = BeanConv.toBean(getOne(warrantyQueryWrapper), WarrantyVO.class);
                        warranty.setApproveState(WarrantyConstant.APPROVE_STATE_APPROVE);
                        update(warranty);
                    }
                } catch (Exception e) {
                    log.error("报批补发异常：{}", ExceptionsUtil.getStackTraceAsString(e));
                }
            }
        }
    }

    @Override
    public WarrantyVO findWarranty(String warrantyId) {
        try {
            //执行查询
            WarrantyVO warrantyVO = BeanConv.toBean(getById(warrantyId), WarrantyVO.class);
            WarrantyOrderVO warrantyOrderVO = WarrantyOrderVO.builder()
                .orderState(WarrantyOrderConstant.ORDER_STATE_0)
                .warrantyNo(warrantyVO.getWarrantyNo())
                .build();
            List<WarrantyOrderVO> list = warrantyOrderService.findList(warrantyOrderVO);
            warrantyVO.setWarrantyOrderVOList(list);
            return warrantyVO;
        } catch (Exception e) {
            log.error("合同信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 根据合同编号获取合同信息
      * @param warrantyNo
     * @return
     */
    @Override
    public WarrantyVO findByWarrantyNo(String warrantyNo) {
        try {
            LambdaQueryWrapper<Warranty> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Warranty::getWarrantyNo, warrantyNo);
            //执行查询
            WarrantyVO warrantyVO = BeanConv.toBean(getOne(queryWrapper), WarrantyVO.class);
            WarrantyOrderVO warrantyOrderVO = WarrantyOrderVO.builder()
                    .orderState(WarrantyOrderConstant.ORDER_STATE_0)
                    .warrantyNo(warrantyVO.getWarrantyNo())
                    .build();
            List<WarrantyOrderVO> list = warrantyOrderService.findList(warrantyOrderVO);
            warrantyVO.setWarrantyOrderVOList(list);
            return warrantyVO;
        } catch (Exception e) {
            log.error("合同信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.FIND_ONE_FAIL);
        }
    }

    @Autowired
    RedissonClient redissonClient;

    @Override
    @Transactional
    public Boolean cleanWarranty(String warrantyNo) {
        //注意保险合同加锁：防止删除同时，计划任务在进行周期性扣款
        String key = "lock_warranty:"+warrantyNo;
        RLock fairLock = redissonClient.getFairLock(key);
        try {
            if (fairLock.tryLock(CacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
                LambdaQueryWrapper<Warranty> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Warranty::getWarrantyNo, warrantyNo)
                        .eq(Warranty::getWarrantyState,WarrantyConstant.STATE_NOT_PAY);
                Warranty warranty = getOne(queryWrapper);
                if (!EmptyUtil.isNullOrEmpty(warranty)){
                    //删除保险合同
                    UpdateWrapper<Warranty> warrantyUpdateWrapper = new UpdateWrapper<>();
                    warrantyUpdateWrapper.lambda().eq(Warranty::getWarrantyNo, warrantyNo)
                            .eq(Warranty::getWarrantyState,WarrantyOrderConstant.ORDER_STATE_0);
                    this.remove(warrantyUpdateWrapper);
                    //删除合同给付计划
                    UpdateWrapper<WarrantyEarningsOrder> warrantyEarningsOrderUpdateWrapper = new UpdateWrapper<>();
                    warrantyEarningsOrderUpdateWrapper.lambda().eq(WarrantyEarningsOrder::getWarrantyNo, warrantyNo);
                    warrantyEarningsOrderService.remove(warrantyEarningsOrderUpdateWrapper);
                    //删除合同被保人
                    UpdateWrapper<WarrantyInsured> warrantyInsuredUpdateWrapper = new UpdateWrapper<>();
                    warrantyInsuredUpdateWrapper.lambda().eq(WarrantyInsured::getWarrantyNo, warrantyNo);
                    warrantyInsuredService.remove(warrantyInsuredUpdateWrapper);
                    //删除合同订单
                    UpdateWrapper<WarrantyOrder> warrantyOrderUpdateWrapper = new UpdateWrapper<>();
                    warrantyOrderUpdateWrapper.lambda().eq(WarrantyOrder::getWarrantyNo, warrantyNo);
                    warrantyOrderService.remove(warrantyOrderUpdateWrapper);
                    //删除合同核保
                    UpdateWrapper<WarrantyVerify> warrantyVerifyUpdateWrapper = new UpdateWrapper<>();
                    warrantyVerifyUpdateWrapper.lambda().eq(WarrantyVerify::getWarrantyNo, warrantyNo);
                    warrantyVerifyService.remove(warrantyVerifyUpdateWrapper);
                }
            }
        }catch (Exception e){
            log.error("保险合同加锁失败：{}",ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.DEL_FAIL);
        }finally {
            fairLock.unlock();
        }
        return true;
    }

    @Override
    public SaleReportDTO doInsureDetailDay(String reportTime) {
        try {
            //目标日期
            TimeDTO yesterdayTime = TimeHandlerUtils.getYesterdayTime(reportTime);
            //查询合同不为待付款状态的订单
            QueryWrapper<Warranty> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                .between(Warranty::getCreateTime,yesterdayTime.getBeginTime(),yesterdayTime.getEndTime())
                .ne(Warranty::getWarrantyState,WarrantyConstant.STATE_NOT_PAY);
            List<Warranty> listHandler = list(queryWrapper);
            if (EmptyUtil.isNullOrEmpty(listHandler)){
                return SaleReportDTO.builder()
                    .totalAmountDay(BigDecimal.ZERO)
                    .totalWarrantyDay(0L)
                    .avgPieceAmountDay(BigDecimal.ZERO)
                    .avgPersonAmountDay(BigDecimal.ZERO)
                    .persons(Long.valueOf(0L))
                    .reportTime(yesterdayTime.getTargetDate())
                    .build();
            }
            //日投保总额度
            BigDecimal totalAmountDay = listHandler.stream().map(Warranty::getPremiums).reduce(BigDecimal.ZERO, BigDecimal::add);
            //日总合同数
            Integer totalWarrantyDay = listHandler.size();
            //投保人数
            Integer persons = listHandler.stream().collect(Collectors.groupingBy(Warranty::getApplicantIdentityCard)).size();
            //日投保件均额度
            BigDecimal avgPieceAmountDay = totalAmountDay.divide(new BigDecimal(totalWarrantyDay),BigDecimal.ROUND_HALF_UP);
            //日投保人均额度
            BigDecimal avgPersonAmountDay = totalAmountDay.divide(new BigDecimal(persons),BigDecimal.ROUND_HALF_UP);
            //构建返回
            return SaleReportDTO.builder()
                .totalAmountDay(totalAmountDay)
                .totalWarrantyDay(Long.valueOf(totalWarrantyDay))
                .avgPieceAmountDay(avgPieceAmountDay)
                .avgPersonAmountDay(avgPersonAmountDay)
                .persons(Long.valueOf(persons))
                .reportTime(yesterdayTime.getTargetDate())
                .build();
        }catch (Exception e){
            log.error("查询指定时间保单出错：{}",ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.LIST_FAIL);
        }
    }

    @Autowired
    ICategoryService categoryService;

    @Override
    public List<InsureCategoryDTO> doInsureCategory(String reportTime) {
        try {
            //目标日期
            TimeDTO yesterdayTime = TimeHandlerUtils.getYesterdayTime(reportTime);
            //查询合同不为待付款状态的订单
            QueryWrapper<Warranty> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                .between(Warranty::getCreateTime,yesterdayTime.getBeginTime(),yesterdayTime.getEndTime())
                .ne(Warranty::getWarrantyState,WarrantyConstant.STATE_NOT_PAY);
            List<Warranty> listHandler = list(queryWrapper);
            //无保单则返回空
            if (EmptyUtil.isNullOrEmpty(listHandler)){
                return null;
            }
            //处理订单分类情况
            List<WarrantyVO> warrantyVOs = BeanConv.toBeanList(listHandler, WarrantyVO.class);
            for (WarrantyVO warrantyVO : warrantyVOs) {
                InsureProcessVO insureProcessVO = JSONObject.parseObject(warrantyVO.getInsuranceJson(), InsureProcessVO.class);
                warrantyVO.setCategoryNo(insureProcessVO.getInsuranceVO().getCategoryNo());
            }
            //按照分类编号进行分组
            Map<Long, List<WarrantyVO>> mapWarrantyVOs = warrantyVOs.stream().collect(Collectors.groupingBy(WarrantyVO::getCategoryNo));
            //获得所有二级分类
            List<CategoryVO> categoryReportVOs = categoryService
                    .findList(CategoryVO.builder().nodeFloors(Lists.newArrayList(2L)).build());
            //无分类直接返回空
            if (EmptyUtil.isNullOrEmpty(categoryReportVOs)){
                return null;
            }
            //补全分类名称返回结果
            List<InsureCategoryDTO> insureCategoryDTOs = Lists.newArrayList();
            for (CategoryVO c : categoryReportVOs) {
                for (Map.Entry<Long, List<WarrantyVO>> e : mapWarrantyVOs.entrySet()) {
                    if (NoProcessing.isParent(c.getCategoryNo(),String.valueOf(e.getKey()))){
                        insureCategoryDTOs.add(InsureCategoryDTO.builder()
                            .categoryName(c.getCategoryName())
                            .doInsureNums(Long.valueOf(e.getValue().size()))
                            .reportTime(yesterdayTime.getTargetDate())
                            .build());
                    }
                }
            }
            return insureCategoryDTOs;
        }catch (Exception e){
            log.error("查询指定时间保单出错：{}",ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.LIST_FAIL);
        }
    }

}
