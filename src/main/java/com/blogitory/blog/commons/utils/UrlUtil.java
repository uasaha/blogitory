package com.blogitory.blog.commons.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Util class for Url.
 *
 * @author woonseok
 * @since 1.0
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtil {
  private static final String REFERER = "Referer";

  /**
   * Get base url on request.
   *
   * @param request HttpServletRequest.
   * @return Base URL.
   */
  public static String getBaseUrl(HttpServletRequest request) {
    return request.getRequestURL().substring(0,
            request.getRequestURL().length()
                    - request.getRequestURI().length())
            + request.getContextPath();
  }

  public static String getRefererUrl(HttpServletRequest request) {
    return request.getHeader(REFERER);
  }
}
