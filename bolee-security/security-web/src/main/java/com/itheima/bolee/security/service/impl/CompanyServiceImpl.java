package com.itheima.bolee.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.CompanyCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.CompanyConstant;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.security.CompanyEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.init.InitCompany;
import com.itheima.bolee.security.mapper.AuthChannelMapper;
import com.itheima.bolee.security.mapper.CompanyMapper;
import com.itheima.bolee.security.pojo.AuthChannel;
import com.itheima.bolee.security.pojo.Company;
import com.itheima.bolee.security.service.IAuthChannelService;
import com.itheima.bolee.security.service.ICompanyService;
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
import java.util.stream.Collectors;

/**
 * @Description：公司表服务实现类
 */
@Slf4j
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    IAuthChannelService authChannelService;

    /**
     * @description 构建多条件查询
     * @param queryWrapper 查询条件
     * @param companyVO 查询对象
     * @return
     */
    private QueryWrapper<Company> queryWrapper(QueryWrapper<Company> queryWrapper,CompanyVO companyVO){
        //负责人手机
        if (!EmptyUtil.isNullOrEmpty(companyVO.getLeaderMobile())) {
            queryWrapper.lambda().likeRight(Company::getLeaderMobile,companyVO.getLeaderMobile());
        }
        //负责人姓名
        if (!EmptyUtil.isNullOrEmpty(companyVO.getLeaderName())) {
            queryWrapper.lambda().likeRight(Company::getLeaderName,companyVO.getLeaderName());
        }
        //公司编号
        if (!EmptyUtil.isNullOrEmpty(companyVO.getCompanyNo())) {
            queryWrapper.lambda().likeRight(Company::getCompanyNo,companyVO.getCompanyNo());
        }
        //公司名称
        if (!EmptyUtil.isNullOrEmpty(companyVO.getCompanName())) {
            queryWrapper.lambda().likeRight(Company::getCompanName,companyVO.getCompanName());
        }
        //统一社会信用代码
        if (!EmptyUtil.isNullOrEmpty(companyVO.getRegisteredNo())) {
            queryWrapper.lambda().likeRight(Company::getRegisteredNo,companyVO.getRegisteredNo());
        }
        //公司邮箱查询
        if (!EmptyUtil.isNullOrEmpty(companyVO.getEmail())) {
            queryWrapper.lambda().likeRight(Company::getEmail,companyVO.getEmail());
        }
        //创建者查询
        if (!EmptyUtil.isNullOrEmpty(companyVO.getCreateBy())) {
            queryWrapper.lambda().eq(Company::getCreateBy,companyVO.getCreateBy());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(companyVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Company::getUpdateBy,companyVO.getUpdateBy());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(companyVO.getDataState())) {
            queryWrapper.lambda().eq(Company::getDataState,companyVO.getDataState());
        }
        //公司id查询
        if (!EmptyUtil.isNullOrEmpty(companyVO.getCheckIds())) {
            queryWrapper.lambda().in(Company::getId,companyVO.getCheckIds());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Company::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CompanyCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#companyVO.hashCode()")
    public Page<CompanyVO> findCompanyPage(CompanyVO companyVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Company> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,companyVO);
            //执行分页查询
            Page<CompanyVO> pageResult = BeanConv.toPage(page(page, queryWrapper),CompanyVO.class);
            return pageResult;
        }catch (Exception e){
            log.error("公司表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CompanyEnum.PAGE_FAIL);
        }
    }

    @Autowired
    InitCompany initCompany;

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CompanyCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = CompanyCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =CompanyCacheConstant.BASIC,key = "#result.id")})
    public CompanyVO createCompany(CompanyVO companyVO) {
        try {
            //转换CompanyVO为Company
            Company company = BeanConv.toBean(companyVO, Company.class);
            boolean flag = save(company);
            if (!flag){
                throw new RuntimeException("保存公司信息出错");
            }
            CompanyVO companyVOResult = BeanConv.toBean(company, CompanyVO.class);
            //同步缓存
            initCompany.addWebSiteforRedis(companyVOResult);
            return companyVOResult;
        } catch (Exception e) {
            log.error("保存公司表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CompanyEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CompanyCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = CompanyCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = CompanyCacheConstant.BASIC,key = "#companyVO.id")})
    public Boolean updateCompany(CompanyVO companyVO) {
        try {
            //转换CompanyVO为Company
            Company company = BeanConv.toBean(companyVO, Company.class);
            boolean flag = updateById(company);
            if (!flag){
                throw new RuntimeException("修改公司信息出错");
            } else {
                initCompany.updataWebSiteforRedis(companyVO);
            }
            return flag;
        } catch (Exception e) {
            log.error("修改公司表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CompanyEnum.UPDATE_FAIL);
        }

    }

    @Override
    @Cacheable(value = CompanyCacheConstant.LIST,key ="#companyVO.hashCode()")
    public List<CompanyVO> findCompanyList(CompanyVO companyVO) {
        try {
            //构建查询条件
            QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,companyVO);
            return  BeanConv.toBeanList(list(queryWrapper), CompanyVO.class);
        } catch (Exception e) {
            log.error("查询公司表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CompanyEnum.LIST_FAIL);
        }

    }

    @Override
    public List<CompanyVO> findCompanyVOValidation() {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Company::getDataState, SuperConstant.DATA_STATE_0)
            .and(wrapper->wrapper
                .eq(Company::getStatus, CompanyConstant.status_1)
                .or()
                .eq(Company::getStatus,CompanyConstant.status_2));
        return BeanConv.toBeanList(list(queryWrapper), CompanyVO.class);
    }

    @Override
    public CompanyVO findCompanyByNo(String companyNo) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Company::getCompanyNo,companyNo);
        CompanyVO companyVOs = BeanConv.toBean(getOne(queryWrapper), CompanyVO.class);
        return companyVOs;
    }
}
