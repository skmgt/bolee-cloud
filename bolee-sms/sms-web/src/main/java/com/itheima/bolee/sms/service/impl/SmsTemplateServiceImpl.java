package com.itheima.bolee.sms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsCacheConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSignEnum;
import com.itheima.bolee.framework.commons.enums.sms.SmsTemplateEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.adapter.SmsTemplateAdapter;
import com.itheima.bolee.sms.mapper.SmsTemplateMapper;
import com.itheima.bolee.sms.pojo.SmsTemplate;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description：模板表 服务实现类
 */
@Slf4j
@Service
public class SmsTemplateServiceImpl extends ServiceImpl<SmsTemplateMapper, SmsTemplate> implements ISmsTemplateService {

    @Autowired
    SmsTemplateAdapter smsTemplateAdapter;
    @Override
    public List<SmsTemplateVO> findSmsTemplateByTemplateNo(String templateNo) {
        try {
            QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsTemplate::getTemplateNo,templateNo)
                    .eq(SmsTemplate::getDataState, SuperConstant.DATA_STATE_0)
                    .eq(SmsTemplate::getAuditStatus, SmsConstant.STATUS_AUDIT_0);
            return BeanConv.toBeanList(list(queryWrapper),SmsTemplateVO.class);
        } catch (Exception e) {
            log.error("查询签名状态异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
    }

    @Override
    public Page<SmsTemplateVO> findSmsTemplateVOPage(SmsTemplateVO smsTemplateVO, int pageNum, int pageSize) {
        //构建分页对象
        Page<SmsTemplate> page = new Page<>(pageNum,pageSize);
        //构建查询条件
        QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
        //按模板名称查询
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVO.getTemplateName())) {
            queryWrapper.lambda().likeRight(SmsTemplate::getTemplateName,smsTemplateVO.getTemplateName());
        }
        //按模板名称查询
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVO.getChannelLabel())) {
            queryWrapper.lambda().eq(SmsTemplate::getChannelLabel,smsTemplateVO.getChannelLabel());
        }
        //按模板发送状态查询
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVO.getAcceptStatus())) {
            queryWrapper.lambda().eq(SmsTemplate::getAcceptStatus,smsTemplateVO.getAcceptStatus());
        }
        //按模板审核状态查询
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVO.getAuditStatus())) {
            queryWrapper.lambda().eq(SmsTemplate::getAuditStatus,smsTemplateVO.getAuditStatus());
        }
        //按模板状态查询
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVO.getDataState())) {
            queryWrapper.lambda().eq(SmsTemplate::getDataState,smsTemplateVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SmsTemplate::getCreateTime);
        //执行分页查询
        Page<SmsTemplateVO> smsTemplateVOPage = BeanConv.toPage(page(page, queryWrapper), SmsTemplateVO.class);
        if (!EmptyUtil.isNullOrEmpty(smsTemplateVOPage.getRecords())){
            smsTemplateVOPage.getRecords().forEach(n->{
                List <OtherConfigVO> list = JSONArray.parseArray(n.getOtherConfig(), OtherConfigVO.class);
                n.setOtherConfigs(list);
            });
        }
        //返回结果
        return smsTemplateVOPage;
    }

    @Override
    public SmsTemplateVO findSmsTemplateByTemplateNameAndChannelLabel(String templateName, String channelLabel) {
        try {
            QueryWrapper<SmsTemplate> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsTemplate::getTemplateName,templateName)
                    .eq(SmsTemplate::getChannelLabel, channelLabel);
            return BeanConv.toBean(getOne(queryWrapper),SmsTemplateVO.class);
        }catch (Exception e){
            log.error("查询签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_TEMPLATE,allEntries = true)},
            put={@CachePut(value =SmsCacheConstant.PREFIX_TEMPLATE,key = "#result.id")})
    public SmsTemplateVO addSmsTemplate(SmsTemplateVO smsTemplateVO) throws ProjectException {
        try {
            return smsTemplateAdapter.addSmsTemplate(smsTemplateVO);
        } catch (Exception e) {
            log.error("添加签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_TEMPLATE,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_TEMPLATE,allEntries = true)})
    public Boolean deleteSmsTemplate(String[] checkedIds) throws ProjectException {
        try {
            return smsTemplateAdapter.deleteSmsTemplate(checkedIds);
        } catch (Exception e) {
            log.error("删除签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.DELETE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_TEMPLATE,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_TEMPLATE,key = "#smsTemplateVO.id")})
    public SmsTemplateVO modifySmsTemplate(SmsTemplateVO smsTemplateVO) throws ProjectException {
        try {
            return smsTemplateAdapter.modifySmsTemplate(smsTemplateVO);
        } catch (Exception e) {
            log.error("修改签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Cacheable(value =SmsCacheConstant.PREFIX_TEMPLATE,key = "#smsTemplateVO.id")
    public SmsTemplateVO querySmsTemplate(SmsTemplateVO smsTemplateVO) throws ProjectException {
        try {
            return smsTemplateAdapter.querySmsTemplate(smsTemplateVO);
        } catch (Exception e) {
            log.error("查询签名状态异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
    }

    @Override
    public SmsTemplateVO disableEnable(SmsTemplateVO smsTemplateVO) {
        try {
            SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
            updateById(smsTemplate);
            return BeanConv.toBean(smsTemplate, SmsTemplateVO.class);
        } catch (Exception e) {
            log.error("修改签名状态异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.UPDATE_FAIL);
        }
    }
}
