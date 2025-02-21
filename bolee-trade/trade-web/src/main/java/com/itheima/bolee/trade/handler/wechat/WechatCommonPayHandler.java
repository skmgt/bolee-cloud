package com.itheima.bolee.trade.handler.wechat;

import com.alibaba.fastjson.JSONObject;
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
import com.itheima.bolee.trade.client.wechat.Config;
import com.itheima.bolee.trade.client.wechat.Factory;
import com.itheima.bolee.trade.client.wechat.response.CloseResponse;
import com.itheima.bolee.trade.client.wechat.response.QueryResponse;
import com.itheima.bolee.trade.client.wechat.response.RefundResponse;
import com.itheima.bolee.trade.config.WechatPayConfig;
import com.itheima.bolee.trade.handler.BeforePayHandler;
import com.itheima.bolee.trade.handler.CommonPayHandler;
import com.itheima.bolee.trade.pojo.RefundRecord;
import com.itheima.bolee.trade.pojo.Trade;
import com.itheima.bolee.trade.service.IRefundRecordService;
import com.itheima.bolee.trade.service.ITradeService;
import com.itheima.bolee.trade.utils.ResponseChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName BasicPayHandlerImpl.java
 * @Description 微信交易基础类
 */
@Service
@Slf4j
public class WechatCommonPayHandler implements CommonPayHandler {

    @Autowired
    ITradeService tradeService;

    @Autowired
    IRefundRecordService refundRecordService;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    BeforePayHandler beforePayHandler;

    @Autowired
    WechatPayConfig wechatPayConfig;

    @Override
    public TradeVO queryTrade(TradeVO tradeVO) {
        //1、查询前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeQueryTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.CHECK_TRADE_FAIL);
        }
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config(tradeVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        //5、调用接口
        try {
            QueryResponse queryResponse = factory.Common()
                .query(String.valueOf(tradeVO.getTradeOrderNo()));
            //6、判断响应是否成功
            boolean success = ResponseChecker.success(queryResponse);
            //7、响应成功，分析交易状态
            if (success&&!EmptyUtil.isNullOrEmpty(queryResponse.getTradeState())){
                //SUCCESS：支付成功,REFUND：转入退款,NOTPAY：未支付,CLOSED：已关闭,REVOKED：已撤销（仅付款码支付会返回）
                //USERPAYING：用户支付中（仅付款码支付会返回）,PAYERROR：支付失败（仅付款码支付会返回）
                switch (queryResponse.getTradeState()){
                    case TradeConstant.WECHAT_TRADE_CLOSED:
                        tradeHandler.setTradeState(TradeConstant.TRADE_CLOSED);break;
                    case TradeConstant.WECHAT_TRADE_REVOKED:
                        tradeHandler.setTradeState(TradeConstant.TRADE_CLOSED);break;
                    case TradeConstant.WECHAT_TRADE_SUCCESS:
                        tradeHandler.setTradeState(TradeConstant.TRADE_SUCCESS);break;
                    case TradeConstant.WECHAT_TRADE_REFUND:
                        tradeHandler.setTradeState(TradeConstant.TRADE_SUCCESS);break;
                    default:
                        flag = false;break;
                }
                //8、修改交易单状态
                if (flag){
                    tradeHandler.setResultCode(queryResponse.getTradeState());
                    tradeHandler.setResultMsg(queryResponse.getTradeStateDesc());
                    tradeHandler.setResultJson(JSONObject.toJSONString(queryResponse));
                    tradeService.updateById(tradeHandler);
                    return BeanConv.toBean(tradeHandler,TradeVO.class);
                }else {
                    log.info("查询微信交易单：{},结果：{}", tradeVO.getTradeOrderNo(),queryResponse.getTradeState());
                }
            }else {
                throw new RuntimeException("查询微信统一下单失败！");
            }
        } catch (Exception e) {
            log.warn("查询微信统一下单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询微信统一下单失败！");
        }
        return tradeVO;
    }

    @Override
    public TradeVO refundTrade(TradeVO tradeVO) {
        //1、生成退款请求编号
        tradeVO.setOutRequestNo(String.valueOf(identifierGenerator.nextId(tradeVO)));
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        tradeVO.setTradeAmount(tradeHandler.getTradeAmount());
        //2、退款前置处理：检测交易单参数
        Boolean flag = beforePayHandler.checkeRefundTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }
        //3、退款前置处理：退款幂等性校验
        beforePayHandler.idempotentRefundTrade(tradeVO);
        //4、获得微信客户端
        Config config = wechatPayConfig.config(tradeVO.getCompanyNo());
        //5、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //6、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        //7、调用接口
        try {
            RefundResponse refundResponse = factory.Common().refund(
                    String.valueOf(tradeVO.getTradeOrderNo()),
                    String.valueOf(tradeVO.getOperTionRefund()),
                    tradeVO.getOutRequestNo(),String.valueOf(tradeVO.getTradeAmount()));
            boolean success = ResponseChecker.success(refundResponse);
            if (success&&String.valueOf(tradeVO.getTradeOrderNo()).equals(refundResponse.getOutTradeNo())){
                //8、指定此交易单是否有退款：YES
                tradeHandler.setIsRefund(SuperConstant.YES);
                tradeHandler.setRefund(tradeVO.getRefund().add(tradeVO.getOperTionRefund()));
                Trade trade = BeanConv.toBean(tradeVO, Trade.class);
                tradeService.updateById(trade);
                //6、保存退款单信息
                RefundRecord refundRecord = RefundRecord.builder()
                    .companyNo(tradeHandler.getCompanyNo())
                    .refundNo(tradeVO.getOutRequestNo())
                    .refundAmount(tradeVO.getOperTionRefund())
                    .refundCode(refundResponse.getCode())
                    .refundMsg(refundResponse.getMessage())
                    .productOrderNo(tradeHandler.getProductOrderNo())
                    .tradeChannel(tradeHandler.getTradeChannel())
                    .tradeOrderNo(tradeHandler.getTradeOrderNo())
                    .memo("退款："+tradeHandler.getMemo())
                    .build();
                switch (refundResponse.getStatus()){
                    case TradeConstant.WECHAT_REFUND_SUCCESS:
                        refundRecord.setRefundStatus(TradeConstant.REFUND_STATUS_SUCCESS);break;
                    case TradeConstant.WECHAT_REFUND_CLOSED:
                        refundRecord.setRefundStatus(TradeConstant.REFUND_STATUS_CLOSED);break;
                    case TradeConstant.WECHAT_REFUND_PROCESSING:
                        refundRecord.setRefundStatus(TradeConstant.REFUND_STATUS_SENDING);break;
                    default:
                        refundRecord.setRefundStatus(TradeConstant.REFUND_STATUS_FAIL);break;
                }
                refundRecordService.save(refundRecord);
            }else {
                log.error("网关：微信统一下单退款失败：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(refundResponse));
                throw new RuntimeException("网关：微信统一下单退款失败!");
            }
        } catch (Exception e) {
            log.error("微信统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradeEnum.TRAD_REFUND_FAIL);
        }
        return BeanConv.toBean(tradeHandler,TradeVO.class);
    }

    @Override
    public RefundRecordVO queryRefundTrade(RefundRecordVO refundRecordVO) {
        //1、退款前置处理：检测退款单参数
        Boolean flag = beforePayHandler.checkeQueryRefundTrade(refundRecordVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_QUERY_REFUND_FAIL);
        }
        RefundRecord refundRecordHandler = refundRecordService.findRefundRecordByRefundNo(refundRecordVO.getRefundNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config(refundRecordVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、指定配置文件
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            RefundResponse refundResponse = factory.Common().queryRefund(refundRecordVO.getRefundNo());
            //5、判断响应是否成功
            boolean success = ResponseChecker.success(refundResponse);
            //6、查询出的退款状态
            if (success&&TradeConstant.WECHAT_REFUND_SUCCESS.equals(refundResponse.getStatus())){
                refundRecordHandler.setRefundStatus(TradeConstant.REFUND_STATUS_SUCCESS);
                refundRecordHandler.setRefundCode(refundResponse.getCode());
                refundRecordHandler.setRefundMsg(refundResponse.getMessage());
                refundRecordService.updateById(BeanConv.toBean(refundRecordHandler,RefundRecord.class));
            }else {
                log.error("网关：查询微信统一下单退款失败：{},结果：{}", refundRecordVO.getTradeOrderNo(),
                        JSONObject.toJSONString(refundResponse));
                throw new RuntimeException("网关：查询微信统一下单退款失败！");
            }
        } catch (Exception e) {
            log.warn("查询微信统一下单退款失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new RuntimeException("查询微信统一下单退款失败！");
        }
        return refundRecordVO;
    }

    @Override
    public TradeVO closeTrade(TradeVO tradeVO) {
        //1、退款前置处理：检测关闭参数tradeVO
        Boolean flag = beforePayHandler.checkeCloseTrade(tradeVO);
        if (!flag){
            throw new ProjectException(TradeEnum.TRAD_CLOSE_FAIL);
        }
        Trade tradeHandler = tradeService.findTradByTradeOrderNo(tradeVO.getTradeOrderNo());
        //2、获得微信客户端
        Config config = wechatPayConfig.config(tradeVO.getCompanyNo());
        //3、配置如果为空，抛出异常
        if (EmptyUtil.isNullOrEmpty(config)){
            throw new ProjectException(TradeEnum.CONFIG_ERROR);
        }
        //4、使用配置
        Factory factory = new Factory();
        factory.setOptions(config);
        try {
            //5、调用三方API关闭订单
            CloseResponse closeResponse = factory.Common()
                    .close(String.valueOf(tradeVO.getTradeOrderNo()));
            //6、关闭订单受理情况
            boolean success = ResponseChecker.success(closeResponse);
            if (success){
                tradeHandler.setTradeState(TradeConstant.TRADE_CLOSED);
                tradeService.updateById(tradeHandler);
                return BeanConv.toBean(tradeHandler,TradeVO.class);
            }else {
                log.error("网关：微信关闭订单失败：{},结果：{}", tradeVO.getTradeOrderNo(),
                        JSONObject.toJSONString(closeResponse));
                throw  new RuntimeException("网关：微信关闭订单失败!");
            }
        } catch (Exception e) {
            log.warn("微信关闭订单失败：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw  new ProjectException(TradeEnum.TRAD_CLOSE_FAIL);
        }
    }

    @Override
    public TradeVO downLoadBill(TradeVO tradeVO) {
        //toDo 后面补齐
        throw  new RuntimeException("未支持：微信关下载账单!");
    }


}
