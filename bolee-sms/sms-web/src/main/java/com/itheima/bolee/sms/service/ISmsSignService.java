package com.itheima.bolee.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.sms.pojo.SmsSign;

import java.util.List;

/**
 * @Description： 签名服务类
 */
public interface ISmsSignService extends IService<SmsSign> {

    /**
     * @Description 签名列表
     * @param smsSignVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<SmsSignVO>
     */
    Page<SmsSignVO> findSmsSignVOPage(SmsSignVO smsSignVO, int pageNum, int pageSize);

    /***
     * @description 申请签名
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSignVO addSmsSign(SmsSignVO smsSignVO) ;

    /***
     * @description 删除签名
     * @param checkedIds 签名ids
     * @return 请求成功
     */
    Boolean deleteSmsSign(String[] checkedIds);

    /***
     * @description 修改签名
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSignVO modifySmsSign(SmsSignVO smsSignVO);

    /***
     * @description 禁用启用
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSignVO disableEnable(SmsSignVO smsSignVO);


    /***
     * @description 查询签名审核状态
     * @param smsSignVO 签名
     * @return 请求成功
     */
    SmsSignVO querySmsSign(SmsSignVO smsSignVO) ;

    /***
     * @description 查询签名下拉框
     * @return
     */
    List<SmsSignVO> findSmsSignVOList();

    /***
     * @description 按签名和渠道查询签名信息
     * @param signName
     * @param channelLabel
     * @return
     */
    SmsSignVO findSmsSignBySignNameAndChannelLabel(String signName, String channelLabel);

    /***
     * @description 按签名No和渠道查询签名信息
     * @param signNo
     * @param channelLabel
     * @return
     */
    SmsSignVO findSmsSignBySignNoAndChannelLabel(String signNo, String channelLabel);

    /***
     * @description 按签名Code和渠道查询签名信息
     * @param signCode
     * @param channelLabel
     * @return
     */
    SmsSignVO findSmsSignBySignCodeAndChannelLabel(String signCode, String channelLabel);
}
