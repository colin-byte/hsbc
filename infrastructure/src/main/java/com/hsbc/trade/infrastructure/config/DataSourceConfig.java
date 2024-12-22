package com.hsbc.trade.infrastructure.config;


import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

public class DataSourceConfig {


    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @Primary
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("datasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Value("${mybatis.mysql.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mybatis.mysql.mapper-locations}")
    private String mapperLocation;

    @Value("${mybatis.mysql.config-location}")
    private String configurationFile;

    @Bean
    @Primary
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("datasource") DataSource dataSource)
            throws IOException {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource);
        fb.setTypeAliasesPackage(typeAliasesPackage);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        fb.setMapperLocations(resolver.getResources(mapperLocation));
        fb.setConfigLocation(resolver.getResource(configurationFile));
        return fb;
    }
}
