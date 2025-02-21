package com.itheima.bolee.insurance.handler.impl;

import com.itheima.bolee.insurance.handler.InsureHandler;
import org.springframework.stereotype.Service;

/**
 * @ClassName AnnuityInsureHandler.java
 * @Description 年金理财型保险投保、收益计算、保险订单生成
 */
@Service("annuityInsureHandler")
public class AnnuityInsureHandler extends EarningsInsureHandler implements InsureHandler {
}
