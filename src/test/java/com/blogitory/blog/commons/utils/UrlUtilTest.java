package com.blogitory.blog.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Url util test.
 *
 * @author woonseok
 * @Date 2024-09-11
 * @since 1.0
 **/
class UrlUtilTest {
  MockHttpServletRequest request;

  @Test
  @DisplayName("이전 url 획득")
  void getRefererUrl() {
    String expect = "https://test.com/test";

    request = new MockHttpServletRequest();
    request.addHeader("Referer", expect);

    String url = UrlUtil.getRefererUrl(request);

    assertEquals(expect, url);
  }

  @Test
  @DisplayName("IP 조회")
  void getClientIp() {
    request = new MockHttpServletRequest();
    String ip = "127.0.0.1";
    request.addHeader("Proxy-Client-IP", ip);
    request.addHeader("X-Forwarded-For", ip);
    request.addHeader("WL-Proxy-Client-IP", ip);
    request.addHeader("HTTP_CLIENT_IP", ip);
    request.addHeader("HTTP_X_FORWARDED_FOR", ip);
    request.setRemoteAddr(ip);

    String actual = UrlUtil.getClientIp(request);

    assertEquals("127.0.0.1", actual);
  }

  @Test
  @DisplayName("request로부터 post url 획득")
  void getPostsUrlFromRequest() {
    request = new MockHttpServletRequest();
    request.setRequestURI("/@test/test/test");

    String postsUrl = UrlUtil.getPostsUrlFromRequest(request);

    assertEquals("@test/test/test", postsUrl);
  }

  @Test
  @DisplayName("request로부터 post url 획득 - null")
  void getPostsUrlFromRequestNull() {
    request = new MockHttpServletRequest();
    request.setRequestURI("/@test/test");

    String actual = UrlUtil.getPostsUrlFromRequest(request);

    assertNull(actual);
  }

  @Test
  @DisplayName("request로부터 blog url 획득")
  void getBlogUrlFromRequest() {
    request = new MockHttpServletRequest();
    request.setRequestURI("/@blog/test/test/test");

    String blogUrl = UrlUtil.getBlogUrlFromRequest(request);

    assertEquals("@blog/test", blogUrl);
  }

  @Test
  @DisplayName("request로부터 blog url 획득 - null")
  void getBlogUrlFromRequestNull() {
    request = new MockHttpServletRequest();
    request.setRequestURI("/test/test");

    String actual = UrlUtil.getBlogUrlFromRequest(request);

    assertNull(actual);
  }

  @Test
  @DisplayName("Get request from joinpoint")
  void getRequestFromJoinPoint() {
    JoinPoint joinPoint = mock(JoinPoint.class);
    request = new MockHttpServletRequest();

    when(joinPoint.getArgs()).thenReturn(new Object[] {request});

    HttpServletRequest actual = UrlUtil.getHttpServletRequest(joinPoint);

    assertEquals(request, actual);
  }

  @Test
  @DisplayName("Get request from joinpoint - null")
  void getRequestFromJoinPointNull() {
    JoinPoint joinPoint = mock(JoinPoint.class);

    when(joinPoint.getArgs()).thenReturn(new Object[] {});

    HttpServletRequest actual = UrlUtil.getHttpServletRequest(joinPoint);

    assertNull(actual);
  }
}
