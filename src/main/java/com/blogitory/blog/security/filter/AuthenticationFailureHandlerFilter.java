package com.blogitory.blog.security.filter;

import com.blogitory.blog.commons.utils.UrlUtil;
import com.blogitory.blog.security.exception.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * AuthenticationFailureHandlerFilter.
 *
 * @author woonseok
 * @since 1.0
 **/
public class AuthenticationFailureHandlerFilter extends OncePerRequestFilter {

  private static final String FAILED_PARAM = "?login-failed";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (AuthenticationException e) {
      String refererUrl = UrlUtil.getRefererUrl(request);

      if (refererUrl.contains(FAILED_PARAM)) {
        response.sendRedirect(refererUrl);
      } else {
        response.sendRedirect(UrlUtil.getRefererUrl(request) + FAILED_PARAM);
      }
    }

  }
}
