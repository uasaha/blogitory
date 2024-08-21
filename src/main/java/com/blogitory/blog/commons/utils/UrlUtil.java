package com.blogitory.blog.commons.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
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
  private static final String INDEX_PAGE = "/";

  /**
   * Get base url on request.
   *
   * @param request HttpServletRequest.
   * @return Base URL.
   */
  public static String getBaseUrl(HttpServletRequest request) {
    return request.getRequestURL()
            .substring(0, request.getRequestURL().length()
                    - request.getRequestURI().length())
            + request.getContextPath();
  }

  /**
   * Get referer url.
   *
   * @param request HttpServletRequest
   * @return url
   */
  public static String getRefererUrl(HttpServletRequest request) {
    String refererUrl = request.getHeader(REFERER);

    return Objects.isNull(refererUrl) ? INDEX_PAGE : refererUrl;
  }

  /**
   * Make Blog key.
   *
   * @param username username
   * @param blogUrl  blog url
   * @return blog key
   */
  public static String getBlogKey(String username, String blogUrl) {
    return "@" + username + "/" + blogUrl;
  }

  /**
   * Make Post key.
   *
   * @param username username
   * @param blogUrl  blog url
   * @param postsUrl posts url
   * @return post key
   */
  public static String getPostsKey(String username, String blogUrl, String postsUrl) {
    return getBlogKey(username, blogUrl) + "/" + postsUrl;
  }
}
