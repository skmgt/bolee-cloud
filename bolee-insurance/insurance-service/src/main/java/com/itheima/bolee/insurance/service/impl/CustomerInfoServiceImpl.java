package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.anno.SensitiveResponse;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.framework.outinterface.dto.TokenDTO;
import com.itheima.bolee.framework.outinterface.service.BankCardService;
import com.itheima.bolee.framework.outinterface.service.TokenService;
import com.itheima.bolee.insurance.constant.CustomerInfoCacheConstant;
import com.itheima.bolee.insurance.dto.BaiduCloudTokenVO;
import com.itheima.bolee.insurance.dto.CustomerCardVO;
import com.itheima.bolee.insurance.dto.CustomerInfoVO;
import com.itheima.bolee.insurance.enums.CustomerInfoEnum;
import com.itheima.bolee.insurance.enums.OutDataInfoOcrEnum;
import com.itheima.bolee.insurance.mapper.CustomerInfoMapper;
import com.itheima.bolee.insurance.pojo.CustomerCard;
import com.itheima.bolee.insurance.pojo.CustomerInfo;
import com.itheima.bolee.insurance.service.ICustomerInfoService;
import com.itheima.bolee.security.feign.CustomerFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：客户信息表服务实现类
 */
@Slf4j
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    @Autowired
    BankCardService bankCardService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private CustomerFeign customerFeign;

    /***
     * @description 客户信息表多条件组合
     * @param customerInfoVO 客户信息表
     * @return QueryWrapper查询条件
     */
    private QueryWrapper<CustomerInfo> queryWrapper(CustomerInfoVO customerInfoVO) {
        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
        //系统账号编号查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getCustomerId())) {
            queryWrapper.lambda().eq(CustomerInfo::getCustomerId, customerInfoVO.getCustomerId());
        }
        //备注查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getRemark())) {
            queryWrapper.lambda().eq(CustomerInfo::getRemark, customerInfoVO.getRemark());
        }
        //常驻城市查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getCityName())) {
            queryWrapper.lambda().eq(CustomerInfo::getCityName, customerInfoVO.getCityName());
        }
        //收入查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getIncome())) {
            queryWrapper.lambda().eq(CustomerInfo::getIncome, customerInfoVO.getIncome());
        }
        //负债查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getLiabilities())) {
            queryWrapper.lambda().eq(CustomerInfo::getLiabilities, customerInfoVO.getLiabilities());
        }
        //实名认证（0是 1否）查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getRealNameAuthentication())) {
            queryWrapper.lambda().eq(CustomerInfo::getRealNameAuthentication, customerInfoVO.getRealNameAuthentication());
        }
        //绑卡（0是 1否）查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getBindingCard())) {
            queryWrapper.lambda().eq(CustomerInfo::getBindingCard, customerInfoVO.getBindingCard());
        }
        //身份证号码查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getIdentityCard())) {
            queryWrapper.lambda().eq(CustomerInfo::getIdentityCard, customerInfoVO.getIdentityCard());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerInfoVO.getDataState())) {
            queryWrapper.lambda().eq(CustomerInfo::getDataState, customerInfoVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(CustomerInfo::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Page<CustomerInfoVO> findPage(CustomerInfoVO customerInfoVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<CustomerInfo> CustomerInfoPage = new Page<>(pageNum, pageSize);
            //构建查询条件
            QueryWrapper<CustomerInfo> queryWrapper = queryWrapper(customerInfoVO);
            //执行分页查询
            Page<CustomerInfoVO> customerInfoVOPage = BeanConv.toPage(
                    page(CustomerInfoPage, queryWrapper), CustomerInfoVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(customerInfoVOPage) &&
                    !EmptyUtil.isNullOrEmpty(customerInfoVOPage.getRecords())) {
                //获得所有业务主键ID
                List<Long> customerInfoIds = customerInfoVOPage.getRecords()
                        .stream().map(CustomerInfoVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(customerInfoIds));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                customerInfoVOPage.getRecords().forEach(customerInfoVOHandler -> {
                    fileVOs.forEach(fileVO -> {
                        if (customerInfoVOHandler.getId().equals(fileVO.getBusinessId())) {
                            fileVOsHandler.add(fileVO);
                        }
                    });
                    //补全附件信息
                    customerInfoVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            //返回结果
            return customerInfoVOPage;
        } catch (Exception e) {
            log.error("客户信息表分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.PAGE_FAIL);
        }
    }

    @Override
    public CustomerInfoVO findById(String customerInfoId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(customerInfoId), CustomerInfoVO.class);
        } catch (Exception e) {
            log.error("客户信息表单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    public CustomerInfoVO save(CustomerInfoVO customerInfoVO) {
        try {
            //转换CustomerInfoVO为CustomerInfo
            CustomerInfo customerInfo = BeanConv.toBean(customerInfoVO, CustomerInfo.class);
            boolean flag = save(customerInfo);
            if (!flag) {
                throw new RuntimeException("保存客户信息表失败");
            }
            //保存附件信息
            if (EmptyUtil.isNullOrEmpty(customerInfoVO.getFileVOs())) {
                throw new RuntimeException("合同附件为空");
            }
            //构建附件对象
            customerInfoVO.getFileVOs().forEach(fileVO -> {
                fileVO.setBusinessId(customerInfo.getId());
            });
            //调用附件接口
            List<FileVO> fileVOs = fileBusinessFeign.bindBatchFile(Lists.newArrayList(customerInfoVO.getFileVOs()));
            if (EmptyUtil.isNullOrEmpty(fileVOs)) {
                throw new RuntimeException("合同附件绑定失败");
            }
            //转换返回对象CustomerInfoVO
            CustomerInfoVO customerInfoVOHandler = BeanConv.toBean(customerInfo, CustomerInfoVO.class);
            customerInfoVOHandler.setFileVOs(fileVOs);
            return customerInfoVOHandler;
        } catch (Exception e) {
            log.error("保存客户信息表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.SAVE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    public Boolean update(CustomerInfoVO customerInfoVO) {
        try {
            //转换CustomerInfoVO为CustomerInfo
            CustomerInfo customerInfo = BeanConv.toBean(customerInfoVO, CustomerInfo.class);
            boolean flag = updateById(customerInfo);
            if (!flag) {
                throw new RuntimeException("修改客户信息表失败");
            }
            //替换附件信息
            flag = fileBusinessFeign.replaceBindBatchFile(Lists.newArrayList(customerInfoVO.getFileVOs()));
            if (!flag) {
                throw new RuntimeException("移除客户信息表附件失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改客户信息表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.UPDATE_FAIL);
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
                throw new RuntimeException("删除客户信息表失败");
            }
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag) {
                throw new RuntimeException("删除客户信息表附件失败");
            }
            return flag;
        } catch (Exception e) {
            log.error("删除客户信息表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.DEL_FAIL);
        }
    }

    @Override
    public List<CustomerInfoVO> findList(CustomerInfoVO customerInfoVO) {
        try {
            //构建查询条件
            QueryWrapper<CustomerInfo> queryWrapper = queryWrapper(customerInfoVO);
            //执行列表查询
            List<CustomerInfoVO> customerInfoVOs = BeanConv.toBeanList(list(queryWrapper), CustomerInfoVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(customerInfoVOs)) {
                //获得所有业务主键ID
                List<Long> customerInfoIds = customerInfoVOs.stream().map(CustomerInfoVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(customerInfoIds));
                //构建附件处理List对象
                List<FileVO> fileVOsHandler = Lists.newArrayList();
                customerInfoVOs.forEach(customerInfoVOHandler -> {
                    fileVOs.forEach(fileVO -> {
                        if (customerInfoVOHandler.getId().equals(fileVO.getBusinessId())) {
                            fileVOsHandler.add(fileVO);
                        }
                    });
                    //补全附件信息
                    customerInfoVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            return customerInfoVOs;
        } catch (Exception e) {
            log.error("客户信息表列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.LIST_FAIL);
        }
    }


    @Override
    public BaiduCloudTokenVO getOcrToken(UserVO userVO) {
        try {
            if (ObjectUtil.isNull(userVO)) {
                log.error("获取OCRToken必须先登录");
                throw new ProjectException(OutDataInfoOcrEnum.LOGIN_FAIL);
            }
            TokenDTO token = tokenService.getToken();
            BaiduCloudTokenVO res = BaiduCloudTokenVO.builder().
                    dataState(SuperConstant.DATA_STATE_0).
                    refreshToken(token.getRefresh_token()).
                    accessToken(token.getAccess_token()).
                    expiresIn(token.getExpires_in()).
                    sessionKey(token.getSession_key()).
                    sessionSecret(token.getSession_secret()).
                    scope(token.getScope()).
                    build();
            return res;
        } catch (ProjectException projectException) {
            throw projectException;
        } catch (Exception e) {
            log.error("百度云OCRToken获取失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(OutDataInfoOcrEnum.TOKEN_ERROR);
        }
    }

    @Override
    public Boolean resetPassword(UserVO userVO) {
        //todo wgl 发送邮件
        return customerFeign.resetPasswords(String.valueOf(userVO.getId()));
    }

    @Override
    public CustomerInfoVO findOneByIdentityCard(String identityCard) {
        try {
            //构建查询条件
            QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CustomerInfo::getIdentityCard, identityCard);
            return BeanConv.toBean(getOne(queryWrapper), CustomerInfoVO.class);
        } catch (Exception e) {
            log.error("按身份证查询用户失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 人脸识别认证
     *
     * @return
     */
    @Override
    public Boolean faceCheck() {
        try {
            //首先查询当前登录人
            LambdaQueryWrapper<CustomerInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CustomerInfo::getCustomerId, SubjectContent.getUserVO().getId());
            lambdaQueryWrapper.eq(CustomerInfo::getDataState, SuperConstant.DATA_STATE_0);
            CustomerInfo customerInfo = getOne(lambdaQueryWrapper);
            customerInfo.setRealNameAuthentication(SuperConstant.DATA_STATE_0);
            updateById(customerInfo);
            return true;
        } catch (Exception e) {
            log.error("人脸识别修改失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 身份证号认证
     *
     * @param customerInfoVO
     * @return
     */
    @Override
    @SensitiveResponse
    public CustomerInfoVO idCard(CustomerInfoVO customerInfoVO) {
        try {
            //首先查询当前登录人
            LambdaQueryWrapper<CustomerInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CustomerInfo::getCustomerId, SubjectContent.getUserVO().getId());
            lambdaQueryWrapper.eq(CustomerInfo::getDataState, SuperConstant.DATA_STATE_0);
            CustomerInfo customerInfo = getOne(lambdaQueryWrapper);
            customerInfo.setIdentityCard(customerInfoVO.getIdentityCard());
            updateById(customerInfo);
            //修改姓名
            customerFeign.updateRealName(String.valueOf(customerInfoVO.getId()), customerInfoVO.getName());
            return customerInfoVO;
        } catch (Exception e) {
            log.error("身份证号修改失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 身份证信息
     * @return
     */
    @Override
    @SensitiveResponse
    public CustomerInfoVO idCardInfo() {
        try {
            //首先查询当前登录人
            LambdaQueryWrapper<CustomerInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CustomerInfo::getId, SubjectContent.getUserVO().getId());
            lambdaQueryWrapper.eq(CustomerInfo::getDataState, SuperConstant.DATA_STATE_0);
            CustomerInfoVO res = BeanConv.toBean(getOne(lambdaQueryWrapper), CustomerInfoVO.class);
            if(ObjectUtil.isNull(res)){
                return null;
            }
            res.setName(SubjectContent.getUserVO().getRealName());
            return res;
        } catch (Exception e) {
            log.error("身份证信息获取失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerInfoEnum.FIND_ONE_FAIL);
        }
    }


}
