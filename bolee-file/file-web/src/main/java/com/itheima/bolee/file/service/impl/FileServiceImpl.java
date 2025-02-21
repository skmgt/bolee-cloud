package com.itheima.bolee.file.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.model.UploadPartResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itheima.bolee.file.adapter.FileStorageAdapter;
import com.itheima.bolee.file.mapper.FileMapper;
import com.itheima.bolee.file.pojo.File;
import com.itheima.bolee.file.pojo.FilePart;
import com.itheima.bolee.file.service.IFilePartService;
import com.itheima.bolee.file.service.IFileService;
import com.itheima.bolee.file.utils.FileUrlContext;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.constant.file.FileCacheConstant;
import com.itheima.bolee.framework.commons.constant.file.FileConstant;
import com.itheima.bolee.framework.commons.dto.file.FileVO;
import com.itheima.bolee.framework.commons.dto.file.FilePartVO;
import com.itheima.bolee.framework.commons.dto.file.UploadMultipartFile;
import com.itheima.bolee.framework.commons.enums.file.FileEnum;
import com.itheima.bolee.framework.commons.exception.ProjectException;
import com.itheima.bolee.framework.commons.utils.BeanConv;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.EncodesUtil;
import com.itheima.bolee.framework.commons.utils.ExceptionsUtil;
import com.itheima.bolee.framework.rabbitmq.pojo.MqMessage;
import com.itheima.bolee.framework.rabbitmq.source.FileSource;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description：附件 服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Value("${file-delay-time}")
    Integer fileDelayTime;

    @Autowired
    private FileUrlContext fileUrlContext;

    @Autowired
    FileStorageAdapter fileStorageAdapter;

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    FileSource fileSource;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    IFilePartService filePartService;

    private QueryWrapper<File> queryWrapper(FileVO fileVO){
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        if (!EmptyUtil.isNullOrEmpty(fileVO.getBusinessType())) {
            queryWrapper.lambda().eq(File::getBusinessType,fileVO.getBusinessType());
        }
        if (!EmptyUtil.isNullOrEmpty(fileVO.getFileName())) {
            queryWrapper.lambda().likeRight(File::getFileName,fileVO.getFileName());
        }
        if (!EmptyUtil.isNullOrEmpty(fileVO.getPathUrl())) {
            queryWrapper.lambda().likeRight(File::getPathUrl,fileVO.getPathUrl());
        }
        if (!EmptyUtil.isNullOrEmpty(fileVO.getDataState())) {
            queryWrapper.lambda().likeRight(File::getDataState,fileVO.getDataState());
        }
        if (!EmptyUtil.isNullOrEmpty(fileVO.getStatus())) {
            queryWrapper.lambda().likeRight(File::getStatus,fileVO.getStatus());
        }
        queryWrapper.lambda().orderByDesc(File::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Cacheable(value = FileCacheConstant.BUSINESS_KEY,key = "#businessId")
    public List<FileVO> findFileVoByBusinessId(Long businessId) {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(File::getBusinessId,businessId);
            List<File> files = list(queryWrapper);
            if (!EmptyUtil.isNullOrEmpty(files)){
                files.forEach(n->{
                    String fileUrl = fileUrlContext.getFileUrl(n.getStoreFlag(), n.getPathUrl());
                    n.setPathUrl(fileUrl);
                });
            }
            return BeanConv.toBeanList(files,FileVO.class);
        }catch (Exception e){
            log.error("查询业务对应附件：{}异常：{}", businessId,ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.SELECT_FILE_BUSINESSID_FAIL);
        }
    }

    @Override
    @Cacheable(value = FileCacheConstant.PAGE,key ="#pageNum+'-'+#pageSize+'-'+#fileVO.hashCode()")
    public Page<FileVO> findFileVOPage(FileVO fileVO, int pageNum , int pageSize) {
        try {
            Page<File> page = new Page<>(pageNum,pageSize);
            QueryWrapper<File> queryWrapper = queryWrapper(fileVO);
            Page<FileVO> fileVOPage = BeanConv.toPage(page(page, queryWrapper), FileVO.class);
            if (!EmptyUtil.isNullOrEmpty(fileVOPage)&&!EmptyUtil.isNullOrEmpty(fileVOPage.getRecords())){
                fileVOPage.getRecords().forEach(n->{
                    n.setPathUrl(fileUrlContext.getFileUrl(n.getStoreFlag(), n.getPathUrl()));
                });
            }
            return fileVOPage;
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.PAGE_FAIL);
        }
    }

    @Override
    public List<FileVO> needClearFile() {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            LocalDateTime localDateTime =  LocalDateTime.now().minusSeconds(fileDelayTime/1000);
            queryWrapper.lambda().isNull(File::getBusinessId).lt(File::getCreateTime,localDateTime);
            return BeanConv.toBeanList(list(queryWrapper), FileVO.class);
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.SELECT_FILE_BUSINESSID_FAIL);
        }
    }

    @Override
    public FileVO needClearFileById(String id) {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            LocalDateTime localDateTime =  LocalDateTime.now().minusSeconds(fileDelayTime/1000);
            queryWrapper.lambda().isNull(File::getBusinessId).lt(File::getCreateTime,localDateTime).eq(File::getId,id);
            return BeanConv.toBean(getOne(queryWrapper),FileVO.class);
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.SELECT_FILE_BUSINESSID_FAIL);
        }

    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,key = "#fileVO.id"),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,key = "#fileVO.businessId")})
    public FileVO bindFile(FileVO fileVO) {
        try {
            //修改file表中的businessId
            File file = BeanConv.toBean(fileVO, File.class);
            boolean flag = updateById(file);
            //构建完整返回对象
            if (flag){
                QueryWrapper<File> queryWrapper = new QueryWrapper();
                queryWrapper.lambda().eq(File::getBusinessId,fileVO.getBusinessId());
                File fileResult = getOne(queryWrapper);
                if (!EmptyUtil.isNullOrEmpty(file)){
                    fileResult.setPathUrl(fileUrlContext.getFileUrl(fileResult.getStoreFlag(), fileResult.getPathUrl()));
                }
                return BeanConv.toBean(fileResult,FileVO.class);
            }
            return null;
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.SELECT_FILE_BUSINESSID_FAIL);
        }

    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,key = "#fileVOs.get(0).getBusinessId()")})
    public List<FileVO> bindBatchFile(List<FileVO> fileVOs) {
        Long businessId = fileVOs.get(0).getBusinessId();
        if (EmptyUtil.isNullOrEmpty(businessId)) {
            throw new ProjectException(FileEnum.SELECT_BUSUBBESSID_FAIL);
        }
        try {
            //修改file表中的businessId
            updateBatchById(BeanConv.toBeanList(fileVOs, File.class));
            QueryWrapper<File> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(File::getBusinessId,businessId);
            List<File> files = list(queryWrapper);
            //构建完整返回对象
            if (!EmptyUtil.isNullOrEmpty(files)){
                files.forEach(n->{
                    String fileUrl = fileUrlContext.getFileUrl(n.getStoreFlag(), n.getPathUrl());
                    n.setPathUrl(fileUrl);
                });
            }
            return BeanConv.toBeanList(files,FileVO.class);
        } catch (Exception e) {
            log.error("绑定业务：{}异常：{}", fileVOs.toString(),ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.BIND_FAIL);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true),
            @CacheEvict(value = FileCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,key = "#fileVO.getBusinessId()")})
    public Boolean replaceBindFile(FileVO fileVO) {
        try {
            //删除老图片
            ArrayList<Long> ids = Lists.newArrayList();
            ids.add(fileVO.getId());
            deleteInIds(ids);
            //绑定新图片
           FileVO fileVOResult = bindFile(fileVO);
            return !EmptyUtil.isNullOrEmpty(fileVOResult);
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.BIND_FAIL);
        }

    }

    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true),
            @CacheEvict(value = FileCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,key = "#fileVOs.get(0).getBusinessId()")})
    public Boolean replaceBindBatchFile(List<FileVO> fileVOs) {
        try {
            //查询当前业务图片
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(File::getBusinessId,fileVOs.get(0).getBusinessId());
            List<File> oldList = list(queryWrapper);
            List<Long> oldIds = oldList.stream().map(File::getId).collect(Collectors.toList());
            List<Long> newIds = fileVOs.stream().map(FileVO::getId).collect(Collectors.toList());
            //删除：老图片对新图片的差集
            List<Long> delIds = oldIds.stream().filter(n -> {
                return !newIds.contains(n);
            }).collect(Collectors.toList());
            if (!EmptyUtil.isNullOrEmpty(delIds)){
                deleteInIds(delIds);
            }
            //绑定新图片
            List<FileVO> newFiles = fileVOs.stream().filter(n -> {
                return !oldIds.contains(n.getId());
            }).collect(Collectors.toList());
            if (!EmptyUtil.isNullOrEmpty(newFiles)){
                List<FileVO> fileVOsResult = bindBatchFile(newFiles);
                return !EmptyUtil.isNullOrEmpty(fileVOsResult);
            }
            return true;
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.BIND_FAIL);
        }
    }

    @Override
    @Cacheable(value = FileCacheConstant.LIST,key ="#businessIds.hashCode()")
    public List<FileVO> findInBusinessIds(List<Long> businessIds) {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(File::getBusinessId,businessIds);
            List<FileVO> fileVOList = BeanConv.toBeanList(list(queryWrapper), FileVO.class);
            for (FileVO fileVO : fileVOList) {
                fileVO.setPathUrl(fileUrlContext.getFileUrl(fileVO.getStoreFlag(), fileVO.getPathUrl()));
            }
            return fileVOList;
        }catch (Exception e){
            log.error("查询文件分页异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.BIND_FAIL);
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
        @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true),
        @CacheEvict(value = FileCacheConstant.LIST,key ="#businessIds.hashCode()"),
        @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true)})
    @Transactional
    public Boolean deleteInBusinessIds(List<Long> businessIds) {
        try {
            //删除数据库
            List<FileVO> files = findInBusinessIds(businessIds);
            UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().in(File::getBusinessId,businessIds);
            Boolean flag = remove(updateWrapper);
            if (!flag){
                throw new ProjectException(FileEnum.DELETE_FAIL);
            }
            //删除OSS中的图片
            if (!EmptyUtil.isNullOrEmpty(files)){
                List<String> getPathUrls = files.stream().map(FileVO::getPathUrl).collect(Collectors.toList());
                FileVO fileVO = files.get(0);
                String bucketName = fileVO.getBucketName();
                String storeFlag = fileVO.getStoreFlag();
                fileStorageAdapter.deleteBatch(storeFlag,bucketName,getPathUrls);
            }
            return flag;
        }catch (Exception e){
            log.error("删除业务对应附件：{}失败",businessIds);
            throw new ProjectException(FileEnum.DELETE_FILE_BUSINESSID_FAIL);
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true),
            @CacheEvict(value = FileCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true)})
    public Boolean deleteInIds(List<Long> ids) {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(File::getId,ids);
            List<File> files = list(queryWrapper);
            boolean flag = removeByIds(ids);
            if (!flag){
                throw new ProjectException(FileEnum.DELETE_FAIL);
            }
            //移除对象存储数据
            for (File file : files) {
                fileStorageAdapter.delete(file.getStoreFlag(),file.getBucketName(),file.getPathUrl());
            }
            return flag;
        }catch (Exception e){
            log.error("删除文件异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.DELETE_FAIL);
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,allEntries = true)})
    @Transactional
    public Boolean clearFile() {
        try {
            //查询需要清理的文件
            List<FileVO> fileList = needClearFile();
            if (EmptyUtil.isNullOrEmpty(fileList)){
                return true;
            }
            List<Long> fileListIds = fileList.stream().map(FileVO::getId).collect(Collectors.toList());
            //移除数据库信息
            Boolean flag =  removeByIds(fileListIds);
            //移除对象存储数据
            for (FileVO fileVO : fileList) {
                fileStorageAdapter.delete(fileVO.getStoreFlag(),fileVO.getBucketName(),fileVO.getPathUrl());
            }
            return flag;
        }catch (Exception e){
            log.error("定时清理文件异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.CLEAR_FILE_TASK_FAIL);
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BASIC,key = "#id"),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true)})
    @Transactional
    public Boolean clearFileById(String id) {
        try {
            FileVO fileVO =  needClearFileById(id);
            if (EmptyUtil.isNullOrEmpty(fileVO)){
                return true;
            }
            Boolean flag =  removeById(fileVO.getId());
            if (!flag){
                throw new ProjectException(FileEnum.DELETE_FAIL);
            }
            //删除OSS中的图片
            if (!EmptyUtil.isNullOrEmpty(fileVO)){
                fileStorageAdapter.delete(fileVO.getStoreFlag(),fileVO.getBucketName(),fileVO.getPathUrl());
            }
            return flag;
        }catch (Exception e){
            log.error("定时清理文件异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.CLEAR_FILE_TASK_FAIL);
        }
    }

    @Override
    public Set<Long> findBusinessIdAll() {
        try {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(File::getDataState, SuperConstant.DATA_STATE_0).isNotNull(File::getBusinessId);
            List<File> list = list(queryWrapper);
            if (!EmptyUtil.isNullOrEmpty(list)){
                return list.stream().map(File::getBusinessId).collect(Collectors.toSet());
            }
            return null;
        }catch (Exception e){
            log.error("查询附件列表异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.SELECT_BUSUBBESSID_FAIL);
        }
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true)},
        put={@CachePut(value =FileCacheConstant.BASIC,key = "#result.id")})
    @Transactional
    public FileVO upLoad(UploadMultipartFile multipartFile, FileVO fileVO) throws ProjectException {
        //获得文件ByteArrayInputStream
        ByteArrayInputStream byteArrayInputStream =new ByteArrayInputStream(multipartFile.getFileByte());
        try {
            //文件重命名
            String filename = identifierGenerator.nextId(fileVO)+"-"+multipartFile.getOriginalFilename();;
            fileVO.setFileName(filename);
            //文件后缀名
            String suffix = fileVO.getFileName().substring(fileVO.getFileName().lastIndexOf("."));
            fileVO.setSuffix(suffix);
            //调用简单上传
            String pathUrl = fileStorageAdapter.uploadFile(fileVO, byteArrayInputStream);
            //保存数据库
            File file = BeanConv.toBean(fileVO, File.class);
            file.setStatus(FileConstant.STATUS_SUCCEED);
            file.setPathUrl(pathUrl);
            boolean flag = save(file);
            if (!flag){
                throw new ProjectException(FileEnum.UPLOAD_FAIL);
            }
            //补全完整路径
            pathUrl = fileUrlContext.getFileUrl(fileVO.getStoreFlag(), pathUrl);
            fileVO.setId(file.getId());
            fileVO.setPathUrl(pathUrl);
            //发送延迟信息:上传如果超过10分钟不进行文件业务绑定则会被消息队列清空
            Long messageId = (Long) identifierGenerator.nextId(fileVO);
            MqMessage mqMessage = MqMessage.builder()
                .id(messageId)
                .title("file-message")
                .content(JSONObject.toJSONString(fileVO))
                .messageType("file-request")
                .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                .sender("system")
                .build();
            Message<MqMessage> message = MessageBuilder.withPayload(mqMessage).setHeader("x-delay", fileDelayTime).build();
            fileSource.fileOutput().send(message);
            return fileVO;
        }catch (Exception e) {
            log.error("文件上传异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.UPLOAD_FAIL);
        }finally {
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (Exception e) {
                    log.error("文件上传操作失败：{}", ExceptionsUtil.getStackTraceAsString(e));
                }
            }
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = FileCacheConstant.PAGE,allEntries = true),
            @CacheEvict(value = FileCacheConstant.BUSINESS_KEY,allEntries = true)},
        put={@CachePut(value =FileCacheConstant.BASIC,key = "#result.id")})
    @Transactional
    public FileVO initiateMultipartUpload(FileVO fileVO) {
        try {
            //文件重命名
            String filename = identifierGenerator.nextId(fileVO)+"-"+fileVO.getFileName();
            fileVO.setFileName(filename);
            //文件后缀名
            String suffix = fileVO.getFileName().substring(fileVO.getFileName().lastIndexOf("."));
            fileVO.setSuffix(suffix);
            //分片上传-初始化
            File file  =  fileStorageAdapter.initiateMultipartUpload(fileVO);
            //保存数据库
            file.setBusinessType(fileVO.getBusinessType());
            file.setSuffix(suffix);
            file.setStoreFlag(fileVO.getStoreFlag());
            file.setMd5(fileVO.getMd5());
            file.setCompanyNo(fileVO.getCompanyNo());
            file.setStatus(FileConstant.STATUS_SENDING);
            boolean flag = save(file);
            if (!flag){
                throw new ProjectException(FileEnum.UPLOAD_FAIL);
            }
            //补全完整路径
            FileVO fileVOResult = BeanConv.toBean(file, FileVO.class);
            String pathUrl = fileUrlContext.getFileUrl(fileVOResult.getStoreFlag(), fileVOResult.getPathUrl());
            fileVOResult.setPathUrl(pathUrl);
            //发送队列信息:上传如果超过10分钟不进行文件业务绑定则会被消息队列清空
            Long messageId = (Long) identifierGenerator.nextId(fileVOResult);
            MqMessage mqMessage = MqMessage.builder()
                .id(messageId)
                .title("file-message")
                .content(JSONObject.toJSONString(fileVOResult))
                .messageType("file-request")
                .produceTime(Timestamp.valueOf(LocalDateTime.now()))
                .sender("system")
                .build();
            Message<MqMessage> message = MessageBuilder.withPayload(mqMessage).setHeader("x-delay", fileDelayTime).build();
            fileSource.fileOutput().send(message);
            return fileVOResult;
        } catch (Exception e) {
            log.error("文件上传初始化异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.INIT_UPLOAD_FAIL);
        }
    }

    @Override
    @Transactional
    public String uploadPart(UploadMultipartFile multipartFile, FilePartVO filePartVO) {
        try {
            //上传分片数据
            String partETagString = fileStorageAdapter.uploadPart(filePartVO, new ByteArrayInputStream(multipartFile.getFileByte()));
            //保存分片信息
            FilePart filePart = BeanConv.toBean(filePartVO, FilePart.class);
            filePart.setUploadResult(partETagString);
            filePartService.save(filePart);
            return partETagString;
        }catch (Exception e) {
            log.error("文件分片上传异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.UPLOAD_PART_FAIL);
        }
    }

    @Override
    @Transactional
    public String completeMultipartUpload(FileVO fileVO) {
        try {
            //移除分片记录
            Boolean flag = filePartService.deleteFilePartByUpLoadId(fileVO.getUploadId());
            if (!flag){
                throw new ProjectException(FileEnum.COMPLETE_PART_FAIL);
            }
            //修改文件记录状态
            fileVO.setStatus(FileConstant.STATUS_SUCCEED);
            flag = updateById(BeanConv.toBean(fileVO,File.class));
            if (!flag){
                throw new ProjectException(FileEnum.COMPLETE_PART_FAIL);
            }
            //合并结果
            return fileStorageAdapter.completeMultipartUpload(fileVO);
        } catch (Exception e) {
            log.error("文件分片上传异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.COMPLETE_PART_FAIL);
        }
    }

    @Override
    @Cacheable(value = FileCacheConstant.BASIC,key = "#fileId")
    public FileVO downLoad(Long fileId) {
        try {
            File file = getById(fileId);
            InputStream inputStream = fileStorageAdapter
                    .downloadFile(file.getStoreFlag(), file.getBucketName(), file.getPathUrl());
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String base64Image = EncodesUtil.encodeBase64(bytes);
            FileVO fileVO = BeanConv.toBean(file, FileVO.class);
            fileVO.setBase64Image(base64Image);
            return fileVO;
        } catch (Exception e) {
            log.error("文件下载传异常：{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(FileEnum.DOWNLOAD_FAIL);
        }
    }
}
