package com.itheima.bolee.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.sms.SmsBlacklistVO;
import com.itheima.bolee.sms.pojo.SmsBlacklist;

/**
 * @Description：黑名单表 服务类
 */
public interface ISmsBlacklistService extends IService<SmsBlacklist> {

    /**
     * @Description 黑名单列表
     * @param smsBlacklistVO 查询条件
     * @param pageNum 当前页
     * @param pageSize 当前页
     * @return Page<SmsBlacklist>
     */
    Page<SmsBlacklistVO> findSmsBlacklistVOPage(
            SmsBlacklistVO smsBlacklistVO,
            int pageNum,
            int pageSize);

    /**
     * @Description 创建黑名单
     * @param smsBlacklistVO 对象信息
     * @return SmsBlacklist
     */
    SmsBlacklistVO createSmsBlacklist(SmsBlacklistVO smsBlacklistVO);

    /**
     * @Description 修改黑名单
     * @param smsBlacklistVO 对象信息
     * @return Boolean
     */
    Boolean updateSmsBlacklist(SmsBlacklistVO smsBlacklistVO);

    /**
     * @Description 删除黑名单
     * @param checkedIds 选择的黑名单ID
     * @return Boolean
     */
    Boolean deleteSmsBlacklist(String[] checkedIds);

}
