package com.blogitory.blog.security.handler;

import com.blogitory.blog.commons.utils.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Implementation of LogoutSuccessHandler.
 *
 * @author woonseok
 * @since 1.0
 **/
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request,
                              HttpServletResponse response,
                              Authentication authentication)
          throws IOException {
    String refererUrl = UrlUtil.getRefererUrl(request);

    response.sendRedirect(refererUrl);
  }
}
