package com.itheima.bolee.framework.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.bolee.framework.commons.constant.basic.SuperConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description：自动填充
 */
@Slf4j
@Component
public class AutoMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充.....");
        UserVO userVO = SubjectContent.getUserVO();
        if (EmptyUtil.isNullOrEmpty(userVO)){
            userVO= new UserVO();
        }
        Object dataState = getFieldValByName("dataState", metaObject);
        Object companyNo = getFieldValByName("companyNo", metaObject);
        if (metaObject.hasSetter("dataState")&&dataState==null) {
            this.setFieldValByName("dataState", SuperConstant.DATA_STATE_0,metaObject);
        }
        if (metaObject.hasSetter("createTime")) {
            this.setFieldValByName("createTime", LocalDateTime.now(),metaObject);
        }
        if (metaObject.hasSetter("updateTime")) {
            this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        }
        if (metaObject.hasSetter("createBy")) {
            this.setFieldValByName("createBy",userVO.getId(),metaObject);
        }
        if (metaObject.hasSetter("updateBy")) {
            this.setFieldValByName("updateBy",userVO.getId(),metaObject);
        }
        if (metaObject.hasSetter("dataDeptNo")) {
            this.setFieldValByName("dataDeptNo",userVO.getDeptNo(),metaObject);
        }
        if (metaObject.hasSetter("companyNo")&&companyNo==null) {
            this.setFieldValByName("companyNo",userVO.getCompanyNo(),metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充.....");
        UserVO userVO = SubjectContent.getUserVO();
        if (EmptyUtil.isNullOrEmpty(userVO)){
            userVO= new UserVO();
        }
        if (metaObject.hasSetter("updateTime")) {
            this.setFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        }
        if (metaObject.hasSetter("updateBy")) {
            this.setFieldValByName("updateBy",userVO.getId(),metaObject);
        }
    }
}
