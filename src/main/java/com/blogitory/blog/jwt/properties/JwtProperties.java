package com.blogitory.blog.jwt.properties;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for JWT.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "blogitory.jwt")
public class JwtProperties {
  private String accessSecret;
  private Duration accessExpire;
  private String refreshSecret;
  private Duration refreshExpire;
}

