package com.itheima.bolee.insurance.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * HomePeopleVO
 *
 * @author: wgl
 * @describe: 家庭人身保障
 * @date: 2022/12/28 10:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="家庭人身保障对象", description="家庭人身保障")
public class HomePeopleVO {

    @ApiModelProperty(value = "姓名(脱敏后)")
    private String name;

    @ApiModelProperty(value = "保险信息")
    private List<InsureTypeOnHomePeopleVO> type;

    @ApiModelProperty(value = "关系")
    private String relation;

    @ApiModelProperty(value = "性别")
    private String sex;
}
