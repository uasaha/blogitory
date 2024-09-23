package com.blogitory.blog.visitant.aspect;

import com.blogitory.blog.commons.utils.UrlUtil;
import com.blogitory.blog.security.util.SecurityUtils;
import com.blogitory.blog.viewer.exception.NotExistRequestParameter;
import com.blogitory.blog.visitant.service.VisitantService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Visitant aspect.
 *
 * @author woonseok
 * @since 1.0
 **/
@Aspect
@Component
@RequiredArgsConstructor
public class VisitantAspect {
  private final VisitantService visitantService;

  /**
   * Save history of blog visitant.
   *
   * @param joinPoint join point
   */
  @Before(value = "@annotation(com.blogitory.blog.visitant.aspect.point.Visitant)")
  public void countVisit(JoinPoint joinPoint) {
    Integer memberNo = null;

    if (SecurityUtils.isAuthenticated()) {
      memberNo = SecurityUtils.getCurrentUserNo();
    }

    HttpServletRequest request = UrlUtil.getHttpServletRequest(joinPoint);

    if (Objects.isNull(request)) {
      throw new NotExistRequestParameter("HttpServletRequest not found");
    }

    String blogUrl = UrlUtil.getBlogUrlFromRequest(request);
    String ip = UrlUtil.getClientIp(request);

    visitantService.viewBlogs(blogUrl, memberNo, ip);
  }
}
