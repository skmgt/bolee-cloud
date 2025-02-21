package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.constant.CustomerCardCacheConstant;
import com.itheima.bolee.insurance.dto.CustomerCardVO;
import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import com.itheima.bolee.insurance.enums.CustomerCardEnum;
import com.itheima.bolee.insurance.enums.CustomerInfoEnum;
import com.itheima.bolee.insurance.mapper.CustomerCardMapper;
import com.itheima.bolee.insurance.pojo.CustomerCard;
import com.itheima.bolee.insurance.service.ICustomerCardService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description：绑卡信息服务实现类
 */
@Slf4j
@Service
public class CustomerCardServiceImpl extends ServiceImpl<CustomerCardMapper, CustomerCard> implements ICustomerCardService {

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    /***
     * @description 绑卡信息多条件组合
     * @param customerCardVO 绑卡信息
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<CustomerCard> queryWrapper(CustomerCardVO customerCardVO) {
        QueryWrapper<CustomerCard> queryWrapper = new QueryWrapper<>();
        //银行名称查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getBankName())) {
            queryWrapper.lambda().eq(CustomerCard::getBankName, customerCardVO.getBankName());
        }
        //开户行地址查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getBankAddress())) {
            queryWrapper.lambda().eq(CustomerCard::getBankAddress, customerCardVO.getBankAddress());
        }
        //银行卡号查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getBankCardNo())) {
            queryWrapper.lambda().eq(CustomerCard::getBankCardNo, customerCardVO.getBankCardNo());
        }
        //是否默认（0是 1否）查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getIsDefault())) {
            queryWrapper.lambda().eq(CustomerCard::getIsDefault, customerCardVO.getIsDefault());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getDataState())) {
            queryWrapper.lambda().eq(CustomerCard::getDataState, customerCardVO.getDataState());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerCardVO.getCustomerId())) {
            queryWrapper.lambda().eq(CustomerCard::getCustomerId, customerCardVO.getCustomerId());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CustomerCard::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Page<CustomerCardVO> findPage(CustomerCardVO customerCardVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CustomerCard> CustomerCardPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<CustomerCard> queryWrapper = queryWrapper(customerCardVO);
            //执行分页查询
            Page<CustomerCardVO> customerCardVOPage = BeanConv.toPage(
                    page(CustomerCardPage, queryWrapper), CustomerCardVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(customerCardVOPage) &&
                    !EmptyUtil.isNullOrEmpty(customerCardVOPage.getRecords())) {
                //获得所有业务主键ID
                List<Long> customerCardIds = customerCardVOPage.getRecords()
                        .stream().map(CustomerCardVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(customerCardIds));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                customerCardVOPage.getRecords().forEach(customerCardVOHandler -> {
                    fileVOs.forEach(fileVO -> {
                        if (customerCardVOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    customerCardVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            //返回结果
            return customerCardVOPage;
        } catch (Exception e) {
            log.error("绑卡信息分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.PAGE_FAIL);
        }
    }

    @Override
    public CustomerCardVO findById(String customerCardId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(customerCardId), CustomerCardVO.class);
        } catch (Exception e) {
            log.error("绑卡信息单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    public CustomerCardVO save(CustomerCardVO customerCardVO) {
        try {
            //转换CustomerCardVO为CustomerCard
            CustomerCard customerCard = BeanConv.toBean(customerCardVO, CustomerCard.class);
            boolean flag = save(customerCard);
            if (!flag) {
                throw new RuntimeException("保存绑卡信息失败");
            }
            //转换返回对象CustomerCardVO
            CustomerCardVO customerCardVOHandler = BeanConv.toBean(customerCard, CustomerCardVO.class);
            return customerCardVOHandler;
        } catch (Exception e) {
            log.error("添加银行卡信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.SAVE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    public Boolean update(CustomerCardVO customerCardVO) {
        try {
            //转换CustomerCardVO为CustomerCard
            CustomerCard customerCard = BeanConv.toBean(customerCardVO, CustomerCard.class);
            boolean flag = updateById(customerCard);
            if (!flag) {
                throw new RuntimeException("修改绑卡信息失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改绑卡信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.UPDATE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                    .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag) {
                throw new RuntimeException("删除绑卡信息失败");
            }
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag) {
                throw new RuntimeException("删除绑卡信息附件失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除绑卡信息异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.DEL_FAIL);
        }
    }

    @Override
    public List<CustomerCardVO> findList(CustomerCardVO customerCardVO) {
        try {
            //构建查询条件
            QueryWrapper<CustomerCard> queryWrapper = queryWrapper(customerCardVO);
            //执行列表查询
            List<CustomerCardVO> customerCardVOs = BeanConv.toBeanList(list(queryWrapper), CustomerCardVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(customerCardVOs)) {
                //获得所有业务主键ID
                List<Long> customerCardIds = customerCardVOs.stream().map(CustomerCardVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(customerCardIds));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                customerCardVOs.forEach(customerCardVOHandler -> {
                    fileVOs.forEach(fileVO -> {
                        if (customerCardVOHandler.getId().equals(fileVO.getBusinessId())) {
                            fileVOsHandler.add(fileVO);
                        }
                    });
                    //补全附件信息
                    customerCardVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            return customerCardVOs;
        } catch (Exception e) {
            log.error("绑卡信息列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.LIST_FAIL);
        }
    }

    @Override
    @SensitiveResponse
    public List<CustomerCardVO> bankCardList(CustomerCardVO customerCardVO) {
        return this.findList(customerCardVO);
    }

    /**
     * 添加银行卡
     * @param customerCardVO
     * @return
     */
    @Override
    public CustomerCardVO saveBankCard(CustomerCardVO customerCardVO) {
        try {
            Long userId = SubjectContent.getUserVO().getId();
            customerCardVO.setCustomerId(String.valueOf(userId));
            //先查询当前登录人是否已经绑定过银行卡
            List<CustomerCardVO> customerCardVOS = findList(customerCardVO);
            if (CollectionUtil.isNotEmpty(customerCardVOS)) {
                customerCardVOS.forEach(e->e.setIsDefault(SuperConstant.DATA_STATE_1));
                updateBatchById(BeanConv.toBeanList(customerCardVOS, CustomerCard.class));
            }
            customerCardVO.setIsDefault(SuperConstant.DATA_STATE_0);
            save(customerCardVO);
            return customerCardVO;
        }catch (Exception e){
            log.error("添加银行卡异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerCardEnum.SAVE_FAIL);
        }
    }

    /**
     * 默认银行卡设置
     * @param customerCardId
     * @return
     */
    @Override
    @Transactional
    public CustomerCardVO defaultBankCard(String customerCardId) {
        try {
            //首先查询当前登录人--所有的银行卡
            LambdaUpdateWrapper<CustomerCard> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(CustomerCard::getCustomerId, SubjectContent.getUserVO().getId());
            lambdaUpdateWrapper.eq(CustomerCard::getDataState, SuperConstant.DATA_STATE_0);
            lambdaUpdateWrapper.set(CustomerCard::getIsDefault,SuperConstant.DATA_STATE_1);
            update(lambdaUpdateWrapper);
            //修改默认银行卡
            CustomerCardVO customerCard = findById(String.valueOf(customerCardId));
            customerCard.setIsDefault(SuperConstant.DATA_STATE_0);
            update(customerCard);
            return customerCard;
        } catch (Exception e) {
            log.error("身份证信息获取失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 解绑银行卡
     * @param customerCardVO
     * @return
     */
    @Override
    public CustomerCardVO removeBankCard(CustomerCardVO customerCardVO) {
        try {
            //先查询当前登录人--所有的银行卡
            List<CustomerCardVO> customerCardVOS = findList(customerCardVO);
            if (CollectionUtil.isEmpty(customerCardVOS)) {
                throw new ProjectException(CustomerCardEnum.LIST_FAIL);
            } else if (customerCardVOS.size() == 1) {
                throw new ProjectException(CustomerCardEnum.ONLY_ONE);
            }
            //删除绑卡信息
            removeById(String.valueOf(customerCardVO.getId()));
            //如果没有默认绑卡，则将第一个绑卡设置为默认
            CustomerCardVO customerCardQuery = CustomerCardVO.builder().
                customerId(String.valueOf(SubjectContent.getUserVO().getId())).
                isDefault(SuperConstant.DATA_STATE_0).
                build();
            List<CustomerCardVO> customerCardVOs = findList(customerCardQuery);
            //找到默认使用的银行卡
            Optional<CustomerCardVO> first = customerCardVOs.stream().filter(e -> SuperConstant.DATA_STATE_0.equals(e.getIsDefault())).findFirst();
            if(first.isPresent()){
                //如果找到了，什么都不做
                return customerCardVO;
            }else{
                //如果没找到，则将第一个设置为默认
                CustomerCardVO updateCard = customerCardVOs.get(0);
                updateCard.setIsDefault(SuperConstant.DATA_STATE_0);
                update(updateCard);
                return updateCard;
            }
        } catch (Exception e) {
            log.error("身份证信息获取失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }
}
