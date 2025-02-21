package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.relations.RelationEnum;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.pojo.CustomerRelation;
import com.itheima.bolee.insurance.mapper.CustomerRelationMapper;
import com.itheima.bolee.insurance.service.ICustomerRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.CustomerRelationCacheConstant;
import com.itheima.bolee.insurance.dto.CustomerRelationVO;
import com.itheima.bolee.insurance.enums.CustomerRelationEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

/**
 * @Description：客户关系表服务实现类
 */
@Slf4j
@Service
public class CustomerRelationServiceImpl extends ServiceImpl<CustomerRelationMapper, CustomerRelation> implements ICustomerRelationService {


    /***
    * @description 客户关系表多条件组合
    * @param customerRelationVO 客户关系表
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<CustomerRelation> queryWrapper(CustomerRelationVO customerRelationVO){
        QueryWrapper<CustomerRelation> queryWrapper = new QueryWrapper<>();
        //关系查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getRelation())) {
            queryWrapper.lambda().eq(CustomerRelation::getRelation,customerRelationVO.getRelation());
        }
        //名称查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getName())) {
            queryWrapper.lambda().eq(CustomerRelation::getName,customerRelationVO.getName());
        }
        //身份证号码查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getIdentityCard())) {
            queryWrapper.lambda().eq(CustomerRelation::getIdentityCard,customerRelationVO.getIdentityCard());
        }
        //公司查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getCompanyNo())) {
            queryWrapper.lambda().eq(CustomerRelation::getCompanyNo,customerRelationVO.getCompanyNo());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getSortNo())) {
            queryWrapper.lambda().eq(CustomerRelation::getSortNo,customerRelationVO.getSortNo());
        }
        //社保状态（0有 1无）查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getSocialSecurity())) {
            queryWrapper.lambda().eq(CustomerRelation::getSocialSecurity,customerRelationVO.getSocialSecurity());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getDataState())) {
            queryWrapper.lambda().eq(CustomerRelation::getDataState,customerRelationVO.getDataState());
        }
        //关系查询
        if(!EmptyUtil.isNullOrEmpty(customerRelationVO.getRelations())){
            queryWrapper.lambda().in(CustomerRelation::getRelation,customerRelationVO.getRelations());
        }
        //关系查询
        if (!EmptyUtil.isNullOrEmpty(customerRelationVO.getCustomerId())) {
            queryWrapper.lambda().eq(CustomerRelation::getCustomerId,customerRelationVO.getCustomerId());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByAsc(CustomerRelation::getRelation);
        return queryWrapper;
    }

    @Override
    public Page<CustomerRelationVO> findPage(CustomerRelationVO customerRelationVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CustomerRelation> CustomerRelationPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<CustomerRelation> queryWrapper = queryWrapper(customerRelationVO);
            //执行分页查询
            Page<CustomerRelationVO> customerRelationVOPage = BeanConv.toPage(
                page(CustomerRelationPage, queryWrapper), CustomerRelationVO.class);
            //返回结果
            return customerRelationVOPage;
        }catch (Exception e){
            log.error("客户关系表分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.PAGE_FAIL);
        }
    }

    @Override
    public CustomerRelationVO findById(String customerRelationId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(customerRelationId),CustomerRelationVO.class);
        }catch (Exception e){
            log.error("客户关系表单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    public List<CustomerRelationVO> findInId(List<String> customerRelationIds) {
        try {
            //执行查询
            QueryWrapper<CustomerRelation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(CustomerRelation::getId,customerRelationIds);
            return BeanConv.toBeanList(list(queryWrapper),CustomerRelationVO.class);
        }catch (Exception e){
            log.error("客户关系表单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.LIST_FAIL);
        }
    }

    @Override
    @Transactional
    public CustomerRelationVO save(CustomerRelationVO customerRelationVO) {
        try {
            //转换CustomerRelationVO为CustomerRelation
            CustomerRelation customerRelation = BeanConv.toBean(customerRelationVO, CustomerRelation.class);
            customerRelation.setCustomerId(String.valueOf(SubjectContent.getUserVO().getId()));
            boolean flag = save(customerRelation);
            if (!flag){
                throw new RuntimeException("保存客户关系表失败");
            }
            //转换返回对象CustomerRelationVO
            CustomerRelationVO customerRelationVOHandler = BeanConv.toBean(customerRelation, CustomerRelationVO.class);
            return customerRelationVOHandler;
        }catch (Exception e){
            log.error("保存客户关系表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    public Boolean update(CustomerRelationVO customerRelationVO) {
        try {
            CustomerRelation existingCustomerRelation = getById(customerRelationVO.getId());
            if (existingCustomerRelation == null) {
                throw new RuntimeException("未找到相应的客户关系记录");
            }
            // 检查哪些字段需要更新
            if (customerRelationVO.getName() != null) {
                existingCustomerRelation.setName(customerRelationVO.getName());
            }
            if (customerRelationVO.getIdentityCard() != null) {
                existingCustomerRelation.setIdentityCard(customerRelationVO.getIdentityCard());
            }

            if (customerRelationVO.getRelation() != null) {
                existingCustomerRelation.setRelation(customerRelationVO.getRelation());
            }

            if (customerRelationVO.getSocialSecurity() != null) {
                existingCustomerRelation.setSocialSecurity(customerRelationVO.getSocialSecurity());
            }
            if (customerRelationVO.getCityNo() != null) {
                existingCustomerRelation.setCityNo(customerRelationVO.getCityNo());
            }
            if (customerRelationVO.getIncome() != null) {
                existingCustomerRelation.setIncome(customerRelationVO.getIncome());
            }

            boolean flag = updateById(existingCustomerRelation);

            if (!flag) {
                throw new RuntimeException("修改客户关系表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改客户关系表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除客户关系表失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除客户关系表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.DEL_FAIL);
        }
    }

    @Override
    public List<CustomerRelationVO> findList(CustomerRelationVO customerRelationVO) {
        try {
            //构建查询条件
            QueryWrapper<CustomerRelation> queryWrapper = queryWrapper(customerRelationVO);
            //执行列表查询
            List<CustomerRelationVO> customerRelationVOs = BeanConv.toBeanList(list(queryWrapper),CustomerRelationVO.class);
            for (CustomerRelationVO index : customerRelationVOs) {
                index.setSexByIdCard();
                index.setAge(IdcardUtil.getAgeByIdCard(index.getIdentityCard()));
            }
            return customerRelationVOs;
        }catch (Exception e){
            log.error("客户关系表列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.LIST_FAIL);
        }
    }


    @Override
    @SensitiveResponse
    public List<CustomerRelationVO> myRelation(CustomerRelationVO customerRelationVO) {
        return this.findList(customerRelationVO);
    }

    /**
     * 根据用户id查询当前的用户敏感信息
     * @param userId
     * @return
     */
    @Override
    public CustomerRelationVO findOne(Long userId) {
        try {
            CustomerRelationVO customerRelationVO = CustomerRelationVO.builder().
                customerId(userId).
                relation(RelationEnum.SELF.getRelation()).
                dataState(SuperConstant.DATA_STATE_0).
                build();
            QueryWrapper<CustomerRelation> queryWrapper = queryWrapper(customerRelationVO);
            CustomerRelationVO relationVO = BeanConv.toBean(getOne(queryWrapper), CustomerRelationVO.class);
            if (!EmptyUtil.isNullOrEmpty(relationVO)){
                relationVO.setSexByIdCard();
            }
            return relationVO;
        }catch (Exception e){
            log.error("根据用户id查询当前的用户敏感信息：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 根据关系字段获取亲人关系网
     * @param fields
     * @return
     */
    @Override
    public String[] getRelations(String fields){
        String[] relations = fields.split(SuperConstant.COMMA);
        for (String indexRelation : relations) {
            if(ObjectUtil.isNull(RelationEnum.valueOf(indexRelation))){
                throw new ProjectException(CustomerRelationEnum.RELATION_FIELD_ERROR);
            }
        }
        return relations;
    }

    /**
     * 获取家庭成员列表
     * @return
     */
    @Override
    public List<CustomerRelationVO> findHomeList(UserVO userVO) {
        try {
            //拿到所有的家庭情况信息
            CustomerRelationVO customerRelation = CustomerRelationVO.builder()
                .customerId(userVO.getId())
                .dataState(SuperConstant.DATA_STATE_0)
                .build();
            QueryWrapper<CustomerRelation> queryWrapper = queryWrapper(customerRelation);
            List<CustomerRelationVO> customerRelationVOs = BeanConv.toBeanList(list(queryWrapper), CustomerRelationVO.class);
            for (CustomerRelationVO index : customerRelationVOs) {
                index.setSexByIdCard();
            }
            return customerRelationVOs;
        }catch (Exception e){
            log.error("客户关系表列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerRelationEnum.LIST_FAIL);
        }
    }

}
