package com.blogitory.blog.security.handler;

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
    response.setContentType("application/json;charset=UTF-8");
  }
}