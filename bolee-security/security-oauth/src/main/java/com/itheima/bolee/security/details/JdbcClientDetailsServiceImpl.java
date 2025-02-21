package com.itheima.bolee.security.details;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/***
 * @description 对JdbcClientDetailsService的增强
 */
public class JdbcClientDetailsServiceImpl extends JdbcClientDetailsService {

    //使用数据库中配置进行对象构建
    public JdbcClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    /***
     * @description 加载指定客户端配置信息
     * @param clientId
     * @return
 * @return: org.springframework.security.oauth2.provider.ClientDetails
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId)  {
        return super.loadClientByClientId(clientId);
    }
}
