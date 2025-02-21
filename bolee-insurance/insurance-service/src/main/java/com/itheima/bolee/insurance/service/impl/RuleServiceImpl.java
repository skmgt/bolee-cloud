package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.dict.DataDictCacheConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.framework.rule.Utils;
import com.itheima.bolee.framework.rule.model.Label;
import com.itheima.bolee.framework.rule.model.rule.RuleInfo;
import com.itheima.bolee.framework.rule.runtime.KnowledgePackage;
import com.itheima.bolee.framework.rule.runtime.KnowledgeSession;
import com.itheima.bolee.framework.rule.runtime.KnowledgeSessionFactory;
import com.itheima.bolee.framework.rule.runtime.response.FlowExecutionResponse;
import com.itheima.bolee.framework.rule.runtime.response.RuleExecutionResponse;
import com.itheima.bolee.framework.rule.runtime.service.KnowledgeService;
import com.itheima.bolee.insurance.constant.CustomerRelationConstant;
import com.itheima.bolee.insurance.constant.RuleConstant;
import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.dto.DoInsureVo;
import com.itheima.bolee.insurance.dto.WorryFreeCustomerInfoVO;
import com.itheima.bolee.insurance.enums.CustomerInfoEnum;
import com.itheima.bolee.insurance.enums.RuleEnum;
import com.itheima.bolee.insurance.rule.AccessRuleDTO;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;
import com.itheima.bolee.insurance.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * RuleServiceImpl
 *
 * @author: wgl
 * @describe: 规则引擎业务层
 * @date: 2022/12/28 10:10
 */
@Service
@Slf4j
public class RuleServiceImpl implements IRuleService {

    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private ICustomerRelationService customerRelationService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IWorryFreeFlowNodeService flowNodeService;

    @Autowired
    private IWorryFreeInsuranceMatchService insuranceMatchService;

    @Autowired
    private IWorryFreeRiskItemService riskItemService;

    @Autowired
    private IWorryFreeSafeguardQuotaService safeguardQuotaService;



    /**
     * 获取投保人信息
     * @param worryFreeCustomerInfoVO
     * @return
     */
    @Override
    public void buildBaseParams(WorryFreeCustomerInfoVO worryFreeCustomerInfoVO){
        try {
            CustomerRelationVO customerRelationVQueryVO = CustomerRelationVO.builder().
                    customerId(SubjectContent.getUserVO().getId()).
                    dataState(SuperConstant.DATA_STATE_0).
                    build();
            //获取当前申请人
            List<CustomerRelationVO> customerRelationVOList = customerRelationService.findList(customerRelationVQueryVO);
            Map<String, List<CustomerRelationVO>> groupMap = customerRelationVOList.stream().collect(Collectors.groupingBy(CustomerRelationVO::getRelation));
            Optional<CustomerRelationVO> selfDTOOptional = groupMap.get(CustomerRelationConstant.SELF).stream().findFirst();
            if(selfDTOOptional.isPresent()){
                CustomerRelationVO selfDTO = selfDTOOptional.get();
                worryFreeCustomerInfoVO.setIdCard(selfDTO.getIdentityCard());
                worryFreeCustomerInfoVO.setName(selfDTO.getName());
            }else{
                throw new ProjectException(RuleEnum.DATA_ERROR_PER_ERROR);
            }
            //子女数量
            List<CustomerRelationVO> childerList = groupMap.get(CustomerRelationConstant.CHILDREN);
            if(CollectionUtil.isNotEmpty(childerList)){
                //封装子女数
                worryFreeCustomerInfoVO.setChildNum(childerList.size());
            }
            //双亲数量
            List<CustomerRelationVO> parentList = groupMap.get(CustomerRelationConstant.PARENTS);
            if(CollectionUtil.isNotEmpty(parentList)){
                //封装赡养父母数
                worryFreeCustomerInfoVO.setParentNum(parentList.size());
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("投保人你基本信息查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 省心配
      * @param costomerInfo
     */
    @Override
    public WorryFreeCustomerInfoVO worryFreePairing(WorryFreeCustomerInfoVO costomerInfo) {
        RLock lock = redissonClient.getLock(WorryFreeConstant.getLockKey(costomerInfo.getId()));
        try {
            boolean locked = lock.tryLock(30, 60, TimeUnit.SECONDS);
            cleanHistorry(costomerInfo.getId());
            if (locked) {
                //获取uruleService
                KnowledgeService knowledgeService = (KnowledgeService) Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
                //根据银保监编号获取对应的知识包
                KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("四方保险销售平台/" + "worry_free_stream_0011_a");
                //获取交互Session
                KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
                Map requestParam = objectToMap(costomerInfo);
                log.info("省心配模块传入参数：" + requestParam);
                session.startProcess("worry_free_stream_0002", requestParam);
                Map<String, Object> parameters = session.getParameters();
                log.info("规则引擎的返回结果：" + JSONUtil.parse(parameters).toString());
                //将返回的map结果转换成WorryFreeCustomerInfoVO类型
                WorryFreeCustomerInfoVO res = JSONUtil.toBean(JSONUtil.parseObj(parameters), WorryFreeCustomerInfoVO.class);
                return res;
            } else {
                // 未能获取锁，可能是其他线程正在执行
                // 可以选择等待一段时间后重试或者执行其他逻辑
                throw new ProjectException(RuleEnum.LOCK_NOT_ACQUIRED_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ProjectException(RuleEnum.WORRRY_FREE_INSURANCE_ERROR);
        }finally {
            // 无论如何都要释放锁
            lock.unlock();
        }
    }

    /**
     * 清空当前登录人的历史记录
     * @param id
     */
    private void cleanHistorry(String id) {
        flowNodeService.cleanCustomerHistry(id);
        insuranceMatchService.cleanCustomerHistry(id);
        riskItemService.cleanCustomerHistry(id);
        safeguardQuotaService.cleanCustomerHistry(id);
    }


    /**
     * 提交问卷信息到规则引擎
     * @param adviceHealthDTO
     * @return
     */
    @Cacheable(value = DataDictCacheConstant.QUESTION,key ="#adviceHealthDTO.hashCode()")
    @Override
    public AdviceHealthDTO submitQuestionChoose(AdviceHealthDTO adviceHealthDTO) {
        try {
            //获取uruleService
            KnowledgeService knowledgeService = (KnowledgeService) Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
            //根据健康咨询对应的知识包
            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge(RuleConstant.sickAdviceAdviceKnowledge());
            //获取交互Session
            KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
            Map requestParam = objectToMap(adviceHealthDTO);
            log.info("问卷信息为："+requestParam);
            session.startProcess(RuleConstant.sickAdviceProcessId(),requestParam);
            Map<String, Object> parameters = session.getParameters();
            log.info("规则引擎的返回结果："+parameters);
            log.info("规则的执行结果集合为："+parameters.get("sicks")+"");
            List<String> sicks = (List<String>) parameters.get("sicks");
            if (EmptyUtil.isNullOrEmpty(sicks)){
                parameters.put("result",true);
            }
            return BeanConv.toBean(parameters,AdviceHealthDTO.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new ProjectException(RuleEnum.SICK_ERROR_INSURANCE_ERROR);
        }
    }

    //==================================RuleDemo===============================

    @Override
    public void testRule(String id) {
        try {
            //获取uruleService
            KnowledgeService knowledgeService = (KnowledgeService) Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
            //根据银保监编号获取对应的知识包
            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("urule学习/" + id);
            //获取交互Session
            KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
            Map requestParam = buildParams();
            log.info("贷款申请人信息为："+requestParam);
            session.fireRules(requestParam);
            Map<String, Object> parameters = session.getParameters();
            log.info("规则引擎的返回结果："+parameters);
            log.info("规则的执行结果集合为："+parameters.get("checkList")+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map buildParams() throws IllegalAccessException {
        ApplicantInfo applicantInfo = new ApplicantInfo();
        applicantInfo.setId("1234564878");
        //设置姓名
        applicantInfo.setName("张三");
        //设置年龄
        applicantInfo.setAge(21);
        //设置性别
        applicantInfo.setSex("男");
        //设置身份证号
        applicantInfo.setIdCard("430203199206251018");
        //设置收入
        applicantInfo.setIncome(BigDecimal.valueOf(6000));
        //设置负债金额
        applicantInfo.setLiabilities(BigDecimal.valueOf(25000));
        //设置平均每月支出
        applicantInfo.setExpensesMonth(BigDecimal.valueOf(5000));
        //设置存款金额
        applicantInfo.setDeposit(BigDecimal.valueOf(20000));
        //设置自女数量
        applicantInfo.setChildNum(1);
        //设置还款周期
        applicantInfo.setRepaymentCycle(2);
        //设置学历编号
        applicantInfo.setEduNo(3);
        //设置城市编号
        applicantInfo.setCityNo("1");
        //设置贷款申请金额
        applicantInfo.setApplicationAmount(BigDecimal.valueOf(250000));
        //设置校验结果列表
        applicantInfo.setCheckList(new ArrayList());

        applicantInfo.setDieScore(0);
        applicantInfo.setSeriousScore(0);
        applicantInfo.setAccidentScore(0);
        applicantInfo.setMedicalScore(0);
        return objectToMap(applicantInfo);
    }


    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
    /**
     * ApplicantInfo
     *
     * @author: wgl
     * @describe: 申请人信息
     * @date: 2022/12/28 10:10
     */
    @Data
    public class ApplicantInfo {

        @Label("用户id")
        private String id;

        @Label("申请人姓名")
        private String name;

        @Label("年龄")
        private int age;

        @Label("性别")
        private String sex;

        @Label("身份证号")
        private String idCard;

        @Label("每月收入")
        private BigDecimal income;

        @Label("负债金额")
        private BigDecimal liabilities;

        @Label("平均每月支出")
        private BigDecimal expensesMonth;

        @Label("存款金额")
        private BigDecimal deposit;

        @Label("子女数量")
        private int childNum;

        @Label("还款周期")
        private int repaymentCycle;

        @Label("学历编号")
        private int eduNo;

        @Label("城市编号")
        private String cityNo;

        @Label("申请金额")
        private BigDecimal applicationAmount;

        @Label("校验结果")
        private List checkList;

        @Label("医疗风险总分")
        private Integer medicalScore;

        @Label("意外风险总分")
        private Integer accidentScore;

        @Label("身故风险总分")
        private Integer dieScore;

        @Label("重疾风险总分")
        private Integer seriousScore;

    }

    @Override
    public void testRuleTable(String id) {
        try {
            //获取uruleService
            KnowledgeService knowledgeService = (KnowledgeService) Utils.getApplicationContext().getBean(KnowledgeService.BEAN_ID);
            //根据银保监编号获取对应的知识包
            KnowledgePackage knowledgePackage = knowledgeService.getKnowledge("urule学习/" + id);
            //获取交互Session
            KnowledgeSession session = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
            Map requestParam = buildTableParamss();
            RuleExecutionResponse ruleExecutionResponse = session.fireRules(requestParam);
            Map<String, Object> parameters = session.getParameters();
            System.out.println(parameters);
            List<RuleInfo> firedRules = ruleExecutionResponse.getFiredRules();
            System.out.println(firedRules);
            List<FlowExecutionResponse> flowExecutionResponses = ruleExecutionResponse.getFlowExecutionResponses();
            System.out.println(flowExecutionResponses);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * ApplicantInfo
     *
     * @author: wgl
     * @describe: 申请人信息
     * @date: 2022/12/28 10:10
     */
    @Data
    public class StudentScore {

        @Label("学员id")
        private String id;

        @Label("姓名")
        private String name;

        @Label("语文成绩")
        private BigDecimal score_chinese;

        @Label("数学成绩")
        private BigDecimal score_math;

        @Label("英语成绩")
        private BigDecimal score_english;

        @Label("分班结果")
        private String class_level;

    }
    /**
     *
     * @return
     */
    private Map buildTableParamss() {
        try {
            StudentScore studentScore = new StudentScore();
            studentScore.setName("李四");
            studentScore.setScore_chinese(BigDecimal.valueOf(87));
            studentScore.setScore_english(BigDecimal.valueOf(90));
            studentScore.setScore_math(BigDecimal.valueOf(90));
            return objectToMap(studentScore);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
