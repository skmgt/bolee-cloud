package com.itheima.bolee.security.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.security.pojo.Company;

import java.util.List;

/**
 * @Description：企业服务类
 */
public interface ICompanyService extends IService<Company> {

    /**
     * @Description 多条件查询客户分页列表
     * @param companyVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<Company>
     */
    Page<CompanyVO> findCompanyPage(CompanyVO companyVO, int pageNum, int pageSize);

    /**
     * @Description 创建客户
     * @param companyVO 对象信息
     * @return Company
     */
    CompanyVO createCompany(CompanyVO companyVO);

    /**
     * @Description 修改客户
     * @param companyVO 对象信息
     * @return Boolean
     */
    Boolean updateCompany(CompanyVO companyVO);

    /**
     * @description 多条件查询客户列表
     * @param companyVO 查询条件
     * @return: List<Company>
     */
    List<CompanyVO> findCompanyList(CompanyVO companyVO);

    /***
     * @description 查找正式，适用且未有效所有企业
     *
     * @return
     */
    List<CompanyVO> findCompanyVOValidation();

    /**
     * 根据企业编号查找企业数据
     * @param companyNo
     * @return
     */
    CompanyVO findCompanyByNo(String companyNo);
}
