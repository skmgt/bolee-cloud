package com.itheima.bolee.insurance.mapper;

import com.itheima.bolee.insurance.dto.SickVO;
import com.itheima.bolee.insurance.pojo.SickSearchRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description：疾病搜索记录Mapper接口
 */
@Mapper
public interface SickSearchRecordMapper extends BaseMapper<SickSearchRecord> {

    @Select("SELECT ts.*" +
            "FROM (" +
            "    SELECT DISTINCT content" +
            "    FROM tab_sick_search_record" +
            "    WHERE create_time >= #{begin} AND create_time <= #{end}" +
            "    GROUP BY content" +
            "    ORDER BY COUNT(content) DESC" +
            "    LIMIT 10" +
            ") AS top_content " +
            "JOIN tab_sick AS ts ON top_content.content = ts.sick_key")
    List<SickVO> findSearchList(@Param("begin") String begin,@Param("end") String end);
}
