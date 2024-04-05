package com.blogitory.blog.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for Mail Service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
  private String host;
  private String port;
  private String username;
  private String password;
}
