package com.itheima.bolee.insurance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.itheima.bolee.RequestTemplate;
import com.itheima.bolee.dto.ResponseDTO;
import com.itheima.bolee.framework.commons.constant.security.AuthChannelConstant;
import com.itheima.bolee.framework.commons.dto.basic.OtherConfigVO;
import com.itheima.bolee.framework.commons.dto.external.TripartiteInsureDTO;
import com.itheima.bolee.framework.commons.dto.security.AuthChannelVO;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.insurance.dto.WarrantyVO;
import com.itheima.bolee.insurance.dto.WarrantyVerifyVO;
import com.itheima.bolee.insurance.service.IWarrantyVerifyService;
import com.itheima.bolee.insurance.service.TripartiteInsureService;
import com.itheima.bolee.security.feign.AuthChannelFeign;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TripartiteInsureServiceImpl
 *
 * @author: wgl
 * @describe: 三方公司：投保接口
 * @date: 2022/12/28 10:10
 */
@Service
public class TripartiteInsureServiceImpl implements TripartiteInsureService {

    @Autowired
    AuthChannelFeign authChannelFeign;

    @Autowired
    IWarrantyVerifyService warrantyVerifyService;

    @Override
    public ResponseDTO insure(WarrantyVO warrantyVO) {
        try {
            //查询三方接口配置信息
            AuthChannelVO authChannelVO = authChannelFeign.findAuthChannelByCompanyNoAndChannelLabel(
                warrantyVO.getCompanyNo(),
                AuthChannelConstant.CHANNEL_LABEL_INSURE);
            if (EmptyUtil.isNullOrEmpty(authChannelVO)){
                throw new RuntimeException("尚未配置三方秘钥");
            }
            List<OtherConfigVO> otherConfigVOs = JSONArray.parseArray(authChannelVO.getOtherConfig(), OtherConfigVO.class);
            List<OtherConfigVO> otherConfigVOsHandler = otherConfigVOs.stream().filter(n -> {
                return n.getConfigKey().equals(AuthChannelConstant.PUBLICKEY);
            }).collect(Collectors.toList());
            if (EmptyUtil.isNullOrEmpty(otherConfigVOsHandler)){
                throw new RuntimeException("尚未配置三方公钥");
            }
            URIBuilder urlBuilder = new URIBuilder()
                .setScheme("http")
                .setHost(authChannelVO.getDomain())
                .setPath("/insure");
            RequestTemplate requestTemplate = RequestTemplate.builder()
                .appId(authChannelVO.getAppId())
                .privateKey(authChannelVO.getAppSecret())
                .publicKey(otherConfigVOsHandler.get(0).getConfigValue())
                .uriBuilder(urlBuilder)
                .build();
            ResponseDTO responseDTO = requestTemplate.doRequest(warrantyVO, ResponseDTO.class);
            //保存审核信息
            WarrantyVerifyVO warrantyVerifyVO = WarrantyVerifyVO.builder()
                .warrantyNo(warrantyVO.getWarrantyNo())
                .companyNo(warrantyVO.getCompanyNo())
                .verifyCode(responseDTO.getCode())
                .verifyMsg(responseDTO.getMsg())
                .build();
            warrantyVerifyService.save(warrantyVerifyVO);
            return responseDTO;
        }catch (Exception e){
           throw new RuntimeException("调用三方接口失败");
        }
    }
}
