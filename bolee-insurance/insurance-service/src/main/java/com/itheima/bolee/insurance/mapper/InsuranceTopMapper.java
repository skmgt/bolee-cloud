package com.itheima.bolee.insurance.mapper;

import com.itheima.bolee.insurance.pojo.InsuranceTop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description：人气保险Mapper接口
 */
@Mapper
public interface InsuranceTopMapper extends BaseMapper<InsuranceTop> {


    /**
     * SELECT top.* FROM `tab_insurance_top` top LEFT JOIN `tab_insurance` insure ON insure.id = top.`insurance_id` WHERE insure.`data_state` = 0 AND top.`create_time`>= DATE_SUB(CURDATE(), INTERVAL 30 DAY) ORDER BY top.sales_volume DESC LIMIT 3;
     * @param dataState 数据状态
     * @param num   查询条数
     * @param dateLimit 日期范围
     * @return
     */
    @Select("SELECT top.* FROM tab_insurance_top top LEFT JOIN tab_insurance insure ON insure.id = top.`insurance_id` WHERE insure.`data_state` = #{dataState} AND top.`create_time`>= DATE_SUB(CURDATE(), INTERVAL ${dateLimit} DAY) ORDER BY top.sales_volume DESC LIMIT ${num}")
    List<InsuranceTop> findTopInsurance(@Param("dataState")String dataState,
                                        @Param("num") Integer num,
                                        @Param("dateLimit")Integer dateLimit);
}
