package com.itheima.bolee.insurance.mapper;

import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerInsuranceDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisCustomerSexDTO;
import com.itheima.bolee.framework.commons.dto.analysis.AnalysisInsuranceTypeDTO;
import com.itheima.bolee.insurance.pojo.WarrantyOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description：合同扣款计划Mapper接口
 */
@Mapper
public interface WarrantyOrderMapper extends BaseMapper<WarrantyOrder> {

    /**
     * 统计分析：统计昨日保费及保单数量
     * @param begin
     * @param end
     * @return
     */
    @Select("SELECT IFNULL(SUM(premium),0) AS money,COUNT(DISTINCT(warranty_no)) AS insurance_nums FROM tab_warranty_order WHERE create_time >= #{begin} AND create_time <= #{end} AND data_state IN (1,4) AND data_state = 0")
    AnalysisCustomerInsuranceDTO analysisInsurance(@Param("begin") String begin,@Param("end") String end);

    /**
     * 统计分析：统计保险
     * @param begin
     * @param end
     * @return
     */
    @Select("SELECT " +
            "  IFNULL(SUM(CASE WHEN SUBSTR(applicant_identity_card, 17, 1) % 2 = 0 THEN premiums ELSE 0 END), 0) AS sexManNums," +
            "  IFNULL(SUM(CASE WHEN SUBSTR(applicant_identity_card, 17, 1) % 2 = 1 THEN premiums ELSE 0 END), 0) AS sexWomanNums " +
            "FROM " +
            "  `tab_warranty`" +
            " WHERE " +
            "   `create_time` >= #{begin} " +
            "   AND " +
            "   `create_time` <= #{end} " +
            "   AND " +
            "   data_state = 0" +
            "   AND " +
            "   warranty_state IN (0,1,2)")
    AnalysisCustomerSexDTO analysisWarrantySex(@Param("begin") String begin,@Param("end") String end);

    @Select("SELECT \n" +
            "  insure.`check_rule` AS insuranceTypeId,\n" +
            "  COUNT(DISTINCT(warr.warranty_no)) AS insuranceNums\n" +
            "FROM\n" +
            "  `tab_warranty_order` warrOrder \n" +
            "  LEFT JOIN `tab_warranty` warr \n" +
            "    ON warrOrder.`warranty_no` = warr.`warranty_no`\n" +
            "  LEFT JOIN `tab_insurance` insure \n" +
            "    ON insure.id = warr.`insurance_id`\n" +
            "  WHERE \n" +
            "     warrOrder.`data_state` = 0\n" +
            "     AND\n" +
            "     warr.`warranty_state` != 0\n" +
            "     AND\n" +
            "     warr.create_time BETWEEN #{begin} AND #{end}\n" +
            "  GROUP BY insure.`check_rule`")
    List<AnalysisInsuranceTypeDTO> analysisCustomerInsuranceType(@Param("begin") String begin,@Param("end") String end);
}
