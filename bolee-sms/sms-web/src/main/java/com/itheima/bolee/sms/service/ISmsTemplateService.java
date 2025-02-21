package com.itheima.bolee.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.sms.SmsTemplateVO;
import com.itheima.bolee.sms.pojo.SmsTemplate;

import java.util.List;

/**
 * @Description：模板表 服务类
 */
public interface ISmsTemplateService extends IService<SmsTemplate> {

    /***
     * @description 查询相同模板code审核通过模板模板
     * @param templateNo 模板编号
     * @return
     */
    List<SmsTemplateVO> findSmsTemplateByTemplateNo(String templateNo);

    /**
     * @Description 模板列表
     * @param smsTemplateVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SmsSignVO>
     */
    Page<SmsTemplateVO> findSmsTemplateVOPage(SmsTemplateVO smsTemplateVO, int pageNum, int pageSize);

    /***
     * @description 按照模板名称和通道查询模板
     * @param templateName 模板名称
     * @param channelLabel 通道表示
     * @return
     */
    SmsTemplateVO findSmsTemplateByTemplateNameAndChannelLabel(String templateName, String channelLabel);

    /***
     * @description 申请模板
     * @param smsTemplate 模板信息
     * @return
     */
    SmsTemplateVO addSmsTemplate(SmsTemplateVO smsTemplate) ;

    /***
     * @description 删除模板
     * @param checkedIds 模板信息id
     * @return
     */
    Boolean deleteSmsTemplate(String[] checkedIds) ;

    /***
     * @description 修改模板
     * @param smsTemplate 模板信息
     * @return
     */
    SmsTemplateVO modifySmsTemplate(SmsTemplateVO smsTemplate) ;

    /***
     * @description 查询模板审核状态
     * @param smsTemplate 模板信息
     * @return
     */
    SmsTemplateVO querySmsTemplate(SmsTemplateVO smsTemplate) ;

    /***
     * @description 禁用启用
     * @param smsTemplateVO 模板信息
     * @return
     */
    SmsTemplateVO disableEnable(SmsTemplateVO smsTemplateVO);
}
