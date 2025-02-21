package com.itheima.bolee.insurance.mapper;

import com.itheima.bolee.insurance.dto.HomeSafeguardDTO;
import com.itheima.bolee.insurance.pojo.Warranty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description：合同信息Mapper接口
 */
@Mapper
public interface WarrantyMapper extends BaseMapper<Warranty> {


    @Select("SELECT " +
            "  war.warranty_no AS warrantyNo," +
            "  warIn.insured_identity_card AS idCard," +
            "  insu.id AS insuranceId," +
            "  insu.check_rule AS checkRule" +
            " FROM" +
            "  tab_warranty_insured warIn" +
            "  LEFT JOIN tab_warranty war" +
            "    ON warIn.warranty_no = war.warranty_no" +
            "  LEFT JOIN tab_insurance insu " +
            "    ON insu.id = war.insurance_id" +
            " WHERE warIn.insured_identity_card IN" +
            " (${idCardList}) "+
            "  AND war.data_state = #{dataState}" +
            "  AND war.warranty_state = #{warrantyState}")
    List<HomeSafeguardDTO> findHomeSafeInfo(@Param("idCardList") String idCard, @Param("dataState") String dataState, @Param("warrantyState") String warrantyState);

}