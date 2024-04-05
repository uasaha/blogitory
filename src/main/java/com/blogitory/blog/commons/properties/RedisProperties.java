package com.blogitory.blog.commons.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for redis service.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
  private String host;
  private String port;
}
