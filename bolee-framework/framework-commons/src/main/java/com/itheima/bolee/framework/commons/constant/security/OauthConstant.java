package com.itheima.bolee.framework.commons.constant.security;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OauthConstant.java
 * @Description oauth2认证常量
 */
public class OauthConstant {
    //授权方式：刷新令牌
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    //CLIENTID
    public static final String OPERATORS_PC = "operators-pc";
    public static final String OPERATORS_MOBILE = "operators-mobile";
    public static final String SERVICER_PC = "servicer-pc";
    public static final String SERVICER_MOBILE = "servicer-mobile";

    //登录类型
    public static final String USER_USERNAME = "user-username";
    public static final String USER_MOBILE = "user-mobile";
    public static final String USER_WECHAT = "user-wechat";
    public static final String CUSTOMER_USERNAME = "customer-username";
    public static final String CUSTOMER_MOBILE = "customer-mobile";
    public static final String CUSTOMER_WECHAT = "customer-wechat";

    //三方登录默认创建用户时默认角色
    public static final String DEFAULT_ROLE_USER = "DEFAULT_ROLE_USER";
    public static final String DEFAULT_ROLE_CUSTOMER = "DEFAULT_ROLE_CUSTOMER";


    public static final String CLIENT_DETAILS_FIELDS = "client_id, client_secret as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";
    public static final String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS + " from tab_client_details";
    public static final String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " order by client_id";
    public static final String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where client_id = ?";

    //登录参数KEY定义
    public static final String USER_ID_KEY = "user_id";
    public static final String REAL_NAME_KEY = "real_name";
    public static final String LOGIN_TYPE_KEY = "login_type";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String USER_NAME_KEY = "username";
    public static final String SEX_KEY = "sex";
    public static final String MOBILE_KEY = "mobile";
    public static final String PASSWORD_KEY = "password";
    public static final String EXPIRES_IN_KEY = "expires_in";
    public static final String RESOURCS_KEY = "resources";
    public static final String AUTHORITIES_KEY = "authorities";

    public static final String ROLES_KEY = "roles";
    public static final String JTI_KEY = "jti";
    public static final String CODE_KEY = "code";
    public static final String OPEN_ID_KEY ="open_id" ;
    public static final String DEPT_NO_KEY = "dept_no";
    public static final String POST_NO_KEY = "post_no";
    public static final String DATA_SECURITY_KEY = "data_security";
    public static final String ONLY_AUTHENTICATE_KEY = "only_authenticate";
    public static final String COMPANY_NO_KEY = "company_no";;

    //登录处理集合
    public static Map<String,String> loginBeanNames = new HashMap<>();
    static {
        loginBeanNames.put("username","usernameLoginAuthHandler");
        loginBeanNames.put("mobile","mobileLoginAuthHandler");
        loginBeanNames.put("wechat","wechatLoginAuthHandler");
    }

}
