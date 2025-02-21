package com.itheima.bolee.framework.intercept;

import com.itheima.bolee.framework.commons.constant.security.OauthCacheConstant;
import com.itheima.bolee.framework.commons.constant.security.SecurityConstant;
import com.itheima.bolee.framework.commons.dto.security.UserVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WebAuthIntercept.java
 * @Description 认证信息放到SubjectContent上下文中
 */
@Component
public class WebAuthIntercept implements HandlerInterceptor {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        //从头部中拿到当前userToken
        String userToken = request.getHeader(SecurityConstant.USER_TOKEN);
        if (!EmptyUtil.isNullOrEmpty(userToken)){
            RBucket<UserVO> userVORBucket = redissonClient.getBucket(OauthCacheConstant.USER_TOKEN + userToken);
            UserVO userVO = userVORBucket.get();
            //放入当前线程中：用户当前的web直接获得userVO使用
            SubjectContent.setUserVO(userVO);
            //下游传递userToken
            SubjectContent.setUserToken(userToken);
            //下游传递companyNo
            SubjectContent.setCompanyNo(userVO.getCompanyNo());
            //下游传递companyNo
            SubjectContent.setDataSecurityVO(userVO.getDataSecurityVO());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        //移除当前线程中的参数
        SubjectContent.removeUserVO();
        SubjectContent.removeUserToken();
        SubjectContent.removeCompanyNo();
        SubjectContent.removeDataSecurityVO();
    }
}