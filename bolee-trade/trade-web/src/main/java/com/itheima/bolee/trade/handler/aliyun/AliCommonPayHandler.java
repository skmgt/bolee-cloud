package com.itheima.bolee.trade.handler.aliyun;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.*;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.trade.TradeConstant;
import com.itheima.bolee.framework.commons.dto.trade.RefundRecordVO;
import com.itheima.bolee.framework.commons.dto.trade.TradeVO;
import com.itheima.bolee.framework.commons.enums.trade.TradeEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.TradeSource;
import com.itheima.bolee.trade.handler.BeforePayHandler;
import com.itheima.bolee.trade.handler.CommonPayHandler;
import com.itheima.bolee.trade.service.IRefundRecordService;
import com.itheima.bolee.trade.service.ISignContractService;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.client.alipay.Factory;
import com.itheima.bolee.trade.config.AliPayConfig;
import com.itheima.bolee.trade.pojo.RefundRecord;
import com.itheima.bolee.trade.pojo.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @ClassName AliCommonPayHandler.java
 * @Description 阿里支付基础支付
 */
@Slf4j
@Service
public class AliCommonPayHandler implements CommonPayHandler {

    @Autowired
    AliPayConfig aliPayConfig;

    @Autowired
    ITradeService tradeService;

    @Autowired
    ISignContractService signContractService;

    @Autowired
    IRefundRecordService refundRecordService;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    BeforePayHandler beforePayHandler;

    @Autowired
    TradeSource tradeSource;

    @Override
    public TradeVO queryTrade(TradeVO tradeVO) {
        //1、查询前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeQueryTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        //2、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //5、调用支付宝API：通用查询支付情况
            AlipayTradeQueryResponse queryResponse = factory.Common()
                .query(String.valueOf(tradeVO.getTradeOrderNo()));
            //6、判断响应是否成功
            boolean success = ResponseChecker.success(queryResponse);
            //7、响应成功，分析交易状态
            if (success&&!EmptyUtil.isNullOrEmpty(queryResponse.getTradeStatus())){
                switch (queryResponse.getTradeStatus()){
                    //支付取消：TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
                    case TradeConstant.ALI_TRADE_CLOSED:
                        tradeHandler.setTradeState(TradeConstant.TRADE_CLOSED);break;
                    //支付成功：TRADE_SUCCESS（交易支付成功）
                    case TradeConstant.ALI_TRADE_SUCCESS:
                        tradeHandler.setTradeState(TradeConstant.TRADE_SUCCESS);break;
                    //支付成功：TRADE_FINISHED（交易结束，不可退款）
                    case TradeConstant.ALI_TRADE_FINISHED:
                        tradeHandler.setTradeState(TradeConstant.TRADE_SUCCESS);break;
                    //非最终状态不处理，当前交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）不处理
                    default:
                        flag = false;break;
                }
                //8.1、修改交易单状态
                if (flag){
                    tradeHandler.setResultCode(queryResponse.getSubCode());
                    tradeHandler.setResultMsg(queryResponse.getSubMsg());
                    tradeHandler.setResultJson(JSONObject.toJSONString(queryResponse));
                    tradeService.updateById(tradeHandler);
                    //8.2 发送同步业务信息的MQ信息
                    Long messageId = (Long) identifierGenerator.nextId(tradeHandler);
                    MqMessage mqMessage = MqMessage.builder()
                        .id(messageId)
                        .title("trade-message")
                        .content(JSONObject.toJSONString(tradeHandler))
                        .messageType("trade-project-sync")
                        .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                        .sender("system")
                        .build();
                    //指定通知的企业
                    Message<MqMessage> message = MessageBuilder.withPayload(mqMessage)
                            .setHeader("type", "trade-key").build();
                    tradeSource.tradeOutput().send(message);
                    return BeanConv.toBean(tradeHandler,TradeVO.class);
                }else {
                    log.info("查询支付宝交易单：{},结果：{}", tradeVO.getTradeOrderNo(),queryResponse.getTradeStatus());
                }
            }else {
                throw new RuntimeException("网关：查询支付宝统一下单失败！");
            }
        } catch (Exception e) {
            log.warn("查询支付宝统一下单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询支付宝统一下单失败！");
        }
        //7、返回结果
        return tradeVO;
    }

    @Override
    public TradeVO refundTrade(TradeVO tradeVO) {
        //1、生成退款请求编号
        tradeVO.setOutRequestNo(String.valueOf(identifierGenerator.nextId(tradeVO)));
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        tradeVO.setTradeAmount(tradeHandler.getTradeAmount());
        tradeVO.setRefund(tradeHandler.getRefund());
        //2、退款前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeRefundTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        //3、退款前置处理：退款幂等性校验
        beforePayHandler.idempotentRefundTrade(tradeVO);
        //4、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //5、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //6、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        //7、调用退款接口
        try {
            AlipayTradeRefundResponse refundResponse = factory.Common()
                .optional("out_request_no", tradeVO.getOutRequestNo())
                .refund(String.valueOf(tradeVO.getTradeOrderNo()),
                        String.valueOf(tradeVO.getOperTionRefund()));
            //8、判断响应是否成功
            boolean success = ResponseChecker.success(refundResponse);
            if (success&&String.valueOf(tradeVO.getTradeOrderNo()).equals(refundResponse.getOutTradeNo())){
                //9、保存交易单信息
                tradeHandler.setIsRefund(SuperConstant.YES);
                tradeHandler.setRefund(tradeHandler.getRefund().add(tradeVO.getOperTionRefund()));
                tradeService.updateById(tradeHandler);
                //10、保存退款单信息
                RefundRecord refundRecord = RefundRecord.builder()
                    .companyNo(tradeHandler.getCompanyNo())
                    .refundNo(tradeVO.getOutRequestNo())
                    .refundAmount(tradeVO.getOperTionRefund())
                    .refundCode(refundResponse.getSubCode())
                    .refundMsg(refundResponse.getSubMsg())
                    .productOrderNo(tradeHandler.getProductOrderNo())
                    .tradeChannel(tradeHandler.getTradeChannel())
                    .tradeOrderNo(tradeHandler.getTradeOrderNo())
                    .refundStatus(success?TradeConstant.REFUND_STATUS_SENDING:TradeConstant.REFUND_STATUS_FAIL)
                    .memo("退款："+tradeHandler.getMemo())
                    .build();
                refundRecordService.save(refundRecord);
            }else {
                log.error("网关：支付宝统一下单退款：{},结果：{}", tradeVO.getTradeOrderNo(),
                    JSONObject.toJSONString(refundResponse));
                throw new RuntimeException("网关：支付宝统一下单退款失败");
            }
        } catch (Exception e) {
            log.error("支付宝统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }
        return BeanConv.toBean(tradeHandler,TradeVO.class);
    }

    @Override
    public RefundRecordVO queryRefundTrade(RefundRecordVO refundRecordVO) {
        //1、退款前置处理：检测退款单参数
        Boolean flag = beforePayHandler.checkeQueryRefundTrade(refundRecordVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        RefundRecord refundRecordHandler = refundRecordService.findRefundRecordByRefundNo(refundRecordVO.getRefundNo());
        //2、获得支付宝配置文件
        Config config = aliPayConfig.config(refundRecordVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            AlipayTradeFastpayRefundQueryResponse refundQueryResponse =
                factory.Common().queryRefund(
                String.valueOf(refundRecordVO.getTradeOrderNo()),
                refundRecordVO.getRefundNo());
            //5、判断响应是否成功
            boolean success = ResponseChecker.success(refundQueryResponse);
            //6、查询出的退款状态
            String refundStatus = refundQueryResponse.getRefundStatus();
            if (success&&TradeConstant.REFUND_SUCCESS.equals(refundStatus)){
                refundRecordHandler.setRefundStatus(TradeConstant.REFUND_STATUS_SUCCESS);
                refundRecordHandler.setRefundCode(refundQueryResponse.getSubCode());
                refundRecordHandler.setRefundMsg(refundQueryResponse.getSubMsg());
                refundRecordService.updateById(refundRecordHandler);
            }else {
                log.error("网关：查询支付宝统一下单退款：{},结果：{}", refundRecordVO.getTradeOrderNo(),
                    JSONObject.toJSONString(refundQueryResponse));
                throw new RuntimeException("网关：查询支付宝统一下单退款失败！");
            }
        } catch (Exception e) {
            log.warn("查询支付宝统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询支付宝统一下单退款失败！");
        }
        return refundRecordVO;
    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        //1、退款前置处理：检测关闭参数tradeVO
        Boolean flag = beforePayHandler.checkeCloseTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        //2、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //5、调用三方API关闭订单
            AlipayTradeCloseResponse closeResponse = factory.Common().close(
                String.valueOf(tradeVO.getTradeOrderNo()));
            //6、关闭订单受理情况
            boolean success = ResponseChecker.success(closeResponse);
            if (closeResponse.getCode().equals("40004")||
                (success&&tradeVO.getTradeOrderNo().equals(closeResponse.getOutTradeNo()))){
                tradeHandler.setTradeState(TradeConstant.TRADE_CLOSED);
                tradeService.updateById(tradeHandler);
                return BeanConv.toBean(tradeHandler,TradeVO.class);
            }else {
                log.error("网关：支付宝关闭订单：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(closeResponse));
                throw  new RuntimeException("网关：支付宝关闭订单失败!");
            }
        } catch (Exception e) {
            log.warn("关闭订单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw  new ProjectException(TradeEnum.TRAD_CLOSE_FAIL);
        }
    }

    @Override
    public TradeVO downLoadBill(TradeVO tradeVO) {
        //1、下载账单前置处理：检测关闭参数tradeVO
        Boolean flag = beforePayHandler.checkeDownLoadBill(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        //2、获得支付宝配置文件
        Config config = aliPayConfig.config(tradeVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryResponse = factory.Common()
                .downloadBill(tradeVO.getBillType(), DateUtil.formatDate(tradeVO.getBillDate()));
            //5、请求下载受理情况
            boolean success = ResponseChecker.success(billDownloadurlQueryResponse);
            if (success){
                String billDownloadUrl = billDownloadurlQueryResponse.getBillDownloadUrl();
                tradeVO.setBillDownloadUrl(billDownloadUrl);
            }else {
                log.warn("网关：支付宝下载订单类型：{},时间：{},结果：{}", tradeVO.getBillType(),tradeVO.getBillDate(),
                        JSONObject.toJSONString(billDownloadurlQueryResponse));
                throw new RuntimeException("网关：支付宝下载订单失败！");
            }
        } catch (Exception e) {
            log.warn("支付宝下载订单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw  new RuntimeException("支付宝下载订单失败！");
        }
        return tradeVO;
    }

}
