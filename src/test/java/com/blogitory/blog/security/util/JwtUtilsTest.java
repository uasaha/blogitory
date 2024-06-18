package com.blogitory.blog.security.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;

/**
 * 설명 작성 필!
 *
 * @author woonseok
 * @since 1.0
 **/
class JwtUtilsTest {

  @Test
  void makeSecureCookie() {
    String key = "cookie";
    String value = "value";
    Integer expireTime = 3600;
    String path = "/";

    Cookie cookie = JwtUtils.makeSecureCookie(key, value, expireTime);

    assertAll(
            () -> assertTrue(cookie.getSecure()),
            () -> assertEquals(cookie.getMaxAge(), expireTime),
            () -> assertEquals(cookie.getPath(), path),
            () -> assertEquals(cookie.getValue(), value),
            () -> assertEquals(cookie.getName(), key)
    );
  }
}