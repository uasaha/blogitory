package com.blogitory.blog.commons.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;

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
  private static final String BLOG_URL_REGEX = "^(/)(@\\w+/[a-zA-Z0-9_-]+)(/.*)?$";
  private static final String POSTS_URL_REGEX =
          "^(/)(@\\w+/[a-zA-Z0-9_-]+/[ㄱ-ㅎ가-힣a-zA-Z0-9_-]+)(/.*)?$";

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

  /**
   * Get client IP address.
   *
   * @param request request
   * @return IP
   */
  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  /**
   * Get post url from HttpServletRequest.
   *
   * @param request HttpServletRequest
   * @return post url
   */
  public static String getPostsUrlFromRequest(HttpServletRequest request) {
    String url = request.getRequestURI();
    url = URLDecoder.decode(url, StandardCharsets.UTF_8);

    Pattern pattern = Pattern.compile(POSTS_URL_REGEX);
    Matcher matcher = pattern.matcher(url);

    if (matcher.matches()) {
      return matcher.group(2);
    }

    return null;
  }

  /**
   * Get blog url from HttpServletRequest.
   *
   * @param request HttpServletRequest
   * @return blog url
   */
  public static String getBlogUrlFromRequest(HttpServletRequest request) {
    String url = request.getRequestURI();

    Pattern pattern = Pattern.compile(BLOG_URL_REGEX);
    Matcher matcher = pattern.matcher(url);

    if (matcher.matches()) {
      return matcher.group(2);
    }

    return null;
  }

  /**
   * Get HttpServletRequest from JoinPoint.
   *
   * @param joinPoint AOP join point
   * @return HttpServletRequest
   */
  public static HttpServletRequest getHttpServletRequest(JoinPoint joinPoint) {
    for (Object arg : joinPoint.getArgs()) {
      if (arg instanceof HttpServletRequest servletRequest) {
        return servletRequest;
      }
    }

    return null;
  }
}
