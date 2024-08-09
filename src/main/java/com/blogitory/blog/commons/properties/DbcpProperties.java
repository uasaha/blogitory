package com.blogitory.blog.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Properties for DBCP2.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "blogitory.dbcp")
public class DbcpProperties {
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private Integer initialSize;
  private Integer minIdle;
  private Integer maxIdle;
  private Integer maxTotal;
  private boolean testOnBorrow;
  private boolean testOnReturn;
  private boolean testWhileIdle;
  private String validationQuery;
}
