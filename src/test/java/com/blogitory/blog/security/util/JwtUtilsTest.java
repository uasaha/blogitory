package com.blogitory.blog.security.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

/**
 * JwtUtilsText.
 *
 * @author woonseok
 * @since 1.0
 **/
class JwtUtilsTest {

  @Test
  void makeSecureCookie() {
    String key = "cookie";
    String value = "value";
    Duration expireTime = Duration.ofSeconds(3600);
    String path = "/";

    ResponseCookie cookie = JwtUtils.makeSecureCookie(key, value, 3600);

    assertAll(
            () -> assertTrue(cookie.isSecure()),
            () -> assertEquals(cookie.getMaxAge(), expireTime),
            () -> assertEquals(cookie.getPath(), path),
            () -> assertEquals(cookie.getValue(), value),
            () -> assertEquals(cookie.getName(), key)
    );
  }
}