package com.itheima.bolee.insurance.service.impl;

import com.itheima.bolee.insurance.pojo.Banner;
import com.itheima.bolee.insurance.mapper.BannerMapper;
import com.itheima.bolee.insurance.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import io.seata.spring.annotation.GlobalTransactional;
import com.itheima.bolee.insurance.constant.BannerCacheConstant;
import com.itheima.bolee.insurance.dto.BannerVO;
import com.itheima.bolee.insurance.enums.BannerEnum;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import lombok.extern.slf4j.Slf4j;
/**
 * @Description：服务实现类
 */
@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    /***
    * @description 多条件组合
    * @param bannerVO
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<Banner> queryWrapper(BannerVO bannerVO){
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(bannerVO.getSortNo())) {
            queryWrapper.lambda().eq(Banner::getSortNo,bannerVO.getSortNo());
        }
        //调整路径查询
        if (!EmptyUtil.isNullOrEmpty(bannerVO.getUrl())) {
            queryWrapper.lambda().likeRight(Banner::getUrl,bannerVO.getUrl());
        }
        //导航类型（0首页 1推荐）
        if (!EmptyUtil.isNullOrEmpty(bannerVO.getBannerType())) {
            queryWrapper.lambda().eq(Banner::getBannerType,bannerVO.getBannerType());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(bannerVO.getDataState())) {
            queryWrapper.lambda().eq(Banner::getDataState,bannerVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(Banner::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = BannerCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#bannerVO.hashCode()")
    public Page<BannerVO> findPage(BannerVO bannerVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<Banner> BannerPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<Banner> queryWrapper = queryWrapper(bannerVO);
            //执行分页查询
            Page<BannerVO> bannerVOPage = BeanConv.toPage(
                page(BannerPage, queryWrapper), BannerVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(bannerVOPage)&&
                !EmptyUtil.isNullOrEmpty(bannerVOPage.getRecords())){
                //获得所有业务主键ID
                List<Long> bannerIds = bannerVOPage.getRecords()
                    .stream().map(BannerVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(bannerIds));
                bannerVOPage.getRecords().forEach(bannerVOHandler->{
                    //构建附件处理List对象
                    List<FileVO> fileVOsHandler = Lists.newArrayList();
                    fileVOs.forEach(fileVO -> {
                        if (bannerVOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    bannerVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            //返回结果
            return bannerVOPage;
        }catch (Exception e){
            log.error("分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = BannerCacheConstant.BASIC,key ="#bannerId")
    public BannerVO findById(String bannerId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(bannerId),BannerVO.class);
        }catch (Exception e){
            log.error("单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = BannerCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BannerCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =BannerCacheConstant.BASIC,key = "#result.id")})
    public BannerVO save(BannerVO bannerVO) {
        try {
            //转换BannerVO为Banner
            Banner banner = BeanConv.toBean(bannerVO, Banner.class);
            boolean flag = save(banner);
            if (!flag){
                throw new RuntimeException("保存失败");
            }
            //保存附件信息
            if (EmptyUtil.isNullOrEmpty(bannerVO.getFileVOs())){
                throw new RuntimeException("合同附件为空");
            }
            //构建附件对象
            bannerVO.getFileVOs().forEach(fileVO -> {
                fileVO.setBusinessId(banner.getId());
            });
            //调用附件接口
            List<FileVO> fileVOs = fileBusinessFeign.bindBatchFile(Lists.newArrayList(bannerVO.getFileVOs()));
            if (EmptyUtil.isNullOrEmpty(fileVOs)){
                throw new RuntimeException("合同附件绑定失败");
            }
            //转换返回对象BannerVO
            BannerVO bannerVOHandler = BeanConv.toBean(banner, BannerVO.class);
            bannerVOHandler.setFileVOs(fileVOs);
            return bannerVOHandler;
        }catch (Exception e){
            log.error("保存异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.SAVE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = BannerCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BannerCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = BannerCacheConstant.BASIC,key = "#bannerVO.id")})
    public Boolean update(BannerVO bannerVO) {
        try {
            //转换BannerVO为Banner
            Banner banner = BeanConv.toBean(bannerVO, Banner.class);
            boolean flag = updateById(banner);
            if (!flag){
                throw new RuntimeException("修改失败");
            }
            //替换附件信息
            flag = fileBusinessFeign.replaceBindBatchFile(Lists.newArrayList(bannerVO.getFileVOs()));
            if (!flag){
                throw new RuntimeException("移除附件失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.UPDATE_FAIL);
        }
    }

    @Override
    @GlobalTransactional
    @Caching(evict = {@CacheEvict(value = BannerCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = BannerCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = BannerCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除失败");
            }
            //删除附件
            flag = fileBusinessFeign.deleteByBusinessIds(Lists.newArrayList(idsLong));
            if (!flag){
                throw new RuntimeException("删除附件失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = BannerCacheConstant.LIST,key ="#bannerVO.hashCode()")
    public List<BannerVO> findList(BannerVO bannerVO) {
        try {
            //构建查询条件
            QueryWrapper<Banner> queryWrapper = queryWrapper(bannerVO);
            //执行列表查询
            List<BannerVO> bannerVOs = BeanConv.toBeanList(list(queryWrapper),BannerVO.class);
            //构建补充信息
            if (!EmptyUtil.isNullOrEmpty(bannerVOs)){
                //获得所有业务主键ID
                List<Long> bannerIds = bannerVOs.stream().map(BannerVO::getId).collect(Collectors.toList());
                //调用fileBusinessFeign附件信息
                List<FileVO> fileVOs = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(bannerIds));
                bannerVOs.forEach(bannerVOHandler->{
                    //构建附件处理List对象
                    List<FileVO> fileVOsHandler = Lists.newArrayList();
                    fileVOs.forEach(fileVO -> {
                        if (bannerVOHandler.getId().equals(fileVO.getBusinessId()))
                            fileVOsHandler.add(fileVO);
                    });
                    //补全附件信息
                    bannerVOHandler.setFileVOs(fileVOsHandler);
                });
            }
            return bannerVOs;
        }catch (Exception e){
            log.error("列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(BannerEnum.LIST_FAIL);
        }
    }
}
