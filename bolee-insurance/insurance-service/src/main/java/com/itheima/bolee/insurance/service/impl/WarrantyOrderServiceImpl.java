package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.basic.CacheConstant;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyConstant;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerInsuranceDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerSexDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisInsuranceTypeDTO;
import com.itheima.bolee.framework.commons.dto.analysis.TimeDTO;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.constant.warranty.WarrantyOrderConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.enums.analysis.AnalysisEnum;
import com.itheima.bolee.framework.commons.utils.TimeHandlerUtils;
import com.itheima.bolee.framework.commons.dto.trade.AliPeriodicVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.constant.InsuranceConstant;
import com.itheima.bolee.insurance.enums.WarrantyEnum;
import com.itheima.bolee.insurance.pojo.*;
import com.itheima.bolee.insurance.mapper.WarrantyOrderMapper;
import com.itheima.bolee.insurance.service.IWarrantyOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.bolee.insurance.service.IWarrantyService;
import com.itheima.bolee.trade.feign.PeriodicPayFeign;
import com.itheima.bolee.trade.feign.WapPayFeign;
import io.seata.spring.annotation.GlobalTransactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WarrantyOrderCacheConstant;
import com.itheima.bolee.insurance.dto.WarrantyOrderVO;
import com.itheima.bolee.insurance.enums.WarrantyOrderEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import javax.annotation.Resource;

/**
 * @Description：合同扣款计划服务实现类
 */
@Slf4j
@Service
public class WarrantyOrderServiceImpl extends ServiceImpl<WarrantyOrderMapper, WarrantyOrder> implements IWarrantyOrderService {

    @Autowired
    WarrantyOrderMapper warrantyOrderMapper;

    @Autowired
    WapPayFeign wapPayFeign;

    @Autowired
    PeriodicPayFeign periodicPayFeign;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Resource(name = "periodicPayExecutor")
    Executor periodicPayExecutor;

    /***
    * @description 合同扣款计划多条件组合
    * @param warrantyOrderVO 合同扣款计划
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WarrantyOrder> queryWrapper(WarrantyOrderVO warrantyOrderVO){
        QueryWrapper<WarrantyOrder> queryWrapper = new QueryWrapper<>();
        //订单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getOrderNo())) {
            queryWrapper.lambda().eq(WarrantyOrder::getOrderNo,warrantyOrderVO.getOrderNo());
        }
        //保单编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getWarrantyNo())) {
            queryWrapper.lambda().eq(WarrantyOrder::getWarrantyNo,warrantyOrderVO.getWarrantyNo());
        }
        //总期数查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getPeriods())) {
            queryWrapper.lambda().eq(WarrantyOrder::getPeriods,warrantyOrderVO.getPeriods());
        }
        //当前期数查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getCurrentPeriod())) {
            queryWrapper.lambda().eq(WarrantyOrder::getCurrentPeriod,warrantyOrderVO.getCurrentPeriod());
        }
        //期交保费查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getPremium())) {
            queryWrapper.lambda().eq(WarrantyOrder::getPremium,warrantyOrderVO.getPremium());
        }
        //计划扣除时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getScheduleTime())) {
            queryWrapper.lambda().eq(WarrantyOrder::getScheduleTime,warrantyOrderVO.getScheduleTime());
        }
        //实际扣除时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getActualTime())) {
            queryWrapper.lambda().eq(WarrantyOrder::getActualTime,warrantyOrderVO.getActualTime());
        }
        //宽限期截止时间查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getGraceTime())) {
            queryWrapper.lambda().eq(WarrantyOrder::getGraceTime,warrantyOrderVO.getGraceTime());
        }
        //状态（0待付款 1已付款 3 逾期 4 补缴 5 付款失败 6 付款中）
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getOrderState())) {
            queryWrapper.lambda().eq(WarrantyOrder::getOrderState,warrantyOrderVO.getOrderState());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getSortNo())) {
            queryWrapper.lambda().eq(WarrantyOrder::getSortNo,warrantyOrderVO.getSortNo());
        }
        //企业编号查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getCompanyNo())) {
            queryWrapper.lambda().eq(WarrantyOrder::getCompanyNo,warrantyOrderVO.getCompanyNo());
        }
        //支付渠道【支付宝、微信、三方银行】查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getTradingChannel())) {
            queryWrapper.lambda().eq(WarrantyOrder::getTradingChannel,warrantyOrderVO.getTradingChannel());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getDataState())) {
            queryWrapper.lambda().eq(WarrantyOrder::getDataState,warrantyOrderVO.getDataState());
        }
        //代理人ID
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getAgentId())) {
            queryWrapper.lambda().eq(WarrantyOrder::getAgentId,warrantyOrderVO.getAgentId());
        }
        //代理人姓名
        if (!EmptyUtil.isNullOrEmpty(warrantyOrderVO.getAgentName())) {
            queryWrapper.lambda().likeRight(WarrantyOrder::getAgentName,warrantyOrderVO.getAgentName());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByAsc(WarrantyOrder::getCurrentPeriod);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WarrantyOrderCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#warrantyOrderVO.hashCode()")
    public Page<WarrantyOrderVO> findPage(WarrantyOrderVO warrantyOrderVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WarrantyOrder> WarrantyOrderPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WarrantyOrder> queryWrapper = queryWrapper(warrantyOrderVO);
            //执行分页查询
            Page<WarrantyOrderVO> warrantyOrderVOPage = BeanConv.toPage(
                page(WarrantyOrderPage, queryWrapper), WarrantyOrderVO.class);
            //返回结果
            return warrantyOrderVOPage;
        }catch (Exception e){
            log.error("合同扣款计划分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyOrderCacheConstant.BASIC,key ="#warrantyOrderId")
    public WarrantyOrderVO findById(String warrantyOrderId) {
        try {
            //执行查询
            QueryWrapper<WarrantyOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WarrantyOrder::getId,warrantyOrderId);
            return BeanConv.toBean(getOne(queryWrapper),WarrantyOrderVO.class);
        }catch (Exception e){
            log.error("合同扣款计划单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    public WarrantyOrderVO findByIdAndOrderState(String warrantyOrderId, String OrderState) {
        try {
            //执行查询
            QueryWrapper<WarrantyOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WarrantyOrder::getId,warrantyOrderId)
                .eq(WarrantyOrder::getDataState,SuperConstant.DATA_STATE_0)
                .eq(WarrantyOrder::getOrderState, OrderState);
            return BeanConv.toBean(getOne(queryWrapper),WarrantyOrderVO.class);
        }catch (Exception e){
            log.error("合同扣款计划单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyOrderCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WarrantyOrderCacheConstant.BASIC,key = "#result.id")})
    public WarrantyOrderVO save(WarrantyOrderVO warrantyOrderVO) {
        try {
            //转换WarrantyOrderVO为WarrantyOrder
            WarrantyOrder warrantyOrder = BeanConv.toBean(warrantyOrderVO, WarrantyOrder.class);
            boolean flag = save(warrantyOrder);
            if (!flag){
                throw new RuntimeException("保存合同扣款计划失败");
            }
            //转换返回对象WarrantyOrderVO
            WarrantyOrderVO warrantyOrderVOHandler = BeanConv.toBean(warrantyOrder, WarrantyOrderVO.class);
            return warrantyOrderVOHandler;
        }catch (Exception e){
            log.error("保存合同扣款计划异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyOrderCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyOrderCacheConstant.BASIC,key = "#warrantyOrderVO.id")})
    public Boolean update(WarrantyOrderVO warrantyOrderVO) {
        try {
            //转换WarrantyOrderVO为WarrantyOrder
            WarrantyOrder warrantyOrder = BeanConv.toBean(warrantyOrderVO, WarrantyOrder.class);
            boolean flag = updateById(warrantyOrder);
            if (!flag){
                throw new RuntimeException("修改合同扣款计划失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改合同扣款计划异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WarrantyOrderCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WarrantyOrderCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WarrantyOrderCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除合同扣款计划失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除合同扣款计划异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WarrantyOrderCacheConstant.LIST,key ="#warrantyOrderVO.hashCode()")
    public List<WarrantyOrderVO> findList(WarrantyOrderVO warrantyOrderVO) {
        try {
            //构建查询条件
            QueryWrapper<WarrantyOrder> queryWrapper = queryWrapper(warrantyOrderVO);
            //执行列表查询
            List<WarrantyOrderVO> warrantyOrderVOs = BeanConv.toBeanList(list(queryWrapper),WarrantyOrderVO.class);
            return warrantyOrderVOs;
        }catch (Exception e){
            log.error("合同扣款计划列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.LIST_FAIL);
        }
    }

    @Override
    public TradeVO doPayment(String warrantyOrderId, String tradingChannel) {
        //重新查询合同订单信息
        WarrantyOrderVO warrantyOrderVO = findById(warrantyOrderId);
        if (EmptyUtil.isNullOrEmpty(warrantyOrderVO)){
            throw new RuntimeException("合同订单查询异常");
        }
        //用户信息
        UserVO userVO = SubjectContent.getUserVO();
        //订单支付
        TradeVO tradeVO = TradeVO.builder()
            .productOrderNo(Long.valueOf(warrantyOrderVO.getOrderNo()))
            .tradeChannel(tradingChannel)
            .payerId(userVO.getId())
            .companyNo(warrantyOrderVO.getCompanyNo())
            .payerName(userVO.getRealName())
            .tradeAmount(warrantyOrderVO.getPremium())
            .refund(BigDecimal.ZERO)
            .isRefund(SuperConstant.NO)
            .memo("保单"+warrantyOrderVO.getWarrantyNo()+"第"+warrantyOrderVO.getCurrentPeriod()+"期")
            .build();
        TradeVO tradeVOResult = wapPayFeign.wapTrade(tradeVO);
        return tradeVOResult;
    }

    @Override
    public TradeVO doClose(String tradeOrderNo, String tradingChannel) {
        //订单支付
        TradeVO tradeVO = TradeVO.builder()
            .tradeOrderNo(Long.valueOf(tradeOrderNo))
            .tradeChannel(tradingChannel)
            .build();
        return wapPayFeign.closeTrade(tradeVO);
    }

    @Override
    @Transactional
    public String signContract(String warrantyOrderId, String tradingChannel) {
        //复核合同订单信息
        WarrantyOrderVO warrantyOrderVO = findByIdAndOrderState(warrantyOrderId,WarrantyOrderConstant.ORDER_STATE_0);
        if (EmptyUtil.isNullOrEmpty(warrantyOrderVO)){
            throw new RuntimeException("合同订单查询异常");
        }
        UserVO userVO = SubjectContent.getUserVO();
        //签约交易对象
        TradeVO tradeVO = TradeVO.builder()
            .productOrderNo(Long.valueOf(warrantyOrderVO.getOrderNo()))
            .tradeChannel(tradingChannel)
            .payerId(userVO.getId())
            .companyNo(warrantyOrderVO.getCompanyNo())
            .payerName(userVO.getRealName())
            .notifyUrl("http://www.eeho.cn")
            .returnUrl("http://www.eeho.cn")
            .tradeAmount(BigDecimal.ZERO)
            .refund(BigDecimal.ZERO)
            .isRefund(SuperConstant.NO)
            .memo("保单"+warrantyOrderVO.getWarrantyNo()+"第"+warrantyOrderVO.getCurrentPeriod()+"期")
            .build();
        //支付宝周期扣款对象
        if (TradeConstant.TRADE_CHANNEL_ALI_PAY.equals(tradingChannel)){
            AliPeriodicVO aliPeriodicVO = AliPeriodicVO.builder()
                .signScene("INDUSTRY|REPAYMENT")
                .ruleTotalAmount(String.valueOf(warrantyOrderVO.getPremiums()))
                .ruleSingleAmount(String.valueOf(warrantyOrderVO.getPremium()))
                .ruleTotalPayments(String.valueOf(warrantyOrderVO.getPeriods()))
                .externalAgreementNo(String.valueOf(identifierGenerator.nextId(warrantyOrderVO)))
                .accessChannel("ALIPAYAPP")
                .contractNo(warrantyOrderVO.getWarrantyNo())
                .build();
            //签约扣款：周期数、周期类型、下次扣款時間
            String rulePeriod = null;
            String rulePeriodType = null;
            if (InsuranceConstant.WEEK.equals(warrantyOrderVO.getPeriodicUnit())){
                rulePeriodType = InsuranceConstant.DAY;
                rulePeriod ="7";
            }else if (InsuranceConstant.MONTH.equals(warrantyOrderVO.getPeriodicUnit())){
                rulePeriodType = InsuranceConstant.MONTH;
                rulePeriod ="1";
            }else if (InsuranceConstant.YEAR.equals(warrantyOrderVO.getPeriodicUnit())){
                rulePeriodType = InsuranceConstant.MONTH;
                rulePeriod ="12";
            }
            aliPeriodicVO.setRulePeriod(rulePeriod);
            aliPeriodicVO.setRulePeriodType(rulePeriodType);
            aliPeriodicVO.setRuleExecuteTime(warrantyOrderVO.getScheduleTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            tradeVO.setAliPeriodicVO(aliPeriodicVO);
        //微信周期扣款对象
        }else if(TradeConstant.TRADE_CHANNEL_WECHAT_PAY.equals(tradingChannel)){
            throw new RuntimeException("微信周期扣款未开发");
        //未定义周期扣款
        }else {
            throw new RuntimeException("未定义的周期性扣款");
        }
        //发起签约
        TradeVO tradeVOResult  = periodicPayFeign.h5SignContract(tradeVO);
        //返回签约签名
        return tradeVOResult.getPlaceOrderJson();
    }

    @Autowired
    IWarrantyService warrantyService;

    @Autowired
    RedissonClient redissonClient;

    @Override
    @GlobalTransactional
    public Boolean signContractSync(String warrantyOrderId, String tradingChannel, String agreementNo) {
        //合同订单信息
        WarrantyOrderVO warrantyOrderVO = findById(warrantyOrderId);
        if (EmptyUtil.isNullOrEmpty(warrantyOrderVO)){
            throw new RuntimeException("合同订单不存在");
        }
        //保险合同加锁：防止删除同时，计划任务在进行周期性扣款
        String key = "lock_warranty:"+warrantyOrderVO.getWarrantyNo();
        RLock fairLock = redissonClient.getFairLock(key);
        try {
            if (fairLock.tryLock(CacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS)){
                //合同订单关联支付签约号
                UpdateWrapper<WarrantyOrder> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda()
                    .set(WarrantyOrder::getAgreementNo,agreementNo)
                    .set(WarrantyOrder::getTradingChannel,tradingChannel)
                    .eq(WarrantyOrder::getWarrantyNo,warrantyOrderVO.getWarrantyNo());
                boolean flag = update(updateWrapper);
                if (!flag){
                    throw new RuntimeException("合同订单关联支付签约号异常");
                }
                //支付平台同步签约结果
                warrantyOrderVO.setAgreementNo(agreementNo);
                warrantyOrderVO.setTradingChannel(tradingChannel);
                flag = periodicPayFeign.signContractSync(warrantyOrderVO.getWarrantyNo(),tradingChannel,agreementNo);
                if (!flag){
                    throw new RuntimeException("签约合同关联支付签约号异常");
                }
                //扣除首期款项
                flag = this.installmentDeduction(BeanConv.toBean(warrantyOrderVO,WarrantyOrder.class));
                if (!flag){
                    throw new RuntimeException("合同订单关联支付签约号异常");
                }
                //修改保险合同状态
                UpdateWrapper<Warranty> warrantyUpdateWrapper = new UpdateWrapper<>();
                warrantyUpdateWrapper.lambda()
                    .set(Warranty::getWarrantyState,WarrantyConstant.STATE_SAFEING)
                    .eq(Warranty::getWarrantyNo,warrantyOrderVO.getWarrantyNo());
                return warrantyService.update(warrantyUpdateWrapper);
            }
        }catch (Exception e){
            log.error("保险合同加锁失败：{}",ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyEnum.DEL_FAIL);
        }finally {
            fairLock.unlock();
        }
        return false;

    }

    /***
     * @description 扣款操作
     * @param warrantyOrder
     * @return
     */
    private Boolean installmentDeduction(WarrantyOrder warrantyOrder){
        //签约信息
        TradeVO tradeVO = TradeVO.builder()
            .productOrderNo(Long.valueOf(warrantyOrder.getOrderNo()))
            .tradeChannel(warrantyOrder.getTradingChannel())
            .payerId(Long.valueOf(warrantyOrder.getApplicantIdentityCard()))
            .companyNo(warrantyOrder.getCompanyNo())
            .payerName(warrantyOrder.getApplicantName())
            .tradeAmount(warrantyOrder.getPremium())
            .refund(BigDecimal.ZERO)
            .isRefund(SuperConstant.NO)
            .memo("保单"+warrantyOrder.getWarrantyNo()+"第"+warrantyOrder.getCurrentPeriod()+"期")
            .build();
        if (TradeConstant.TRADE_CHANNEL_ALI_PAY.equals(warrantyOrder.getTradingChannel())){
            AliPeriodicVO aliPeriodicVO = AliPeriodicVO.builder()
                    .agreementNo(warrantyOrder.getAgreementNo()).build();
            tradeVO.setAliPeriodicVO(aliPeriodicVO);
        }
        TradeVO tradeVOResult = periodicPayFeign.h5PeriodicPay(tradeVO);
        //周期扣款成功
        if (TradeConstant.TRADE_SUCCESS.equals(tradeVOResult.getTradeState())){
            //修改合同订单状态
            warrantyOrder.setOrderState(WarrantyOrderConstant.ORDER_STATE_1);
            warrantyOrder.setActualTime(LocalDateTime.now());
            return updateById(warrantyOrder);
            //周期扣款失败
        }else {
            //修改合同订单状态
            warrantyOrder.setOrderState(WarrantyOrderConstant.ORDER_STATE_4);
            warrantyOrder.setActualTime(LocalDateTime.now());
            return updateById(warrantyOrder);
        }
    }
    @Override
    @GlobalTransactional
    public Boolean doCloseSignContract(String warrantyOrderId, String tradingChannel) {
        //重新查询合同订单信息
        WarrantyOrderVO warrantyOrderVO = findById(warrantyOrderId);
        if (EmptyUtil.isNullOrEmpty(warrantyOrderVO)){
            throw new RuntimeException("合同订单查询异常");
        }
        //废弃支付签约号
        UpdateWrapper<WarrantyOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(WarrantyOrder::getAgreementNo,null)
            .eq(WarrantyOrder::getWarrantyNo,warrantyOrderVO.getWarrantyNo());
        boolean flag =update(updateWrapper);
        if (!flag){
            return flag;
        }
        return periodicPayFeign.h5CloseSignContract(warrantyOrderVO.getWarrantyNo(),tradingChannel);
    }

    @Override
    public Boolean periodicPay() {
        //查询当天应该支付订单
        LocalDateTime localDateTime = LocalDateTime.now();
        String minTime = localDateTime.with(LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String maxTime = localDateTime.with(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        QueryWrapper<WarrantyOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .between(WarrantyOrder::getScheduleTime, minTime,maxTime)
            .gt(WarrantyOrder::getPeriods,1);
        List<WarrantyOrder> warrantyOrderList = list(queryWrapper);
        if (EmptyUtil.isNullOrEmpty(warrantyOrderList)){
            return true;
        }
        //线程处周期性扣款
        for (WarrantyOrder warrantyOrder: warrantyOrderList) {
            periodicPayExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    installmentDeduction(warrantyOrder);
                }
            });
        }
        return true;
    }

    /**
     * 统计分析：统计昨日投保保单类型对应的保单数量
     * @return
     */
    @Override
    public List<AnalysisInsuranceTypeDTO> analysisCustomerInsuranceType() {
        try {
            TimeDTO yesterdayTime = TimeHandlerUtils.getYesterdayTime();
            return warrantyOrderMapper.analysisCustomerInsuranceType(yesterdayTime.getBegin(),yesterdayTime.getEnd());
        }catch (Exception e){
            log.error("合同扣款计划列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.LIST_FAIL);
        }
    }

    @Override
    public Boolean syncPayment(String orderNo,String tradeState) {
        try {
            //修改合同订单状态
            UpdateWrapper<WarrantyOrder> updateWrapper = new UpdateWrapper<>();
            if (TradeConstant.TRADE_SUCCESS.equals(tradeState)){
                updateWrapper.lambda()
                .set(WarrantyOrder::getOrderState,WarrantyOrderConstant.ORDER_STATE_1)
                .eq(WarrantyOrder::getOrderNo,orderNo);
            }else {
                updateWrapper.lambda()
                .set(WarrantyOrder::getOrderState,WarrantyOrderConstant.ORDER_STATE_4)
                .eq(WarrantyOrder::getOrderNo,orderNo);
            }
            Boolean flag = update(updateWrapper);
            if(!flag) {
                return flag;
            }
            //查询保险合同订单
            QueryWrapper<WarrantyOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WarrantyOrder::getOrderNo,orderNo);
            WarrantyOrder warrantyOrder = getOne(queryWrapper);
            //修改保险合同状态
            UpdateWrapper<Warranty> warrantyUpdateWrapper = new UpdateWrapper<>();
            warrantyUpdateWrapper.lambda()
                .set(Warranty::getWarrantyState,WarrantyConstant.STATE_SAFEING)
                .eq(Warranty::getWarrantyNo,warrantyOrder.getWarrantyNo());
            return warrantyService.update(warrantyUpdateWrapper);
        }catch (Exception e){
            log.error("合同同步结果异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WarrantyOrderEnum.SYNC_PAYMENT_FAIL);
        }
    }

    /**
     * 统计保险数量及保险金额
     * @return
     */
    @Override
    public AnalysisCustomerInsuranceDTO analysisInsurance() {
        try {
            TimeDTO yesterdayTime = TimeHandlerUtils.getYesterdayTime();
            return warrantyOrderMapper.analysisInsurance(yesterdayTime.getBegin(),yesterdayTime.getEnd());
        }catch (Exception e){
            log.error("统计保险数量及保险金额异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AnalysisEnum.ANALYSIS_CUSTOMER_CITY);
        }
    }

    /**
     * 统计投保人性别数据
     * @return
     */
    @Override
    public AnalysisCustomerSexDTO analysisWarrantySex() {
        try {
            TimeDTO yesterdayTime = TimeHandlerUtils.getYesterdayTime();
            return warrantyOrderMapper.analysisWarrantySex(yesterdayTime.getBegin(),yesterdayTime.getEnd());
        }catch (Exception e){
            log.error("统计投保人性别异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(AnalysisEnum.ANALYSIS_CUSTOMER_SEX);
        }
    }
}
