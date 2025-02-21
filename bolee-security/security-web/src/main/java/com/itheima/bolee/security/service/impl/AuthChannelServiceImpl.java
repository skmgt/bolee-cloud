package com.itheima.bolee.security.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.AuthChannelCacheConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.AuthChannelEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.mapper.AuthChannelMapper;
import com.itheima.bolee.security.pojo.AuthChannel;
import com.itheima.bolee.security.service.IAuthChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description：三方授权渠道信息
 */
@Slf4j
@Service
public class AuthChannelServiceImpl extends ServiceImpl<AuthChannelMapper, AuthChannel> implements IAuthChannelService {

    @Autowired
    AuthChannelMapper authChannelMapper;

    /***
     * @description 构建多条件查询
     * @param queryWrapper 查询条件
     * @param authChannelVO 查询对象
     * @return
     */
    private QueryWrapper<AuthChannel> queryWrapper(QueryWrapper<AuthChannel> queryWrapper,AuthChannelVO authChannelVO){
        //渠道名称
        if (!EmptyUtil.isNullOrEmpty(authChannelVO.getChannelName())) {
            queryWrapper.lambda().likeRight(AuthChannel::getChannelName,authChannelVO.getChannelName());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(authChannelVO.getAppId())) {
            queryWrapper.lambda().eq(AuthChannel::getAppId,authChannelVO.getAppId());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(authChannelVO.getDataState())) {
            queryWrapper.lambda().eq(AuthChannel::getDataState,authChannelVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(AuthChannel::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = AuthChannelCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#authChannelVO.hashCode()")
    public Page<AuthChannelVO> findAuthChannelPage(AuthChannelVO authChannelVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<AuthChannel> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<AuthChannel> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,authChannelVO);
            //执行分页查询
            Page<AuthChannelVO> pageVo = BeanConv.toPage(page(page, queryWrapper), AuthChannelVO.class);
            List<AuthChannelVO> records = pageVo.getRecords();
            if (!EmptyUtil.isNullOrEmpty(records)){
                records.forEach(n->{
                    if (!EmptyUtil.isNullOrEmpty(n.getOtherConfig())){
                        List<OtherConfigVO> list = JSONArray.parseArray(n.getOtherConfig(),OtherConfigVO.class);
                        n.setOtherConfigVOs(list);
                    }
                });
            }
            pageVo.setRecords(records);
            return pageVo;
        }catch (Exception e){
            log.error("客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = AuthChannelCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = AuthChannelCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =AuthChannelCacheConstant.BASIC,key = "#result.id")})
    public AuthChannelVO createAuthChannel(AuthChannelVO authChannelVO) {
        try {
            //转换AuthChannelVO为AuthChannel
            AuthChannel authChannel = BeanConv.toBean(authChannelVO, AuthChannel.class);
            authChannel.setOtherConfig(JSONObject.toJSONString(authChannelVO.getOtherConfigVOs()));
            boolean flag = save(authChannel);
            if (!flag){
                throw new RuntimeException("保存客户信息出错");
            }
            return BeanConv.toBean(authChannel, AuthChannelVO.class);
        } catch (Exception e) {
            log.error("保存客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = AuthChannelCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = AuthChannelCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = AuthChannelCacheConstant.BASIC,key = "#authChannelVO.id")})
    public Boolean updateAuthChannel(AuthChannelVO authChannelVO) {
        try {
            //转换AuthChannelVO为AuthChannel
            AuthChannel authChannel = BeanConv.toBean(authChannelVO, AuthChannel.class);
            authChannel.setOtherConfig(JSONObject.toJSONString(authChannelVO.getOtherConfigVOs()));
            boolean flag = updateById(authChannel);
            if (!flag){
                throw new RuntimeException("修改客户信息出错");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.UPDATE_FAIL);
        }

    }

    @Override
    @Cacheable(value = AuthChannelCacheConstant.LIST,key ="#authChannelVO.hashCode()")
    public List<AuthChannelVO> findAuthChannelList(AuthChannelVO authChannelVO) {
        try {
            //构建查询条件
            QueryWrapper<AuthChannel> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,authChannelVO);
            List<AuthChannelVO> records = BeanConv.toBeanList(list(queryWrapper), AuthChannelVO.class);
            if (!EmptyUtil.isNullOrEmpty(records)){
                records.forEach(n->{
                    if (!EmptyUtil.isNullOrEmpty(n.getOtherConfig())){
                        List<OtherConfigVO> list = JSONArray.parseArray(n.getOtherConfig(),OtherConfigVO.class);
                        n.setOtherConfigVOs(list);
                    }
                });
            }
            return records;
        } catch (Exception e) {
            log.error("查询客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.LIST_FAIL);
        }

    }

    @Override
    @Cacheable(value = AuthChannelCacheConstant.LIST,key ="#companyNos.hashCode()")
    public List<AuthChannelVO> findAuthChannelListInCompanyNos(List<String> companyNos) {
        try {
            //构建查询条件
            QueryWrapper<AuthChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(AuthChannel::getCompanyNo,companyNos);
            return BeanConv.toBeanList(list(queryWrapper),AuthChannelVO.class);
        } catch (Exception e) {
            log.error("查询客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.LIST_FAIL);
        }
    }

    @Override
    public boolean delAuthChannelByCompanyNo(String companyNo) {
        try {
            //构建查询条件
            UpdateWrapper<AuthChannel> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(AuthChannel::getCompanyNo,companyNo);
            return this.remove(updateWrapper);
        } catch (Exception e) {
            log.error("查询客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.DEL_FAIL);
        }
    }

    @Override
    public AuthChannelVO findAuthChannelByCompanyNoAndChannelLabel(String companyNo, String channelLabel) {
        try {
            //构建查询条件
            QueryWrapper<AuthChannel> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AuthChannel::getCompanyNo,companyNo)
                .eq(AuthChannel::getChannelLabel,channelLabel);
            return BeanConv.toBean(getOne(queryWrapper),AuthChannelVO.class);
        } catch (Exception e) {
            log.error("查询客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AuthChannelEnum.LIST_FAIL);
        }
    }

}
