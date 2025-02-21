package com.itheima.bolee.sms.handler.tencent;

import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSignEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.handler.SmsSignHandler;
import com.itheima.bolee.sms.handler.tencent.config.TencentSmsConfig;
import com.itheima.bolee.sms.pojo.SmsSign;
import com.itheima.bolee.sms.service.ISmsSignService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TencentSmsSignHandlerImpl.java
 * @Description 阿里云签名处理器接口
 */
@Slf4j
@Service("tencentSmsSignHandler")
public class TencentSmsSignHandlerImpl implements SmsSignHandler {

    @Autowired
    TencentSmsConfig tencentSmsConfig;

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
            DescribeSmsSignListResponse querySmsSignResponse = query(smsSignVO);
            //处理结果
            DescribeSignListStatus[] describeSignListStatusSet = querySmsSignResponse.getDescribeSignListStatusSet();
            if (!EmptyUtil.isNullOrEmpty(describeSignListStatusSet)){
                DescribeSignListStatus describeSignListStatus = describeSignListStatusSet[0];
                Long statusCode = describeSignListStatus.getStatusCode();
                //审核通过
                if (statusCode==0){
                    smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                    smsSignVO.setAuditMsg("审核通过");
                //审核失败
                }else if (statusCode==-1){
                    smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                    smsSignVO.setAuditMsg(describeSignListStatus.getReviewReply());
                }else {
                    smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
                    smsSignVO.setAuditMsg(describeSignListStatus.getReviewReply());
                }
                SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
                boolean flag = smsSignService.saveOrUpdate(smsSign);
                if (flag){
                    return smsSign;
                }
                throw new ProjectException(SmsSignEnum.CREATE_FAIL);
            }
        }
        // 实例化一个请求对象,每个接口都会对应一个request对象
        AddSmsSignRequest request = new AddSmsSignRequest();
        //签名名称
        request.setSignName(smsSignVO.getSignName());
        //签名类型。其中每种类型后面标注了其可选的 DocumentType（证明类型）：
        //0：公司，可选 DocumentType 有（0，1，2，3）。
        //1：APP，可选 DocumentType 有（0，1，2，3，4） 。
        //2：网站，可选 DocumentType 有（0，1，2，3，5）。
        //3：公众号或者小程序，可选 DocumentType 有（0，1，2，3，6）。
        //4：商标，可选 DocumentType 有（7）。
        //5：政府/机关事业单位/其他机构，可选 DocumentType 有（2，3）。
        request.setSignType(Long.valueOf(smsSignVO.getSignType()));
        //证明类型：
        //0：三证合一。
        //1：企业营业执照。
        //2：组织机构代码证书。
        //3：社会信用代码证书。
        //4：应用后台管理截图（个人开发APP）。
        //5：网站备案后台截图（个人开发网站）。
        //6：小程序设置页面截图（个人认证小程序）。
        //7：商标注册书
        request.setDocumentType(Long.valueOf(smsSignVO.getDocumentType()));
        //是否国际/港澳台短信：
        //0：表示国内短信。
        //1：表示国际/港澳台短信。
        request.setInternational(Long.valueOf(smsSignVO.getInternational()));
        //签名用途：0：自用。1：他用。
        request.setSignPurpose(Long.valueOf(smsSignVO.getSignPurpose()));
        //签名对应的资质证明图片需先进行 base64 编码格式转换
        request.setProofImage(smsSignVO.getProofImage());
        //签名的申请备注。
        request.setRemark(smsSignVO.getRemark());
        // 返回的resp是一个AddSmsSignResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        AddSmsSignResponse response = null;
        try {
            response = smsClient.AddSmsSign(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求添加腾讯云签名出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.CREATE_FAIL);
        }
        Long signId = response.getAddSignStatus().getSignId();
        if (!EmptyUtil.isNullOrEmpty(signId)){
            //受理成功
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsSignVO.setAcceptMsg("受理成功");
            //审核中
            smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsSignVO.setAuditMsg("审核中");
            smsSignVO.setSignCode(String.valueOf(signId));
        }else {
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsSignVO.setAcceptMsg("受理失败");
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
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeleteSmsSignRequest request = new DeleteSmsSignRequest();
        request.setSignId(Long.valueOf(smsSignVO.getSignCode()));
        // 返回的resp是一个DeleteSmsSignResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        DeleteSmsSignResponse response = null;
        try {
            response = smsClient.DeleteSmsSign(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求删除腾讯云签名出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.DELETE_FAIL);
        }
        response.getDeleteSignStatus().getDeleteStatus();
        return smsSignService.removeById(smsSignVO.getId());
    }

    @Override
    public SmsSign modifySmsSign(SmsSignVO smsSignVO) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        ModifySmsSignRequest request = new ModifySmsSignRequest();
        //签名编号
        request.setSignId(Long.valueOf(smsSignVO.getSignCode()));
        //签名名称
        request.setSignName(smsSignVO.getSignName());
        //签名类型。其中每种类型后面标注了其可选的 DocumentType（证明类型）：
        //0：公司，可选 DocumentType 有（0，1，2，3）。
        //1：APP，可选 DocumentType 有（0，1，2，3，4） 。
        //2：网站，可选 DocumentType 有（0，1，2，3，5）。
        //3：公众号或者小程序，可选 DocumentType 有（0，1，2，3，6）。
        //4：商标，可选 DocumentType 有（7）。
        //5：政府/机关事业单位/其他机构，可选 DocumentType 有（2，3）。
        request.setSignType(Long.valueOf(smsSignVO.getSignType()));
        //证明类型：
        //0：三证合一。
        //1：企业营业执照。
        //2：组织机构代码证书。
        //3：社会信用代码证书。
        //4：应用后台管理截图（个人开发APP）。
        //5：网站备案后台截图（个人开发网站）。
        //6：小程序设置页面截图（个人认证小程序）。
        //7：商标注册书
        request.setDocumentType(Long.valueOf(smsSignVO.getDocumentType()));
        //是否国际/港澳台短信：
        //0：表示国内短信。
        //1：表示国际/港澳台短信。
        request.setInternational(Long.valueOf(smsSignVO.getInternational()));
        //签名用途：
        //0：自用。
        //1：他用。
        request.setSignPurpose(Long.valueOf(smsSignVO.getSignPurpose()));
        //签名对应的资质证明图片需先进行 base64 编码格式转换
        request.setProofImage(smsSignVO.getProofImage());
        //签名的申请备注。
        request.setRemark(smsSignVO.getRemark());
        // 返回的resp是一个ModifySmsSignResponse的实例，与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        ModifySmsSignResponse response = null;
        try {
            response = smsClient.ModifySmsSign(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求修改腾讯云签名出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.UPDATE_FAIL);
        }
        Long signId = response.getModifySignStatus().getSignId();
        if (!EmptyUtil.isNullOrEmpty(signId)){
            //受理成功
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_0);
            smsSignVO.setAcceptMsg("受理成功");
            //审核中
            smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_2);
            smsSignVO.setAuditMsg("审核中");
            smsSignVO.setSignCode(String.valueOf(signId));
        }else {
            //受理失败
            smsSignVO.setAcceptStatus(SmsConstant.STATUS_ACCEPT_1);
            smsSignVO.setAcceptMsg("受理失败");
            //重置审核状态
            smsSignVO.setAuditStatus(null);
            smsSignVO.setAuditMsg(null);
            smsSignVO.setSignCode(null);
        }
        //本地持久化
        SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
        smsSignService.updateById(smsSign);
        return smsSign;
    }

    private DescribeSmsSignListResponse query(SmsSignVO smsSignVO) {
        //实例化一个请求对象,每个接口都会对应一个request对象
        DescribeSmsSignListRequest request = new DescribeSmsSignListRequest();
        //是否国际/港澳台短信： 0：表示国内短信。 1：表示国际/港澳台短信。
        request.setInternational(Long.valueOf(smsSignVO.getInternational()));
        //签名ID数组。 注：默认数组最大长度100。
        List<Long> signCodes = new ArrayList<>();
        signCodes.add(Long.valueOf(smsSignVO.getSignCode()));
        request.setSignIdSet(signCodes.stream().toArray(Long[]::new));
        //返回的resp是一个DescribeSmsSignListResponse的实例与请求对象对应
        SmsClient smsClient = tencentSmsConfig.queryClient();
        DescribeSmsSignListResponse response = null;
        try {
            response = smsClient.DescribeSmsSignList(request);
        } catch (TencentCloudSDKException e) {
            log.error("请求查询腾讯云签名出错：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
        return response;
    }

    @Override
    public SmsSign querySmsSign(SmsSignVO smsSignVO) {
        DescribeSmsSignListResponse response = query(smsSignVO);
        //处理结果
        DescribeSignListStatus[] describeSignListStatusSet = response.getDescribeSignListStatusSet();
        SmsSign smsSign = BeanConv.toBean(smsSignVO,SmsSign.class);
        if (!EmptyUtil.isNullOrEmpty(describeSignListStatusSet)){
            DescribeSignListStatus describeSignListStatus = describeSignListStatusSet[0];
            Long statusCode = describeSignListStatus.getStatusCode();
            //审核通过
            if (statusCode==0){
                smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_0);
                smsSignVO.setAuditMsg("审核通过");
                smsSignService.updateById(smsSign);
            //审核失败
            }else if (statusCode==-1){
                smsSignVO.setAuditStatus(SmsConstant.STATUS_AUDIT_1);
                smsSignVO.setAuditMsg(describeSignListStatus.getReviewReply());
                smsSignService.updateById(smsSign);
            }else {
                log.info("腾讯云签名：{},审核中",describeSignListStatus.getSignName());
            }
        }else {
            log.warn("受理查询腾讯签名出错");
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
        return smsSign;
    }
}
