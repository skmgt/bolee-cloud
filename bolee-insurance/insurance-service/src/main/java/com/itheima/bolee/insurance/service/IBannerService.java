package com.itheima.bolee.insurance.service;

import com.itheima.bolee.insurance.pojo.Banner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.bolee.insurance.dto.BannerVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Description：服务类
 */
public interface IBannerService extends IService<Banner> {

    /**
     * @Description 多条件查询分页
     * @param bannerVO 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<BannerVO>
     */
    Page<BannerVO> findPage(BannerVO bannerVO, int pageNum, int pageSize);

    /**
    * @Description 多条件查询
    * @param bannerId 合同信息ID
    * @return BannerVO
    */
    BannerVO findById(String bannerId);

    /**
     * @Description 新增
     * @param bannerVO 查询条件
     * @return BannerVO
     */
    BannerVO save(BannerVO bannerVO);

    /**
     * @Description 修改
     * @param bannerVO 对象
     * @return BannerVO
     */
    Boolean update(BannerVO bannerVO);

    /**
     * @Description 删除
     * @param checkedIds 选择中对象Ids
     * @return Boolean
     */
    Boolean delete(String[] checkedIds);

    /**
     * @description 多条件查询列表
     * @param bannerVO 查询条件
     * @return: List<BannerVO>
     */
    List<BannerVO> findList(BannerVO bannerVO);
}
