package com.blogitory.blog.commons.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoderConfigTest.
 *
 * @author woonseok
 * @since 1.0
 **/
class PasswordEncoderConfigTest {

  @Test
  void passwordEncoder() {
    PasswordEncoderConfig config = new PasswordEncoderConfig();
    PasswordEncoder encoder = config.passwordEncoder();

    assertInstanceOf(BCryptPasswordEncoder.class, encoder);
  }
}