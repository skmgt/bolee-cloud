package com.itheima.bolee.sms.handler.baidu;

import com.baidubce.services.sms.SmsClient;
import com.baidubce.services.sms.model.v3.*;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSignEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.sms.handler.SmsSignHandler;
import com.itheima.bolee.sms.handler.baidu.config.BaiduSmsConfig;
import com.itheima.bolee.sms.pojo.SmsSign;
import com.itheima.bolee.sms.service.ISmsSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName BaiduSmsSignHandlerImpl.java
 * @Description 百度签名审核
 */
@Slf4j
@Service("baiduSmsSignHandler")
public class BaiduSmsSignHandlerImpl implements SmsSignHandler {

    @Autowired
    BaiduSmsConfig baiduSmsConfig;

    @Autowired
    ISmsSignService smsSignService;

    @Override
    public SmsSign addSmsSign(SmsSignVO smsSignVO) {
        //查询当前签名是否保存过
        SmsSignVO smsSignHandler = smsSignService.findSmsSignBySignNameAndChannelLabel
                (smsSignVO.getSignName(),smsSignVO.getChannelLabel());
        if (!EmptyUtil.isNullOrEmpty(smsSignHandler)){
            smsSignVO = BeanConv.toBean(smsSignHandler,SmsSignVO.class);
            //查询当前签名在远程的信息
            GetSignatureResponse getSignatureResponse = query(smsSignVO);
            //处理返回结果
            String getSignatureStatus = getSignatureResponse.getStatus();
            //审核通过
            if ("READY".equals(getSignatureStatus)) {
                smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                smsSignVO.setAuditMsg("审核通过");
                //审核失败
            } else if ("REJECTED".equals(getSignatureStatus) || "ABORTED".equals(getSignatureStatus)) {
                smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                smsSignVO.setAuditMsg(getSignatureResponse.getReview());
            } else {
                smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
                smsSignVO.setAuditMsg(getSignatureResponse.getReview());
            }
            SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
            boolean flag = smsSignService.saveOrUpdate(smsSign);
            if (flag){
                return smsSign;
            }
            throw new ProjectException(SmsSignEnum.CREATE_FAIL);
        }
        CreateSignatureRequest request = new CreateSignatureRequest()
            //签名内容
            .withContent(smsSignVO.getSignName())
            //签名类型。
            //Enterprise：企业
            //MobileApp：移动应用名称
            //Web：工信部备案的网站名称
            //WeChatPublic：微信公众号名称
            //Brand：商标名称
            //Else：其他
            .withContentType(smsSignVO.getSignType())
            //签名适用的国家类型
            //DOMESTIC：国内
            //INTERNATIONAL：国际/港澳台
            //GLOBAL：全球均适用
            //默认为DOMESTIC
            .withCountryType(smsSignVO.getInternational())
            //对于签名的描述
            .withDescription(smsSignVO.getRemark())
            //签名的证明文件经过base64编码后的字符串。文件大小不超过2MB。
            .withSignatureFileBase64(smsSignVO.getProofImage())
            //签名证明文件的格式，目前支持JPG、PNG、JPEG三种格式
            .withSignatureFileFormat(smsSignVO.getProofType().replace(".",""));
        SmsClient client = baiduSmsConfig.queryClient();
        CreateSignatureResponse response = client.createSignature(request);
        String status = response.getStatus();
        if ("SUBMITTED".equals(status)) {
            //受理成功
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsSignVO.setAcceptMsg("受理成功");
            //审核中
            smsSignVO.setAuditStatus(SmsConstant.STATUS_ACCEPT_2);
            smsSignVO.setAuditMsg("审核中");
            smsSignVO.setSignCode(response.getSignatureId());

        } else {
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsSignVO.setAcceptMsg(response.getStatus());
        }
        //本地持久化
        SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
        boolean flag = smsSignService.save(smsSign);
        if (flag){
            return smsSign;
        }
        throw new ProjectException(SmsSignEnum.CREATE_FAIL);
    }

    @Override
    public Boolean deleteSmsSign(SmsSignVO smsSignVO) {
        DeleteSignatureRequest request = new DeleteSignatureRequest()
            //短信模板唯一识别码
            .withSignatureId(smsSignVO.getSignCode());
        SmsClient client = baiduSmsConfig.queryClient();
        client.deleteSignature(request);
        return smsSignService.removeById(smsSignVO.getId());
    }

    @Override
    public SmsSign modifySmsSign(SmsSignVO smsSignVO) {
        ModifySignatureRequest request = new ModifySignatureRequest()
            .withSignatureId(smsSignVO.getSignCode())
            //签名内容
            .withContent(smsSignVO.getSignName())
            //签名类型。
            //Enterprise：企业
            //MobileApp：移动应用名称
            //Web：工信部备案的网站名称
            //WeChatPublic：微信公众号名称
            //Brand：商标名称
            //Else：其他
            .withContentType(smsSignVO.getSignType())
            //签名适用的国家类型
            //DOMESTIC：国内
            //INTERNATIONAL：国际/港澳台
            //GLOBAL：全球均适用
            //默认为DOMESTIC
            .withCountryType(smsSignVO.getInternational())
            //对于签名的描述
            .withDescription(smsSignVO.getRemark())
            //签名的证明文件经过base64编码后的字符串。文件大小不超过2MB。
            .withSignatureFileBase64(smsSignVO.getProofImage())
            //签名证明文件的格式，目前支持JPG、PNG、JPEG三种格式
            .withSignatureFileFmt(smsSignVO.getProofType().replace(",",""));
        SmsClient client = baiduSmsConfig.queryClient();
        client.modifySignature(request);
        //受理成功
        smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
        smsSignVO.setAcceptMsg("受理成功");
        //审核中
        smsSignVO.setAuditStatus(SmsConstant.STATUS_ACCEPT_2);
        smsSignVO.setAuditMsg("审核中");
        //本地持久化
        SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
        smsSignService.updateById(smsSign);
        return smsSign;
    }

    private GetSignatureResponse query(SmsSignVO smsSignVO) {
        GetSignatureRequest request = new GetSignatureRequest();
        //查询
        request.setSignatureId(smsSignVO.getSignCode());
        SmsClient client = baiduSmsConfig.queryClient();
        return client.getSignature(request);
    }

    @Override
    public SmsSign querySmsSign(SmsSignVO smsSignVO) {
        GetSignatureResponse response = query(smsSignVO);
        //处理返回结果
        String status = response.getStatus();
        SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
        //审核通过
        if ("READY".equals(status)) {
            smsSignVO.setAuditStatus(SmsConstant.STATUS_ACCEPT_0);
            smsSignVO.setAuditMsg("审核通过");
            smsSignService.updateById(smsSign);
            //审核失败
        } else if ("REJECTED".equals(status) || "ABORTED".equals(status)) {
            smsSignVO.setAuditStatus(SmsConstant.STATUS_ACCEPT_1);
            smsSignVO.setAuditMsg(response.getReview());
            smsSignService.updateById(smsSign);
        } else {
            log.info("百度云签名：{},审核中", response.getSignatureId());
        }
        return smsSign;
    }
}
