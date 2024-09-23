package com.blogitory.blog.commons.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * Utils class for cookie.
 *
 * @author woonseok
 * @since 1.0
 **/
@Component
public class CookieUtils {

  @Value("${blogitory.cookie.domain}")
  private String domain;

  private static final Integer ZERO = 0;

  /**
   * make secure cookie.
   *
   * @param key    key
   * @param value  value
   * @param expire expire time
   * @return new cookie
   */
  public ResponseCookie makeSecureCookie(String key, String value, Integer expire) {
    return ResponseCookie.from(key, value)
            .secure(true)
            .httpOnly(true)
            .maxAge(expire)
            .sameSite("Strict")
            .path("/")
            .domain(domain)
            .build();
  }

  public void addSecureCookie(HttpServletResponse response,
                              String key, String value, Integer expire) {
    ResponseCookie cookie = this.makeSecureCookie(key, value, expire);
    response.addHeader("Set-Cookie", cookie.toString());
  }

  public void deleteSecureCookie(HttpServletResponse response, String key) {
    ResponseCookie cookie = this.makeSecureCookie(key, Strings.EMPTY, ZERO);
    response.addHeader("Set-Cookie", cookie.toString());
  }
}
