package com.itheima.bolee.sms.handler.tencent;

import com.alibaba.fastjson.JSONObject;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsTemplateEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.handler.SmsTemplateHandler;
import com.itheima.bolee.sms.handler.tencent.config.TencentSmsConfig;
import com.itheima.bolee.sms.pojo.SmsTemplate;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TencentTemplateHandlerImpl.java
 * @Description 腾讯云模板处理器接口
 */
@Slf4j
@Service("tencentTemplateHandler")
public class TencentTemplateHandlerImpl implements SmsTemplateHandler {

    @Autowired
    TencentSmsConfig tencentSmsConfig;

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Override
    public SmsTemplate addSmsTemplate(SmsTemplateVO smsTemplateVO) {
        //查询是否添加过模板
        SmsTemplateVO smsTemplateHandler = smsTemplateService.findSmsTemplateByTemplateNameAndChannelLabel(
                smsTemplateVO.getTemplateName(),
                smsTemplateVO.getChannelLabel());
        if (!EmptyUtil.isNullOrEmpty(smsTemplateHandler)){
            smsTemplateVO = BeanConv.toBean(smsTemplateHandler,SmsTemplateVO.class);
            DescribeSmsTemplateListResponse response = query(smsTemplateVO);
            //处理结果
            DescribeTemplateListStatus[] describeTemplateStatusSet = response.getDescribeTemplateStatusSet();
            response.getDescribeTemplateStatusSet();
            if (!EmptyUtil.isNullOrEmpty(describeTemplateStatusSet)){
                DescribeTemplateListStatus describeTemplateListStatus = describeTemplateStatusSet[0];
                Long statusCode = describeTemplateListStatus.getStatusCode();
                //审核通过
                if (statusCode == 0) {
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                    smsTemplateVO.setAuditMsg("审核通过");
                //审核失败
                } else if (statusCode == -1) {
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                    smsTemplateVO.setAuditMsg(describeTemplateListStatus.getReviewReply());
                } else {
                    log.info("腾讯云模板：{},审核中", describeTemplateListStatus.getTemplateId());
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
                    smsTemplateVO.setAuditMsg(describeTemplateListStatus.getReviewReply());
                }
                SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
                smsTemplate.setOtherConfig(JSONObject.toJSONString(smsTemplateVO.getOtherConfigs()));
                boolean flag = smsTemplateService.saveOrUpdate(smsTemplate);
                if (flag){
                    return smsTemplate;
                }
                throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
            }
        }
        // 实例化一个请求对象,每个接口都会对应一个request对象
        AddSmsTemplateRequest request = new AddSmsTemplateRequest();
        //模板名称
        request.setTemplateName(smsTemplateVO.getTemplateName());
        //模板内容
        request.setTemplateContent(smsTemplateVO.getContent());
        //短信类型，0表示普通短信, 1表示营销短信。
        request.setSmsType(Long.valueOf(smsTemplateVO.getSmsType()));
        //是否国际/港澳台短信：
        //0：表示国内短信。
        //1：表示国际/港澳台短信。
        request.setInternational(Long.valueOf(smsTemplateVO.getInternational()));
        //模板备注，例如申请原因，使用场景等
        request.setRemark(smsTemplateVO.getRemark());
        // 返回的resp是一个AddSmsTemplateResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        AddSmsTemplateResponse response = null;
        try {
            response = smsClient.AddSmsTemplate(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求添加腾讯云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
        }
        //受理状态
        String templateId = response.getAddTemplateStatus().getTemplateId();
        if (!EmptyUtil.isNullOrEmpty(templateId)) {
            //受理成功
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsTemplateVO.setAcceptMsg("受理成功");
            //审核中
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsTemplateVO.setAuditMsg("审核中");
            //短信模板CODE。
            //您可以使用模板CODE通过
            smsTemplateVO.setTemplateCode(templateId);
            //QuerySmsTemplate接口或短信服务控制台查看模板申请状态和结果。
        } else {
            //受理失败
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsTemplateVO.setAcceptMsg("受理失败");
        }
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        boolean flag = smsTemplateService.save(smsTemplate);
        if (flag){
            return smsTemplate;
        }
        throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
    }

    @Override
    public Boolean deleteSmsTemplate(SmsTemplateVO smsTemplateVO) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeleteSmsTemplateRequest request = new DeleteSmsTemplateRequest();
        request.setTemplateId(Long.valueOf(smsTemplateVO.getTemplateCode()));
        // 返回的resp是一个DeleteSmsTemplateResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        DeleteSmsTemplateResponse response = null;
        try {
            response = smsClient.DeleteSmsTemplate(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求删除腾讯云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.DELETE_FAIL);
        }
        return smsTemplateService.removeById(smsTemplateVO.getId());
    }

    @Override
    public SmsTemplate modifySmsTemplate(SmsTemplateVO smsTemplateVO) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        ModifySmsTemplateRequest request = new ModifySmsTemplateRequest();
        //模板名称
        request.setTemplateName(smsTemplateVO.getTemplateName());
        //模板code
        request.setTemplateId(Long.valueOf(smsTemplateVO.getTemplateCode()));
        //模板内容
        request.setTemplateContent(smsTemplateVO.getContent());
        //短信类型，0表示普通短信, 1表示营销短信。
        request.setSmsType(Long.valueOf(smsTemplateVO.getSmsType()));
        //是否国际/港澳台短信：
        //0：表示国内短信。
        //1：表示国际/港澳台短信。
        request.setInternational(Long.valueOf(smsTemplateVO.getInternational()));
        //模板备注，例如申请原因，使用场景等
        request.setRemark(smsTemplateVO.getRemark());
        // 返回的resp是一个ModifySmsTemplateResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        ModifySmsTemplateResponse response = null;
        try {
            response = smsClient.ModifySmsTemplate(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求修改腾讯云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.UPDATE_FAIL);
        }
        Long templateId = response.getModifyTemplateStatus().getTemplateId();
        if (!EmptyUtil.isNullOrEmpty(templateId)) {
            //受理成功
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsTemplateVO.setAcceptMsg("受理成功");
            //审核中
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsTemplateVO.setAuditMsg("审核中");
            smsTemplateVO.setTemplateCode(String.valueOf(templateId));
        }else {
            //受理失败
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsTemplateVO.setAcceptMsg("受理失败");
            //重置审核状态
            smsTemplateVO.setAuditStatus(null);
            smsTemplateVO.setAuditMsg(null);
            smsTemplateVO.setTemplateCode(null);
        }
        //本地持久化
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        smsTemplate.setOtherConfig(JSONObject.toJSONString(smsTemplateVO.getOtherConfigs()));
        smsTemplateService.updateById(smsTemplate);
        return smsTemplate;
    }

    private DescribeSmsTemplateListResponse query(SmsTemplateVO smsTemplateVO) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeSmsTemplateListRequest request = new DescribeSmsTemplateListRequest();
        //模板 ID 数组。 注：默认数组最大长度100。
        List<Long> templateCodes = new ArrayList<>();
        templateCodes.add(Long.valueOf(smsTemplateVO.getTemplateCode()));
        request.setTemplateIdSet(templateCodes.stream().toArray(Long[]::new));
        request.setInternational(Long.valueOf(smsTemplateVO.getInternational()));
        // 返回的resp是一个DescribeSmsTemplateListResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        DescribeSmsTemplateListResponse response = null;
        try {
            response = smsClient.DescribeSmsTemplateList(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求查询腾讯云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
        return response;
    }

    @Override
    public SmsTemplate querySmsTemplate(SmsTemplateVO smsTemplateVO) {
        DescribeSmsTemplateListResponse response = query(smsTemplateVO);
        //处理结果
        DescribeTemplateListStatus[] describeTemplateStatusSet = response.getDescribeTemplateStatusSet();
        response.getDescribeTemplateStatusSet();
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        if (!EmptyUtil.isNullOrEmpty(describeTemplateStatusSet)){
            DescribeTemplateListStatus describeTemplateListStatus = describeTemplateStatusSet[0];
            Long statusCode = describeTemplateListStatus.getStatusCode();
            //审核通过
            if (statusCode == 0) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                smsTemplateVO.setAuditMsg("审核通过");
                smsTemplateService.updateById(smsTemplate);
            //审核失败
            } else if (statusCode == -1) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                smsTemplateVO.setAuditMsg(describeTemplateListStatus.getReviewReply());
                smsTemplateService.updateById(smsTemplate);
            } else {
                log.info("腾讯云模板：{},审核中", describeTemplateListStatus.getTemplateId());
            }
        }else {
            log.warn("受理查询腾讯云签名出错");
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
        return smsTemplate;
    }
}
