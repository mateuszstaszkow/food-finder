package com.foodfinder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabasePoolConfiguration {

    @Value("${hikari.driver-class-name}")
    private String driverClassName;

    @Value("${hikari.jdbc-url}")
    private String jdbcUrl;

    @Value("${hikari.username}")
    private String user;

    @Value("${hikari.password}")
    private String password;

    @Value("${hikari.maximum-pool-size}")
    private int poolSize;

    @Value("${hikari.connection-test-query}")
    private String connectionTestQuery;

    @Value("${hikari.pool-name}")
    private String poolName;

    @Value("${spring.data-source.cache-prep-stmts}")
    private String cachePrepStmts;

    @Value("${spring.data-source.prep-stmt-cache-size}")
    private String stmtSize;

    @Value("${spring.data-source.prep-stmt-cache-sql-limit}")
    private String stmtLimit;

    @Value("${spring.data-source.use-server-prep-stmts}")
    private String useStmts;

    @Value("${food-finder.packages-to-scan}")
    private String packagesToScan;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateAuto;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setConnectionTestQuery(connectionTestQuery);
        hikariConfig.setPoolName(poolName);

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", cachePrepStmts);
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", stmtSize);
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", stmtLimit);
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", useStmts);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(packagesToScan);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateAuto);
        properties.setProperty("hibernate.dialect", hibernateDialect);
        return properties;
    }
}
