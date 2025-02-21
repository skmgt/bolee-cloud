package com.itheima.bolee.insurance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.worryfree.WorryFreeConstant;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.insurance.pojo.WorryFreeFlowNode;
import com.itheima.bolee.insurance.mapper.WorryFreeFlowNodeMapper;
import com.itheima.bolee.insurance.service.IWorryFreeFlowNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.bolee.insurance.constant.WorryFreeFlowNodeCacheConstant;
import com.itheima.bolee.insurance.dto.WorryFreeFlowNodeVO;
import com.itheima.bolee.insurance.enums.WorryFreeFlowNodeEnum;
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
 * @Description：省心配流程节点记录服务实现类
 */
@Slf4j
@Service
public class WorryFreeFlowNodeServiceImpl extends ServiceImpl<WorryFreeFlowNodeMapper, WorryFreeFlowNode> implements IWorryFreeFlowNodeService {

    @Autowired
    RedissonClient redissonClient;

    /***
    * @description 省心配流程节点记录多条件组合
    * @param worryFreeFlowNodeVO 省心配流程节点记录
    * @return QueryWrapper查询条件
    */
    private QueryWrapper<WorryFreeFlowNode> queryWrapper(WorryFreeFlowNodeVO worryFreeFlowNodeVO){
        QueryWrapper<WorryFreeFlowNode> queryWrapper = new QueryWrapper<>();
        //风险项名称列表 json查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeFlowNodeVO.getNodes())) {
            queryWrapper.lambda().eq(WorryFreeFlowNode::getNodes,worryFreeFlowNodeVO.getNodes());
        }
        //用户id查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeFlowNodeVO.getCustomerId())) {
            queryWrapper.lambda().eq(WorryFreeFlowNode::getCustomerId,worryFreeFlowNodeVO.getCustomerId());
        }
        //排序查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeFlowNodeVO.getSortNo())) {
            queryWrapper.lambda().eq(WorryFreeFlowNode::getSortNo,worryFreeFlowNodeVO.getSortNo());
        }
        //状态查询
        if (!EmptyUtil.isNullOrEmpty(worryFreeFlowNodeVO.getDataState())) {
            queryWrapper.lambda().eq(WorryFreeFlowNode::getDataState,worryFreeFlowNodeVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(WorryFreeFlowNode::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = WorryFreeFlowNodeCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#worryFreeFlowNodeVO.hashCode()")
    public Page<WorryFreeFlowNodeVO> findPage(WorryFreeFlowNodeVO worryFreeFlowNodeVO, int pageNum, int pageSize) {
        try {
            //构建分页对象
            Page<WorryFreeFlowNode> WorryFreeFlowNodePage = new Page<>(pageNum,pageSize);
            //构建查询条件
            QueryWrapper<WorryFreeFlowNode> queryWrapper = queryWrapper(worryFreeFlowNodeVO);
            //执行分页查询
            Page<WorryFreeFlowNodeVO> worryFreeFlowNodeVOPage = BeanConv.toPage(
                page(WorryFreeFlowNodePage, queryWrapper), WorryFreeFlowNodeVO.class);
            //返回结果
            return worryFreeFlowNodeVOPage;
        }catch (Exception e){
            log.error("省心配流程节点记录分页查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.PAGE_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeFlowNodeCacheConstant.BASIC,key ="#worryFreeFlowNodeId")
    public WorryFreeFlowNodeVO findById(String worryFreeFlowNodeId) {
        try {
            //执行查询
            return BeanConv.toBean(getById(worryFreeFlowNodeId),WorryFreeFlowNodeVO.class);
        }catch (Exception e){
            log.error("省心配流程节点记录单条查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.FIND_ONE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeFlowNodeCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeFlowNodeCacheConstant.LIST,allEntries = true)},
        put={@CachePut(value =WorryFreeFlowNodeCacheConstant.BASIC,key = "#result.id")})
    public WorryFreeFlowNodeVO save(WorryFreeFlowNodeVO worryFreeFlowNodeVO) {
        try {
            //转换WorryFreeFlowNodeVO为WorryFreeFlowNode
            WorryFreeFlowNode worryFreeFlowNode = BeanConv.toBean(worryFreeFlowNodeVO, WorryFreeFlowNode.class);
            boolean flag = save(worryFreeFlowNode);
            if (!flag){
                throw new RuntimeException("保存省心配流程节点记录失败");
            }
            //转换返回对象WorryFreeFlowNodeVO
            WorryFreeFlowNodeVO worryFreeFlowNodeVOHandler = BeanConv.toBean(worryFreeFlowNode, WorryFreeFlowNodeVO.class);
            return worryFreeFlowNodeVOHandler;
        }catch (Exception e){
            log.error("保存省心配流程节点记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.SAVE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeFlowNodeCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeFlowNodeCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeFlowNodeCacheConstant.BASIC,key = "#worryFreeFlowNodeVO.id")})
    public Boolean update(WorryFreeFlowNodeVO worryFreeFlowNodeVO) {
        try {
            //转换WorryFreeFlowNodeVO为WorryFreeFlowNode
            WorryFreeFlowNode worryFreeFlowNode = BeanConv.toBean(worryFreeFlowNodeVO, WorryFreeFlowNode.class);
            boolean flag = updateById(worryFreeFlowNode);
            if (!flag){
                throw new RuntimeException("修改省心配流程节点记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("修改省心配流程节点记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = WorryFreeFlowNodeCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = WorryFreeFlowNodeCacheConstant.LIST,allEntries = true),
        @CacheEvict(value = WorryFreeFlowNodeCacheConstant.BASIC,allEntries = true)})
    public Boolean delete(String[] checkedIds) {
        try {
            List<Long> idsLong = Arrays.asList(checkedIds)
                .stream().map(Long::new).collect(Collectors.toList());
            boolean flag = removeByIds(idsLong);
            if (!flag){
                throw new RuntimeException("删除省心配流程节点记录失败");
            }
            return flag;
        }catch (Exception e){
            log.error("删除省心配流程节点记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.DEL_FAIL);
        }
    }

    @Override
    @Cacheable(value = WorryFreeFlowNodeCacheConstant.LIST,key ="#worryFreeFlowNodeVO.hashCode()")
    public List<WorryFreeFlowNodeVO> findList(WorryFreeFlowNodeVO worryFreeFlowNodeVO) {
        try {
            //构建查询条件
            QueryWrapper<WorryFreeFlowNode> queryWrapper = queryWrapper(worryFreeFlowNodeVO);
            //执行列表查询
            List<WorryFreeFlowNodeVO> worryFreeFlowNodeVOs = BeanConv.toBeanList(list(queryWrapper),WorryFreeFlowNodeVO.class);
            return worryFreeFlowNodeVOs;
        }catch (Exception e){
            log.error("省心配流程节点记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.LIST_FAIL);
        }
    }

    /**
     * 根据登录用户id查询当前登录用户的流程节点
     * @param id
     * @return
     */
    @Override
    public List<String> flowNode(Long id) {
        try{
            //先查询缓存中的流程节点
            String userId = String.valueOf(SubjectContent.getUserVO().getId());
            // redis队列的key
            String listKey = WorryFreeConstant.WORRYFREE_PREFIX + userId;
            // 往队列中添加数据
            RList<String> list = redissonClient.getList(listKey);
            //如果查询出的节点为空，查询数据库中的流程节点
            List<String> res = list.stream().collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(res)) {
                return res;
            }
            //否则根据当前登录人查询数据库
            QueryWrapper<WorryFreeFlowNode> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(WorryFreeFlowNode::getCustomerId, id);
            WorryFreeFlowNode worryFreeFlowNodes = getOne(queryWrapper);
            if(worryFreeFlowNodes != null) {
                List<String> strings = JSONUtil.toList(worryFreeFlowNodes.getNodes(), String.class);
                return strings;
            }
            //如果数据库中也没有
            //直接返回空
            return null;
        }catch (Exception e){
            log.error("省心配流程节点记录列表查询异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.FIND_ONE_FAIL);
        }
    }

    /**
     * 清空当前登录人的历史记录
     * @param id
     */
    @Override
    public void cleanCustomerHistry(String id) {
        try {
            QueryWrapper<WorryFreeFlowNode> worryFreeFlowNodeQueryWrapper = new QueryWrapper<>();
            worryFreeFlowNodeQueryWrapper.lambda().eq(WorryFreeFlowNode::getCustomerId, id);
            remove(worryFreeFlowNodeQueryWrapper);
        }catch (Exception e){
            log.error("删除省心配流程节点记录异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(WorryFreeFlowNodeEnum.DEL_FAIL);
        }
    }
}
