package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.security.pojo.AuthChannel;

import java.util.List;

/**
 * @Description：授权渠道服务类
 */
public interface IAuthChannelService extends IService<AuthChannel> {

    /**
     * @Description 多条件查询授权渠道分页列表
     * @param authChannelVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<AuthChannel>
     */
    Page<AuthChannelVO> findAuthChannelPage(AuthChannelVO authChannelVO, int pageNum, int pageSize);

    /**
     * @Description 创建授权渠道
     * @param authChannelVO 对象信息
     * @return AuthChannel
     */
    AuthChannelVO createAuthChannel(AuthChannelVO authChannelVO);

    /**
     * @Description 修改授权渠道
     * @param authChannelVO 对象信息
     * @return Boolean
     */
    Boolean updateAuthChannel(AuthChannelVO authChannelVO);

    /**
     * @description 多条件查询授权渠道列表
     * @param authChannelVO 查询条件
     * @return: List<AuthChannel>
     */
    List<AuthChannelVO> findAuthChannelList(AuthChannelVO authChannelVO);

    /***
     * @description 查询企业组中的所有配置
     *
     * @param companyNos 企业号
     * @return
     */
    List<AuthChannelVO> findAuthChannelListInCompanyNos(List<String> companyNos);

    /***
     * @description 移除企业对应的三方授权渠道
     *
     * @param companyNo
     * @return
     */
    boolean delAuthChannelByCompanyNo(String companyNo);

    /***
     * @description 按企业编号和配置类型查询对应的企业信息
     *
     * @param companyNo
     * @return
     */
    AuthChannelVO findAuthChannelByCompanyNoAndChannelLabel(String companyNo, String channelLabel);
}
