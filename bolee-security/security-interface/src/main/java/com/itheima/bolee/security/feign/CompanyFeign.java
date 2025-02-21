package com.itheima.bolee.security.feign;

import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.security.hystrix.UserHtstrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description：用户权限适配服务接口定义
 */
@FeignClient(value = "security-web", fallback = UserHtstrix.class)
public interface CompanyFeign {

   /***
    * @description 按企业号查询公司
    * @param companyNo 企业号
    * @return
    */
    @PostMapping("company-feign/find-company/{companyNo}")
    CompanyVO findCompanyByNo(@PathVariable("companyNo") String companyNo);

    /***
     * @description 按多个企业号查询公司
     * @param companyNos 企业号
     * @return
     */
    @PostMapping("company-feign/find-company")
    List<CompanyVO> findCompanyByNos(@RequestBody List<String> companyNos);
}
