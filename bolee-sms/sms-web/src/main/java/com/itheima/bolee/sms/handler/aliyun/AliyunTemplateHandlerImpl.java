package com.itheima.bolee.sms.handler.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsTemplateEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.handler.SmsTemplateHandler;
import com.itheima.bolee.sms.handler.aliyun.config.AliyunSmsConfig;
import com.itheima.bolee.sms.pojo.SmsTemplate;
import com.itheima.bolee.sms.service.ISmsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SmsTemplateAdapter.java
 * @Description 阿里云模板处理器接口
 */
@Slf4j
@Service("aliyunTemplateHandler")
public class AliyunTemplateHandlerImpl implements SmsTemplateHandler {

    @Autowired
    AliyunSmsConfig aliyunSmsConfig;

    @Autowired
    ISmsTemplateService smsTemplateService;

    @Override
    public SmsTemplate addSmsTemplate(SmsTemplateVO smsTemplateVO) {
        //查询是否添加过模板
        SmsTemplateVO smsTemplateHandler = smsTemplateService.findSmsTemplateByTemplateNameAndChannelLabel(
                smsTemplateVO.getTemplateName(),
                smsTemplateVO.getChannelLabel());
        //保存过则同步远程数据
        if (!EmptyUtil.isNullOrEmpty(smsTemplateHandler)){
            smsTemplateVO = BeanConv.toBean(smsTemplateHandler,SmsTemplateVO.class);
            QuerySmsTemplateResponse response = query(smsTemplateVO);
            //受理状态
            String code = response.getBody().getCode();
            String message = response.getBody().getMessage();
            if ("OK".equals(code)){
                Integer templateStatus =response.getBody().getTemplateStatus();
                smsTemplateVO.setTemplateCode(response.getBody().getTemplateCode());
                //审核通过
                if (templateStatus == 1) {
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                    smsTemplateVO.setAuditMsg("审核通过");
                //审核失败
                } else if (templateStatus == 2) {
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                    smsTemplateVO.setAuditMsg(response.getBody().getReason());
                } else {
                    log.info("阿里云模板：{},审核中", response.getBody().getTemplateCode());
                    smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
                    smsTemplateVO.setAuditMsg(response.getBody().getReason());
                }
                SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
                boolean flag = smsTemplateService.saveOrUpdate(smsTemplate);
                if (flag){
                    return smsTemplate;
                }
                throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
            }
        }
        //构建请求对象
        AddSmsTemplateRequest request = new AddSmsTemplateRequest();
        //模板类型
        request.setTemplateType(Integer.valueOf(smsTemplateVO.getSmsType()));
        //模板名称
        request.setTemplateName(smsTemplateVO.getTemplateName());
        //模板内容
        request.setTemplateContent(smsTemplateVO.getContent());
        //模板说明
        request.setRemark(smsTemplateVO.getRemark());
        //获得客户端
        Client client =aliyunSmsConfig.queryClient();
        //发起三方请求
        AddSmsTemplateResponse response = null;
        try {
            response = client.addSmsTemplate(request);
        } catch (Exception e) {
            log.error("请求添加阿里云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
        }
        //同步结果并保存
        String code = response.getBody().getCode();
        String message = response.getBody().getMessage();
        if ("OK".equals(code)){
            //受理成功
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsTemplateVO.setAcceptMsg("受理成功");
            //审核中
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsTemplateVO.setAuditMsg("审核中");
            //短信模板CODE。
            //您可以使用模板CODE通过
            //QuerySmsTemplate接口或短信服务控制台查看模板申请状态和结果。
            smsTemplateVO.setTemplateCode(response.getBody().getTemplateCode());
        }else {
            //受理失败
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_AUDIT_1);
            smsTemplateVO.setAcceptMsg(message);
        }
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        smsTemplate.setOtherConfig(JSONObject.toJSONString(smsTemplateVO.getOtherConfigs()));
        boolean flag = smsTemplateService.save(smsTemplate);
        if (flag){
            return smsTemplate;
        }
        throw new ProjectException(SmsTemplateEnum.CREATE_FAIL);
    }

    @Override
    public Boolean deleteSmsTemplate(SmsTemplateVO smsTemplateVO) {
        DeleteSmsTemplateRequest request = new DeleteSmsTemplateRequest();
        request.setTemplateCode(smsTemplateVO.getTemplateCode());
        Client client =aliyunSmsConfig.queryClient();
        DeleteSmsTemplateResponse response = null;
        try {
            response = client.deleteSmsTemplate(request);
        } catch (Exception e) {
            log.error("请求删除阿里云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.DELETE_FAIL);
        }
        //受理状态
        String code = response.getBody().getCode();
        String message = response.getBody().getMessage();
        return smsTemplateService.removeById(smsTemplateVO.getId());
    }

    @Override
    public SmsTemplate modifySmsTemplate(SmsTemplateVO smsTemplateVO) {
        //构建请求对象
        ModifySmsTemplateRequest request = new ModifySmsTemplateRequest();
        //模板类型
        request.setTemplateType(Integer.valueOf(smsTemplateVO.getSmsType()));
        //模板名称
        request.setTemplateName(smsTemplateVO.getTemplateName());
        //模板内容
        request.setTemplateContent(smsTemplateVO.getContent());
        //模板说明
        request.setRemark(smsTemplateVO.getRemark());
        //获得客户端
        Client client =aliyunSmsConfig.queryClient();
        ModifySmsTemplateResponse response = null;
        try {
            response = client.modifySmsTemplate(request);
        } catch (Exception e) {
            log.error("请求修改阿里云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.UPDATE_FAIL);
        }
        //同步结果并保存
        String code = response.getBody().getCode();
        String message = response.getBody().getMessage();
        if ("OK".equals(code)){
            //受理成功
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsTemplateVO.setAcceptMsg("受理成功");
            //审核中
            smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsTemplateVO.setAuditMsg("审核中");
            //短信模板CODE。
            //您可以使用模板CODE通过
            //QuerySmsTemplate接口或短信服务控制台查看模板申请状态和结果。
            smsTemplateVO.setTemplateCode(response.getBody().getTemplateCode());
        }else {
            //受理失败
            smsTemplateVO.setAcceptStatus(SmsConstant.STATUS_AUDIT_1);
            smsTemplateVO.setAcceptMsg( message);
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

    private QuerySmsTemplateResponse query(SmsTemplateVO smsTemplateVO) {
        QuerySmsTemplateRequest request = new QuerySmsTemplateRequest();
        request.setTemplateCode(smsTemplateVO.getTemplateCode());
        Client client =aliyunSmsConfig.queryClient();
        QuerySmsTemplateResponse response = null;
        try {
            response = client.querySmsTemplate(request);
        } catch (Exception e) {
            log.error("请求查询阿里云模板出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
        return response;
    }

    @Override
    public SmsTemplate querySmsTemplate(SmsTemplateVO smsTemplateVO) {
        QuerySmsTemplateResponse response =query(smsTemplateVO);
        //受理状态
        String code = response.getBody().getCode();
        String message = response.getBody().getMessage();
        SmsTemplate smsTemplate = BeanConv.toBean(smsTemplateVO, SmsTemplate.class);
        if ("OK".equals(code)){
            Integer templateStatus =response.getBody().getTemplateStatus();
            //审核通过
            if (templateStatus == 1) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                smsTemplateVO.setAuditMsg("审核通过");
                smsTemplateService.updateById(smsTemplate);
            //审核失败
            } else if (templateStatus == 2) {
                smsTemplateVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                smsTemplateVO.setAuditMsg(response.getBody().getReason());
                smsTemplateService.updateById(smsTemplate);
            } else {
                log.info("阿里云模板：{},审核中", response.getBody().getTemplateCode());
            }
        }else {
            log.warn("受理查询阿里云模板出错：{}", message);
            throw new ProjectException(SmsTemplateEnum.SELECT_FAIL);
        }
        return smsTemplate;
    }
}
