package com.itheima.bolee.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.security.CompanyCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.CustomerCacheConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.CustomerVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.dto.sms.SendMessageVO;
import com.itheima.bolee.framework.commons.enums.security.AuthEnum;
import com.itheima.bolee.framework.commons.enums.security.CustomerEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.properties.SecurityConfigProperties;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.security.mapper.CustomerMapper;
import com.itheima.bolee.security.pojo.Customer;
import com.itheima.bolee.security.service.ICustomerService;
import com.itheima.bolee.sms.feign.SmsSendFeign;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description：客户表服务实现类
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    @Autowired
    HttpServletRequest request;

    /***
     * @description 构建多条件查询
     * @param queryWrapper 查询条件
     * @param customerVO 查询对象
     * @return
     */
    private QueryWrapper<Customer> queryWrapper(QueryWrapper<Customer> queryWrapper,CustomerVO customerVO){
        //客户账号查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getUsername())) {
            queryWrapper.lambda().eq(Customer::getUsername,customerVO.getUsername());
        }
        //open_id标识查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getOpenId())) {
            queryWrapper.lambda().eq(Customer::getOpenId,customerVO.getOpenId());
        }
        //客户昵称查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getNickName())) {
            queryWrapper.lambda().likeRight(Customer::getNickName,customerVO.getNickName());
        }
        //客户邮箱查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getEmail())) {
            queryWrapper.lambda().likeRight(Customer::getEmail,customerVO.getEmail());
        }
        //真实姓名查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getRealName())) {
            queryWrapper.lambda().likeRight(Customer::getRealName,customerVO.getRealName());
        }
        //手机号码查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getMobile())) {
            queryWrapper.lambda().likeRight(Customer::getMobile,customerVO.getMobile());
        }
        //客户性别（0男 1女 2未知）查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getSex())) {
            queryWrapper.lambda().eq(Customer::getSex,customerVO.getSex());
        }
        //创建者查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getCreateBy())) {
            queryWrapper.lambda().eq(Customer::getCreateBy,customerVO.getCreateBy());
        }
        //更新者查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getUpdateBy())) {
            queryWrapper.lambda().eq(Customer::getUpdateBy,customerVO.getUpdateBy());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getDataState())) {
            queryWrapper.lambda().eq(Customer::getDataState,customerVO.getDataState());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(customerVO.getId())) {
            queryWrapper.lambda().like(Customer::getId,customerVO.getId());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Customer::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = CustomerCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#customerVO.hashCode()")
    public Page<CustomerVO> findCustomerPage(CustomerVO customerVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Customer> page = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            //多条件查询
            this.queryWrapper(queryWrapper,customerVO);
            //执行分页查询
            return BeanConv.toPage(page(page, queryWrapper),CustomerVO.class);
        }catch (Exception e){
            log.error("客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.PAGE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CustomerCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = CustomerCacheConstant.LIST,allEntries = true)},
            put={@CachePut(value =CustomerCacheConstant.BASIC,key = "#result.id")})
    public CustomerVO createCustomer(CustomerVO customerVO) {
        try {
            //设置公司id--从request中获取域名
            String host = request.getHeaders("x-forwarded-host").nextElement().split(",")[0].split(":")[0];
            String key = CompanyCacheConstant.WEBSITE+host;
            //域名校验
            RBucket<CompanyVO> bucket = redissonClient.getBucket(key);
            CompanyVO companyVO = bucket.get();
            if (EmptyUtil.isNullOrEmpty(companyVO)){
                throw  new ProjectException(AuthEnum.HSOT_FAIL);
            }
            customerVO.setCompanyNo(companyVO.getCompanyNo());
            //转换CustomerVO为Customer
            String password = bCryptPasswordEncoder.encode(securityConfigProperties.getPassworddDfaule());
            customerVO.setPassword(password);
            //保存账号
            Customer customer = BeanConv.toBean(customerVO, Customer.class);
            boolean flag = save(customer);
            if (!flag){
                throw new RuntimeException("保存客户信息出错");
            }
//            //保存附件信息
//            if (EmptyUtil.isNullOrEmpty(customerVO.getFileVOs())){
//                throw new RuntimeException("头像为空");
//            }
//            //构建附件对象
//            customerVO.getFileVOs().forEach(fileVO -> {
//                fileVO.setBusinessId(customer.getId());
//            });
//            //调用附件接口
//            List<FileVO> fileVOs = fileBusinessFeign.bindBatchFile(Lists.newArrayList(customerVO.getFileVOs()));
//            if (EmptyUtil.isNullOrEmpty(fileVOs)){
//                throw new RuntimeException("头像绑定失败");
//            }
            //转换返回对象VO
            return BeanConv.toBean(customer, CustomerVO.class);
        } catch (Exception e) {
            log.error("保存客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = CustomerCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = CustomerCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = CustomerCacheConstant.LOGIN,key = "#customerVO.username"),
            @CacheEvict(value = CustomerCacheConstant.LOGIN,key = "#customerVO.openId"),
            @CacheEvict(value = CustomerCacheConstant.LOGIN,key = "#customerVO.mobile"),
            @CacheEvict(value = CustomerCacheConstant.BASIC,key = "#customerVO.id")})
    public Boolean updateCustomer(CustomerVO customerVO) {
        try {
            //转换CustomerVO为Customer
            Customer user = BeanConv.toBean(customerVO, Customer.class);
            boolean flag = updateById(user);
            if (!flag){
                throw new RuntimeException("修改客户信息出错");
            }
            return flag;
        } catch (Exception e) {
            log.error("修改客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.UPDATE_FAIL);
        }

    }

    @Override
    @Cacheable(value = CustomerCacheConstant.LIST,key ="#customerVO.hashCode()")
    public List<CustomerVO> findCustomerList(CustomerVO customerVO) {
        try {
            //构建查询条件
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            //构建多条件查询
            this.queryWrapper(queryWrapper,customerVO);
            return BeanConv.toBeanList(list(queryWrapper),CustomerVO.class);
        } catch (Exception e) {
            log.error("查询客户表列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.LIST_FAIL);
        }

    }

    @Override
    public Boolean resetPassword(String customerId) {
        try {
            String password = bCryptPasswordEncoder.encode(securityConfigProperties.getPassworddDfaule());
            Customer customer = Customer.builder().id(Long.valueOf(customerId)).password(password).build();
            return updateById(customer);
        } catch (Exception e) {
            log.error("重置密码：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.RESET_PASSWORD_FAIL);
        }

    }

    @Override
    @Cacheable(value = CustomerCacheConstant.LOGIN,key ="#username+'-'+#companyNo")
    public UserVO usernameLogin(String username,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Customer::getUsername,username).eq(Customer::getCompanyNo,companyNo)
                    .eq(Customer::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper),UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.FIND_FAIL);
        }

    }

    @Override
    @Cacheable(value = CustomerCacheConstant.LOGIN,key ="#mobile+'-'+#companyNo")
    public UserVO mobileLogin(String mobile,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Customer::getMobile,mobile).eq(Customer::getCompanyNo,companyNo)
                    .eq(Customer::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper),UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.FIND_FAIL);
        }
    }

    @Override
    @Cacheable(value = CustomerCacheConstant.LOGIN,key ="#openId+'-'+#companyNo")
    public UserVO wechatLogin(String openId,String companyNo) {
        try {
            //构建查询条件
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Customer::getOpenId,openId).eq(Customer::getCompanyNo,companyNo)
                    .eq(Customer::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBean(getOne(queryWrapper),UserVO.class);
        } catch (Exception e) {
            log.error("查询客户表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(CustomerEnum.FIND_FAIL);
        }
    }



    @Override
    public Boolean resetPasswords(String userId) {
        //随机生成密码
        String password = bCryptPasswordEncoder.encode(securityConfigProperties.getPassworddDfaule());
        //短信通知：用户id查询手机号码
        Customer customer = Customer.builder().id(Long.valueOf(userId)).password(password).build();
        return updateById(customer);
    }

    @Autowired
    SmsSendFeign smsSendFeign;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public Boolean sendLoginCode(String mobile) {
        String key = SuperConstant.LOGIN_CODE+mobile;
        RBucket<String> bucket = redissonClient.getBucket(key);
        //已经发送直接重置时间
        if (!EmptyUtil.isNullOrEmpty(bucket.get())){
            bucket.expire(300, TimeUnit.SECONDS);
            return true;
        }
        //存储手机发送的验证码到存在中
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        bucket.set(code,300, TimeUnit.SECONDS);
        String templateNo = "template_00001";
        String sginNo= "sign_0001";
        String loadBalancerType= SmsConstant.ROUND_ROBIN;
        Set<String> mobiles=new HashSet<>();
        mobiles.add(mobile);
        LinkedHashMap<String,String> templateParam = new LinkedHashMap<>();
        templateParam.put("code",code);
        SendMessageVO sendMessageVo = SendMessageVO.builder()
            .templateNo(templateNo)
            .sginNo(sginNo)
            .loadBalancerType(loadBalancerType)
            .mobiles(mobiles)
            .templateParam(templateParam)
            .build();
        return true;
        //TODO 开发环境不真实发送手机短信验证码
        //return smsSendFeign.sendSmsForMq(sendMessageVo);
    }

    /**
     * 修改用户真实姓名
     * @param userId
     * @param name
     * @return
     */
    @Override
    public Boolean updateRealName(String userId, String name) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getId,userId);
        queryWrapper.eq(Customer::getDataState,SuperConstant.DATA_STATE_0);
        Customer customer = getOne(queryWrapper);
        customer.setRealName(name);
        return updateById(customer);
    }
}
