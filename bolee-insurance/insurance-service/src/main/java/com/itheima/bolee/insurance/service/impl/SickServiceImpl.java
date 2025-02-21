package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.resource.FileObjectResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.bolee.dict.feign.DataDictFeign;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.sick.SickConstant;
import com.itheima.bolee.framework.commons.dto.dict.DataDictVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.dto.*;
import com.itheima.bolee.insurance.enums.SickSearchRecordEnum;
import com.itheima.bolee.insurance.pojo.SearchRecord;
import com.itheima.bolee.insurance.pojo.Sick;
import com.itheima.bolee.insurance.mapper.SickMapper;
import com.itheima.bolee.insurance.rule.advice.AdviceHealthDTO;
import com.itheima.bolee.insurance.service.IInsuranceSievingService;
import com.itheima.bolee.insurance.service.IRuleService;
import com.itheima.bolee.insurance.service.ISickSearchRecordService;
import com.itheima.bolee.insurance.service.ISickService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.security.feign.CustomerFeign;
import com.itheima.bolee.security.feign.UserFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.SickCacheConstant;
import com.itheima.bolee.insurance.enums.SickEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Description：疾病表服务实现类
 */
@Slf4j
@Service
public class SickServiceImpl extends ServiceImpl<SickMapper, Sick> implements ISickService {

    @Autowired
    private SickMapper sickMapper;

    @Autowired
    private DataDictFeign dataDictFeign;

    @Autowired
    private ISickSearchRecordService sickSearchRecordService;

    @Resource(name = "searchRecordExecutor")
    private Executor searchRecordExecutor;

    @Autowired
    private IRuleService ruleService;

    @Autowired
    private IInsuranceSievingService insuranceSievingService;


    /***
    * @description 疾病表多条件组合
    * @param sickVO 疾病表
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Sick> queryWrapper(SickVO sickVO){
        QueryWrapper<Sick> queryWrapper = new QueryWrapper<>();
        //疾病Key查询
        if (!EmptyUtil.isNullOrEmpty(sickVO.getSickKey())) {
            queryWrapper.lambda().eq(Sick::getSickKey,sickVO.getSickKey());
        }
        //疾病名称查询
        if (!EmptyUtil.isNullOrEmpty(sickVO.getSickKeyName())) {
            queryWrapper.lambda().like(Sick::getSickKeyName,sickVO.getSickKeyName());
        }
        //疾病项值查询
        if (!EmptyUtil.isNullOrEmpty(sickVO.getSickVal())) {
            queryWrapper.lambda().eq(Sick::getSickVal,sickVO.getSickVal());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(sickVO.getRemake())) {
            queryWrapper.lambda().eq(Sick::getRemake,sickVO.getRemake());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(sickVO.getDataState())) {
            queryWrapper.lambda().eq(Sick::getDataState,sickVO.getDataState());
        }
        if (!EmptyUtil.isNullOrEmpty(sickVO.getSickType())) {
            queryWrapper.lambda().eq(Sick::getSickType,sickVO.getSickType());
        }
        if (!EmptyUtil.isNullOrEmpty(sickVO.getQuestion())) {
            queryWrapper.lambda().eq(Sick::getQuestion,sickVO.getQuestion());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Sick::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SickCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#sickVO.hashCode()")
    public Page<SickVO> findPage(SickVO sickVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Sick> SickPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Sick> queryWrapper = queryWrapper(sickVO);
            //执行分页查询
            Page<SickVO> sickVOPage = BeanConv.toPage(
                page(SickPage, queryWrapper), SickVO.class);
            //返回结果
            return sickVOPage;
        }catch (Exception e){
            log.error("疾病表分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = SickCacheConstant.BASIC,key ="#sickId")
    public SickVO findById(String sickId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(sickId),SickVO.class);
        }catch (Exception e){
            log.error("疾病表单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =SickCacheConstant.BASIC,key = "#result.id")})
    public SickVO save(SickVO sickVO) {
        try {
            //转换SickVO为Sick
            Sick sick = BeanConv.toBean(sickVO, Sick.class);
            boolean flag = save(sick);
            if (!flag){
                throw new RuntimeException("保存疾病表失败");
            }
            //转换返回对象SickVO
            SickVO sickVOHandler = BeanConv.toBean(sick, SickVO.class);
            return sickVOHandler;
        }catch (Exception e){
            log.error("保存疾病表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SickCacheConstant.BASIC,key = "#sickVO.id")})
    public Boolean update(SickVO sickVO) {
        try {
            //转换SickVO为Sick
            Sick sick = BeanConv.toBean(sickVO, Sick.class);
            boolean flag = updateById(sick);
            if (!flag){
                throw new RuntimeException("修改疾病表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改疾病表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SickCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = SickCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = SickCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除疾病表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除疾病表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = SickCacheConstant.LIST,key ="#sickVO.hashCode()")
    public List<SickVO> findList(SickVO sickVO) {
        try {
            //构建查询条件
            QueryWrapper<Sick> queryWrapper = queryWrapper(sickVO);
            //执行列表查询
            List<SickVO> sickVOs = BeanConv.toBeanList(list(queryWrapper),SickVO.class);
            return sickVOs;
        }catch (Exception e){
            log.error("疾病表列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.LIST_FAIL);
        }
    }

    /**
     * 获取所有的疾病类型
     * @return
     */
    @Override
    public List<DataDictVO> findSickType() {
        try{
            List<DataDictVO> res = dataDictFeign.findDataDictVOByParentKey(SickConstant.SICK_TYPE);
            return res;
        }catch (Exception e){
            log.error("疾病类型查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.SICK_TYPE_FAIL);
        }
    }

    /**
     * 搜索疾病
     * @param sickName
     * @return
     */
    @Override
    public List<SickVO> searchSick(String sickName,UserVO userVO) {
        try{
            SickVO sickVO = SickVO.builder().
                    dataState(SuperConstant.DATA_STATE_0).
                    sickKeyName(sickName).
                    build();
            //疾病名称
            if(StrUtil.isNotEmpty(sickName)){
                sickVO.setSickKeyName(sickName);
            }
            List<SickVO> res = findList(sickVO);
            //疾病搜索
            if(CollectionUtil.isNotEmpty(res)&&StrUtil.isNotEmpty(sickName)){
                //异步插入搜索记录
                searchRecordExecutor.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                List<SickSearchRecordVO> searchRecordVOList = new ArrayList<>();
                                for (SickVO index :res) {
                                    SickSearchRecordVO sickSearchRecordVO = SickSearchRecordVO.builder().
                                            content(index.getSickKey()).
                                            dataState(SuperConstant.DATA_STATE_0).
                                            build();
                                    searchRecordVOList.add(sickSearchRecordVO);
                                }
                                sickSearchRecordService.saveRecord(searchRecordVOList);
                            }
                        }
                );
            }
            return res;
        }catch (Exception e){
            log.error("疾病搜索异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.SICK_TYPE_FAIL);
        }
    }

    /**
     * 健康咨询
     * @param seekAdviceVO
     * @return
     */
    @Override
    public QuestionTemplateVO seekAdviceHealth(SeekAdviceVO seekAdviceVO) {
        try{
            QuestionTemplateVO res = new QuestionTemplateVO();
            Map<String,List<QuestionVO>> questions = new HashMap<>();
            if(ObjectUtil.isNull(seekAdviceVO)){
                throw new ProjectException(SickEnum.ADVICE_SICK_DATA_FAIL);
            }
            if(CollectionUtil.isEmpty(seekAdviceVO.getSickList())){
                throw new ProjectException(SickEnum.ADVICE_SICK_DATA_FAIL);
            }
            LambdaQueryWrapper<Sick> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Sick::getSickKey,seekAdviceVO.getSickList());
            queryWrapper.eq(Sick::getDataState,SuperConstant.DATA_STATE_0);
            //查询所有的疾病项
            List<SickVO> sickVOs = BeanConv.toBeanList(list(queryWrapper),SickVO.class);
            //根据疾病项中的内容构建问卷信息
            for (SickVO sickIndex: sickVOs) {
                List<QuestionVO> questionVO = null;
                if(StrUtil.isNotEmpty(sickIndex.getQuestion())){
                    questionVO = JSONUtil.toBean(sickIndex.getQuestion(), List.class);
                }
                questions.put(sickIndex.getSickKey(),questionVO);
            }
            res.setSicks(sickVOs);
            res.setQuestions(questions);
            return res;
        }catch (ProjectException e){
            throw e;
        }catch (Exception e){
            log.error("疾病搜索异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.SICK_TYPE_FAIL);
        }
    }


    /**
     * 提交答案
     * 提交数据到规则引擎
     * @param adviceHealthDTO
     * @return
     */
    @Override
    public AdviceHealthDTO submitQuestionChoose(AdviceHealthDTO adviceHealthDTO) {
        try{
            AdviceHealthDTO res = ruleService.submitQuestionChoose(adviceHealthDTO);
            //疾病名称替换
            List<String> sicks = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(res.getSicks())){
                LambdaQueryWrapper<Sick> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(Sick::getSickKey, res.getSicks());
                queryWrapper.eq(Sick::getDataState, SuperConstant.DATA_STATE_0);
                //查询所有的疾病项
                List<SickVO> sickVOs = BeanConv.toBeanList(list(queryWrapper), SickVO.class);
                sicks = sickVOs.stream().map(SickVO::getSickKeyName).collect(Collectors.toList());
                res.setSicks(sicks);
            }
            UserVO userVO = SubjectContent.getUserVO();
            //异步插入保险初筛记录
            List<String> finalSicks = sicks;
            searchRecordExecutor.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            InsuranceSievingVO insuranceSievingVO = new InsuranceSievingVO();
                            //根据结果返回
                            insuranceSievingVO.setResult(res.getResult());
                            insuranceSievingVO.setName(userVO.getRealName());
                            insuranceSievingVO.setSicksName(String.join(",", finalSicks));
                            insuranceSievingVO.setSicksKey(String.join(",", res.getSicks()));
                            insuranceSievingService.save(insuranceSievingVO);
                        }
                    }
            );
            //记录健康咨询日志

            return res;
        }catch (Exception e){
            log.error("保险问卷问答异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickEnum.SICK_TYPE_FAIL);
        }
    }

    /**
     * 常见疾病:
     * 脂肪肝,             肝硬化,        乙肝,         糖尿病,    甲状腺结节,      骨折,         急性肺炎,       新型冠状病毒肺炎，哮喘
     * disease_04_10,disease_04_11,disease_04_12,disease_03_06,disease_01_01,disease_02_01,disease_06_01,disease_06_03,disease_06_12
     *
     * @return
     */
    @Override
    public List<SickVO> commonDiseases() {
        try{
            List<String> sickNames = Arrays.asList("disease_04_10","disease_04_11","disease_04_12","disease_03_06","disease_01_01","disease_02_01","disease_06_01","disease_06_03","disease_06_12");
            LambdaQueryWrapper<Sick> queryWrapper = new LambdaQueryWrapper<Sick>();
            queryWrapper.in(Sick::getSickKey,sickNames);
            List res = list(queryWrapper);
            return res;
        }catch (Exception e){
            log.error("常见疾病搜索异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SickSearchRecordEnum.LIST_FAIL);
        }
    }
}
