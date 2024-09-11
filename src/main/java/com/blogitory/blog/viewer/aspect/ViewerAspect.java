package com.blogitory.blog.viewer.aspect;

import com.blogitory.blog.commons.utils.UrlUtil;
import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.viewer.exception.NotExistRequestParameter;
import com.blogitory.blog.viewer.service.ViewerService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Viewer aspect.
 *
 * @author woonseok
 * @Date 2024-09-09
 * @since 1.0
 **/
@Aspect
@Component
@RequiredArgsConstructor
public class ViewerAspect {
  private final ViewerService viewerService;

  /**
   * Posts view counts.
   *
   * @param joinPoint joinPoint
   */
  @Before(value = "@annotation(com.blogitory.blog.viewer.aspect.point.Viewer)")
  public void countView(JoinPoint joinPoint) {
    Integer memberNo = null;

    if (SecurityUtils.isAuthenticated()) {
      memberNo = SecurityUtils.getCurrentUserNo();
    }

    HttpServletRequest request = UrlUtil.getHttpServletRequest(joinPoint);

    if (Objects.isNull(request)) {
      throw new NotExistRequestParameter("HttpServletRequest not found");
    }

    String postsUrl = UrlUtil.getPostsUrlFromRequest(request);
    String ip = UrlUtil.getClientIp(request);

    viewerService.viewPosts(postsUrl, memberNo, ip);
  }
}
