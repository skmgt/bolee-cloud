package com.itheima.bolee.security.web;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Slf4j
@Api(tags = "获取公钥")
@RestController
@RequestMapping("rsa")
public class PublicKeyController {

    @Autowired
    private KeyPair keyPair;

    @ApiOperation(value = "获取公钥",notes = "获取公钥")
    @GetMapping("/public-key")
    public Map<String, Object> loadPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        Map<String, Object> map = new JWKSet(key).toJSONObject();
        log.info("========获得公钥的信息：{}===========",map.toString());
        return map;
    }

}
