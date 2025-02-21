package com.itheima.bolee.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.sms.SmsSendRecordVO;
import com.itheima.bolee.sms.pojo.SmsSendRecord;

import java.util.List;

/**
 * @Description：发送记录表 服务类
 */
public interface ISmsSendRecordService extends IService<SmsSendRecord> {

    /**
     * @Description 发送记录列表
     * @param smsSendRecordVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<SmsSendRecord>
     */
    Page<SmsSendRecordVO> findSmsSendRecordVOPage(
            SmsSendRecordVO smsSendRecordVO,
            int pageNum,
            int pageSize);

    /**
     * @Description 创建发送记录
     * @param smsSendRecordVO 对象信息
     * @return SmsSendRecord
     */
    SmsSendRecordVO createSmsSendRecord(SmsSendRecordVO smsSendRecordVO);

    /**
     * @Description 修改发送记录
     * @param smsSendRecordVO 对象信息
     * @return Boolean
     */
    Boolean updateSmsSendRecord(SmsSendRecordVO smsSendRecordVO);

    /**
     * @Description 删除发送记录
     * @param checkedIds 选择的发送记录ID
     * @return Boolean
     */
    Boolean deleteSmsSendRecord(String[] checkedIds);

    /**
     * @Description 查找发送记录
     * @param smsSendRecordId 选择对象信息Id
     * @return SmsSendRecordVO
     */
    SmsSendRecordVO findSmsSendRecordBySmsSendRecordId(Long smsSendRecordId);

    /**
     * @Description 重发
     * @param smsSendRecordVO 重发记录
     * @return SmsSendRecordVO
     */
    Boolean retrySendSms(SmsSendRecordVO smsSendRecordVO);

    /***
     * @description 查询受理成功发送中状态的前20条短信
     * @return
     * @return: java.util.List<com.itheima.restkeeper.pojo.SmsSendRecord>
     */
    List<SmsSendRecordVO> callBackSmsSendRecords();
}
