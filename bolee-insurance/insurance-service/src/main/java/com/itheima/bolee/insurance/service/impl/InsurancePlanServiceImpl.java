package com.itheima.bolee.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.insurance.constant.InsurancePlanCacheConstant;
import com.itheima.bolee.insurance.dto.InsurancePlanVO;
import com.itheima.bolee.insurance.dto.PlanEarningsVO;
import com.itheima.bolee.insurance.dto.PlanSafeguardVO;
import com.itheima.bolee.insurance.enums.InsurancePlanEnum;
import com.itheima.bolee.insurance.mapper.InsurancePlanMapper;
import com.itheima.bolee.insurance.pojo.InsurancePlan;
import com.itheima.bolee.insurance.pojo.PlanEarnings;
import com.itheima.bolee.insurance.pojo.PlanSafeguard;
import com.itheima.bolee.insurance.service.IInsurancePlanService;
import com.itheima.bolee.insurance.service.IPlanEarningsService;
import com.itheima.bolee.insurance.service.IPlanSafeguardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * @Description：保险方案服务实现类
 */
@Slf4j
@Service
public class InsurancePlanServiceImpl extends ServiceImpl<InsurancePlanMapper, InsurancePlan> implements IInsurancePlanService {

    @Autowired
    IPlanSafeguardService planSafeguardService;

    @Autowired
    IPlanEarningsService planEarningsService;

    @Autowired
    IdentifierGenerator identifierGenerator;


    /***
    * @description 保险方案多条件组合
    * @param insurancePlanVO 保险方案
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<InsurancePlan> queryWrapper(InsurancePlanVO insurancePlanVO){
        QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
        //保险商品id查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getInsuranceId())) {
            queryWrapper.lambda().eq(InsurancePlan::getInsuranceId,insurancePlanVO.getInsuranceId());
        }
        //保险商品ids列表查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getInsuranceIds())) {
            queryWrapper.lambda().in(InsurancePlan::getInsuranceId,insurancePlanVO.getInsuranceIds());
        }
        //保险计划名称查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPalnName())) {
            queryWrapper.lambda().eq(InsurancePlan::getPalnName,insurancePlanVO.getPalnName());
        }
        //默认定价查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPrice())) {
            queryWrapper.lambda().eq(InsurancePlan::getPrice,insurancePlanVO.getPrice());
        }
        //默认定价单位：y/d,y/m,y/y查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getPriceUnit())) {
            queryWrapper.lambda().eq(InsurancePlan::getPriceUnit,insurancePlanVO.getPriceUnit());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getSortNo())) {
            queryWrapper.lambda().eq(InsurancePlan::getSortNo,insurancePlanVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(insurancePlanVO.getDataState())) {
            queryWrapper.lambda().eq(InsurancePlan::getDataState,insurancePlanVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(InsurancePlan::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#insurancePlanVO.hashCode()")
    public Page<InsurancePlanVO> findPage(InsurancePlanVO insurancePlanVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<InsurancePlan> InsurancePlanPage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<InsurancePlan> queryWrapper = queryWrapper(insurancePlanVO);
            //执行分页查询
            Page<InsurancePlanVO> insurancePlanVOPage = BeanConv.toPage(page(InsurancePlanPage, queryWrapper), InsurancePlanVO.class);
            if (!EmptyUtil.isNullOrEmpty(insurancePlanVOPage.getRecords())){
                //补全方案保障项、给付计划
                Set<Long> planIds = insurancePlanVOPage.getRecords().stream().map(InsurancePlanVO::getId).collect(Collectors.toSet());
                //保障项
                List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findInPlanId(planIds);
                List<PlanSafeguardVO> planSafeguardVOsHandler = Lists.newArrayList();
                //给付计划
                List<PlanEarningsVO> planEarningsVOs = planEarningsService.findInPlanId(planIds);
                insurancePlanVOPage.getRecords().forEach(n->{
                    planSafeguardVOs.forEach(i->{
                        if (n.getId().equals(i.getPlanId())) {
                            planSafeguardVOsHandler.add(i);
                        }
                    });
                    n.setPlanSafeguardVOs(planSafeguardVOsHandler);
                    planEarningsVOs.forEach(j->{
                        if (n.getId().equals(j.getPalnId())) {
                            n.setPlanEarningsVO(j);
                        }
                    });
                });
            }
            //返回结果
            return insurancePlanVOPage;
        }catch (Exception e){
            log.error("保险方案分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.BASIC,key ="#insurancePlanId")
    public InsurancePlanVO findByIdAndInsuranceId(Long insurancePlanId,Long insuranceId){
        try {
            //执行查询
            QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InsurancePlan::getId,insurancePlanId).eq(InsurancePlan::getInsuranceId,insuranceId);
            InsurancePlanVO insurancePlanVO = BeanConv.toBean(getOne(queryWrapper), InsurancePlanVO.class);
            //方案保障项
            List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findByPlanId(insurancePlanVO.getId());
            insurancePlanVO.setPlanSafeguardVOs(planSafeguardVOs);
            //方案给付计划
            PlanEarningsVO planEarningsVO =planEarningsService.findByPlanId(insurancePlanVO.getId());
            insurancePlanVO.setPlanEarningsVO(planEarningsVO);
            return insurancePlanVO;
        }catch (Exception e){
            log.error("保险方案单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true)})
    public Boolean save(List<InsurancePlanVO> insurancePlanVOs) {
        try {
            //创建方案编号
            insurancePlanVOs.forEach(n->n.setPlanNo(identifierGenerator.nextId(insurancePlanVOs).longValue()));
            //保存产品方案
            List<InsurancePlan> insurancePlans = BeanConv.toBeanList(insurancePlanVOs, InsurancePlan.class);
            boolean flag = saveBatch(insurancePlans);
            if (!flag){
                throw new RuntimeException("保存保险方案失败");
            }
            //构建方案保障项List对象
            List<PlanSafeguardVO> planSafeguardVOs = Lists.newArrayList();
            //构建方案给付计划List对象
            List<PlanEarningsVO> planEarningsVOs = Lists.newArrayList();
            //回填产品方案ID
            insurancePlanVOs.forEach(ipv->{
                insurancePlans.forEach(ip->{
                    //产品保障项：回填产品方案ID
                    if (ipv.getPlanNo().equals(ip.getPlanNo())){
                        ipv.getPlanSafeguardVOs().forEach(y->{
                            y.setPlanId(ip.getId());
                        });
                    }
                    //产品给付计划：回填产品方案ID
                    if (ipv.getPlanNo().equals(ip.getPlanNo())&&
                        !EmptyUtil.isNullOrEmpty(ipv.getPlanEarningsVO())){
                        ipv.getPlanEarningsVO().setPalnId(ip.getId());
                    }
                });
                planEarningsVOs.add(ipv.getPlanEarningsVO());
                planSafeguardVOs.addAll(ipv.getPlanSafeguardVOs());
            });
            //保存方案保障项
            flag = planSafeguardService.saveBatch(BeanConv.toBeanList(planSafeguardVOs,PlanSafeguard.class));
            if (!flag){
                throw new RuntimeException("保存保险方案保障项失败");
            }
            //保存方案给付计划
            if (!EmptyUtil.isNullOrEmpty(planEarningsVOs)){
                flag =planEarningsService.saveBatch(BeanConv.toBeanList(planEarningsVOs,PlanEarnings.class));
                if (!flag){
                    throw new RuntimeException("保存保险方案给付失败");
                }
            }
            return flag;
        }catch (Exception e){
            log.error("保存保险方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true)})
    public Boolean update(List<InsurancePlanVO> insurancePlanVOs) {
        try {
            //查询原产品方案
            QueryWrapper<InsurancePlan> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InsurancePlan::getInsuranceId,insurancePlanVOs.get(0).getInsuranceId());
            List<InsurancePlan> insurancePlans = list(queryWrapper);
            //删除原产品方案
            List<Long> insurancePlanIds = insurancePlans.stream().map(InsurancePlan::getId).collect(Collectors.toList());
            boolean flag = removeByIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除保险方案失败");
            }
            //删除原产品方案保障项
            flag = planSafeguardService.deleteInPlanIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除方案保障项失败");
            }
            //删除原产品方案给付计划
            planEarningsService.deleteInPlanIds(insurancePlanIds);
            if (!flag){
                throw new RuntimeException("删除方案给付计划失败");
            }
            //清空产品方案ID
            insurancePlanVOs.forEach(n->n.setId(null));
            //再次重新保存
            return this.save(insurancePlanVOs);
        }catch (Exception e){
            log.error("修改保险方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = InsurancePlanCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = InsurancePlanCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案失败");
            }
            flag = planSafeguardService.deleteInPlanId(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案保障项失败");
            }
            flag = planEarningsService.deleteInPlanId(idsLong);
            if (!flag){
                throw new RuntimeException("删除保险方案给付计划失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除保险方案异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = InsurancePlanCacheConstant.LIST,key ="#insurancePlanVO.hashCode()")
    public List<InsurancePlanVO> findList(InsurancePlanVO insurancePlanVO) {
        try {
            //构建查询条件
            QueryWrapper<InsurancePlan> queryWrapper = queryWrapper(insurancePlanVO);
            //执行列表查询
            List<InsurancePlanVO> insurancePlanVOs = BeanConv.toBeanList(list(queryWrapper),InsurancePlanVO.class);
            if (!EmptyUtil.isNullOrEmpty(insurancePlanVOs)){
                //补全方案保障项、给付计划
                Set<Long> planIds = insurancePlanVOs.stream().map(InsurancePlanVO::getId).collect(Collectors.toSet());
                //保障项
                List<PlanSafeguardVO> planSafeguardVOs = planSafeguardService.findInPlanId(planIds);
                //给付计划
                List<PlanEarningsVO> planEarningsVOs = planEarningsService.findInPlanId(planIds);
                insurancePlanVOs.forEach(n->{
                    List<PlanSafeguardVO> planSafeguardVOsHandler = Lists.newArrayList();
                    planSafeguardVOs.forEach(i->{
                        if (n.getId().equals(i.getPlanId())) {
                            planSafeguardVOsHandler.add(i);
                        }
                    });
                    n.setPlanSafeguardVOs(planSafeguardVOsHandler);
                    planEarningsVOs.forEach(j->{
                        if (n.getId().equals(j.getPalnId())) {
                            n.setPlanEarningsVO(j);
                        }
                    });
                });
            }
            return insurancePlanVOs;
        }catch (Exception e){
            log.error("保险方案列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(InsurancePlanEnum.LIST_FAIL);
        }
    }

}
