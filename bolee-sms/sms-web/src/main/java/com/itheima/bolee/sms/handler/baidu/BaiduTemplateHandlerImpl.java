package com.itheima.bolee.sms.handler.baidu;

import com.alibaba.fastjson.JSONObject;
import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.model.v3.*;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsTemplateEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.sms.handler.SmsTemplateHandler;
import com.itheima.bolee.sms.handler.baidu.config.BaiduSmsConfig;
import com.itheima.bolee.sms.pojo.SmsTemplate;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName BaiduTemplateHandlerImpl.java
 * @Description 百度模板审核
 */
@Slf4j
@Service("baiduTemplateHandler")
public class BaiduTemplateHandlerImpl implements SmsTemplateHandler {

    @Autowired
    BaiduSmsConfig baiduSmsConfig;

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
            GetTemplateResponse response = query(smsTemplateVO);
            //处理返回结果
            String status = response.getStatus();
            //审核通过
            if ("READY".equals(status)) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                smsTemplateVO.setAuditMsg("审核通过");
                //审核失败
            } else if ("REJECTED".equals(status) || "ABORTED".equals(status)) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                smsTemplateVO.setAuditMsg(response.getReview());
            } else {
                log.info("百度云模板：{},审核中", response.getTemplateId());
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
                smsTemplateVO.setAuditMsg(response.getReview());
            }
            SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
            boolean flag = smsTemplateService.saveOrUpdate(smsTemplate);
            if (flag){
                return smsTemplate;
            }
            throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
        }
        CreateTemplateRequest request = new CreateTemplateRequest()
            //模板名称
            .withName(smsTemplateVO.getTemplateName())
            //模板内容
            .withContent(smsTemplateVO.getContent())
            //短信类型
            .withSmsType(smsTemplateVO.getSmsType())
            //适用国家类型
            //DOMESTIC：国内
            //INTERNATIONAL：国际/港澳台
            //GLOBAL：全球
            .withCountryType(smsTemplateVO.getInternational())
            //模板描述
            .withDescription(smsTemplateVO.getRemark());
        SmsClient client = baiduSmsConfig.queryClient();
        CreateTemplateResponse response = client.createTemplate(request);
        String status = response.getStatus();
        if ("SUBMITTED".equals(status)) {
            //受理成功
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsTemplateVO.setAcceptMsg("受理成功");
            //审核中
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsTemplateVO.setAuditMsg("审核中");
            smsTemplateVO.setTemplateCode(response.getTemplateId());

        } else {
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsTemplateVO.setAcceptMsg(response.getStatus());
        }
        //本地持久化
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        smsTemplate.setOtherConfig(JSONObject.toJSONString(smsTemplateVO.getOtherConfigs()));
        boolean flag = smsTemplateService.save(smsTemplate);
        if (flag){
            return smsTemplate;
        }
        return null;
    }

    @Override
    public Boolean deleteSmsTemplate(SmsTemplateVO smsTemplateVO) {
        DeleteTemplateRequest request =new DeleteTemplateRequest()
            .withTemplateId(smsTemplateVO.getTemplateCode());
        SmsClient client = baiduSmsConfig.queryClient();
        client.deleteTemplate(request);
        return smsTemplateService.removeById(smsTemplateVO.getId());
    }

    @Override
    public SmsTemplate modifySmsTemplate(SmsTemplateVO smsTemplateVO) {
        ModifyTemplateRequest request = new ModifyTemplateRequest()
            //模板id
            .withTemplateId(smsTemplateVO.getTemplateCode())
            //模板名称
            .withName(smsTemplateVO.getTemplateName())
            //模板内容
            .withContent(smsTemplateVO.getContent())
            //短信类型
            .withSmsType(smsTemplateVO.getSmsType())
            //适用国家类型
            //DOMESTIC：国内
            //INTERNATIONAL：国际/港澳台
            //GLOBAL：全球
            .withCountryType(smsTemplateVO.getInternational())
            //模板描述
            .withDescription(smsTemplateVO.getRemark());
        SmsClient client = baiduSmsConfig.queryClient();
        client.modifyTemplate(request);
        //受理成功
        smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
        smsTemplateVO.setAcceptMsg("受理成功");
        //审核中
        smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
        smsTemplateVO.setAuditMsg("审核中");
        //本地持久化
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        smsTemplate.setOtherConfig(JSONObject.toJSONString(smsTemplateVO.getOtherConfigs()));
        smsTemplateService.updateById(smsTemplate);
        return smsTemplate;
    }

    private GetTemplateResponse query(SmsTemplateVO smsTemplateVO){
        GetTemplateRequest request = new GetTemplateRequest()
                .withTemplateId(smsTemplateVO.getTemplateCode());
        SmsClient client = baiduSmsConfig.queryClient();
        return client.getTemplate(request);
    }

    @Override
    public SmsTemplate querySmsTemplate(SmsTemplateVO smsTemplateVO) {
        GetTemplateResponse response = query(smsTemplateVO);
        //处理返回结果
        String status = response.getStatus();
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        //审核通过
        if ("READY".equals(status)) {
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
            smsTemplateVO.setAuditMsg("审核通过");
            smsTemplateService.updateById(smsTemplate);
            //审核失败
        } else if ("REJECTED".equals(status) || "ABORTED".equals(status)) {
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
            smsTemplateVO.setAuditMsg(response.getReview());
            smsTemplateService.updateById(smsTemplate);
        } else {
            log.info("阿里云模板：{},审核中", response.getTemplateId());
        }
        return smsTemplate;
    }
}
