package com.itheima.bolee.insurance.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.itheima.bolee.framework.commons.basic.ResponseResult;
import com.itheima.bolee.framework.commons.utils.ResponseResultBuild;
import com.itheima.bolee.insurance.dto.BannerVO;
import com.itheima.bolee.insurance.service.IBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：响应接口
 */
@Slf4j
@Api(tags = "导航信息")
@RestController
@RequestMapping("banner")
public class BannerController {

    @Autowired
    IBannerService bannerService;

    /***
     * @description 多条件查询分页
     * @param bannerVO VO查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return: Page<BannerVO>
     */
    @PostMapping("page/{pageNum}/{pageSize}")
    @ApiOperation(value = "分页",notes = "分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "bannerVO",value = "VO对象",required = true,dataType = "BannerVO"),
        @ApiImplicitParam(paramType = "path",name = "pageNum",value = "页码",example = "1",dataType = "Integer"),
        @ApiImplicitParam(paramType = "path",name = "pageSize",value = "每页条数",example = "10",dataType = "Integer")
    })
    @ApiOperationSupport(includeParameters = {"bannerVO.sortNo","bannerVO.url","bannerVO.bannerType"})
    public ResponseResult<Page<BannerVO>> findBannerVOPage(
                                    @RequestBody BannerVO bannerVO,
                                    @PathVariable("pageNum") int pageNum,
                                    @PathVariable("pageSize") int pageSize) {
        Page<BannerVO> bannerVOPage = bannerService.findPage(bannerVO, pageNum, pageSize);
        return ResponseResultBuild.successBuild(bannerVOPage);
    }

    /**
     * @Description 保存
     * @param bannerVO VO对象
     * @return BannerVO
     */
    @PutMapping
    @ApiOperation(value = "保存Banner",notes = "添加Banner")
    @ApiImplicitParam(name = "bannerVO",value = "VO对象",required = true,dataType = "BannerVO")
    @ApiOperationSupport(includeParameters = {"bannerVO.dataState","bannerVO.sortNo","bannerVO.url","bannerVO.bannerType"})
    public ResponseResult<BannerVO> createBanner(@RequestBody BannerVO bannerVO) {
        BannerVO bannerVOResult = bannerService.save(bannerVO);
        return ResponseResultBuild.successBuild(bannerVOResult);
    }

    /**
     * @Description 修改
     * @param bannerVO VO对象
     * @return Boolean 是否修改成功
     */
    @PatchMapping
    @ApiOperation(value = "修改",notes = "修改")
    @ApiImplicitParam(name = "bannerVO",value = "VO对象",required = true,dataType = "BannerVO")
    @ApiOperationSupport(includeParameters = {"bannerVO.id","bannerVO.dataState","bannerVO.sortNo","bannerVO.url","bannerVO.bannerType"})
    public ResponseResult<Boolean> updateBanner(@RequestBody BannerVO bannerVO) {
        Boolean flag = bannerService.update(bannerVO);
        return ResponseResultBuild.successBuild(flag);
    }

    /**
     * @Description 删除
     * @param bannerVO 刪除条件：checkedIds 不可为空
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "删除",notes = "删除")
    @ApiImplicitParam(name = "bannerVO",value = "VO对象",required = true,dataType = "BannerVO")
    @ApiOperationSupport(includeParameters = {"bannerVO.checkedIds"})
    public ResponseResult<Boolean> deleteBanner(@RequestBody BannerVO bannerVO) {
        Boolean flag = bannerService.delete(bannerVO.getCheckedIds());
        return ResponseResultBuild.successBuild(flag);
    }

    /***
     * @description 多条件查询列表
     * @param bannerVO VO对象
     * @return List<BannerVO>
     */
    @PostMapping("list")
    @ApiOperation(value = "多条件查询列表",notes = "多条件查询列表")
    @ApiImplicitParam(name = "bannerVO",value = "VO对象",required = true,dataType = "BannerVO")
    @ApiOperationSupport(includeParameters = {"bannerVO.sortNo","bannerVO.url","bannerVO.bannerType"})
    public ResponseResult<List<BannerVO>> bannerList(@RequestBody BannerVO bannerVO) {
        List<BannerVO> bannerVOList = bannerService.findList(bannerVO);
        return ResponseResultBuild.successBuild(bannerVOList);
    }

}
