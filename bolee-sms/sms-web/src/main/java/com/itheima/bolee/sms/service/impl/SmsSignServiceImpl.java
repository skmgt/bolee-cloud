package com.itheima.bolee.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.feign.FileBusinessFeign;
import com.itheima.bolee.file.feign.FileDownLoadFeign;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsCacheConstant;
import com.itheima.bolee.framework.commons.constant.sms.SmsConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.sms.SmsSignVO;
import com.itheima.bolee.framework.commons.enums.sms.SmsSignEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.sms.adapter.SmsSignAdapter;
import com.itheima.bolee.sms.mapper.SmsSignMapper;
import com.itheima.bolee.sms.pojo.SmsSign;
import com.itheima.bolee.sms.service.ISmsSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description： 签名服务实现类
 */
@Slf4j
@Service
public class SmsSignServiceImpl extends ServiceImpl<SmsSignMapper, SmsSign> implements ISmsSignService {

    @Autowired
    FileBusinessFeign fileBusinessFeign;

    @Autowired
    FileDownLoadFeign fileDownLoadFeign;

    @Autowired
    SmsSignAdapter smsSignAdapter;

    private QueryWrapper<SmsSign> queryWrapper(SmsSignVO smsSignVO){
        //构建查询条件
        QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
        //按签名名称查询
        if (!EmptyUtil.isNullOrEmpty(smsSignVO.getSignName())) {
            queryWrapper.lambda().likeRight(SmsSign::getSignName,smsSignVO.getSignName());
        }
        //按签名通道查询
        if (!EmptyUtil.isNullOrEmpty(smsSignVO.getChannelLabel())) {
            queryWrapper.lambda().eq(SmsSign::getChannelLabel,smsSignVO.getChannelLabel());
        }
        //按签名发送状态查询
        if (!EmptyUtil.isNullOrEmpty(smsSignVO.getAcceptStatus())) {
            queryWrapper.lambda().eq(SmsSign::getAcceptStatus,smsSignVO.getAcceptStatus());
        }
        //按签名审核状态查询
        if (!EmptyUtil.isNullOrEmpty(smsSignVO.getAuditStatus())) {
            queryWrapper.lambda().eq(SmsSign::getAuditStatus,smsSignVO.getAuditStatus());
        }
        //按签名状态查询
        if (!EmptyUtil.isNullOrEmpty(smsSignVO.getDataState())) {
            queryWrapper.lambda().eq(SmsSign::getDataState,smsSignVO.getDataState());
        }
        //按创建时间降序
        queryWrapper.lambda().orderByDesc(SmsSign::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = SmsCacheConstant.PAGE_SIGN,key ="#pageNum+'-'+#pageSize+'-'+#smsSignVO.hashCode()")
    public Page<SmsSignVO> findSmsSignVOPage(SmsSignVO smsSignVO, int pageNum, int pageSize) {
        //构建分页对象
        Page<SmsSign> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SmsSign> queryWrapper = this.queryWrapper(smsSignVO);
        //执行分页查询
        Page<SmsSignVO> pageVO = BeanConv.toPage(page(page, queryWrapper),SmsSignVO.class);
        if (!EmptyUtil.isNullOrEmpty(pageVO)&&!EmptyUtil.isNullOrEmpty(pageVO.getRecords())){
            List<Long> SmsSignIds = pageVO.getRecords().stream().map(SmsSignVO::getId).collect(Collectors.toList());
            List<FileVO> fileVos = fileBusinessFeign.findInBusinessIds(Lists.newArrayList(SmsSignIds));
            pageVO.getRecords().forEach(n->{
                LinkedList<FileVO> list = Lists.newLinkedList();
                fileVos.forEach(flieVo->{
                    if (flieVo.getBusinessId().equals(n.getId()))
                        list.add(flieVo);
                });
                n.setFileVOs(list);
            });
        }
        return pageVO;
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SIGN,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.SIGN_LIST,allEntries = true)},
            put={@CachePut(value =SmsCacheConstant.PREFIX_SIGN,key = "#result.id")})
    public SmsSignVO addSmsSign(SmsSignVO smsSignVO) throws ProjectException{
        try {
            //获取证明资料相关
            LinkedList<FileVO> fileVos = smsSignVO.getFileVOs();
            StringBuffer base64Image =new StringBuffer();
            StringBuffer suffix =new StringBuffer();
            for (FileVO fileVO : fileVos) {
                FileVO fileVOHandler = fileDownLoadFeign.downLoad(fileVO.getId());
                if (!EmptyUtil.isNullOrEmpty(fileVOHandler)){
                    base64Image.append(fileVOHandler.getBase64Image()).append("@");
                    suffix.append(fileVOHandler.getSuffix()).append("@");
                }
            }
            //填充签名文件属性
            if (!EmptyUtil.isNullOrEmpty(base64Image)&&!EmptyUtil.isNullOrEmpty(suffix)){
                String base64ImageString = base64Image.toString();
                String suffixString = suffix.toString();
                smsSignVO.setProofImage(base64ImageString.substring(0,base64ImageString.length()-1));
                smsSignVO.setProofType(suffixString.substring(0,suffixString.length()-1));
            }
            //调用签名三方API
            SmsSignVO smsSignVOHandler = smsSignAdapter.addSmsSign(smsSignVO);
            //签名业务附件关联
            if (!EmptyUtil.isNullOrEmpty(smsSignVOHandler)){
                List<FileVO> fileVosHandler = fileVos.stream()
                        .map(n -> FileVO.builder().id(n.getId()).businessId(smsSignVOHandler.getId()).build())
                        .collect(Collectors.toList());
                fileBusinessFeign.bindBatchFile(Lists.newArrayList(fileVosHandler));
            }
            return smsSignVOHandler;
        } catch (Exception e) {
            log.error("添加签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.CREATE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SIGN,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.SIGN_LIST,allEntries = true),
            @CacheEvict(value = SmsCacheConstant.PREFIX_SIGN,allEntries = true)})
    public Boolean deleteSmsSign(String[] checkedIds)throws ProjectException {
        try {
            Boolean flag = smsSignAdapter.deleteSmsSign(checkedIds);
            if (flag){
                List<String> list = Arrays.asList(checkedIds);
                ArrayList<Long> listHandler = Lists.newArrayList();
                list.forEach(n->{listHandler.add(Long.valueOf(n)); });
                fileBusinessFeign.deleteByBusinessIds(listHandler);
            }
            return flag;
        } catch (Exception e) {
            log.error("删除签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.DELETE_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = SmsCacheConstant.PAGE_SIGN,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.SIGN_LIST,allEntries = true),
        @CacheEvict(value = SmsCacheConstant.PREFIX_SIGN,key = "#smsSignVO.getId()")})
    public SmsSignVO modifySmsSign(SmsSignVO smsSignVO)throws ProjectException {
        try {
            //获取证明资料相关
            LinkedList<FileVO> fileVos = smsSignVO.getFileVOs();
            StringBuffer base64Image =new StringBuffer();
            StringBuffer suffix =new StringBuffer();
            for (FileVO fileVO : fileVos) {
                FileVO fileVOHandler = fileDownLoadFeign.downLoad(fileVO.getId());
                if (!EmptyUtil.isNullOrEmpty(fileVOHandler)){
                    base64Image.append(fileVOHandler.getBase64Image()).append("@");
                    suffix.append(fileVOHandler.getSuffix()).append("@");
                }
            }
            //填充签名文件属性
            if (!EmptyUtil.isNullOrEmpty(base64Image)&&!EmptyUtil.isNullOrEmpty(suffix)){
                String base64ImageString = base64Image.toString();
                String suffixString = suffix.toString();
                smsSignVO.setProofImage(base64ImageString.substring(0,base64ImageString.length()-1));
                smsSignVO.setProofType(suffixString.substring(0,suffixString.length()-1));
            }
            //调用签名三方API
            SmsSignVO smsSignVOHandler = smsSignAdapter.modifySmsSign(smsSignVO);
            //替换签名附件相关
            if (!EmptyUtil.isNullOrEmpty(smsSignVOHandler)){
                fileBusinessFeign.replaceBindBatchFile(Lists.newArrayList(fileVos));
            }
            return smsSignVO;
        } catch (Exception e) {
            log.error("修改签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.UPDATE_FAIL);
        }
    }

    @Override
    public SmsSignVO disableEnable(SmsSignVO smsSignVO) {
        try {
            SmsSign smsSign = BeanConv.toBean(smsSignVO, SmsSign.class);
            updateById(smsSign);
            return BeanConv.toBean(smsSign, SmsSignVO.class);
        } catch (Exception e) {
            log.error("修改签名状态异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.UPDATE_FAIL);
        }
    }

    @Override
    @Cacheable(value =SmsCacheConstant.PREFIX_SIGN,key = "#smsSignVO.id")
    @Transactional
    public SmsSignVO querySmsSign(SmsSignVO smsSignVO) throws ProjectException{
        try {
            return smsSignAdapter.querySmsSign(smsSignVO);
        } catch (Exception e) {
            log.error("查询签名状态异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
    }

    @Override
    @Cacheable(value =SmsCacheConstant.SIGN_LIST)
    public List<SmsSignVO> findSmsSignVOList() {
        try {
            QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsSign::getDataState, SuperConstant.DATA_STATE_0);
            return BeanConv.toBeanList(list(queryWrapper),SmsSignVO.class);
        } catch (Exception e) {
            log.error("查找所有签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
    }

    @Override
    public SmsSignVO findSmsSignBySignNameAndChannelLabel(String signName, String channelLabel) {
        try {
            QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsSign::getSignName, signName)
                    .eq(SmsSign::getChannelLabel, channelLabel);
            return BeanConv.toBean(getOne(queryWrapper),SmsSignVO.class);
        } catch (Exception e) {
            log.error("查找签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }
    }

    @Override
    public SmsSignVO findSmsSignBySignNoAndChannelLabel(String signNo, String channelLabel) {
        try {
            QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsSign::getSignNo, signNo)
                .eq(SmsSign::getChannelLabel, channelLabel)
                .eq(SmsSign::getAuditStatus, SmsConstant.STATUS_AUDIT_0);
            return  BeanConv.toBean(getOne(queryWrapper),SmsSignVO.class);
        } catch (Exception e) {
            log.error("查找签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }

    }

    @Override
    public SmsSignVO findSmsSignBySignCodeAndChannelLabel(String signCode, String channelLabel) {
        try {
            QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SmsSign::getSignCode, signCode)
                    .eq(SmsSign::getChannelLabel, channelLabel)
                    .eq(SmsSign::getAuditStatus,SmsConstant.STATUS_AUDIT_0);
            return  BeanConv.toBean(getOne(queryWrapper),SmsSignVO.class);
        } catch (Exception e) {
            log.error("查找签名异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(SmsSignEnum.SELECT_FAIL);
        }

    }
}
