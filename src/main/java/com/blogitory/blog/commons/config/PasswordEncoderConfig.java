package com.blogitory.blog.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration for using PasswordEncoder.
 *
 * @author woonseok
 * @since 1.0
 **/
@Configuration
public class PasswordEncoderConfig {
  /**
   * Register Password encoder as BCryptPasswordEncoder.
   *
   * @return BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
