package com.itheima.bolee.framework.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.google.common.collect.Lists;
import com.itheima.bolee.framework.commons.utils.EmptyUtil;
import com.itheima.bolee.framework.commons.utils.SubjectContent;
import com.itheima.bolee.framework.mybatisplus.constant.MybatisPlusConstant;
import com.itheima.bolee.framework.mybatisplus.handler.AutoMetaObjectHandler;
import com.itheima.bolee.framework.mybatisplus.handler.DeptNoLineHandler;
import com.itheima.bolee.framework.mybatisplus.handler.PersonLineHandler;
import com.itheima.bolee.framework.mybatisplus.interceptor.DeptNoLineInnerInterceptor;
import com.itheima.bolee.framework.mybatisplus.interceptor.PersonLineInnerInterceptor;
import com.itheima.bolee.framework.mybatisplus.properties.DataSecurityProperties;
import com.itheima.bolee.framework.mybatisplus.properties.TenantProperties;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：配置文件
 */
@Slf4j
//申明此类为配置类
@Configuration
//读取配置
@EnableConfigurationProperties({MybatisPlusProperties.class, TenantProperties.class,DataSecurityProperties.class})
public class MyBatisPlusConfig {

    @Autowired
    MybatisPlusProperties mybatisPlusProperties;

    @Autowired
    TenantProperties tenantProperties;

    @Autowired
    DataSecurityProperties dataSecurityProperties;

    /**
     * @Description mybatis提供的主键生成策略【制定雪花】
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {

        return new DefaultIdentifierGenerator();
    }

    /**
     * 自动填充
     */
    @Bean
    @ConditionalOnMissingBean
    public AutoMetaObjectHandler myMetaObjectHandler() {

        return new AutoMetaObjectHandler();
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,
     * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 如果用了分页插件注意
        // 先 add TenantLineInnerInterceptor
        // 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        //多租户租插件
        interceptor.addInnerInterceptor(companyNoInterceptor());
        //数据权限-部门插件
        interceptor.addInnerInterceptor(deptNoLineInnerInterceptor());
        //数据权限-本人插件
        interceptor.addInnerInterceptor(personLineInnerInterceptor());
        //分页的插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public TenantLineInnerInterceptor companyNoInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {

            @Override
            public Expression getTenantId() {
                //从当前的SubjectContent上下文中获取用户信息CompanyNo
                String companyNo = SubjectContent.getCompanyNo();
                return companyNo==null ? null : new StringValue(companyNo);
            }

            @Override
            public String getTenantIdColumn() {

                return MybatisPlusConstant.COMPANY_NO;
            }

            @Override
            public boolean ignoreTable(String tableName) {
                //如果当前为默认管理平台则忽略表
//                String companyNo = SubjectContent.getCompanyNo();
//                if (tenantProperties.getDefaultCompanyNo().equals(companyNo)){
//                    log.info("默认企业查询，忽略表：{}", tableName);
//                    return true;
//                }
                //如果tableName出现在忽略配置中则不添加企业查询条件
                List<String> tableNameList = tenantProperties.getIgnoreCompanyTables();
                if (!EmptyUtil.isNullOrEmpty(tableNameList)&&tableNameList.contains(tableName)) {
                    log.info("企业隐式传参为空，忽略表：{}", tableName);
                    return true;
                }
                //如果查询字段值为空则不添加企业查询条件
                Expression tenantId = this.getTenantId();
                if (EmptyUtil.isNullOrEmpty(tenantId)) {
                    log.info("企业隐式传参为空，忽略表：{}", tableName);
                    return true;
                }
                return false;
            }
        });
    }

    @Bean
    public PersonLineInnerInterceptor personLineInnerInterceptor() {
        return new PersonLineInnerInterceptor(new PersonLineHandler() {
            @Override
            public Expression getCreateBy() {
                if (EmptyUtil.isNullOrEmpty(SubjectContent.getUserVO())){
                    return null;
                }
                if (EmptyUtil.isNullOrEmpty(SubjectContent.getUserVO().getDataSecurityVO())){
                    return null;
                }
                Boolean youselfData = SubjectContent.getUserVO().getDataSecurityVO().getYouselfData();
                Long userId = SubjectContent.getUserVO().getId();
                return youselfData ? new StringValue(String.valueOf(userId)) : null ;
            }
            @Override
            public String getCreateByColumn() {
                return PersonLineHandler.super.getCreateByColumn();
            }
            @Override
            public boolean ignoreTable(String tableName) {
                //如果未指定创建人
                Expression expression = this.getCreateBy();
                if (EmptyUtil.isNullOrEmpty(expression)){
                    log.info("数据权限-本人隐式传参为空，忽略表：{}", tableName);
                    return true;
                }
                //如果tableName出现在忽略配置中则不添加数据权限
                List<String> tableNameList = dataSecurityProperties.getIgnoreDataSecurityTables();
                if (!EmptyUtil.isNullOrEmpty(tableNameList)&&tableNameList.contains(tableName)) {
                    log.info("忽略表：{}", tableName);
                    return true;
                }
                return false;
            }
        });
    }

    @Bean
    public DeptNoLineInnerInterceptor deptNoLineInnerInterceptor() {
        return new DeptNoLineInnerInterceptor(new DeptNoLineHandler() {
            @Override
            public ExpressionList getDeptNoList() {
                if (EmptyUtil.isNullOrEmpty(SubjectContent.getUserVO())){
                    return null;
                }
                if (EmptyUtil.isNullOrEmpty(SubjectContent.getUserVO().getDataSecurityVO())){
                    return null;
                }
                List<String> deptNos = SubjectContent.getUserVO().getDataSecurityVO().getDeptNos();
                return EmptyUtil.isNullOrEmpty(deptNos)?null:new ExpressionList(deptNos
                        .stream().map(StringValue::new).collect(Collectors.toList()));
            }
            @Override
            public String getDeptNoColumn() {

                return DeptNoLineHandler.super.getDeptNoColumn();
            }
            @Override
            public boolean ignoreTable(String tableName) {
                //如果未指定部门列表
                ExpressionList expressionList = this.getDeptNoList();
                if (EmptyUtil.isNullOrEmpty(expressionList)){
                    log.info("数据权限-部门隐式传参为空，忽略表：{}", tableName);
                    return true;
                }
                //如果tableName出现在忽略配置中则不添加数据权限
                List<String> tableNameList = dataSecurityProperties.getIgnoreDataSecurityTables();
                if (!EmptyUtil.isNullOrEmpty(tableNameList)&&tableNameList.contains(tableName)) {
                    log.info("忽略表：{}", tableName);
                    return true;
                }
                return false;
            }
        });
    }


    //分页的插件配置
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor(DbType.MYSQL);
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

}
