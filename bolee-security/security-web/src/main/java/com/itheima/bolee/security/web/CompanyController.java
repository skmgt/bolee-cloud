package com.itheima.bolee.security.web;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.dto.basic.TreeVO;
import com.itheima.bolee.framework.commons.dto.security.CompanyVO;
import com.itheima.bolee.framework.commons.enums.security.CompanyEnum;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.security.service.ICompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：企业前端控制器
 */
@Slf4j
@Api(tags = "企业管理")
@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    ICompanyService companyService;

    /***
     * @description 多条件查询企业分页列表
     * @param companyVO 企业Vo查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<CompanyVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "企业分页",notes = "企业分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "companyVO",value = "企业Vo对象",required = true,dataType = "CompanyVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    public ResponseResult<Page<CompanyVO>> findCompanyVOPage(
                                    @RequestBody CompanyVO companyVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<CompanyVO> companyVOPage = companyService.findCompanyPage(companyVO, pageNum, pageSize);
        return ResponseResultBuild.build(CompanyEnum.SUCCEED,companyVOPage);
    }

    /**
     * @Description 保存企业
     * @param companyVO 企业Vo对象
     * @return CompanyVO
     */
    @PutMapping
    @ApiOperation(value = "企业添加",notes = "企业添加")
    @ApiImplicitParam(name = "companyVO",value = "企业Vo对象",required = true,dataType = "CompanyVO")
    public ResponseResult<CompanyVO> createCompany(@RequestBody CompanyVO companyVO) {
        CompanyVO companyVOResult = companyService.createCompany(companyVO);
        return ResponseResultBuild.build(CompanyEnum.SUCCEED,companyVOResult);
    }

    /**
     * @Description 修改企业
     * @param companyVO 企业Vo对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "企业修改",notes = "企业修改")
    @ApiImplicitParam(name = "companyVO",value = "企业Vo对象",required = true,dataType = "CompanyVO")
    public ResponseResult<Boolean> updateCompany(@RequestBody CompanyVO companyVO) {
        Boolean flag = companyService.updateCompany(companyVO);
        return ResponseResultBuild.build(CompanyEnum.SUCCEED,flag);
    }


    /***
     * @description 多条件查询企业列表
     * @param companyVO 企业Vo对象
     * @return List<CompanyVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "企业列表",notes = "企业列表")
    @ApiImplicitParam(name = "companyVO",value = "企业Vo对象",required = true,dataType = "CompanyVO")
    public ResponseResult<List<CompanyVO>> companyList(@RequestBody CompanyVO companyVO) {
        List<CompanyVO> companyVOList = companyService.findCompanyList(companyVO);
        return ResponseResultBuild.build(CompanyEnum.SUCCEED,companyVOList);
    }

}
