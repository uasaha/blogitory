package com.blogitory.blog.commons.config;

import com.blogitory.blog.commons.properties.DbcpProperties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for DBCP2.
 *
 * @author woonseok
 * @since 1.0
 **/
@RequiredArgsConstructor
@Configuration
public class DbcpConfig {
  private final DbcpProperties dbcpProperties;

  /**
   * Set datasource as BasicDataSource.
   *
   * @return DataSource
   */
  @Bean
  public DataSource dataSource() {
    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName(dbcpProperties.getDriverClassName());
    dataSource.setUrl(dbcpProperties.getUrl());
    dataSource.setUsername(dbcpProperties.getUsername());
    dataSource.setPassword(dbcpProperties.getPassword());

    dataSource.setInitialSize(dbcpProperties.getInitialSize());
    dataSource.setMaxTotal(dbcpProperties.getMaxTotal());
    dataSource.setMinIdle(dbcpProperties.getMinIdle());
    dataSource.setMaxIdle(dbcpProperties.getMaxIdle());

    dataSource.setTestOnBorrow(dbcpProperties.isTestOnBorrow());
    dataSource.setTestOnReturn(dbcpProperties.isTestOnReturn());
    dataSource.setTestWhileIdle(dbcpProperties.isTestWhileIdle());

    dataSource.setValidationQuery(dbcpProperties.getValidationQuery());

    return dataSource;
  }
}
