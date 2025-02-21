package com.itheima.bolee.insurance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.bolee.RequestTemplate;
import com.itheima.bolee.dto.ResponseDTO;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.AuthChannelConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.insurance.dto.InsuranceApproveDetailVO;
import com.itheima.bolee.insurance.dto.WarrantyInsuredVO;
import com.itheima.bolee.insurance.dto.WarrantyVerifyVO;
import com.itheima.bolee.insurance.pojo.InsuranceApprove;
import com.itheima.bolee.insurance.mapper.InsuranceApproveMapper;
import com.itheima.bolee.insurance.pojo.InsuranceApproveDetail;
import com.itheima.bolee.insurance.service.IInsuranceApproveDetailService;
import com.itheima.bolee.insurance.service.IInsuranceApproveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.insurance.service.IWarrantyInsuredService;
import com.itheima.bolee.security.feign.AuthChannelFeign;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.InsuranceApproveCacheConstant;
import com.itheima.bolee.insurance.dto.InsuranceApproveVO;
import com.itheima.bolee.insurance.enums.InsuranceApproveEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Description：保批信息服务实现类
 */
@Slf4j
@Service
public class InsuranceApproveServiceImpl extends ServiceImpl<InsuranceApproveMapper, InsuranceApprove> implements IInsuranceApproveService {


    @Autowired
    private IInsuranceApproveDetailService insuranceApproveDetailService;

    @Autowired
    private IWarrantyInsuredService warrantyInsuredService;

    @Resource(name = "approveExecutor")
    private Executor approveExecutor;

    @Autowired
    private AuthChannelFeign authChannelFeign;


    /***
    * @description 保批信息多条件组合
    * @param insuranceApproveVO 保批信息
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsuranceApprove> queryWrapper(InsuranceApproveVO insuranceApproveVO){
        QueryWrapper<InsuranceApprove> queryWrapper = new QueryWrapper<>();
        //公司编号查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getCompanyNo())) {
            queryWrapper.lambda().eq(InsuranceApprove::getCompanyNo,insuranceApproveVO.getCompanyNo());
        }
        //保险合同编号查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(InsuranceApprove::getWarrantyNo,insuranceApproveVO.getWarrantyNo());
        }
        //批保状态(0未批保 1批保发送失败 2批保中 3批保通过 4.保批不通过)查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getApproveState())) {
            queryWrapper.lambda().eq(InsuranceApprove::getApproveState,insuranceApproveVO.getApproveState());
        }
        //修改时间查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getApproveTime())) {
            queryWrapper.lambda().eq(InsuranceApprove::getApproveTime,insuranceApproveVO.getApproveTime());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getSortNo())) {
            queryWrapper.lambda().eq(InsuranceApprove::getSortNo,insuranceApproveVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insuranceApproveVO.getDataState())) {
            queryWrapper.lambda().eq(InsuranceApprove::getDataState,insuranceApproveVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsuranceApprove::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsuranceApproveCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insuranceApproveVO.hashCode()")
    public Page<InsuranceApproveVO> findPage(InsuranceApproveVO insuranceApproveVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsuranceApprove> InsuranceApprovePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsuranceApprove> queryWrapper = queryWrapper(insuranceApproveVO);
            //执行分页查询
            Page<InsuranceApproveVO> insuranceApproveVOPage = BeanConv.toPage(
                page(InsuranceApprovePage, queryWrapper), InsuranceApproveVO.class);
            //返回结果
            return insuranceApproveVOPage;
        }catch (Exception e){
            log.error("保批信息分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceApproveCacheConstant.BASIC,key ="#insuranceApproveId")
    public InsuranceApproveVO findById(String insuranceApproveId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(insuranceApproveId),InsuranceApproveVO.class);
        }catch (Exception e){
            log.error("保批信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =InsuranceApproveCacheConstant.BASIC,key = "#result.id")})
    public InsuranceApproveVO save(InsuranceApproveVO insuranceApproveVO) {
        try {
            //转换InsuranceApproveVO为InsuranceApprove
            InsuranceApprove insuranceApprove = BeanConv.toBean(insuranceApproveVO, InsuranceApprove.class);
            insuranceApproveVO.setApproveState(WarrantyConstant.APPROVE_STATE_APPROVEING);
            boolean flag = save(insuranceApprove);
            insuranceApproveVO.setId(insuranceApprove.getId());
            if (!flag){
                throw new RuntimeException("保存保批信息失败");
            }
            List<InsuranceApproveDetailVO> approveDetails = insuranceApproveVO.getApproveDetails();
            List<InsuranceApproveDetail> details = BeanConv.toBeanList(approveDetails, InsuranceApproveDetail.class);
            //设置id
            for (InsuranceApproveDetail  index : details) {
                index.setApproveId(insuranceApprove.getId());
            }
            insuranceApproveDetailService.saveBatch(details);

            approveExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //查询三方接口配置信息
                    AuthChannelVO authChannelVO = authChannelFeign.findAuthChannelByCompanyNoAndChannelLabel(
                            insuranceApproveVO.getCompanyNo(),
                            AuthChannelConstant.CHANNEL_LABEL_INSURE);
                    List<OtherConfigVO> otherConfigVOs = JSONArray.parseArray(authChannelVO.getOtherConfig(), OtherConfigVO.class);
                    List<OtherConfigVO> otherConfigVOsHandler = otherConfigVOs.stream().filter(n -> {
                        return n.getConfigKey().equals(AuthChannelConstant.PUBLICKEY);
                    }).collect(Collectors.toList());
                    //获取所有保单的id
                    URIBuilder urlBuilder = new URIBuilder()
                            .setScheme("http")
                            .setHost(authChannelVO.getDomain())
                            .setPath("/approve-info");
                    RequestTemplate requestTemplate = RequestTemplate.builder()
                            .appId(authChannelVO.getAppId())
                            .privateKey(authChannelVO.getAppSecret())
                            .publicKey(otherConfigVOsHandler.get(0).getConfigValue())
                            .uriBuilder(urlBuilder)
                            .build();
                    try {
                        ResponseDTO responseDTO = requestTemplate.doRequest(insuranceApproveVO, ResponseDTO.class);
                        if("2000".equals(responseDTO.getCode())){
                            //修改被保人信息
                            //查询出当前合同所有的被保人信息
                            insuranceApproveVO.setApproveState(WarrantyConstant.APPROVE_STATE_APPROVE);
                            //修改投保人被保人信息
                            for (InsuranceApproveDetail  index : details) {
                                boolean insuredChange = false;
                                boolean approveChange = false;
                                WarrantyInsuredVO warrantyInsuredVO = new WarrantyInsuredVO();
                                //如果是被保人
                                if(WarrantyConstant.isInsured(index.getType())){
                                    insuredChange = true;
                                    WarrantyInsuredVO.builder().warrantyNo(insuranceApproveVO.getWarrantyNo()).build();
                                    //如果是被保人
                                    String changeFieldKey = index.getChangeFieldKey();
                                    Field field = warrantyInsuredVO.getClass().getDeclaredField(changeFieldKey);
                                    field.setAccessible(true);
                                    Object fieldValue = field.get(index.getChangeFieldKey());
                                    field.set(warrantyInsuredVO, index.getModifyCont());
                                }else{
                                    //一定是投保人
                                    approveChange = false;
                                }
                            }
                        }else{
                            insuranceApproveVO.setApproveState(WarrantyConstant.APPROVE_STATE_REFUSE);
                        }
                        update(insuranceApproveVO);

                    } catch (Exception e) {
                        log.error("核保补发异常：{}", ExceptionsUtil.getStackTraceAsString(e));
                        insuranceApproveVO.setApproveState(WarrantyConstant.APPROVE_STATE_REFUSE);
                        update(insuranceApproveVO);
                    }
                }
            });
            //转换返回对象InsuranceApproveVO
            InsuranceApproveVO insuranceApproveVOHandler = BeanConv.toBean(insuranceApprove, InsuranceApproveVO.class);
            return insuranceApproveVOHandler;
        }catch (Exception e){
            log.error("保存保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceApproveCacheConstant.BASIC,key = "#insuranceApproveVO.id")})
    public Boolean update(InsuranceApproveVO insuranceApproveVO) {
        try {
            //转换InsuranceApproveVO为InsuranceApprove
            InsuranceApprove insuranceApprove = BeanConv.toBean(insuranceApproveVO, InsuranceApprove.class);
            boolean flag = updateById(insuranceApprove);
            if (!flag){
                throw new RuntimeException("修改保批信息失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsuranceApproveCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsuranceApproveCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsuranceApproveCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保批信息失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保批信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsuranceApproveCacheConstant.LIST,key ="#insuranceApproveVO.hashCode()")
    public List<InsuranceApproveVO> findList(InsuranceApproveVO insuranceApproveVO) {
        try {
            //构建查询条件
            QueryWrapper<InsuranceApprove> queryWrapper = queryWrapper(insuranceApproveVO);
            //执行列表查询
            List<InsuranceApproveVO> insuranceApproveVOs = BeanConv.toBeanList(list(queryWrapper),InsuranceApproveVO.class);
            return insuranceApproveVOs;
        }catch (Exception e){
            log.error("保批信息列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.LIST_FAIL);
        }
    }

    /**
     * 根据合同编号获取保批信息
      * @param warrantyNo
     * @return
     */
    @Override
    public List<InsuranceApproveVO> findByWarrantyNo(String warrantyNo) {
        try{
            LambdaQueryWrapper<InsuranceApprove> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InsuranceApprove::getWarrantyNo,warrantyNo);
            List<InsuranceApprove> insuranceApproveList = list(queryWrapper);
            List<InsuranceApproveVO> res = BeanConv.toBeanList(insuranceApproveList, InsuranceApproveVO.class);
            for (InsuranceApproveVO index : res) {
                Long id = index.getId();
                InsuranceApproveDetailVO insuranceApproveDetailVO = InsuranceApproveDetailVO.builder().approveId(id).dataState(SuperConstant.DATA_STATE_0).build();
                List<InsuranceApproveDetailVO> data = insuranceApproveDetailService.findList(insuranceApproveDetailVO);
                index.setApproveDetails(data);
            }
            return res;
        }catch (Exception e){
            log.error("保批信息列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsuranceApproveEnum.LIST_FAIL);
        }
    }
}
