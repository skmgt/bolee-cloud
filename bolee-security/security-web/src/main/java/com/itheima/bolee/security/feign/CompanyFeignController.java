package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.security.pojo.Company;
import com.itheima.bolee.security.service.ICompanyService;
import com.itheima.bolee.security.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CompanyFeignController
 *
 * @author: wgl
 * @describe: 系统保险机构租户
 * @date: 2022/12/28 10:10
 */
@RestController
@RequestMapping("company-feign")
@Api(tags = "企业feign管理")
@Slf4j
public class CompanyFeignController {

    @Autowired
    ICompanyService companyService;

    /**
     * @Description 按企业编号查询对应的企业信息
     * @param companyNo 企业编号
     * @return
     */
    @ApiOperation(value = "根据企业编号查找企业",notes = "根据企业编号查找企业信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "companyNo",value = "企业编号",dataType = "String")
    })
    @PostMapping("find-company/{companyNo}")
    public CompanyVO findCompanyByNo(@PathVariable("companyNo") String companyNo){
        return companyService.findCompanyByNo(companyNo);
    }


    /**
     * @Description 按企业编号列表查询对应的企业信息
     * @param companyNos 企业编号列表
     * @return
     */
    @ApiOperation(value = "根据企业编号查找企业信息",notes = "按企业编号列表查询企业")
    @PostMapping("find-company")
    public List<CompanyVO> findCompanyByNos(@RequestBody List<String> companyNos){
        List<Long> validCompanyNos = new ArrayList<>();
        for (String companyNo : companyNos) {
            validCompanyNos.add(Long.parseLong(companyNo));
        }
        CompanyVO companyVO = CompanyVO.builder().
                checkIds(validCompanyNos.toArray(new Long[0])).
                dataState(SuperConstant.DATA_STATE_0).
                build();
        return companyService.findCompanyList(companyVO);
    }
}
