package com.blogitory.blog.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Config class for spring security.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
public class SecurityConfig {
  /**
   * Setting passwordEncoder bean as BCryptPasswordEncoder.
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
